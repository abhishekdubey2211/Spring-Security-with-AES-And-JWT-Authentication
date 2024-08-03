package com.shopping.portal.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.shopping.portal.model.EndUser;
import com.shopping.portal.repository.EnduserRepository;

/**
 *
 * @author Abhishek
 */
@Service
public class EndUserDetailService implements UserDetailsService {

    @Autowired
    EnduserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<EndUser> userByName = userRepository.findByUsername(username);
        if (userByName.isPresent()) {
            EndUser user = userByName.get();
            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(getRoles(user))
                    .build();
        } else {
            Optional<EndUser> userByEmail = userRepository.findByEmail(username);
            if (userByEmail.isPresent()) {
                EndUser user = userByEmail.get();
                return User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .roles(getRoles(user))
                        .build();
            } else {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
        }
    }

    private String[] getRoles(EndUser user) {
        if (user.getRole() == null) {
            return new String[]{"USER"};
        }
        return user.getRole().split(",");
    }
}
