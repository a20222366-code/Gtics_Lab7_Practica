
package com.example.Lab7_Practica.service;

import com.example.Lab7_Practica.dto.InvoiceForm;
import com.example.Lab7_Practica.entity.Customer;
import com.example.Lab7_Practica.entity.Invoice;
import com.example.Lab7_Practica.entity.InvoiceDetail;
import com.example.Lab7_Practica.entity.Product;
import com.example.Lab7_Practica.repository.CustomerRepository;
import com.example.Lab7_Practica.repository.InvoiceDetailRepository;
import com.example.Lab7_Practica.repository.InvoiceRepository;
import com.example.Lab7_Practica.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public InvoiceService(
            InvoiceRepository invoiceRepository,
            InvoiceDetailRepository invoiceDetailRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceDetailRepository = invoiceDetailRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }



    public Customer buscarCliente(Integer customerId) {
        return customerRepository.findById(customerId).orElseThrow(() -> new IllegalArgumentException("El cliente no existe"));
    }

    public Product buscarProducto(Integer productId) {
        return productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("El producto no existe"));
    }


    //Validamos tipo de documento
    public void validarTipoDocumento(String type, Customer customer) {

        //Comprueba si se quiere emitir una Factura
        //y si el cliente NO tiene RUC como tipo de documento.
        //Si se cumplen ambas condiciones, lanza un error.
        if ("Factura".equals(type) && !"RUC".equals(customer.getDocumentType())) {
            throw new IllegalArgumentException("Para emitir una factura, el cliente debe tener RUC");
        }

        // Comprueba si se quiere emitir una Boleta
        // y si el cliente NO tiene DNI como tipo de documento.
        // Si se cumplen ambas condiciones, lanza un error.
        if ("Boleta".equals(type)
                && !"DNI".equals(customer.getDocumentType())) {
            throw new IllegalArgumentException(
                    "Para emitir una boleta, el cliente debe tener DNI");
        }


        // Comprueba que el tipo de comprobante sea válido.
        // Si no es Factura ni Boleta, lanza un error.
        if (!"Factura".equals(type) && !"Boleta".equals(type)) {
            throw new IllegalArgumentException(
                    "El tipo de comprobante debe ser Factura o Boleta");
        }

    }



    //Metodo que valida que la cantidad solicitada sea correcta
    //y que exista suficiente stock del producto.
    public void validarCantidad(Product product, Integer quantity) {

        // Comprueba si la cantidad es null (no se ha ingresado)
        // o si es menor que 1.
        // Si alguna condición se cumple, lanza un error.
        if (quantity == null || quantity < 1) {

            throw new IllegalArgumentException(
                    "La cantidad debe ser como mínimo 1");
        }

        //Comprueba si la cantidad solicitada es mayor
        //que el stock disponible del producto.
        if (quantity > product.getStock()) {

            //Si no hay suficiente stock, lanza un error
            //indicando el nombre del producto.
            throw new IllegalArgumentException("Stock insuficiente para el producto: " + product.getName());
        }
    }


    //Calculamos subtotal: Subtotal=Cantidad x Precio unitario
    public Double calcularSubtotal(Integer quantity, Double price) {
        return quantity * price;
    }





    @Transactional
    public Invoice guardarComprobante(InvoiceForm form) {

        // 1. Validar los productos seleccionados.
        validarDetalles(form);
        validarProductosRepetidos(form);

        // 2. Buscar al cliente y validar el tipo de comprobante.
        Customer customer = buscarCliente(form.getCustomerId());
        validarTipoDocumento(form.getType(), customer);

        // 3. Validar todos los productos y sus cantidades
        // antes de guardar el comprobante.
        for (var detalleForm : form.getDetails()) {

            if (!detalleForm.isSelected()) {
                continue;
            }

            Product product = buscarProducto(detalleForm.getProductId());
            validarCantidad(product, detalleForm.getQuantity());
        }

        // 4. Guardar la cabecera.
        Invoice invoice = new Invoice();
        invoice.setType(form.getType());
        invoice.setDate(form.getDate());
        invoice.setCustomer(customer);

        invoice = invoiceRepository.save(invoice);

        // 5. Guardar solo los productos seleccionados.
        for (var detalleForm : form.getDetails()) {

            if (!detalleForm.isSelected()) {
                continue;
            }

            Product product = buscarProducto(detalleForm.getProductId());

            Double subtotal = calcularSubtotal(
                    detalleForm.getQuantity(),
                    product.getPrice()
            );

            InvoiceDetail detalle = new InvoiceDetail();
            detalle.setInvoice(invoice);
            detalle.setProduct(product);
            detalle.setQuantity(detalleForm.getQuantity());
            detalle.setPrice(product.getPrice());
            detalle.setSubtotal(subtotal);

            invoiceDetailRepository.save(detalle);

            // Descontar las unidades vendidas.
            product.setStock(
                    product.getStock() - detalleForm.getQuantity()
            );

            productRepository.save(product);
        }

        return invoice;
    }


    public void validarDetalles(InvoiceForm form) {

        if (form.getDetails() == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar al menos un producto");
        }

        boolean haySeleccionado = false;

        for (var detalle : form.getDetails()) {

            if (detalle.isSelected()) {
                haySeleccionado = true;

                if (detalle.getProductId() == null) {
                    throw new IllegalArgumentException(
                            "Debe seleccionar un producto válido");
                }

                if (detalle.getQuantity() == null) {
                    throw new IllegalArgumentException(
                            "La cantidad es obligatoria");
                }

                if (detalle.getQuantity() < 1) {
                    throw new IllegalArgumentException(
                            "La cantidad debe ser como mínimo 1");
                }
            }
        }

        if (!haySeleccionado) {
            throw new IllegalArgumentException(
                    "Debe seleccionar al menos un producto");
        }
    }


    public void validarProductosRepetidos(InvoiceForm form) {

        Set<Integer> productosSeleccionados = new HashSet<>();

        for (var detalle : form.getDetails()) {

            if (detalle.isSelected()) {

                Integer productId = detalle.getProductId();

                if (productId != null &&
                        !productosSeleccionados.add(productId)) {

                    throw new IllegalArgumentException(
                            "No puede agregar el mismo producto más de una vez");
                }
            }
        }
    }

















}
