package br.com.reliquiasdamagia.api.controller;

import br.com.reliquiasdamagia.api.entity.CartItem;
import br.com.reliquiasdamagia.api.entity.ShoppingCart;
import br.com.reliquiasdamagia.api.entity.Status;
import br.com.reliquiasdamagia.api.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/shopping-cart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService cartService;

    // Endpoint para adicionar item ao carrinho (acessível para USER e CONSULTANT)
    @PreAuthorize("hasAnyAuthority('USER', 'CONSULTANT')")
    @PostMapping("/{cartId}/add-item")
    public ResponseEntity<ShoppingCart> addItemToCart(@PathVariable Long cartId, @RequestBody CartItem item) {
        ShoppingCart updatedCart = cartService.addItemToCart(cartId, item.getProduct(), item.getQuantity());
        return ResponseEntity.ok(updatedCart);
    }

    // Endpoint para obter carrinhos abandonados (apenas ADMIN)
//    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping("/abandoned")
//    public List<ShoppingCart> getAbandonedCarts() {
//        return cartService.getAbandonedCarts();
//    }

    // Endpoint para visualizar um carrinho específico (acessível para USER e CONSULTANT)
//    @PreAuthorize("hasAnyRole('USER', 'CONSULTANT')")
//    @GetMapping("/{cartId}")
//    public ResponseEntity<ShoppingCart> getCartById(@PathVariable Long cartId) {
//        ShoppingCart cart = cartService.getCartById(cartId);
//        return ResponseEntity.ok(cart);
//    }

    // Endpoint para remover item do carrinho (acessível para USER e CONSULTANT)
    @PreAuthorize("hasAnyRole('USER', 'CONSULTANT')")
    @DeleteMapping("/{cartId}/remove-item/{itemId}")
    public ResponseEntity<ShoppingCart> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {
        ShoppingCart updatedCart = cartService.removeItemFromCart(cartId, itemId);
        return ResponseEntity.ok(updatedCart);
    }

    // Endpoint para processar pagamento (acessível para USER e CONSULTANT)
    @PreAuthorize("hasAnyRole('USER', 'CONSULTANT')")
    @PostMapping("/{cartId}/checkout")
    public ResponseEntity<?> processPayment(@PathVariable Long cartId, @RequestParam BigDecimal amount) {
        cartService.processPayment(cartId, amount);
        return ResponseEntity.ok("Pagamento processado com sucesso.");
    }

    // Endpoint para atualizar o status do carrinho (apenas ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{cartId}/status")
    public ResponseEntity<?> updateCartStatus(@PathVariable Long cartId, @RequestParam Status status) {
        cartService.updateCartStatus(cartId, status);
        return ResponseEntity.ok("Status do carrinho atualizado.");
    }
}
