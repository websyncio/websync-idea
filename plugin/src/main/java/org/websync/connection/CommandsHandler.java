package org.websync.connection;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.websync.WebSyncException;
import org.websync.WebSyncService;
import org.websync.connection.commands.*;
import org.websync.connection.messages.Message;
import org.websync.connection.messages.ResponseMessage;

public class CommandsHandler {
    protected WebSyncService webSyncService;

    public CommandsHandler(WebSyncService webSyncService){
        this.webSyncService = webSyncService;
    }

    public ResponseMessage handle(String messageString) {
        Message message = parseMessage(messageString);
        Command command = getCommand(message.type);
        ResponseMessage responseMessage;
        try {
            Object result = command.execute(messageString);
            responseMessage = new ResponseMessage(message.type, true);
            responseMessage.data = result;
        } catch (WebSyncException e) {
            responseMessage = new ResponseMessage(message.type, false);
            responseMessage.error = e.getMessage();
        }
        return responseMessage;
    }

    private Command getCommand(String messageType) {
        switch (messageType) {
            case "get-projects-list":
                return new GetProjectsCommand(webSyncService);
            case "get-project":
                return new GetProjectCommand(webSyncService);
            case "create-website":
                return new CreateWebsiteCommand(webSyncService);
            case "delete-website":
                return null;
            case "update-website":
                return new UpdateWebsiteCommand(webSyncService);
            case "create-page-type":
                return new CreatePageTypeCommand(webSyncService);
            case "delete-page-type":
                return null;
            case "update-page-type":
                return null;
            case "add-page-instance":
                return null;
            case "delete-page-instance":
                return null;
            case "update-page-instance":
                return null;
            case "create-component-type":
                return new CreateComponentTypeCommand(webSyncService);
            case "delete-component-type":
                return null;
            case "add-component-instance":
                return new AddComponentInstanceCommand(webSyncService);
            case "delete-component-instance":
                return new DeleteComponentInstanceCommand(webSyncService);
            case "update-component-instance":
                return new UpdateComponentInstanceCommand(webSyncService);
            default:
                throw new IllegalArgumentException(messageType);
        }
    }

    private Message parseMessage(String message) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            return mapper.readValue(message, Message.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid message received: " + message, e);
        }
    }
}
