
package com.example.Lab7_Practica.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//Validaciones obligatorias:
//El comprobante debe tener al menos un producto
//No se puede repetir el mismo producto en un comprobante
//Validar stock (No permitir cantidades mayores al stock disponible)
//Validar tipo de comprobante (Factura solo clientes con RUC y Boleta solo permite
//DNI)
//Fecha obligatoria y no puede ser futura
//Cálculo obligatorio: El subtotal es la cantidad x precio y el total es la suma de
//subtotales
//Seleccionar el tipo de comprobante (Boleta o Factura)
//Seleccionar cliente
//Seleccionar por lo menos un producto
//Ingresar cantidad por cada producto


public class InvoiceForm {

    @NotBlank(message = "Debe seleccionar el tipo de comprobante")
    private String type;

    @NotNull(message = "La fecha es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate date;

    @NotNull(message = "Debe seleccionar un cliente")
    private Integer customerId;

    private List<InvoiceDetailForm> details = new ArrayList<>();

    public InvoiceForm() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public List<InvoiceDetailForm> getDetails() {
        return details;
    }

    public void setDetails(List<InvoiceDetailForm> details) {
        this.details = details;
    }
}
