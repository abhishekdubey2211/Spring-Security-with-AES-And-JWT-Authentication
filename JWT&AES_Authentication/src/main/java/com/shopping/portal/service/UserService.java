package com.shopping.portal.service;

import java.util.List;

import com.shopping.portal.dto.EndUserDTO;

public interface UserService {
	public EndUserDTO registerUser(EndUserDTO user);

	public EndUserDTO updateUser(EndUserDTO user);

	public EndUserDTO getUserById(Long id);

	public List<Object> disableUser(Long id);

	public List<Object> getAllActiveUser();
}
