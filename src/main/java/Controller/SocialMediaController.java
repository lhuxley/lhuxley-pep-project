package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import Model.Account;
import Model.Message;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */



    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::createAccountHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::retrieveAllMessagesHandler);
        app.get("/messages/{message_id}", this::retrieveMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::retrieveMessagesByAccountHandler);
        return app;
    }

    private void createAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account createdAccount = accountService.createAccount(account);
        if (createdAccount == null) {
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(createdAccount));
            ctx.status(200);
        }
    }

    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loggedIn = accountService.login(account);
        if (loggedIn == null) {
            ctx.status(401);
        } else {
            ctx.json(mapper.writeValueAsString(loggedIn));
            ctx.status(200);
        }
    }

    private void createMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message createdMessage = messageService.createMessage(message);
        if (createdMessage == null) {
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(createdMessage));
            ctx.status(200);
        }
    }

    private void retrieveAllMessagesHandler(Context ctx) throws JsonProcessingException {
        List<Message> messages = messageService.retrieveAllMessages();
        ctx.json(messages);
        ctx.status(200);
    }

    private void retrieveMessageByIdHandler(Context ctx) throws JsonProcessingException {

        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.retrieveMessageById(message_id);
        if (message == null) {
            ctx.status(200);
            return;
        }

        ctx.json(message);

    }

    private void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException {

        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.deleteMessageById(message_id);

        if (message == null) {
            ctx.status(200);
            return;
        }

        ctx.json(message);

    }

    private void updateMessageHandler(Context ctx) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = mapper.readValue(ctx.body(), Message.class);
        message.setMessage_id(messageId);
        Message returnedMessage = messageService.updateMessage(message);

        if (returnedMessage == null) {
            ctx.status(400);
            return;
        }
        ctx.status(200).json(returnedMessage);

    }

    private void retrieveMessagesByAccountHandler(Context ctx) throws JsonProcessingException {

        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.retrieveMessagesByAccount(account_id);

        ctx.status(200).json(messages);

    }

}