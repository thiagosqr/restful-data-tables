package com.github.thiagosqr.service;

import com.google.common.base.Strings;
import com.github.thiagosqr.entity.Customer;
import com.github.thiagosqr.repository.CustomerRepository;
import javaslang.collection.List;
import javaslang.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.util.Date;

import static com.github.thiagosqr.entity.Customer.*;
import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Created by thiago on 24/07/17.
 */
@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repository;

    @PostConstruct
    private void insert(){
        repository.save(Customer.builder().name("John Appleseed").email("john@email.com").dob(new Date()).build());
        repository.save(Customer.builder().name("Mary McCain").email("mary@email.com").dob(new Date()).build());
        repository.save(Customer.builder().name("Amanda Hugginkiss").email("amanda@email.com").dob(new Date()).build());
    }

    public Try<Page<Customer>> paginate(final Long id, final String nome,
                                         final String email, final PageRequest pr) {

        List<Specification<Customer>> specs = List.empty();

        specs = id != null && id > 0? specs.append(id(id)) : specs;
        specs = Strings.isNullOrEmpty(nome)? specs : specs.append(nameStarts(nome));
        specs = Strings.isNullOrEmpty(email)? specs : specs.append(withEmail(email));

        final boolean noSpec = specs.isEmpty();

        final Specification<Customer> spec = noSpec? null : specs.reduce((a1, a2) -> where(a1).and(a2));

        return Try.of(() -> noSpec? repository.findAll(pr) : repository.findAll(spec, pr));

    }

    public Try<Long> countAll() {
        return Try.of(() -> repository.count());
    }

}


