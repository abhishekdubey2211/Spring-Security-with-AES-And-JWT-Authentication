package com.shopping.portal.dto;

import com.shopping.portal.model.EndUser;

public class EndUserMapper {

    public EndUserDTO toEndUserDTO(EndUser endUser) {
        return new EndUserDTO(
        	endUser.getId(),
            endUser.getUsername(),
            endUser.getPassword(),
            endUser.getDesignation(),
            endUser.getEmail(),
            endUser.getContact(),
            endUser.getProfileimage(),
            endUser.getRole(),
            endUser.getAddress(),
            endUser.getUserParameterDetails(),
            endUser.getRoles()
        );
    }

    public EndUser toEndUser(EndUserDTO endUserDTO) {
        EndUser endUser = new EndUser();
        endUser.setId(endUserDTO.getUserid());
        endUser.setUsername(endUserDTO.getUsername());
        endUser.setPassword(endUserDTO.getPassword());
        endUser.setDesignation(endUserDTO.getDesignation());
        endUser.setEmail(endUserDTO.getEmail());
        endUser.setContact(endUserDTO.getContact());
        endUser.setProfileimage(endUserDTO.getProfileimage());
        endUser.setRole(endUserDTO.getRole());
        endUser.setAddress(endUserDTO.getAddress());
        endUser.setUserParameterDetails(endUserDTO.getUserParameterDetails());
        endUser.setRoles(endUser.getRoles());
        return endUser;
    }
}
