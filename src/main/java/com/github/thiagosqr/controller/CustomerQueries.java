package com.github.thiagosqr.controller;

import com.github.thiagosqr.conf.DataTableResponse;
import com.github.thiagosqr.entity.Customer;
import com.github.thiagosqr.service.CustomerService;
import com.github.thiagosqr.viewmodel.CustomerVM;
import javaslang.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by thiago on 24/07/17.
 */
@Controller
@Path("/customers")
public class CustomerQueries {




    @Context
    protected HttpServletRequest request;

    @Autowired
    private CustomerService service;

    private final String[] cols = new String[]{"id", "name","email"};

    @GET
    @Path("/paginate")
    @Produces({DataTableResponse.JSON})
    public Response list(@QueryParam("draw")   final Integer draw,
                         @QueryParam("start")  final Integer start,
                         @QueryParam("length") final Integer length,
                         @QueryParam("search[value]") final String searchValue,
                         @QueryParam("columns[0][search][value]") final Long id,
                         @QueryParam("columns[1][search][value]") final String name,
                         @QueryParam("columns[2][search][value]") final String email,
                         @QueryParam("order[0][column]") final Integer order,
                         @QueryParam("order[0][dir]") final String orderDir) {

        final Integer page = new Double(Math.ceil(start / length)).intValue();
        final PageRequest pr = new PageRequest(page, length,
                new Sort(new Sort.Order(Sort.Direction.fromString(orderDir), cols[order])));

        final Try<Response> response = service.countAll().flatMap(qt ->
            service.paginate(id, name, email, pr).flatMap(p ->
                transform(p).map(data -> {

                    final DataTableResponse dtr = DataTableResponse.builder()
                            .data(data)
                            .draw(draw)
                            .recordsFiltered(qt)
                            .recordsTotal(qt)
                            .build();

                    return Response.status(Response.Status.OK).entity(dtr).build();

                })
            )
        ).recover(e -> {

            e.printStackTrace(); //LOG
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        });

        return response.get();

    }

    private Try<List<Map<String, Object>>> transform(final Page<Customer> page){
        return Try.of(() -> {
            final Iterable<CustomerVM> iterable = () -> page.map(CustomerVM::of).iterator();
            return StreamSupport.stream(iterable.spliterator(), false)
                    .map(r -> r.asMapofValues( (Object v) -> String.format("row_%s", v), "DT_RowId", "id", cols))
                    .collect(Collectors.toList());
        });
    }

}
