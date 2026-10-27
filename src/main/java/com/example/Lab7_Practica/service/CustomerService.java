
package com.example.Lab7_Practica.service;

import com.example.Lab7_Practica.entity.Customer;
import com.example.Lab7_Practica.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    //Recibe el repositorio que Spring proporciona
    //Automáticamente mediante la inyección de dependencias
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    //Comprueba si un documento ya está registrado
    //Recibe el documento y el ID del cliente
    public boolean documentoDuplicado(String document, Integer id) {

        //Si el ID es null, significa que normalmente estamos
        //registrando un cliente nuevo
        if (id == null) {

            //Comprueba si algún cliente ya tiene ese documento.
            //Devuelve true si existe y false si no existe.
            return customerRepository.existsByDocument(document);
        }

        //Si hay un ID, normalmente estamos editando un cliente.
        //Comprueba si OTRO cliente tiene el mismo documento,
        //excluyendo al cliente cuyo ID se ha recibido.
        return customerRepository.existsByDocumentAndIdNot(document, id);
    }


    //Comprueba si el documento tiene el formato correcto
    //según el tipo de documento seleccionado.
    public boolean documentoValidoParaTipo(String document, String documentType) {

        //Si falta el documento o el tipo, la validación falla.
        if (document == null || documentType == null) {
            return false;
        }

        //Si el tipo es DNI, comprueba que tenga exactamente
        //8 dígitos del 0 al 9.
        if ("DNI".equals(documentType)) {
            return document.matches("[0-9]{8}");
        }

        //Si el tipo es RUC, comprueba que tenga exactamente
        //11 dígitos del 0 al 9.
        if ("RUC".equals(documentType)) {
            return document.matches("[0-9]{11}");
        }

        //Si el tipo no es DNI ni RUC, devuelve false.
        return false;
    }

    //Recibe un objeto Customer y solicita al repositorio
    //que lo guarde en la base de datos.
    //Devuelve el cliente guardado.
    public Customer guardar(Customer customer) {
        return customerRepository.save(customer);
    }

    
}
