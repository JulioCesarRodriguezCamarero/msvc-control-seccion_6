package org.grisu.msvc.items.controllers;

import lombok.RequiredArgsConstructor;
import org.grisu.msvc.items.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
public class ItemController {
    private final ItemService service;

    @GetMapping
    public ResponseEntity<?> listar(@RequestParam(name = "name", required = false) String name,
    @RequestHeader(name = "token-request", required = false) String token) {
        System.out.println(name);
        System.out.println(token);
        return ResponseEntity.ok().body(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Object resultado = service.buscarPorId(id);
        return resultado == null ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje","El recurso no fue encontrado"))
                :
                ResponseEntity.ok(resultado);
    }
}
