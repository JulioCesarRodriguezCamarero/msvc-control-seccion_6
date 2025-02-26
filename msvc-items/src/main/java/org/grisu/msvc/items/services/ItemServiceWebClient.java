package org.grisu.msvc.items.services;

import lombok.RequiredArgsConstructor;
import org.grisu.libs.msvc.commons.entities.Product;
import org.grisu.msvc.items.models.Item;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

//@Primary
@RequiredArgsConstructor
@Service
public class ItemServiceWebClient implements ItemService {

    private final WebClient.Builder client;

    @Override
    public List<Item> listarTodos() {
        return client.build()
                .get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Product.class)
                .map(product -> new Item(product, new Random().nextInt(10) + 1))
                .collectList()
                .block();
    }

    @Override
    public Optional<Item> buscarPorId(Long id) {
        Map<String, Long> params = new HashMap<>();
        params.put("id", id);

        return Optional.ofNullable(
                client.build()
                        .get()
                        .uri("/{id}", params)
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(Product.class)
                        .map(product -> new Item(product, new Random().nextInt(10) + 1))
                        .block());

    }

    @Override
    public Product guardar(Product product) {
        return client.build()
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(product)
                .retrieve()
                .bodyToMono(Product.class)
                .block();
    }

    @Override
    public Product actualizar(Product product, Long id) {
        return client.build()
                .put()
                .uri("/{id}", Collections.singletonMap("id", id))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(product)
                .retrieve()
                .bodyToMono(Product.class)
                .block();
    }

    @Override
    public void eliminar(Long id) {
        client.build()
                .delete()
                .uri("/{id}", Collections.singletonMap("id", id))
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
