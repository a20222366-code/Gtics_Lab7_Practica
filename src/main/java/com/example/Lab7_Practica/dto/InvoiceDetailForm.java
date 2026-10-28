
package com.example.Lab7_Practica.dto;

public class InvoiceDetailForm {

    // ID del producto que aparece en la fila.
    private Integer productId;

    // Cantidad que desea comprar el usuario.
    private Integer quantity;

    // Indica si el usuario seleccionó el producto.
    private boolean selected;

    public InvoiceDetailForm() {
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
