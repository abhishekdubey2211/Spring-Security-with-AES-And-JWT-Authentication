package com.shopping.portal.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
@Table(name = "user")
public class EndUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotBlank(message = "Username is mandatory")
	@Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
	private String username;

	@NotBlank(message = "Password is mandatory")
	@Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
	private String password;

	@NotBlank(message = "Designation is mandatory")
	@Size(min = 8, max = 50, message = "Designation must be between 8 and 50 characters")
	private String designation;

	@NotBlank(message = "Email is mandatory")
	@Email(message = "Email should be valid")
	private String email;

	@NotBlank(message = "Contact is mandatory")
	@Pattern(regexp = "\\d{10}", message = "Contact should be a valid 10-digit number")
	private String contact;

	@NotBlank(message = "Profile image is mandatory")
	private String profileimage;

	@Min(value = 0, message = "isActive must be 0 (inactive) or 1 (active)")
	@JsonIgnore
	private int isactive;

	@Min(value = 0, message = "isDelete must be 0 (not deleted) or 1 (deleted)")
	@JsonIgnore
	private int isdelete;

	@JsonIgnore
	private String usercreationdate;

	@JsonIgnore
	private String userlastupdatedate;

	@JsonIgnore
	@Transient
	private String role;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private Set<Role> roles = new HashSet<>();

	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private Set<Order> orders;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<UserParameterDetails> userParameterDetails = new ArrayList<>();
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore 
	private List<RessidentialAddress> address = new ArrayList<>();
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="cart_id")
	@JsonIgnore
	private Cart cart;
}
