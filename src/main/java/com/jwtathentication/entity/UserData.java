package com.jwtathentication.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user_data")
@EntityListeners(AuditingEntityListener.class)
public class UserData {
	@Id // Sets the id field as the primary key in the database table
    @Column(name = "id") // sets the column name for the id property
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String  address;
	
	public UserData(String name, String address) {
		super();
		this.name = name;
		this.address = address;
	}
	
	
}
