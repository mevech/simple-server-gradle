package com.example;

import com.example.map.HashMapMessageDAO;
import com.example.model.Message;
import io.quarkus.security.UnauthorizedException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
@Path("/message-board")
public class MessageBoardResource {

    @Inject
    HashMapMessageDAO messageDAO;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@CookieParam("clientId") String clientInfo , String body) {
        String clientId = getClientId(clientInfo);
        Message message = messageDAO.insertNewMessage(clientId, body);

        return Response.status(Response.Status.CREATED).entity(message).build();
    }

    @PUT
    @Path("message/{id}")
    public Response update(@CookieParam("clientId") String clientInfo , @HeaderParam("messageId") String messageId, String body) {
        String clientId = getClientId(clientInfo);
        messageDAO.updateMessage(clientId, messageId, body);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("message/{id}")
    public Response remove(@CookieParam("clientId") String clientInfo , @HeaderParam("id") String messageId) {
        String clientId = getClientId(clientInfo);
        messageDAO.deleteMessage(clientId, messageId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    public @Produces(MediaType.APPLICATION_JSON)
    List<Message> get(@CookieParam("clientId") String clientInfo) {
        getClientId(clientInfo);
        return messageDAO.getAllMessages();
    }

    private String getClientId(String clientInfo ) {
        if (clientInfo == null || clientInfo.isEmpty()) {
            throw new UnauthorizedException("no client id found in header, not allowed to use server");
        }
        return clientInfo;
    }

}