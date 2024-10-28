package br.com.reliquiasdamagia.api.repository;

import br.com.reliquiasdamagia.api.entity.Role;
import br.com.reliquiasdamagia.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}

