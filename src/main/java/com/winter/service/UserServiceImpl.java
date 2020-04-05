package com.winter.service;

import com.winter.database.UserRepositoryImpl;
import com.winter.model.Company;
import com.winter.model.User;
import com.winter.model.UserStatus;
import com.winter.network.request.RegisterCompanyRequest;
import com.winter.resources.UserResource;
import org.checkerframework.checker.nullness.Opt;
import org.eclipse.microprofile.opentracing.Traced;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Traced
@ApplicationScoped
public class UserServiceImpl implements UserService {
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Inject
    @RestClient
    CompanyService companyService;

    @Inject
    UserRepositoryImpl userRepository;


    @Override
    public List<User> listAll() {
        return userRepository.listAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        User user = null;
        try {
            user = userRepository.findById(id);
        } catch (NoResultException e) {
            logger.error("", e);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return Optional.ofNullable(userRepository.findByLogin(login));
    }

    @Override
    public Optional<User> save(User user) {
        return Optional.ofNullable(userRepository.save(user));
    }

    @Override
    public Optional<User> registerUser(String login, String password, String companyName) {
        try {
            User user = new User();
            user.setLogin(login);
            user.setPassword(password);
            user.setStatus(UserStatus.REGISTERED.getStatus());

            User savedUser = userRepository.save(user);
            if (savedUser != null) {
                RegisterCompanyRequest request = new RegisterCompanyRequest();
                request.setUserId(savedUser.getId());
                request.setCompanyName(companyName);
                try {
                    Company company = companyService.registerCompany(request);
                    if (company != null) {
                        return Optional.of(user);
                    } else {
                        userRepository.delete(savedUser);
                        return Optional.empty();
                    }
                } catch (Exception e){
                    logger.error("",e);
                    userRepository.delete(savedUser);
                    return Optional.empty();
                }
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.error("", e);
            return Optional.empty();
        }
    }
}
