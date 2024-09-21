package com.shopping.portal.service;

import com.shopping.portal.model.EndUser;
import com.shopping.portal.model.RessidentialAddress;

public interface AddressService {

	public RessidentialAddress addAddress(EndUser user, RessidentialAddress address);

	public RessidentialAddress getAddressByUserId(Long userId);
	
	public boolean deleteAddressByUserId(Long userid);
	
	public boolean deleteAddressById(Long addressId);
}
