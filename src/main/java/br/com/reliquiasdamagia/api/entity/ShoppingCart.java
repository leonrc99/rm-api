package br.com.reliquiasdamagia.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "shopping_carts")
public class ShoppingCart {  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Status status = Status.DRAFT;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "shoppingCart")
    @ToString.Exclude
    private List<CartItem> items = new ArrayList<>();

    private Long userId;

    private LocalDateTime lastModified = LocalDateTime.now();
}
