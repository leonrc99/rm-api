package br.com.reliquiasdamagia.api.controller;

import br.com.reliquiasdamagia.api.entity.Product;
import br.com.reliquiasdamagia.api.service.ProductService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
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
    public ResponseEntity<String> createProduct(@RequestBody Product product) {
        try {
            Product newProduct = productService.createProduct(product);
            return ResponseEntity.ok("Produto criado com sucesso!\n" + newProduct);

        } catch (AuthenticationException e) {  // Para falhas de autenticação
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Erro de autenticação: você precisa estar autenticado para realizar esta ação.");

        } catch (AccessDeniedException e) {  // Para falhas de autorização
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Erro de autorização: você não tem permissão para realizar esta ação.");

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro de integridade dos dados: " + e.getMessage());

        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro de validação dos dados: " + e.getMessage());

        } catch (ConfigDataResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Recurso não encontrado: " + e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar o produto: " + e.getMessage());
        }
    }

    // Endpoint para obter todos os produtos (acessível para USER, CONSULTANT e ADMIN)
    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER', 'CONSULTANT', 'ADMIN')")
    public ResponseEntity<?> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            return ResponseEntity.ok(products);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Erro de autenticação: você precisa estar autenticado para realizar esta ação.");

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Erro de autorização: você não tem permissão para realizar esta ação.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao obter produtos: " + e.getMessage());
        }
    }

    // Endpoint para atualizar produto (apenas ADMIN)
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok("Produto atualizado com sucesso!\n" + updatedProduct);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Erro de autenticação: você precisa estar autenticado para realizar esta ação.");

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Erro de autorização: você não tem permissão para realizar esta ação.");

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro de integridade dos dados: " + e.getMessage());

        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro de validação dos dados: " + e.getMessage());

        } catch (ConfigDataResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Produto não encontrado: " + e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar o produto: " + e.getMessage());
        }
    }

    // Endpoint para deletar produto (apenas ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        try {
            Optional<Product> product = productService.getProductById(id);
            productService.deleteProduct(id);
            return ResponseEntity.ok("Produto excluído com sucesso.\n" + product);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Erro de autenticação: você precisa estar autenticado para realizar esta ação.");

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Erro de autorização: você não tem permissão para realizar esta ação.");

        } catch (ConfigDataResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Produto não encontrado: " + e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao excluir o produto: " + e.getMessage());
        }
    }

    // Endpoint para obter um produto específico por ID (acessível para USER, CONSULTANT e ADMIN)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'CONSULTANT', 'ADMIN')")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            Optional<Product> product = productService.getProductById(id);
            if (product.isPresent()) {
                return ResponseEntity.ok(product);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Produto não encontrado");
            }

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Erro de autenticação: você precisa estar autenticado para realizar esta ação.");

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Erro de autorização: você não tem permissão para realizar esta ação.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao obter o produto: " + e.getMessage());
        }
    }
}
