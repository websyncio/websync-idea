package org.websync.connection.commands;

import com.intellij.openapi.application.ApplicationManager;
import org.jetbrains.annotations.NotNull;
import org.websync.WebSyncException;
import org.websync.WebSyncService;
import org.websync.connection.dto.ComponentTypeDto;
import org.websync.connection.dto.JdiModuleDto;
import org.websync.connection.dto.PageTypeDto;
import org.websync.connection.dto.WebsiteDto;
import org.websync.connection.messages.browser.GetProjectMessage;
import org.websync.models.JdiModule;

import java.util.stream.Collectors;

public class GetProjectCommand extends CommandWithDataBase<GetProjectMessage> {
    public GetProjectCommand(WebSyncService webSyncService) {
        super(webSyncService);
    }

    @Override
    public Object execute(GetProjectMessage commandData) throws WebSyncException {
        // hack to assign variable from read action
        Object[] result = new Object[1];
        ApplicationManager.getApplication().runReadAction(() -> {
            result[0] = createDto(webSyncService.getProvider().getJdiModule(commandData.projectName));
        });
        return result[0];
    }

    @Override
    public Object execute(String commandDataString) throws WebSyncException {
        return execute(parseCommandData(commandDataString, GetProjectMessage.class));
    }

    @NotNull
    private JdiModuleDto createDto(JdiModule module) {
        JdiModuleDto dto = new JdiModuleDto();
        dto.project = module.name;
        dto.pageTypes = module.getPageTypes().values().stream()
                .map(PageTypeDto::new).collect(Collectors.toList());
        dto.componentTypes = module.getComponentTypes().values().stream()
                .map(ComponentTypeDto::new).collect(Collectors.toList());
        dto.webSites = module.getWebsites().values().stream()
                .map(WebsiteDto::new).collect(Collectors.toList());
        return dto;
    }
}
