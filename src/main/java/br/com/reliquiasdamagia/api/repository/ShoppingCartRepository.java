package br.com.reliquiasdamagia.api.repository;

import br.com.reliquiasdamagia.api.entity.ShoppingCart;
import br.com.reliquiasdamagia.api.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    List<ShoppingCart> findByStatus(Status status);
    Optional<ShoppingCart> findByUserIdAndStatus(Long userId, Status status);
}
