package com.tecsup.petclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 
 * @author jgomezm
 *
 */
@SpringBootApplication(scanBasePackages = {"com.tecsup.petclinic", "com.tecsupt.petclinic"})
@EntityScan(basePackages = {"com.tecsup.petclinic.entities", "com.tecsupt.petclinic.domain"})
@EnableJpaRepositories(basePackages = {"com.tecsup.petclinic.repositories", "com.tecsupt.petclinic.repository"})
public class PetClinicApplication {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(PetClinicApplication.class, args);
	}

}
