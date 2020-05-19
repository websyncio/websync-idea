package org.websync.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.DumbService;
import com.intellij.psi.PsiClass;
import org.jetbrains.annotations.NotNull;
import org.websync.WebSyncService;

import static org.websync.PsiUtil.*;

public class TransferPageObjectAction extends DumbAwareAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        PsiClass psiClass = findPsiClass(e.getData(CommonDataKeys.PSI_FILE));
        if (psiClass == null) {
            return;
        }
        boolean isComponent = isComponent(psiClass);
        if (isComponent || isPage(psiClass)) {
            String type = isComponent ? "component" : "page";
            WebSyncService webSyncService = ServiceManager.getService(WebSyncService.class);
            webSyncService.getBrowserConnectionManager().sendShowInPageEditor(type, psiClass.getQualifiedName());
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        PsiClass psiClass = findPsiClass(e.getData(CommonDataKeys.PSI_FILE));
        boolean isNotDumb = e.getProject() != null && !DumbService.getInstance(e.getProject()).isDumb();
        boolean enabledAndVisible = isNotDumb && (isComponent(psiClass) || isPage(psiClass));
        e.getPresentation().setEnabledAndVisible(enabledAndVisible);
    }
}
