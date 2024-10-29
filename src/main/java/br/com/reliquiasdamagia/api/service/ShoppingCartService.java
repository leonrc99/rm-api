package br.com.reliquiasdamagia.api.service;

import br.com.reliquiasdamagia.api.entity.*;
import br.com.reliquiasdamagia.api.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ShoppingCartService {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    public ShoppingCart createCart(User user) {
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);

        return shoppingCartRepository.save(cart);
    }

    public ShoppingCart addItemToCart(Long cartId, Product product, Integer quantity) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()){
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + quantity;

            if (newQuantity > product.getStock()) {
                throw new RuntimeException("Quantidade solicitada excede o estoque disponível.");
            }

            item.setQuantity(newQuantity);
        } else {
            if (quantity > product.getStock()) {
                throw new RuntimeException("Quantidade solicitada excede o estoque disponível.");
            }

            CartItem newItem = new CartItem();

            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setShoppingCart(cart);

            cart.getItems().add(newItem);
        }

        cart.setLastModified(LocalDateTime.now());

        return shoppingCartRepository.save(cart);
    }

    public ShoppingCart removeItemFromCart(Long cartId, Long itemId) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado com o id " + cartId));

        CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item não encontrado no carrinho com o id " + itemId));

        cart.getItems().remove(itemToRemove);

        return shoppingCartRepository.save(cart);
    }

    public void processPayment(Long cartId, BigDecimal amount) {
        // Configuração e chamada à API do MercadoPago
        // Atualiza status para `PROCESSANDO` ou `FINALIZADO` baseado na resposta
    }

    public void updateCartStatus(Long cartId, Status status) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado com o id " + cartId));

        cart.setStatus(status);
        cart.setLastModified(LocalDateTime.now());
        shoppingCartRepository.save(cart);
    }
}
