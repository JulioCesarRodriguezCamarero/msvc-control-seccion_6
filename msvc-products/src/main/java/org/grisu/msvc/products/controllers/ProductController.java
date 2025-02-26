package org.grisu.msvc.products.controllers;

import lombok.RequiredArgsConstructor;
import org.grisu.libs.msvc.commons.entities.Product;
import org.grisu.msvc.products.services.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
public class ProductController {
    private final ProductServiceImpl service;

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok().body(service.listar());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Product> buscar(@PathVariable Long id) throws InterruptedException {
        if(id.equals(10L)){
            throw new IllegalStateException("Error");
        }
        if (id.equals(7L)){
            TimeUnit.SECONDS.sleep(4L);
        }
        Optional<Product> product = service.buscarPorId(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
