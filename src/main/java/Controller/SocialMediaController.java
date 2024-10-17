package Controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

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
    AccountService accountService; 
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        
        // #1 Create POST endpoint for a user to create a new Account
        app.post("/register", this::createAccountHandler);
        
        // #2 POST endpoint for a user to login
        app.post("/login", this::userLoginHandler);

        // #3 POST endpoint for a user to create a new message
        app.post("/messages", this::createMessageHandler);

        // #4 GET endpoint for retrieving all messages
        app.get("/messages", this::getAllMessagesHandler);

        // #5 GET endpoint for retrieving a message by its ID
        app.get("/messages/{message_id}", this::getMessageByIdHandler);

        // #6 DELETE endpoint for deleting a message by its ID
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);

        // #7 PATCH endpoint for updating a specific message
        app.patch("/messages/{message_id}", this::updateMessageTextByIdHandler);

        // #8 GET endpoint for retrieving all messages from a specific account
        app.get("/accounts/{account_id}/messages", this::getAllMessageByAccountIdHandler);
        

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }
   
  
    private void createAccountHandler(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        // Convert JSON Post request body to Java Object
        Account account = mapper.readValue(ctx.body(), Account.class);
        //int id = 1;

        // Get list of accounts
        List<Account> accounts = new ArrayList<>();
        accounts = accountService.getAllAccounts();

        // Iterate through accounts. If account already exists, use a boolean to get to the else block.
        Boolean accountExists = false;
        for (Account tempAccount : accounts)
        {
            if (tempAccount.getUsername().equals(account.getUsername())) {
                accountExists = true;
                break;
            }
        }

        if (account.getUsername() != "" && account.getPassword().length() >= 4 && accountExists == false) {
            account.setAccount_id(2);
            accountService.createAccount(account);
            ctx.json(mapper.writeValueAsString(account)).status(200);
        } else {
            ctx.status(400);
        }
    }

    private void userLoginHandler(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        // Convert JSON Post request body to Java Object
        Account account = mapper.readValue(ctx.body(), Account.class);

        // Get list of accounts
        List<Account> accounts = new ArrayList<>();
        accounts = accountService.getAllAccounts();

        // Iterate through accounts. If account username and password match, set boolean to true.
        Boolean accountExists = false;
        for (Account tempAccount : accounts)
        {
            if (tempAccount.getUsername().equals(account.getUsername()) && tempAccount.getPassword().equals(account.getPassword()))
                accountExists = true;
                break;
        }
        if (accountExists)
        {
            account.setAccount_id(1);
            ctx.json(mapper.writeValueAsString(account)).status(200);
        } else {
            ctx.status(401);
        }
    }

    private void createMessageHandler(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        // Convert JSON Post request body to Java Object
        Message message = mapper.readValue(ctx.body(), Message.class);

        // Get list of accounts
        List<Account> accounts = new ArrayList<>();
        accounts = accountService.getAllAccounts();

        // Iterate through accounts. If Account(account_id) matches Message(posted_by), set accountExists = true
        Boolean accountExists = false;
        for (Account account : accounts)
        {
            if (account.getAccount_id() == message.posted_by)
                accountExists = true;
                break;
        }
        if (accountExists && message.getMessage_text() != "" && message.getMessage_text().length() <= 255)
        {
            message.setMessage_id(2);
            messageService.createMessage(message);
            ctx.json(mapper.writeValueAsString(message)).status(200);
        } else {
            ctx.status(400);
        }
    }

    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();

        // Get list of messages
        List<Message> messages = new ArrayList<>();
        messages = messageService.getAllMessages();
        ctx.json(mapper.writeValueAsString(messages)).status(200);
    }

    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();

        // Get path parameter
        String pathParam = ctx.pathParam("message_id");

        // Convert String to int
        int message_id = Integer.parseInt(pathParam);

        // Get message by id
        Message message = messageService.getMessageById(message_id);

        // Convert Object to JSON and send back response
        if (message != null)
            ctx.json(mapper.writeValueAsString(message)).status(200);
        else 
            ctx.json("").status(200);
    }

    private void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();

        // Get path parameter
        String pathParam = ctx.pathParam("message_id");

        // Convert String to int
        int message_id = Integer.parseInt(pathParam);

        // Get message by id
        Message message = messageService.getMessageById(message_id);

        // // Delete message. Convert Object to JSON and send back response
        if (message != null) {
            messageService.deleteMessageById(message_id);
            ctx.json(mapper.writeValueAsString(message)).status(200);
        } else {
            ctx.json("").status(200);
        }
    }
    private void updateMessageTextByIdHandler(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();

        // Convert JSON Post request body to Java Object
        Message new_message = mapper.readValue(ctx.body(), Message.class);

        // Get path parameter
        String pathParam = ctx.pathParam("message_id");

        // Convert String to int
        int message_id = Integer.parseInt(pathParam);

        // Get message by id
        Message message = messageService.getMessageById(message_id);

        // // Update message_text. Convert Object to JSON and send back response
        if (message != null && !(new_message.getMessage_text().equals("")) && new_message.getMessage_text().length() <= 255) {
            message.setMessage_text(new_message.getMessage_text());
            new_message.setMessage_id(message.getMessage_id());
            new_message.setPosted_by(message.getPosted_by());
            new_message.setTime_posted_epoch(message.getTime_posted_epoch());
            messageService.updateMessage(new_message);
            ctx.json(mapper.writeValueAsString(new_message)).status(200);
        } else {
            ctx.status(400);
        }
    }

    private void getAllMessageByAccountIdHandler(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();

        // Get path parameter
        String pathParam = ctx.pathParam("account_id");

        // Convert String to int
        int account_id = Integer.parseInt(pathParam);

        // Get account by id
        Account account = accountService.getAccountById(account_id);

        List<Message> messages = new ArrayList<>();
        List<Message> account_messages = new ArrayList<>();
       
        // Convert Object to JSON and send back response
        if (account != null) {
            // Get all messages from database
            messages = messageService.getAllMessages();
            
            // Iterate through messages. For each message where posted_by = account_id, add to account_messages arraylist 
            for (Message message : messages) 
            {
                if (message.getPosted_by() == account.getAccount_id())
                    account_messages.add(message);
            }
            // Convert account_messages to JSON and send response
            ctx.json(mapper.writeValueAsString(account_messages)).status(200);
         } else { 
            ctx.json(mapper.writeValueAsString(account_messages)).status(200);
         }
    }

}