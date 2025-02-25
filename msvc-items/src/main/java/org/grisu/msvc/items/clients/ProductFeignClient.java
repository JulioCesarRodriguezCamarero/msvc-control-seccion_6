package org.grisu.msvc.items.clients;

import org.grisu.libs.msvc.commons.entities.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "msvc-products")
public interface ProductFeignClient {

    @GetMapping
    List<Product> listar();

    @GetMapping("/{id}")
   Product buscarPorId(@PathVariable Long id);

}
