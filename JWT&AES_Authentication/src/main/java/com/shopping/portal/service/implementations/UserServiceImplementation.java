package com.shopping.portal.service.implementations;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shopping.portal.dto.EndUserDTO;
import com.shopping.portal.dto.EndUserMapper;
import com.shopping.portal.exceptions.CustomException;
import com.shopping.portal.exceptions.ResourceNotFoundException;
import com.shopping.portal.model.Cart;
import com.shopping.portal.model.EndUser;
import com.shopping.portal.model.Role;
import com.shopping.portal.model.UserParameterDetails;
import com.shopping.portal.redis.RedisUtil;
import com.shopping.portal.repository.AddressRepository;
import com.shopping.portal.repository.CartRepository;
import com.shopping.portal.repository.EnduserRepository;
import com.shopping.portal.repository.UserParameterdetailsRepository;
import com.shopping.portal.security.AdvanceEncryptionStandard;
import com.shopping.portal.service.AddressService;
import com.shopping.portal.service.UserParameterDetailsService;
import com.shopping.portal.service.UserService;

@Service
public class UserServiceImplementation implements UserService {

	public static final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImplementation.class);
	private EndUserMapper userMapper = new EndUserMapper();

	@Autowired
	private AdvanceEncryptionStandard aes;

	@Autowired
	private EnduserRepository enduserRepository;

	@Autowired
	UserParameterdetailsRepository parameterdetailsRepository;

	@Autowired
	AddressService addressService;

	@Autowired
	AddressRepository addressRepository;

	@Autowired
	UserParameterDetailsService userParameterDetailService;

	@Override
	public EndUserDTO registerUser(EndUserDTO pushUserDto) {
		try {
			EndUser pushUser = userMapper.toEndUser(pushUserDto);
			checkUserEmailExists(pushUser);
			Set<Role> roles = assignRoles(pushUser.getRole());
			EndUser newUser = new EndUser();
			newUser.setContact(pushUser.getContact());
			newUser.setDesignation(pushUser.getDesignation());
			newUser.setEmail(pushUser.getEmail());
			newUser.setUsername(pushUser.getUsername());
			newUser.setIsactive(1);
			newUser.setIsdelete(0);
			newUser.setRole(pushUser.getRole());
			if (pushUser.getPassword() == null || pushUser.getPassword().isEmpty()) {
				pushUser.setPassword(aes.encryptData("Test1234!"));
			} else {
				pushUser.setPassword(aes.encryptData(pushUser.getPassword()));
			}
			newUser.setPassword(pushUser.getPassword());
			newUser.setProfileimage(pushUser.getProfileimage());
			newUser.setUsercreationdate(sf.format(new Date()));
			newUser.setUserlastupdatedate("");
			newUser.setRoles(roles);
			newUser.setRole(pushUser.getRole());
			EndUser savedUser = enduserRepository.save(newUser);
			savedUser.setCart(Cart.builder().totalamount(0.0).user(savedUser).build());
			EndUserDTO dto = userMapper.toEndUserDTO(savedUser);
			dto.setAddress(pushUserDto.getAddress().stream()
					.map(address -> addressService.addAddress(savedUser, address)).collect(Collectors.toList()));
			dto.setUserParameterDetails(pushUserDto.getUserParameterDetails().stream()
					.map(upd -> userParameterDetailService.addUserParameterDetails(savedUser, upd))
					.collect(Collectors.toList()));
			return dto;
		} catch (Exception e) {
			logger.error("Unexpected error during reegister user ", e);
			throw e;
		}
	}

	@Override
	public EndUserDTO updateUser(EndUserDTO putUserDto) {
		try {
			EndUser putUser = userMapper.toEndUser(putUserDto);
			Set<Role> roles = assignRoles(putUser.getRole());

			EndUser retrivedUser = getSingleUserByUserId(putUser.getId());
			if (!putUser.getEmail().equals(retrivedUser.getEmail())) {
				checkUserEmailExists(putUser);
			}
			retrivedUser.setContact(putUser.getContact());
			retrivedUser.setDesignation(putUser.getDesignation());
			retrivedUser.setEmail(putUser.getEmail());
			retrivedUser.setUsername(putUser.getUsername());
			retrivedUser.setIsactive(1);
			retrivedUser.setIsdelete(0);
			retrivedUser.setRole(putUser.getRole());
			putUser.setPassword(aes.encryptData(putUser.getPassword()));
			retrivedUser.setPassword(putUser.getPassword());
			retrivedUser.setProfileimage(putUser.getProfileimage());
			retrivedUser.setUserlastupdatedate(sf.format(new Date()));
			retrivedUser.setRoles(roles);
			retrivedUser.getUserParameterDetails().clear();
			retrivedUser.getAddress().clear();

			EndUser savedUser = enduserRepository.save(retrivedUser);
			EndUserDTO dto = userMapper.toEndUserDTO(savedUser);

			putUserDto.getUserParameterDetails().forEach(upd -> {
				retrivedUser.getUserParameterDetails()
						.add(userParameterDetailService.addUserParameterDetails(retrivedUser, upd));
			});

			putUserDto.getAddress().forEach(addr -> {
				retrivedUser.getAddress().add(addressService.addAddress(retrivedUser, addr));
			});
			return dto;
		} catch (Exception e) {
			logger.error("Unexpected error during user update", e);
			throw e;
		}
	}

	@Override
	public EndUserDTO getUserById(Long id) {
		try {
			return userMapper.toEndUserDTO(getSingleUserByUserId(id));
		} catch (Exception e) {
			logger.error("Unexpected error during user update", e);
			throw e;
		}
	}

	@Override
	public List<Object> disableUser(Long id) {
		try {
			EndUser retrivedUser = getSingleUserByUserId(id);
			retrivedUser.setIsactive(0);
			retrivedUser.setIsdelete(1);
			retrivedUser.setUserlastupdatedate(sf.format(new Date()));
			EndUser savedUser = enduserRepository.save(retrivedUser);
			return List.of("User with UserId " + id + " is deleted");
		} catch (Exception e) {
			logger.error("Unexpected error during disableUser", e);
			throw e;
		}
	}

	@Override
	public List<Object> getAllActiveUser() {
		try {
			List<Object> userDtoList = new LinkedList<>();
			List<EndUser> userList = enduserRepository.findAll().stream()
					.filter((user) -> user.getIsdelete() == 0 && user.getIsactive() == 1).collect(Collectors.toList());
			userDtoList = userList.stream().map(user -> userMapper.toEndUserDTO(user)).collect(Collectors.toList());
			return userDtoList;
		} catch (Exception e) {
			logger.error("Unexpected error ocucred in getAllActiveUser", e);
			throw e;
		}
	}

