package com.example.Lab7_Practica.repository;

import com.example.Lab7_Practica.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    //¿Existe algún cliente que tenga este documento?
    //Verifica si ya existe un cliente con el mismo documento.
    boolean existsByDocument(String document);

    //¿Existe otro cliente que tenga este documento, pero cuyo ID sea diferente del ID que estoy indicando?
    //Verifica si otro cliente tiene el mismo documento,
    //excluyendo al cliente con el ID indicado (útil al actualizar).
    boolean existsByDocumentAndIdNot(String document, Integer id);

}
