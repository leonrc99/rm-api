package br.com.reliquiasdamagia.api.repository;

import br.com.reliquiasdamagia.api.entity.ShoppingCart;
import br.com.reliquiasdamagia.api.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    List<ShoppingCart> findByStatusAndLastModifiedBefore(Status status, LocalDateTime time);
}
