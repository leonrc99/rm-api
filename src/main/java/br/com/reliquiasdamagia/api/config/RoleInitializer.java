package br.com.reliquiasdamagia.api.config;

import br.com.reliquiasdamagia.api.entity.Role;
import br.com.reliquiasdamagia.api.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class RoleInitializer implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (roleRepository.findAll().isEmpty()) {
            List<Role> roles = Arrays.asList(
                    new Role("USER"),
                    new Role("CONSULTANT"),
                    new Role("ADMIN")
            );
            roleRepository.saveAll(roles);
        }
    }
}
