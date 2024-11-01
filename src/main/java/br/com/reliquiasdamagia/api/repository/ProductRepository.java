package br.com.reliquiasdamagia.api.repository;

import br.com.reliquiasdamagia.api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
