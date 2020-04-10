package org.websync.websocket;

import com.google.gson.Gson;
import com.intellij.openapi.project.Project;
import org.websync.WebSyncService;
import org.websync.websocket.commands.GetWebSessionCommand;

import java.util.stream.Collectors;

public class CommandHandler {
    public final static String CMD_GET_PAGEOBJECTS = "get-web-session";
    public final static String CMD_GET_PROJECT_PAGEOBJECTS = "get-project-web-session";
    public final static String CMD_PROJECTS = "get-projects";

    WebSyncService webSyncService;

    public CommandHandler(WebSyncService webSyncService) {
        this.webSyncService = webSyncService;
    }

    public void handle(String command) {
        switch (command.split(":")[0]) {
            case CMD_GET_PAGEOBJECTS: {
                new GetWebSessionCommand(webSyncService).execute();
                break;
            }
            case CMD_GET_PROJECT_PAGEOBJECTS: {
                new GetWebSessionCommand(webSyncService).execute(command.split(":")[1]);
                break;
            }
            case CMD_PROJECTS: {
                String projects = new Gson().toJson(webSyncService.getProvider().getProjects().stream().map(Project::getName).collect(Collectors.toList()));
                webSyncService.getBrowserConnection().broadcast(String.format("{\"projects\": %s}", projects));
                break;
            }
        }
    }
}