package br.com.reliquiasdamagia.api.controller;

import br.com.reliquiasdamagia.api.entity.ShoppingCart;
import br.com.reliquiasdamagia.api.entity.Status;
import br.com.reliquiasdamagia.api.entity.User;
import br.com.reliquiasdamagia.api.service.ShoppingCartService;
import br.com.reliquiasdamagia.api.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shopping-cart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService cartService;

    // Endpoint para criar um novo carrinho (acessível para USER e CONSULTANT)
    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('USER', 'CONSULTANT')")
    public ResponseEntity<String> createCart(@AuthenticationPrincipal User user) {
        try {
            ShoppingCart newCart = cartService.createCart(user.getId());
            return ResponseEntity.ok("Carrinho criado com sucesso\n" + newCart);
        } catch (AccessDeniedException e) {            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Erro de autorização: você não tem permissão para realizar esta ação.");

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Erro de autenticação: você precisa estar autenticado para realizar esta ação.");

        } catch (ConfigDataResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Recurso não encontrado: " + e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar o carrinho: " + e.getMessage());
        }
    }

    // Endpoint para adicionar item ao carrinho (acessível para USER e CONSULTANT)
    @PostMapping("/add-item")
    @PreAuthorize("hasAnyAuthority('USER', 'CONSULTANT')")
    public ResponseEntity<String> addItemToCart(
            @RequestParam Long productId,
            @RequestParam Integer quantity,
            Authentication authentication
    ) {
        try {
            Long userId = getUserIdFromAuthentication(authentication);

            ShoppingCart updatedCart = cartService.addItemToCart(userId, productId, quantity);
            return ResponseEntity.ok("Produto adicionado ao carrinho:\n" + updatedCart);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Erro de autorização: você não tem permissão para realizar esta ação.");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Erro de autenticação: você precisa estar autenticado para realizar esta ação.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao adicionar o produto: " + e.getMessage());
        }
    }
    
    // Endpoint para obter carrinhos abandonados (apenas ADMIN)
    @GetMapping("/abandoned")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getAbandonedCarts() {
        try {
            List<ShoppingCart> abandonedCarts = cartService.getAbandonedCarts();
            return ResponseEntity.ok("Carrinhos abandonados:\n" + abandonedCarts);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Erro de autorização: você não tem permissão para realizar esta ação.");

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Erro de autenticação: você precisa estar autenticado para realizar esta ação.");

        } catch (ConfigDataResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Recurso não encontrado: " + e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao encontrar carrinhos: " + e.getMessage());
        }
    }

    // Endpoint para visualizar um carrinho específico (acessível para USER e CONSULTANT)
    @GetMapping("/{cartId}")
    @PreAuthorize("hasAnyAuthority('USER', 'CONSULTANT')")
    public ResponseEntity<String> getCartById(@PathVariable Long cartId) {
        try {
            ShoppingCart cart = cartService.getCartById(cartId);
            return ResponseEntity.ok("Carrinho de compras encontrado!\n" + cart);

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Erro de autorização: você não tem permissão para realizar esta ação.");

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Erro de autenticação: você precisa estar autenticado para realizar esta ação.");

        } catch (ConfigDataResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Recurso não encontrado: " + e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar carrinho: " + e.getMessage());
        }
    }

    // Endpoint para remover item do carrinho (acessível para USER e CONSULTANT)
    @DeleteMapping("/{cartId}/remove-item/{itemId}")
    @PreAuthorize("hasAnyAuthority('USER', 'CONSULTANT')")
    public ResponseEntity<String> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {
        try {
            ShoppingCart updatedCart = cartService.removeItemFromCart(cartId, itemId);
            return ResponseEntity.ok("" + updatedCart);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Erro de autorização: você não tem permissão para realizar esta ação.");

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Erro de autenticação: você precisa estar autenticado para realizar esta ação.");

        } catch (ConfigDataResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Recurso não encontrado: " + e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar carrinho: " + e.getMessage());
        }
    }


    // Endpoint para atualizar o status do carrinho (apenas ADMIN)
    @PatchMapping("/{cartId}/status")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updateCartStatus(@PathVariable Long cartId, @RequestParam Status status) {
        try {
            cartService.updateCartStatus(cartId, status);
            return ResponseEntity.ok("Status do carrinho atualizado.");
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Erro de autorização: você não tem permissão para realizar esta ação.");

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Erro de autenticação: você precisa estar autenticado para realizar esta ação.");

        } catch (ConfigDataResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Recurso não encontrado: " + e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar status do carrinho: " + e.getMessage());
        }
    }


    private Long getUserIdFromAuthentication(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }
}

//    public ResponseEntity<?> processPayment(@PathVariable Long cartId, @RequestParam BigDecimal amount) {}
