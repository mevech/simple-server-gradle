package com.example;

import com.example.map.HashMapMessageDAO;
import com.example.model.Message;
import io.quarkus.security.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/resteasy/hello")
public class ExampleResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }


    @Inject
    HashMapMessageDAO messageDAO;

    @POST
    @Consumes("text/*")
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@CookieParam("clientId") String clientId , String body) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        if (clientId == null || clientId.isEmpty()) {
            throw new UnauthorizedException("no client id found in header, not allowed to use server");
        }
        Message message = messageDAO.insertNewMessage(clientId, body);
        logger.info("create new message {}", message);
        return Response.status(Response.Status.CREATED).entity(message).build();
    }
}