package com.shopping.portal.model;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name = "product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "brand")
	private String brand;

	@Column(name = "category")
	private String category;

	@Column(name = "price")
	private double price;

	@Column(name = "quantity")
	private int quantity;

	@OneToMany(mappedBy = "product",fetch = FetchType.EAGER)
	private List<ProductImages> images;

	@Column(name = "specification")
	private String specification;

	@Column(name = "description")
	private String description;

	@Column(name = "productstatus")
	private String productstatus;

	@Column(name = "instock")
	@JsonIgnore
	private int instock;

	@Column(name = "active")
	@JsonIgnore
	private int active;

	@Column(name = "isdelete")
	@JsonIgnore
	private int isdelete;

	@Column(name = "last_recieved_date")
	@JsonIgnore
	private String last_recieved_date;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIgnore
	List<CartItem> cartItem;
}
