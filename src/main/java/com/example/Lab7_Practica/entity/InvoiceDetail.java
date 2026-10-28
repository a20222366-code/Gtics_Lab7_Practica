
package com.example.Lab7_Practica.entity;

import jakarta.persistence.*;


//Con uniqueConstraints
//Le estamos diciendo a la base de datos:
//La combinación de invoice_id y product_id no puede repetirse.

//¿Por qué necesitamos esta regla en nuestro proyecto?

//Imagina que registramos estos detalles:
//id	invoice_id	product_id	¿Permitido?
//1	10	5	Sí
//2	10	8	Sí
//3	11	5	Sí
//4	10	5	No

//La primera fila indica que el comprobante 10 contiene el producto 5.
//
//La segunda fila también pertenece al comprobante 10, pero contiene
//el producto 8. Está permitido.
//
//La tercera fila contiene el producto 5, pero pertenece al
//comprobante 11. También está permitido.
//
//
//La cuarta fila intenta registrar nuevamente el producto 5 en el
//comprobante 10. No está permitido, porque esa misma combinación
//ya existe.

//¿Por qué necesitamos esta regla en nuestro proyecto?
//Porque el enunciado de la práctica indica que no debe haber
//productos repetidos en un comprobante.
//
//Queremos que esto:
//Comprobante 10
//
//- Teclado: cantidad 2
//- Mouse: cantidad 3
//
//  Correcto: cada producto aparece una sola vez.
//
//  Comprobante 10
//- Teclado: cantidad 2
//- Mouse: cantidad 3
//- Teclado: cantidad 1
//
//  Incorrecto: el teclado está repetido.

@Entity
@Table(name = "invoice_detail", uniqueConstraints = {@UniqueConstraint(columnNames = {"invoice_id", "product_id"})})

public class InvoiceDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Double subtotal;

    public InvoiceDetail() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
}
