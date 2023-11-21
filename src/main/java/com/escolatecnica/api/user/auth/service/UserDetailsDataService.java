package com.escolatecnica.api.user.auth.service;

import com.escolatecnica.api.user.auth.data.UserDetailsData;
import com.escolatecnica.api.user.model.User;
import com.escolatecnica.api.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@AllArgsConstructor
@Service
@Transactional
public class UserDetailsDataService implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String[] credentials = StringUtils.split(username, ";");
        User user;

        try {
            String cpf = credentials[0];
            UUID orgId = UUID.fromString(credentials[1]);

                user = repository.readByCpfAndOrganizationId(cpf, orgId);
        } catch (Exception exception){
            throw new UsernameNotFoundException(exception.getMessage());
        }
        return new UserDetailsData(user);
    }
}
