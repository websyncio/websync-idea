package org.websync.browserConnection;

import com.google.gson.Gson;
import com.intellij.openapi.project.Project;
import org.websync.browserConnection.commands.GetWebSessionCommand;
import org.websync.websession.WebSessionProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommandHandler {
    private List<Project> projects = new ArrayList<>();
    final String CMD_GET_PAGEOBJECTS = "get-web-session";
    final String CMD_GET_PROJECTS = "get-projects";

    WebSessionProvider webSessionProvider;
    WebSessionSerializer webSessionSerializer;

    public CommandHandler(WebSessionProvider webSessionProvider, WebSessionSerializer webSessionSerializer) {
        this.webSessionProvider = webSessionProvider;
        this.webSessionSerializer = webSessionSerializer;
    }

    public String handle(String command) {
        switch (command) {
            case CMD_GET_PAGEOBJECTS:
                return new GetWebSessionCommand(webSessionProvider, webSessionSerializer).execute();
            case CMD_GET_PROJECTS:
                List<Project> sessionProject = webSessionProvider.getProjects();
                if (!sessionProject.isEmpty() && !projects.equals(sessionProject)) {
                    projects = sessionProject;
                }
                String names = new Gson().toJson(projects.stream().map(Project::getName).collect(Collectors.toList()));
                return String.format("{\"projects\": %s}", names);
        }
        return null;
    }
}
