package com.winter.database;

import com.winter.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

public interface UserRepository extends PanacheRepository<User> {

    User save(User user);

    User findByLogin(String login);

}
