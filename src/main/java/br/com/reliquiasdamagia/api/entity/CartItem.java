package br.com.reliquiasdamagia.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    @Positive
    private Integer quantity;

    @ManyToOne
    @ToString.Exclude
    private ShoppingCart shoppingCart;
}
