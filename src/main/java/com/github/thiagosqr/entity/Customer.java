package com.github.thiagosqr.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by thiago on 24/07/17.
 */
@Entity
@Table(name = "Customers")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @Column
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private Date dob;

    @Column
    private String sex;

    @Column
    private Boolean status;

    public static Specification<Customer> id(final Long id) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.<Long>get("id"), id);
    }

    public static Specification<Customer> nameStarts(final String name) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), name+"%");
    }

    public static Specification<Customer> withEmail(final String email) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("email"), email);
    }

}
