
package com.example.Lab7_Practica.repository;

import com.example.Lab7_Practica.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
}
