package org.websync.connection;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.websync.exceptions.DumbProjectException;
import org.websync.exceptions.WebSyncException;
import org.websync.connection.commands.*;
import org.websync.connection.messages.Message;
import org.websync.connection.messages.ResponseMessage;
import org.websync.psi.SeleniumProjectsProvider;

public class CommandsHandler {
    private SeleniumProjectsProvider projectsProvider;
    private ProjectUpdatesQueue projectUpdatesQueue;

    public CommandsHandler(SeleniumProjectsProvider projectsProvider, ProjectUpdatesQueue projectUpdatesQueue){
        this.projectsProvider = projectsProvider;
        this.projectUpdatesQueue = projectUpdatesQueue;
    }

    public ResponseMessage handle(String messageString) throws DumbProjectException {
        Message message = parseMessage(messageString);
        Command command = getCommand(message.type);
        ResponseMessage responseMessage;
        try {
            Object result = command.execute(messageString);
            responseMessage = new ResponseMessage(message.asyncId, message.type, true);
            responseMessage.data = result;
        } catch (WebSyncException e) {
            responseMessage = new ResponseMessage(message.asyncId, message.type, false);
            responseMessage.error = e.getMessage();
        }
        return responseMessage;
    }

    private Command getCommand(String messageType) {
        switch (messageType) {
            case "match-url":
                return new MatchUrlCommand(projectsProvider);
            case "open-file":
                return new OpenFileForClassCommand(projectsProvider);
            case "get-projects-list":
                return new GetProjectsCommand(projectsProvider);
            case "get-project":
                return new GetProjectCommand(projectsProvider);
            case "delete-website":
                return null;
            case "update-website":
                return new UpdateWebsiteCommand(projectsProvider);
            case "create-website":
                return new CreateWebsiteCommand(projectsProvider, projectUpdatesQueue);
            case "create-page-type":
                return new CreatePageTypeCommand(projectsProvider, projectUpdatesQueue);
            case "create-component-type":
                return new CreateComponentTypeCommand(projectsProvider, projectUpdatesQueue);
            case "delete-page-type":
                return null;
            case "update-page-type":
                return null;
            case "add-page-instance":
                return null;
            case "delete-page-instance":
                return null;
            case "update-page-instance":
                return new UpdatePageInstanceCommand(projectsProvider, projectUpdatesQueue);
            case "delete-component-type":
                return null;
            case "add-component-instance":
                return new AddComponentInstanceCommand(projectsProvider);
            case "delete-component-instance":
                return new DeleteComponentInstanceCommand(projectsProvider);
            case "update-component-instance":
                return new UpdateComponentInstanceCommand(projectsProvider, projectUpdatesQueue);
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
