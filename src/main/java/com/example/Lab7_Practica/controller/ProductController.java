
package com.example.Lab7_Practica.controller;

import com.example.Lab7_Practica.entity.Product;
import com.example.Lab7_Practica.repository.ProductRepository;
import com.example.Lab7_Practica.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


//Validaciones obligatorias:
//Nombre requerido
//Precio obligatorio y mayor a 0
//Stock mayor o igual a 0
//Nombre no duplicado
//Manejar errores de tipo numérico (ejm: letras en precio)

@Controller
@RequestMapping("/productos")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductService productService;

    public ProductController(ProductRepository productRepository,
                             ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }

    // Muestra todos los productos.
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", productRepository.findAll());
        return "productos/lista";
    }

    // Abre el formulario para registrar un producto.
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("product", new Product());
        return "productos/formulario";
    }

    //Recibe el formulario, valida los datos y guarda el producto.
    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("product") Product product,
            BindingResult result) {

        // Comprobamos el nombre solo si fue ingresado.
        if (product.getName() != null && !product.getName().isBlank()
                && productService.nombreDuplicado(
                product.getName().trim(), product.getId())) {

            result.rejectValue(
                    "name",
                    "nombre.duplicado",
                    "Ya existe un producto con ese nombre");
        }

        // Si hay errores, volvemos al formulario.
        if (result.hasErrors()) {
            return "productos/formulario";
        }

        // Guardamos o actualizamos el producto.
        product.setName(product.getName().trim());
        productService.guardar(product);

        return "redirect:/productos";
    }

    // Busca un producto y abre el mismo formulario para editarlo.
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return "redirect:/productos";
        }

        model.addAttribute("product", product);
        return "productos/formulario";
    }

    // Elimina un producto.
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        }

        return "redirect:/productos";
    }
}
