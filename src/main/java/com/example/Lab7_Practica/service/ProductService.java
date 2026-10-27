
package com.example.Lab7_Practica.service;

import com.example.Lab7_Practica.entity.Product;
import com.example.Lab7_Practica.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    // Recibimos el repositorio para acceder a la tabla product.
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Verifica si el nombre ya pertenece a otro producto.
    public boolean nombreDuplicado(String name, Integer id) {
        if (id == null) {
            // Si es un producto nuevo, comprobamos si el nombre existe.
            return productRepository.existsByName(name);
        }

        // Si estamos editando, ignoramos el producto actual.
        return productRepository.existsByNameAndIdNot(name, id);
    }

    // Guarda un producto nuevo o actualiza uno existente.
    public Product guardar(Product product) {
        return productRepository.save(product);
    }

    
}
