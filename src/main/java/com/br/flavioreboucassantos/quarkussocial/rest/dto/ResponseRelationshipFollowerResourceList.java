package com.br.flavioreboucassantos.quarkussocial.rest.dto;

import java.util.List;

public class ResponseRelationshipFollowerResourceList {

    private Integer count;
    private List<?> list;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
