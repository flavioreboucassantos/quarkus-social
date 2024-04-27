package com.br.flavioreboucassantos.quarkussocial.rest.dto;

import io.quarkus.hibernate.orm.panache.common.ProjectedFieldName;

public class ResponseRelationshipFollowerResourceProjectListItem2 {

    public final Long userFollowerId;
    public final String userFollowerName;

    public ResponseRelationshipFollowerResourceProjectListItem2(@ProjectedFieldName("userFollower.id") Long userFollowerId, @ProjectedFieldName("userFollower.name") String userFollowerName) {
        this.userFollowerId = userFollowerId;
        this.userFollowerName = userFollowerName;
    }
}
