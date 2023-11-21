package com.escolatecnica.api.user.dto;

import com.escolatecnica.api.user.model.Role;

import java.util.UUID;

public record DTOUserRequest(UUID id, UUID organizationId, String name, String lastName, String cpf, String password, String email, Role role, Boolean enabledAccess) {
}