package com.winter.resources;

import com.winter.model.*;
import com.winter.network.request.LoginRequest;
import com.winter.network.request.RegisterRequest;
import com.winter.network.response.LoggedInResponse;
import com.winter.service.CompanyService;
import com.winter.service.UserService;
import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;
import org.checkerframework.checker.nullness.Opt;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Path("/api/user")
public class UserResource {
    private static final Logger logger = Logger.getLogger(UserResource.class);

    @Inject
    UserService userService;

    @Inject
    @Channel("users-topic")
    Emitter<User> userEmitter;

    @Counted(name = "countGetUsers", description = "Counts how many times the getUsers method has been invoked")
    @Timed(name = "timeGetUsers", description = "Times how long it takes to invoke getUsers method", unit = MetricUnits.MILLISECONDS)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        logger.debug("get all");
        List<User> users = userService.listAll();
        return Response.ok(users).build();
    }

    @Counted(name = "countGetUser", description = "Counts how many times the getUser method has been invoked")
    @Timed(name = "timeGetUser", description = "Times how long it takes to invoke getUser method", unit = MetricUnits.MILLISECONDS)
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") Long id) {
        return userService.findById(id)
                .map(user -> Response.ok(user).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @Counted(name = "countFindUserByHash", description = "Counts how many times the findUserByHash method has been invoked")
    @Timed(name = "timeFindUserByHash", description = "Times how long it takes to invoke timeFindUserByHash method", unit = MetricUnits.MILLISECONDS)
    @GET
    @Path("/hash/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findUserByHash(@PathParam("id") String id) {
        byte[] decodedBytes = Base64.getDecoder().decode(id);
        String login = new String(decodedBytes);

        return userService.findByLogin(login)
                .map(user -> Response.ok(user).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @Counted(name = "countLogin", description = "Counts how many times the getUser method has been invoked")
    @Timed(name = "timeLogin", description = "Times how long it takes to invoke getUser method", unit = MetricUnits.MILLISECONDS)
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest request) {
        logger.error("logging as");
        logger.error(request);
        Optional<User> dbUser = userService.findByLogin(request.getLogin());
        if (dbUser.isPresent()) {
            if (request.getPassword().equals(dbUser.get().getPassword())) {
                LoggedInResponse response = new LoggedInResponse();
                response.setStatus("ok");
                response.setToken(Base64.getEncoder().encodeToString(dbUser.get().getLogin().getBytes()));
                return Response.ok(response).build();
            } else {
                return Response.status(403).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @Counted(name = "countPostRegisterUser", description = "Counts how many times the registerUser method has been invoked")
    @Timed(name = "timePostRegisterUser", description = "Times how long it takes to invoke registerUser method", unit = MetricUnits.MILLISECONDS)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(RegisterRequest request) {
        return userService.registerUser(request.getLogin(),request.getPassword(),request.getCompanyName())
                .map(user -> Response.ok(user).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

}
