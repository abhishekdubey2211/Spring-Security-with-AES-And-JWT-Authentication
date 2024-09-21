package com.shopping.portal.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RessidentialAddressDTO {

    private long addressid;

    @NotBlank(message = "Address line 1 cannot be blank")
    @Size(max = 200, message = "Address line 1 cannot exceed 200 characters")
    private String address1;

    private String address2;
    private String village;
    
    @NotBlank(message = "State cannot be blank")
    @Size(max = 100, message = "State cannot exceed 100 characters")
    private String state;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 100, message = "City cannot exceed 100 characters")
    private String city;

    @NotBlank(message = "Pincode cannot be blank")
    @Size(min = 6, max = 6, message = "Pincode must be exactly 6 characters")
    private String pincode;
    private int primary;
         
    
}
