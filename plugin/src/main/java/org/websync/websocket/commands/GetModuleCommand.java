package org.websync.websocket.commands;

import com.intellij.openapi.application.ApplicationManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.websync.react.dto.ComponentTypeDto;
import org.websync.react.dto.JdiModuleDto;
import org.websync.react.dto.PageTypeDto;
import org.websync.websession.models.JdiModule;

import java.util.stream.Collectors;

public class GetModuleCommand extends WebSyncCommand {
    static class Message extends WebSyncCommand.Message {
        public String data;
    }

    @Nullable
    @Override
    protected Object execute(@NotNull WebSyncCommand.Message inputMessage) {
        String moduleName = ((GetModuleCommand.Message) inputMessage).data;
        Object[] result = new Object[1];
        ApplicationManager.getApplication().runReadAction(() -> {
            result[0] = createDto(getWebSyncService().getProvider().getJdiModule(moduleName));
        });
        return result[0];
    }

    @NotNull
    private JdiModuleDto createDto(JdiModule module) {
        JdiModuleDto dto = new JdiModuleDto();
        dto.module = module.name;
        dto.pages = module.getPageTypes().values().stream()
                .map(PageTypeDto::new).collect(Collectors.toList());
        dto.components = module.getComponentTypes().values().stream()
                .map(ComponentTypeDto::new).collect(Collectors.toList());
        return dto;
    }

}
