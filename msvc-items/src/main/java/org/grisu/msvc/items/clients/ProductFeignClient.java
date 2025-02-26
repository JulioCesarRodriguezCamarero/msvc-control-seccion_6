package org.grisu.msvc.items.clients;

import org.grisu.libs.msvc.commons.entities.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-products")
public interface ProductFeignClient {

    @GetMapping
    List<Product> listar();

    @GetMapping("/{id}")
   Product buscarPorId(@PathVariable Long id);

    @PostMapping
    Product guardar(@RequestBody Product product);

    @PutMapping("/{id}")
    Product actualizar(@PathVariable Long id, @RequestBody Product product);

    @DeleteMapping("/{id}")
    void eliminar(@PathVariable Long id);

}
