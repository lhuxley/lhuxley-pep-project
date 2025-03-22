package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Service.SocialMediaService;
import Model.Account;
import Model.Message;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;




/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */

    SocialMediaService socialMediaService;
    public SocialMediaController(){
        socialMediaService = new SocialMediaService();

    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::createAccountHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::retrieveAllMessagesHandler);
        app.get("/messages/{message_id}", this::retrieveMessageByIdHandler);
        app.delete("messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("messages/{message_id}", this::updateMessageHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void createAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class );
        Account createdAccount = socialMediaService.createAccount(account);
        if(createdAccount==null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(createdAccount));
            ctx.status(200);
        }
    }

    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class );
        Account loggedIn = socialMediaService.login(account);
        if(loggedIn==null){
            ctx.status(401);
        }else{
            ctx.json(mapper.writeValueAsString(loggedIn));
            ctx.status(200);
        }
    }

    private void createMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class );
        Message createdMessage = socialMediaService.createMessage(message);
        if(createdMessage==null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(createdMessage));
            ctx.status(200);
        }
    }

    private void retrieveAllMessagesHandler(Context ctx) throws JsonProcessingException {
        List<Message> messages = socialMediaService.retrieveAllMessages();
        ctx.json(messages); 
        ctx.status(200);
    }

    private void retrieveMessageByIdHandler(Context ctx) throws JsonProcessingException {
        
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = socialMediaService.retrieveMessageById(message_id);
        if (message == null){
            ctx.status(200);
            return;
        }
        
        ctx.json(message); 
        
    }

    private void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException {
        
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = socialMediaService.deleteMessageById(message_id);
        
        if (message == null){
            ctx.status(200);
            return;
        }
        
        ctx.json(message); 
        
    }

    private void updateMessageHandler(Context ctx) throws JsonProcessingException {
        
        ObjectMapper mapper = new ObjectMapper();
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = mapper.readValue(ctx.body(), Message.class );
        message.setMessage_id(messageId);
        Message returnedMessage = socialMediaService.updateMessage(message);
        
        if (returnedMessage == null){
            ctx.status(400);
            return;
        }
        ctx.status(200).json(returnedMessage);
        
    }
}