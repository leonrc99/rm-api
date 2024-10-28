package br.com.reliquiasdamagia.api.controller;

import br.com.reliquiasdamagia.api.entity.Product;
import br.com.reliquiasdamagia.api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    // Endpoint para criar produto (apenas ADMIN)
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.createProduct(product));
    }

    // Endpoint para obter todos os produtos (acessível para USER, CONSULTANT e ADMIN)
    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER', 'CONSULTANT', 'ADMIN')")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // Endpoint para atualizar produto (apenas ADMIN)
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok(updatedProduct);
    }

    // Endpoint para deletar produto (apenas ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para obter um produto específico por ID (acessível para USER, CONSULTANT e ADMIN)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'CONSULTANT', 'ADMIN')")
    public ResponseEntity<Optional<Product>> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }
}
