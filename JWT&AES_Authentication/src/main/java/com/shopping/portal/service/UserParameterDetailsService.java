package com.shopping.portal.service;

import com.shopping.portal.model.EndUser;
import com.shopping.portal.model.UserParameterDetails;
import java.util.*;

public interface UserParameterDetailsService {
	public UserParameterDetails addUserParameterDetails(EndUser user, UserParameterDetails userParameterDetails);

	public List<UserParameterDetails> getUserParameterDetailsByUserId(EndUser user);
}
