package com.winter.database;

import com.winter.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.runtime.JpaOperations;
import io.quarkus.panache.common.Parameters;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Traced
@ApplicationScoped
public class UserRepositoryImpl implements UserRepository {

    @Transactional
    public User save(User user){
        EntityManager em = JpaOperations.getEntityManager();
        if (user.getId() == null) {
            em.persist(user);
            return user;
        } else {
            return em.merge(user);

        }
    }


    public User findByLogin(String login){
        return find("login=:login", Parameters.with("login", login)).singleResult();
    }
}
