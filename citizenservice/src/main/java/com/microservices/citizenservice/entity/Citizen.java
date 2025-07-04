
package com.microservices.citizenservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Shubham Singh
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Citizen {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column
	private String name;

	@Column(name = "vaccinationCenterId")
	private int vaccinationCenterId;
}
