package com.escolatecnica.api.user.repository;

import com.escolatecnica.api.user.model.Role;
import com.escolatecnica.api.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    User readByCpfAndOrganizationId(String cpf, UUID organizationId);

    boolean existsByCpfAndOrganizationId(String cpf, UUID organizationId);

    List<User> readAllByOrganizationId(UUID organizationId);

    Optional<User> readByIdAndOrganizationId(UUID id, UUID organizationId);

    List<User> readByOrganizationIdAndRole(UUID organizationId, Role role);

}
