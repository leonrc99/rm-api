package br.com.reliquiasdamagia.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do produto é obrigatório")
    private String name;

    @NotBlank(message = "A descrição do produto é obrigatória")
    private String description;

    @Positive(message = "O preço do produto deve ser positivo")
    private BigDecimal price;

    @Positive(message = "A quantidade do produto deve ser positiva")
    private Integer stock;
}