//*******************************************************Helper Methods********************************************************************************
	private Set<Role> assignRoles(String roleString) {
		Set<Role> roles = new HashSet<>();
		if (roleString != null && !roleString.isEmpty()) {
			String[] roleArray = roleString.split(",");
			for (String roleName : roleArray) {
				if (isValidRole(roleName)) {
					roles.add(Role.valueOf(roleName.trim().toUpperCase()));
				} else {
					logger.warn("Invalid role name: {}", roleName);
				}
			}
		}
		if (roles.isEmpty()) {
			roles.add(Role.USER);
		}
		return roles;
	}

	public boolean isValidRole(String role) {
		try {
			Role.valueOf(role.toUpperCase());
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public boolean checkUserEmailExists(EndUser pushUser) {
		try {
			Optional<EndUser> existingUser = enduserRepository.findByEmail(pushUser.getEmail());
			if (existingUser.isPresent()) {
				if (existingUser.get().getIsdelete() == 0) {
					logger.warn("User with Email {} already exists", pushUser.getEmail());
					throw new CustomException(301, "User with Email " + pushUser.getEmail() + " already exists",
							"Email already Exists");
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error occured while checkUserEmailExists ", e);
			throw e;
		}
		return true;
	}

	public EndUser getSingleUserByUserId(long id) {
		EndUser user = new EndUser();
		user = enduserRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No user found with UserId " + id));
		if (user.getIsactive() == 0 && user.getIsdelete() == 1) {
			throw new ResourceNotFoundException("No user found with UserId " + id);
		}
		return user;
	}
}