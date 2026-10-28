
package com.example.Lab7_Practica.repository;

import com.example.Lab7_Practica.entity.InvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, Integer> {

    //devuelve una lista porque un comprobante puede tener varios productos.
    List<InvoiceDetail> findByInvoiceId(Integer invoiceId);

}
