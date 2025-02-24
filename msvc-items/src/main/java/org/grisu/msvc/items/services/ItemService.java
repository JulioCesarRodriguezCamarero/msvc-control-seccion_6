package org.grisu.msvc.items.services;


import org.grisu.msvc.items.models.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    List<Item> listarTodos();
    Optional<Item> buscarPorId(Long id);

}
