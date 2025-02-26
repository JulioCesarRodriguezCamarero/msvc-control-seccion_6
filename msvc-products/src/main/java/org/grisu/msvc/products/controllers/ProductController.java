package org.grisu.msvc.products.controllers;

import lombok.RequiredArgsConstructor;
import org.grisu.libs.msvc.commons.entities.Product;
import org.grisu.msvc.products.services.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
public class ProductController {
    private final ProductServiceImpl service;

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok().body(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> buscar(@PathVariable Long id) {
        Optional<Product> product = service.buscarPorId(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Product> guardar(@RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        product.setCreatedAt(LocalDate.now());
        service.guardar(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(product -> {
                    service.eliminar(product);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}

