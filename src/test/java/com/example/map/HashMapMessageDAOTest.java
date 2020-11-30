package com.example.map;

import com.example.model.Message;
import io.quarkus.security.UnauthorizedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.util.List;
import java.util.UUID;

class HashMapMessageDAOTest {

    static HashMapMessageDAO storage = new HashMapMessageDAO();

    static Message initialMessage = Message.builder().clientId("client0").body("message0").build();

    @BeforeAll
    static void setup(){
        storage.messagesById.put(initialMessage.getId(), initialMessage);
    }

    @Test
    @DisplayName("insert message by client with same message should create new message id")
    public void testInsertSameMessage() {
        Message newMessage = storage.insertNewMessage(initialMessage.getClientId(), initialMessage.getBody());
        Assertions.assertNotEquals(initialMessage.getId(), newMessage.getId());
    }

    @Test
    @DisplayName("insert message by new client should create new message id")
    public void testInsertNewClient() {
        Message newMessage = storage.insertNewMessage("new-client", initialMessage.getBody());
        Assertions.assertNotEquals(initialMessage.getId(), newMessage.getId());
    }

    @Test
    @DisplayName("update message should update the original object")
    public void testUpdate() {
        String originalMessageBody = storage.messagesById.get(initialMessage.getId()).getBody();
        int originalSize = storage.messagesById.size();
        storage.updateMessage(initialMessage.getClientId(), initialMessage.getId(), "new message");
        String afterUpdateMessageBody = storage.messagesById.get(initialMessage.getId()).getBody();
        int afterUpdateSize = storage.messagesById.size();

        Assertions.assertEquals(afterUpdateSize, originalSize);
        Assertions.assertEquals("new message", afterUpdateMessageBody);
        Assertions.assertNotEquals(afterUpdateMessageBody, originalMessageBody);
    }

    @Test
    @DisplayName("update message by wrong client should throw Unauthorized Exception")
    public void testUpdateWrongClient() {
        String originalMessageBody = storage.messagesById.get(initialMessage.getId()).getBody();
        Assertions.assertThrows(UnauthorizedException.class, () ->
                storage.updateMessage("wrong-client", initialMessage.getId(), "new message")
        );
        String afterUpdateMessageBody = storage.messagesById.get(initialMessage.getId()).getBody();

        Assertions.assertEquals(originalMessageBody, afterUpdateMessageBody);
    }

    @Test
    @DisplayName("update none existing messageId should throw Bad Request Exception")
    public void testUpdateWrongMessage() {
        String originalMessageBody = storage.messagesById.get(initialMessage.getId()).getBody();
        Assertions.assertThrows(BadRequestException.class, () ->
                storage.updateMessage(initialMessage.getId(), "wrong-message-id", "new message")
        );
        String afterUpdateMessageBody = storage.messagesById.get(initialMessage.getId()).getBody();

        Assertions.assertEquals(originalMessageBody, afterUpdateMessageBody);
    }

    @Test
    @DisplayName("delete message should delete only delete using message id")
    public void testDelete() {
        Message m1 = Message.builder().clientId("client0").body("message0").id(UUID.randomUUID().toString()).build();
        Message m2 = Message.builder().clientId("client0").body("message0").id(UUID.randomUUID().toString()).build();

        storage.messagesById.put(m1.getId(), m1);
        storage.messagesById.put(m2.getId(), m2);

        int originalSize = storage.messagesById.size();
        Assertions.assertEquals(m2, storage.messagesById.get(m2.getId()));

        storage.deleteMessage(m1.getClientId(), m1.getId());

        int afterUpdateSize = storage.messagesById.size();

        // storage size reduced
        Assertions.assertEquals(afterUpdateSize, originalSize - 1);
        // m1 deleted
        Assertions.assertNull(storage.messagesById.get(m1.getId()));
        // m2 has not changed
        Assertions.assertEquals(m2, storage.messagesById.get(m2.getId()));
    }

    @Test
    @DisplayName("delete message by wrong client should throw Unauthorized Exception")
    public void testDeleteWrongClient() {
        Message message = Message.builder().clientId("client0").body("message0").id(UUID.randomUUID().toString()).build();

        storage.messagesById.put(message.getId(), message);

        Assertions.assertThrows(UnauthorizedException.class, () ->
                storage.deleteMessage("wrong-client", message.getId())
        );

        Assertions.assertEquals(message, storage.messagesById.get(message.getId()));
    }

    @Test
    @DisplayName("delete none existing messageId should throw Bad Request Exception")
    public void testDeleteWrongMessage() {
        Message message = Message.builder().clientId("client0").body("message0").id(UUID.randomUUID().toString()).build();
        storage.messagesById.put(message.getId(), message);

        Assertions.assertThrows(BadRequestException.class, () ->
                storage.deleteMessage(message.getClientId(), "wrong-message-id")
        );

        Assertions.assertEquals(message, storage.messagesById.get(message.getId()));
    }

    @Test
    @DisplayName("get all messages should get all messages")
    public void testGetMessages() {
        Message message = Message.builder().clientId("client0").body("message0").id(UUID.randomUUID().toString()).build();
        storage.messagesById.put(message.getId(), message);

        int storageSize = storage.messagesById.size();

        List<Message> messages = storage.getAllMessages();

        Assertions.assertEquals(storageSize, messages.size());
        Assertions.assertTrue(messages.contains(message));


    }

}