package org.grisu.msvc.products.services;


import org.grisu.libs.msvc.commons.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductServiceInter {

    List<Product> listar();

    Optional<Product> buscarPorId(Long id);

    Product guardar(Product product);

    void eliminar(Product product);

}
