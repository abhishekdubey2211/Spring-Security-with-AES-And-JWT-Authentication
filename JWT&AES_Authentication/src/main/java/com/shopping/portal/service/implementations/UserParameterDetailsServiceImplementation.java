package com.shopping.portal.service.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopping.portal.model.EndUser;
import com.shopping.portal.model.UserParameterDetails;
import com.shopping.portal.repository.UserParameterdetailsRepository;
import com.shopping.portal.service.UserParameterDetailsService;

@Service
public class UserParameterDetailsServiceImplementation implements UserParameterDetailsService{

	@Autowired
	UserParameterdetailsRepository parameterdetailsRepository;
	
	@Override
	public UserParameterDetails addUserParameterDetails(EndUser user, UserParameterDetails userParameterDetails) {
		parameterdetailsRepository.findByUser(user).forEach( p -> parameterdetailsRepository.deleteById(p.getId()));
		userParameterDetails.setUser(user);
		return parameterdetailsRepository.save(userParameterDetails);
	}

	@Override
	public List<UserParameterDetails> getUserParameterDetailsByUserId(EndUser user) {
		
		return null;
	}

}
