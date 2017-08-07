package com.github.thiagosqr.repository;

import com.github.thiagosqr.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by thiago on 24/07/17.
 */
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor {}