package com.br.flavioreboucassantos.quarkussocial.rest.dto;

public class ResponseRelationshipFollowerResourceProjectListItem {

    public final Long id;
    public final ResponseFieldsIdName userFollower;

    public ResponseRelationshipFollowerResourceProjectListItem(Long id, ResponseFieldsIdName userFollower) {
        this.id = id;
        this.userFollower = userFollower;
    }
}
