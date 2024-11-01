package br.com.reliquiasdamagia.api.service;

import br.com.reliquiasdamagia.api.entity.*;
import br.com.reliquiasdamagia.api.repository.ProductRepository;
import br.com.reliquiasdamagia.api.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartService {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ProductRepository productRepository;

    public ShoppingCart createCart(Long userId) {
        ShoppingCart cart = new ShoppingCart();
        cart.setUserId(userId);
        cart.setStatus(Status.DRAFT);

        return shoppingCartRepository.save(cart);
    }

    // Metodo para buscar um carrinho em status "DRAFT" para o usuário
    private Optional<ShoppingCart> getDraftCartForUser(Long userId) {
        return shoppingCartRepository.findByUserIdAndStatus(userId, Status.DRAFT);
    }

    public ShoppingCart addItemToCart(Long userId, Long productId, Integer quantity) {
        // Tenta obter um carrinho com status "DRAFT" para o usuário
        ShoppingCart cart = getDraftCartForUser(userId)
                .orElseGet(() -> createCart(userId)); // Cria um novo carrinho caso não exista um em status "DRAFT"

        // Busca o produto pelo ID
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com o ID: " + productId));

        // Verifica se o item já está no carrinho
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + quantity;

            // Verifica o estoque do produto
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

    public ShoppingCart getCartById(Long cartId) {
        return shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado com ID: " + cartId));
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


    public void updateCartStatus(Long cartId, Status status) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado com o id " + cartId));

        cart.setStatus(status);
        cart.setLastModified(LocalDateTime.now());
        shoppingCartRepository.save(cart);
    }

    public List<ShoppingCart> getAbandonedCarts() {
        return shoppingCartRepository.findByStatus(Status.ABANDONED);
    }

}


//    public void processPayment(Long cartId, BigDecimal amount) {}