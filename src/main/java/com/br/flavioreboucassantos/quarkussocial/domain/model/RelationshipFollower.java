package com.br.flavioreboucassantos.quarkussocial.domain.model;

import com.br.flavioreboucassantos.quarkussocial.rest.dto.ResponseRelationshipFollowerResourceProjectListItem;
import com.br.flavioreboucassantos.quarkussocial.rest.dto.ResponseRelationshipFollowerResourceProjectListItem2;
import io.netty.channel.FileRegion;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "relationship_followers", uniqueConstraints = {@UniqueConstraint(columnNames = {"id_user", "id_follower"})})
public class RelationshipFollower extends PanacheEntityBase {

    static public List<ResponseRelationshipFollowerResourceProjectListItem> findByFollowed(Long id) {
        PanacheQuery<ResponseRelationshipFollowerResourceProjectListItem> query = find("user.id", id).project(ResponseRelationshipFollowerResourceProjectListItem.class);
        return query.list();
    }

    static public List<ResponseRelationshipFollowerResourceProjectListItem2> findByFollowed2(Long id) {
        PanacheQuery<ResponseRelationshipFollowerResourceProjectListItem2> query = find("user.id", id).project(ResponseRelationshipFollowerResourceProjectListItem2.class);
        return query.list();
    }

    static public void deleteByUserAndFollower(Long id, Long idFollower) {
        Map<String, Object> params = Parameters.with("idUser", id).and("idFollower", idFollower).map();
        delete("user.id =: idUser and userFollower.id =: idFollower", params);
    }

    static public PanacheQuery<?> tryFind(Long id, Long idFollower) {
        Map<String, Object> params = Parameters.with("idUser", id).and("idFollower", idFollower).map();
        return RelationshipFollower.find("user.id =: idUser and userFollower.id =: idFollower", params);
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_follower")
    private User userFollower;


    /**
     * @return or id if persisted, or null if not persisted.
     */
    @Transactional
    public Long tryPersist() {
        try {
            persist();
            return id;
        } catch (PersistenceException e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelationshipFollower relationshipFollower = (RelationshipFollower) o;
        return id == relationshipFollower.id && Objects.equals(user, relationshipFollower.user) && Objects.equals(userFollower, relationshipFollower.userFollower);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, userFollower);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUserFollower() {
        return userFollower;
    }

    public void setUserFollower(User userFollower) {
        this.userFollower = userFollower;
    }

}
