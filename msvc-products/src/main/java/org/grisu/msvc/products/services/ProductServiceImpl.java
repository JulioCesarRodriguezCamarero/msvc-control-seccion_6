package org.grisu.msvc.products.services;

import lombok.RequiredArgsConstructor;
import org.grisu.libs.msvc.commons.entities.Product;
import org.grisu.msvc.products.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Service
public class ProductServiceImpl implements ProductServiceInter {

    private final ProductRepository repository;

    @Transactional(readOnly = true)
    @Override
    public List<Product> listar() {
        return repository.findAll();
    }
    @Transactional(readOnly = true)
    @Override
    public Optional<Product> buscarPorId(Long id) {
        return repository.findById(id);
    }



    @Transactional
    @Override
    public void guardar(Product product) {
        repository.save(product);
    }
    @Transactional
    @Override
    public void eliminar(Product product) {
        repository.delete(product);
    }
}
