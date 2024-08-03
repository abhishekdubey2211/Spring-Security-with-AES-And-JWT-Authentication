package com.shopping.portal.service;

import java.util.List;

import com.shopping.portal.model.EndUser;

public interface UserService {

    List<EndUser> getAllUsers(String role);

    List<EndUser> getAllAdminUsers(String role);

    EndUser updateUserDetails(EndUser user);

    EndUser getUserById(long id);
}
