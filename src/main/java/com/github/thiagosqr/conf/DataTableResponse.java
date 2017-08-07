package com.github.thiagosqr.conf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * Created by thiago on 24/07/17.
 */
@XmlRootElement(name=DataTableResponse.SCHEMA_NAME)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = DataTableResponse.SCHEMA_NAME,propOrder = {})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataTableResponse {
    private Integer draw;
    private Long recordsTotal;
    private Long recordsFiltered;
    private List data;
    private String error;

    public final static String SCHEMA_NAME = "datatableresponse";

    public final static String JSON ="application/gov.goias.sistema.representation."+SCHEMA_NAME+"+json";

}