package org.websync.websocket.commands;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.websync.react.dto.ComponentTypeDto;
import org.websync.react.dto.PageTypeDto;
import org.websync.react.dto.WebSessionDto;
import org.websync.websession.models.WebSession;

import java.util.stream.Collectors;

public class GetModulesCommand extends WebSyncCommand {
    @Nullable
    @Override
    protected Object execute(@NotNull Message inputMessage) {
        Object[] result = new Object[1];
        ApplicationManager.getApplication().runReadAction(() -> {
            result[0] = createDto(getWebSyncService().getProvider().getWebSessions(false).get(0));
        });
        return result[0];
    }

    @NotNull
    private WebSessionDto createDto(WebSession web) {
        Project project = getWebSyncService().getProvider().getProjects().get(0);
        WebSessionDto dto = new WebSessionDto();
        dto.module = project.getName();
        dto.pages = web.getPageTypes().values().stream()
                .map(PageTypeDto::new).collect(Collectors.toList());
        dto.components = web.getComponentTypes().values().stream()
                .map(ComponentTypeDto::new).collect(Collectors.toList());
        return dto;
    }

}
