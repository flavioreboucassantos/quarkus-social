package com.br.flavioreboucassantos.quarkussocial.domain.repository;

import com.br.flavioreboucassantos.quarkussocial.domain.model.RelationshipFollower;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RelationshipFollowerRepository implements PanacheRepository<RelationshipFollower> {
}
