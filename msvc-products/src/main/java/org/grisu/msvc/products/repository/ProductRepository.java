package org.grisu.msvc.products.repository;

import org.grisu.libs.msvc.commons.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
