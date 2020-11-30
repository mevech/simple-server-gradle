package com.example.map;

import com.example.MessageDAO;
import com.example.model.Message;
import io.quarkus.security.UnauthorizedException;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.BadRequestException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class HashMapMessageDAO implements MessageDAO {

    protected Map<String, Message> messagesById = new HashMap<>();

    @Override
    public Message insertNewMessage(String clientId, String body) {
        Message message = Message.builder()
                .clientId(clientId)
                .body(body)
                .id(UUID.randomUUID().toString())
                .build();
        messagesById.put(message.getId(), message);
        return message;
    }

    @Override
    public void updateMessage(String clientId, String messageId, String body) {
        Message original = messagesById.get(messageId);
        if (original == null) {
            throw new BadRequestException("No message found with id " + messageId);
        }
        if (!original.getClientId().equals(clientId)) {
            throw new UnauthorizedException("client " + clientId + " can not update message " + original);
        }
        original.setBody(body);
    }

    @Override
    public void deleteMessage(String clientId, String messageId) {
        Message original = messagesById.get(messageId);
        if (original == null) {
            throw new BadRequestException("No message found with id " + messageId);
        }
        if (!original.getClientId().equals(clientId)) {
            throw new UnauthorizedException("client " + clientId + " can not delete message " + original);
        }
        messagesById.remove(messageId);
    }

    @Override
    public List<Message> getAllMessages() {
        return new ArrayList<>(messagesById.values());
    }

}
