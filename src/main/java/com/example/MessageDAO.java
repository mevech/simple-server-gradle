package com.example;

import com.example.model.Message;
import io.quarkus.security.UnauthorizedException;

import java.util.List;

public interface MessageDAO {

    Message insertNewMessage(String clientID, String body);

    void updateMessage(String clientId, String messageId, String body);

    void deleteMessage(String clientId, String messageId);

    List<Message> getAllMessages();
}
