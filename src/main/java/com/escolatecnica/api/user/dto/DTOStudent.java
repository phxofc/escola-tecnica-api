package com.escolatecnica.api.user.dto;

import java.util.UUID;
public record DTOStudent(UUID id, String name, String lastName, String cpf, String email, String fileNameCpf, String fileNameRg, String fileNameCr, Boolean enabledAccess) {
}