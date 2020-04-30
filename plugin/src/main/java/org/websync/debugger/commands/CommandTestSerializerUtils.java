package org.websync.debugger.commands;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import org.junit.Test;
import org.websync.WebSyncService;
import org.websync.debugger.testengine.TestEngineUtils;
import org.websync.websession.JdiModulesProvider;
import org.websync.websession.models.ComponentInstance;
import org.websync.websession.models.ComponentType;
import org.websync.websession.models.JdiModule;
import org.websync.websession.psimodels.PsiComponentInstance;

public class CommandTestSerializerUtils {
    public static void run() {
        ApplicationManager.getApplication().runReadAction(() -> {
            TestEngineUtils.run(CommandTestSerializerUtils.class);
        });
    }

    @Test
    public static void testDeserializing() {
        WebSyncService webSyncService = ServiceManager.getService(WebSyncService.class);

        JdiModulesProvider provider = webSyncService.getProvider();

        JdiModule jdiModule0 = provider.getJdiModule(provider.getJdiModuleNames().get(0));
        ComponentType componentType = jdiModule0.getComponentTypes().values().stream().findAny().get();
        ComponentInstance componentInstance = componentType.getComponentInstances().stream().findAny().get();
        PsiComponentInstance psiComponentInstance = (PsiComponentInstance) componentInstance;
        psiComponentInstance.synchronize(psiComponentInstance);
    }
}


