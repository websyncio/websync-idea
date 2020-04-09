package org.websync.websocket.commands;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import org.websync.WebSyncService;
import org.websync.browserConnection.WebSessionSerializer;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


public class GetWebSessionCommand {
    private final WebSyncService webSyncService;

    public GetWebSessionCommand(WebSyncService webSyncService) {
        this.webSyncService = webSyncService;
    }
//
    public void execute() {
        final String[] json = new String[1];
        List<String> projects = webSyncService.getProvider().getProjects().stream().map(Project::getName).collect(Collectors.toList());
        if (projects.isEmpty()) {
            return;
        }
        ApplicationManager.getApplication().runReadAction(() -> {
            json[0] = webSyncService.getSerializer().serialize(webSyncService.getProvider().getWebSessions(false).get(0));
        });
        try {
            WebSessionSerializer.ReactDataPayload res = webSyncService.getSerializer().deserialize(json[0]);
            System.out.println(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        json[0] = json[0].replaceFirst("\\{", String.format("{\"project\": \"%s\",", projects.get(0)));
        webSyncService.getBrowserConnection().broadcast(json[0]);
    }

    public void execute(String projectName) {
        final String[] json = new String[1];
        int index = webSyncService.getProvider().getProjects().stream().map(Project::getName).collect(Collectors.toList()).indexOf(projectName);
        if (index < 0) {
            return;
        }
        ApplicationManager.getApplication().runReadAction(() -> {
            json[0] = webSyncService.getSerializer().serialize(webSyncService.getProvider().getWebSession(
                    webSyncService.getProvider().getProjects().get(index)));
        });
        json[0] = json[0].replaceFirst("\\{", String.format("{\"project\": \"%s\",", projectName));
        webSyncService.getBrowserConnection().broadcast(json[0]);
    }
}