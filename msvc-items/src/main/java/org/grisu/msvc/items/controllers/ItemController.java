package org.grisu.msvc.items.controllers;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.grisu.libs.msvc.commons.entities.Product;
import org.grisu.msvc.items.models.Item;
import org.grisu.msvc.items.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
public class ItemController {
    private final ItemService service;
    private final CircuitBreakerFactory circuitBreakerFactory;


    @GetMapping
    public ResponseEntity<?> listar(@RequestParam(name = "name", required = false) String name,
                                    @RequestHeader(name = "token-request", required = false) String token) {
        System.out.println(name);
        System.out.println(token);
        return ResponseEntity.ok().body(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Product product = new Product();
        product.setId(1L);
        product.setCreatedAt(LocalDateTime.now());
        product.setName("Producto Alternativo");
        product.setPrice(1.00);

        Object resultado = circuitBreakerFactory
                .create("msvc-items")
                .run(() -> service.buscarPorId(id), throwable -> new Item(product, 5));
        return resultado == null ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje", "El recurso no fue encontrado"))
                :
                ResponseEntity.ok(resultado);
    }


    @CircuitBreaker(name = "msvc-items", fallbackMethod = "getProductAlt")
    @GetMapping("details/{id}")
    public ResponseEntity<?> buscarPorIdCircuitBreaker(@PathVariable Long id) {
        Optional<Item> resultado = service.buscarPorId(id);
        if (resultado.isPresent()) {
            return ResponseEntity.ok(resultado.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("mensaje", "El recurso no fue encontrado"));
    }

    @TimeLimiter(name = "msvc-items")
    @GetMapping("details2/{id}")
    public CompletableFuture<?> buscarPorIdCircuitBreaker2(@PathVariable Long id) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Item> resultado = service.buscarPorId(id);
            if (resultado.isPresent()) {
                return ResponseEntity.ok(resultado.get());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "El recurso no fue encontrado"));
        });
    }

    public ResponseEntity<?> getProductAlt(Throwable throwable) {
        Product product = new Product();
        product.setId(1L);
        product.setCreatedAt(LocalDateTime.now());
        product.setName("Producto Alternativo");
        product.setPrice(1.00);
        return ResponseEntity.ok(new Item(product, 5));
    }
}
