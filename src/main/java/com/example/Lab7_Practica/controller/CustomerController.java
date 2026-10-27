
package com.example.Lab7_Practica.controller;

import com.example.Lab7_Practica.entity.Customer;
import com.example.Lab7_Practica.repository.CustomerRepository;
import com.example.Lab7_Practica.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientes")
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final CustomerService customerService;

    public CustomerController(
            CustomerRepository customerRepository,
            CustomerService customerService) {
        this.customerRepository = customerRepository;
        this.customerService = customerService;
    }

    @GetMapping
    public String listar(Model model) {

        // Busca todos los clientes en la base de datos
        // y los envía a la vista con el nombre "clientes".
        model.addAttribute("clientes", customerRepository.findAll());
        return "clientes/lista";
    }

    //Muestra el formulario para registrar un cliente nuevo.
    @GetMapping("/nuevo")
    public String nuevo(Model model) {

        // Crea un objeto Customer vacío y lo envía a la vista.
        // El formulario utilizará este objeto para vincular sus campos.
        model.addAttribute("customer", new Customer());
        return "clientes/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(
            // Vincula los campos del formulario con el objeto Customer
            // y ejecuta las validaciones declaradas con anotaciones.
            @Valid @ModelAttribute("customer") Customer customer,
            // Recoge los errores de las validaciones anteriores.
            BindingResult result,
            //Permite enviar datos a la vista.
            Model model) {

        //Comprueba que el formato del documento corresponda
        //al tipo seleccionado: DNI (8 dígitos) o RUC (11).
        if (!customerService.documentoValidoParaTipo(customer.getDocument(), customer.getDocumentType())) {

            // Añade un error al campo "document".
            // rejectValue recibe: campo, código del error y mensaje.
            //result.rejectValue() se utiliza en Spring MVC (Java) para
            // indicar que un campo de un formulario tiene un error de validación y registrar ese error en el objeto BindingResult.

            //Sus tres argumentos son:
            //-"document": el campo que tiene el error.
            //-documento.duplicado": un código para identificar el error.
            //-El mensaje que se quiere mostrar al usuario.
            //Luego, result.hasErrors() permite saber si hay errores y decidir si se debe volver al formulario o guardar el cliente.
            result.rejectValue(
                    "document",
                    "documento.invalido",
                    "El DNI debe tener 8 dígitos y el RUC 11 dígitos numéricos");
        }


        // Comprueba si el documento ya pertenece a otro cliente.
        // Si el ID es null, normalmente se trata de un registro nuevo.
        // Si tiene ID, excluye al propio cliente al editarlo.
        if (customerService.documentoDuplicado(
                customer.getDocument(), customer.getId())) {
            result.rejectValue(
                    "document",
                    "documento.duplicado",
                    "Este documento ya está registrado");
        }

        if (result.hasErrors()) {
            // Si hay errores, vuelve al formulario para mostrarlos.
            // No guarda el cliente.
            return "clientes/formulario";
        }

        // Si todas las validaciones pasan, guarda el cliente.
        customerService.guardar(customer);
        return "redirect:/clientes";
    }


    // Muestra el formulario con los datos de un cliente existente.
    @GetMapping("/editar/{id}")
    public String editar(
            // Obtiene el ID de la URL.
            @PathVariable Integer id,
            Model model) {

        // Busca el cliente por su ID.
        // Si no existe, devuelve null.
        Customer customer = customerRepository.findById(id).orElse(null);

        // Si no se encuentra el cliente, vuelve a la lista.
        if (customer == null) {
            return "redirect:/clientes";
        }

        // Envía el cliente encontrado a la vista.
        // El formulario mostrará sus datos actuales.
        model.addAttribute("customer", customer);
        return "clientes/formulario";
    }

    //Elimina un cliente a partir de su ID.
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {

        // Comprueba que el cliente exista antes de eliminarlo.
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
        }

        // Vuelve a la lista de clientes después de la operación.
        return "redirect:/clientes";
    }
}
