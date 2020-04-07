package org.websync.debugger.commands;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import org.junit.Test;
import org.websync.WebSyncService;
import org.websync.debugger.testengine.TestEngine;
import org.websync.websession.WebSessionProvider;
import org.websync.websession.models.WebSession;

public class CommandTestSerializer {
    public static void run() {
        ApplicationManager.getApplication().runReadAction(() -> {
            TestEngine.run(CommandTestSerializer.class);
        });
    }

    @Test
    public static void testDeserializing() {
        WebSyncService webSyncService = ServiceManager.getService(WebSyncService.class);

        WebSessionProvider provider = webSyncService.getProvider();
        String json = webSyncService.getSerializer().serialize(provider.getWebSession(
                provider.getProjects().get(0)));

        WebSession websession = webSyncService.getSerializer().deserialize(json);
    }
}


