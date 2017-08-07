package com.github.thiagosqr.viewmodel;

import com.github.thiagosqr.entity.Customer;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.util.Date;

/**
 * Created by thiago on 24/07/17.
 */
@Value
@Getter
@Builder
public class CustomerVM implements ViewModel<CustomerVM, Customer>{

    private Long id;
    private String name;
    private String email;
    private Date dob;
    private String sex;
    private Boolean status;

    public static CustomerVM of(final Customer entity) {
        return CustomerVM.builder().build().from(entity);
    }

    @Override
    public CustomerVM from(Customer entity) {
        return CustomerVM.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .dob(entity.getDob())
                .sex(entity.getSex())
                .status(entity.getStatus())
                .build();
    }

    @Override
    public Customer toTarget() {
        return Customer.builder()
                .id(getId())
                .name(getName())
                .email(getEmail())
                .dob(getDob())
                .sex(getSex())
                .status(getStatus())
                .build();
    }
}
