package org.grisu.msvc.products.services;

import org.grisu.msvc.products.entities.Product;

import java.util.List;

public interface ProductServiceInter {
    List<Product> listar();
    Product buscar(Long id);
    void guardar(Product product);
    void eliminar(Product product);

}
