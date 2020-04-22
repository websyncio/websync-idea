package org.websync.debugger.commands;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import org.junit.Test;
import org.websync.WebSyncService;
import org.websync.debugger.testengine.TestEngine;
import org.websync.websession.WebSessionProvider;
import org.websync.websession.models.ComponentInstance;
import org.websync.websession.models.ComponentType;
import org.websync.websession.models.WebSession;
import org.websync.websession.psimodels.PsiComponentInstance;

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

        WebSession webSession0 = provider.getWebSessions(true).get(0);
        ComponentType componentType = webSession0.getComponentTypes().values().stream().findAny().get();
        ComponentInstance componentInstance = componentType.getComponentInstances().stream().findAny().get();
        PsiComponentInstance psiComponentInstance = (PsiComponentInstance) componentInstance;
        psiComponentInstance.synchronize(psiComponentInstance);
    }
}


