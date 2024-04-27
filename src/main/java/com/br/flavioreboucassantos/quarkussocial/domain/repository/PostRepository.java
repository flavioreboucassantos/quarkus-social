package com.br.flavioreboucassantos.quarkussocial.domain.repository;

import com.br.flavioreboucassantos.quarkussocial.domain.model.Post;
import com.br.flavioreboucassantos.quarkussocial.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PostRepository implements PanacheRepository<Post> {
}
