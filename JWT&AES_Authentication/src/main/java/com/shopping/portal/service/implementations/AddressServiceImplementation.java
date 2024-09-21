package com.shopping.portal.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopping.portal.model.EndUser;
import com.shopping.portal.model.RessidentialAddress;
import com.shopping.portal.repository.AddressRepository;
import com.shopping.portal.service.AddressService;


@Service
public class AddressServiceImplementation implements AddressService{

	@Autowired
	AddressRepository addressRepository;
	
	@Override
	public RessidentialAddress addAddress(EndUser user, RessidentialAddress address) {
		addressRepository.findByUser(user).forEach(data->addressRepository.deleteById(data.getId()));
		address.setUser(user);
		return addressRepository.save(address);
	}

	@Override
	public RessidentialAddress getAddressByUserId(Long userId) {
		return null;
	}

	@Override
	public boolean deleteAddressByUserId(Long userid) {
		return false;
	}

	@Override
	public boolean deleteAddressById(Long addressId) {
		return false;
	}

}
