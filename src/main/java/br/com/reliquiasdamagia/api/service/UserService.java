package br.com.reliquiasdamagia.api.service;

import br.com.reliquiasdamagia.api.entity.Role;
import br.com.reliquiasdamagia.api.entity.User;
import br.com.reliquiasdamagia.api.repository.RoleRepository;
import br.com.reliquiasdamagia.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;


    public boolean passwordMatches(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    @Transactional
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> roles = user.getRoles().stream()
                .map(role -> roleRepository.findByName(role.getName())
                        .orElseThrow(() -> new RuntimeException("Role not found: " + role.getName())))
                .collect(Collectors.toSet());

        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long userId, User updatedUser, String requesterRole) {
        Optional<User> existingUserOpt = userRepository.findById(userId);

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            // Permitir que o ADMIN edite qualquer usuário
            if ("ROLE_ADMIN".equals(requesterRole) || existingUser.getId().equals(userId)) {
                // Atualiza os campos desejados, exceto a role
                existingUser.setUsername(updatedUser.getUsername());
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword())); // Se a senha for alterada
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setName(updatedUser.getName());
                existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
                existingUser.setAddress(updatedUser.getAddress());
                existingUser.setPhoneNumber(updatedUser.getPhoneNumber());

                return userRepository.save(existingUser);
            } else {
                throw new SecurityException("Acesso negado: Você não pode editar este usuário.");
            }
        } else {
            throw new RuntimeException("Usuário não encontrado.");
        }
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public List<User> getAllUsers () {
        return userRepository.findAll();
    }

}