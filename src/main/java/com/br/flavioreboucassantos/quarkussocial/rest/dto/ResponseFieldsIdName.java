package com.br.flavioreboucassantos.quarkussocial.rest.dto;

import io.quarkus.hibernate.orm.panache.common.NestedProjectedClass;

@NestedProjectedClass
public class ResponseFieldsIdName {
    public Long id;
    public String name;

    public ResponseFieldsIdName(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
