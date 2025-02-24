package org.grisu.msvc.items.controllers;

import lombok.RequiredArgsConstructor;
import org.grisu.msvc.items.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
public class ItemController {
    private final ItemService service;

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok().body(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Object resultado = service.buscarPorId(id);
        return resultado == null ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("El recurso no fue encontrado")
                :
                ResponseEntity.ok(resultado);
    }
}
