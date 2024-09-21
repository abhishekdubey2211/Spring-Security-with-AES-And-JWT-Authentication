package com.shopping.portal.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "address")
public class RessidentialAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "address1")
    @NotBlank(message = "Address line 1 cannot be blank")
    @Size(max = 200, message = "Address line 1 cannot exceed 200 characters")
    private String address1;

    @Column(name = "address2")
    private String address2;

    @Column(name = "state")
    @NotBlank(message = "State cannot be blank")
    @Size(max = 100, message = "State cannot exceed 100 characters")
    private String state;

    @Column(name = "city")
    @NotBlank(message = "City cannot be blank")
    @Size(max = 100, message = "City cannot exceed 100 characters")
    private String city;

    @Column(name = "village")
    private String village;

    @Column(name = "pincode")
    @NotBlank(message = "Pincode cannot be blank")
    @Size(min = 6, max = 6, message = "Pincode must be exactly 6 characters")
    private String pincode;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",referencedColumnName = "id")
	@JsonIgnore
    private EndUser user;
}
