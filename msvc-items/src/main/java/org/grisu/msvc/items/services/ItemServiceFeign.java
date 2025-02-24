package org.grisu.msvc.items.services;

import lombok.RequiredArgsConstructor;
import org.grisu.libs.msvc.commons.entities.Product;
import org.grisu.msvc.items.clients.ProductFeignClient;
import org.grisu.msvc.items.models.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class ItemServiceFeign implements ItemService {

    private final ProductFeignClient client;

    @Override
    public List<Item> listarTodos() {
        return client.listar()
                .stream()
                .map(product -> new Item(product, new Random().nextInt(10) + 1))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Item> buscarPorId(Long id) {
        Product product = client.buscarPorId(id);
        if (product.getId() == null) {
            return Optional.empty();
        }
        return Optional.of(new Item(product, new Random().nextInt(10) + 1));
    }
}
