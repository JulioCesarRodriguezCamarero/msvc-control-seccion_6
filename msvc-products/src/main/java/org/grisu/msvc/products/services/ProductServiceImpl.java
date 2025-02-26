package org.grisu.msvc.products.services;

import lombok.RequiredArgsConstructor;
import org.grisu.libs.msvc.commons.entities.Product;
import org.grisu.msvc.products.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Service
public class ProductServiceImpl implements ProductServiceInter {

    private final ProductRepository repository;
    private final Environment environment;

    @Transactional(readOnly = true)
    @Override
    public List<Product> listar() {
        return repository.findAll().stream()
                .map(product -> {
                    product.setPort(Integer.parseInt(Objects.requireNonNull(environment.getProperty("local.server.port"))));
                    return product;
                }).collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    @Override
    public Optional<Product> buscarPorId(Long id) {
        return repository.findById(id)
                .map(product -> {
                    product.setPort(Integer.parseInt(Objects.requireNonNull(environment.getProperty("local.server.port"))));
                    return product;
                });
    }



    @Transactional
    @Override
    public Product guardar(Product product) {
      return repository.save(product);
    }
    @Transactional
    @Override
    public void eliminar(Product product) {
        repository.delete(product);
    }
}
