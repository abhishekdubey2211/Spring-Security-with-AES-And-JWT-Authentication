package com.shopping.portal.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class UserParameterDetailsDTO {
	private int parameterid;    
	private int srno;
	private String value;
	private String description;
}
