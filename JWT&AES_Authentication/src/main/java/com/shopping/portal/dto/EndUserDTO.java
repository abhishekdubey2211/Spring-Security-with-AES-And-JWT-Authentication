package com.shopping.portal.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.shopping.portal.model.RessidentialAddress;
import com.shopping.portal.model.Role;
import com.shopping.portal.model.UserParameterDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EndUserDTO {
	private long  userid;
	private String username;
	private String password;
	private String designation;
	private String email;
	private String contact;
	private String profileimage;
	private String role;
	private List<RessidentialAddress> address;
	private List<UserParameterDetails> userParameterDetails;
    private Set<Role> roles = new HashSet<>();	
}
