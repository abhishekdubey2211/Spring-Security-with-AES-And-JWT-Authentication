package com.shopping.portal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopping.portal.exception.CustomApplicationException;
import com.shopping.portal.model.EndUser;
import com.shopping.portal.repository.EnduserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final EnduserRepository userRepository;

    @Autowired
    public UserServiceImpl(EnduserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<EndUser> getAllUsers(String role) {
        List<EndUser> userList = userRepository.findByRole(role);
         userList.stream() .findFirst() .orElseThrow(() -> new CustomApplicationException(404, "No Users Found with role " + role));
         return userList;
    }

    @Override
    public List<EndUser> getAllAdminUsers(String role) {
        return userRepository.findByRole(role);
    }

    @Override
    public EndUser updateUserDetails(EndUser user) {
        return userRepository.findById(user.getId())
                .map(existingUser -> {
                    existingUser.setAddress(user.getAddress());
                    existingUser.setEmail(user.getEmail());
                    existingUser.setPassword(user.getPassword());
                    existingUser.setProfileimg(user.getProfileimg());
                    existingUser.setRole(user.getRole());
                    existingUser.setUsername(user.getUsername());
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new CustomApplicationException(404, "User not found with UserId " + user.getId()));
    }

    @Override
    public EndUser getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomApplicationException(404, "User not found with UserId " + id));
    }
}
