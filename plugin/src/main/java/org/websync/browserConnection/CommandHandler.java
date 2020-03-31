package org.websync.browserConnection;

import com.google.gson.Gson;
import com.intellij.openapi.project.Project;
import org.websync.browserConnection.commands.GetWebSessionCommand;
import org.websync.websession.WebSessionProvider;

import java.util.stream.Collectors;

public class CommandHandler {
    final String CMD_GET_PAGEOBJECTS = "get-web-session";
    final String CMD_GET_PROJECT_PAGEOBJECTS = "get-project-web-session";
    final String CMD_PROJECTS = "get-projects";

    WebSessionProvider webSessionProvider;
    WebSessionSerializer webSessionSerializer;

    public CommandHandler(WebSessionProvider webSessionProvider, WebSessionSerializer webSessionSerializer) {
        this.webSessionProvider = webSessionProvider;
        this.webSessionSerializer = webSessionSerializer;
    }

    public String handle(String command) {
        switch (command.split(":")[0]) {
            case CMD_GET_PAGEOBJECTS:
                return new GetWebSessionCommand(webSessionProvider, webSessionSerializer).execute();
            case CMD_GET_PROJECT_PAGEOBJECTS:
                return new GetWebSessionCommand(webSessionProvider, webSessionSerializer).execute(command.split(":")[1]);
            case CMD_PROJECTS:
                String projects = new Gson().toJson(webSessionProvider.getProjects().stream().map(Project::getName).collect(Collectors.toList()));
                return String.format("{\"projects\": %s}", projects);
        }
        return null;
    }
}
