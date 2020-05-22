package org.websync;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.ex.FocusChangeListener;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.awt.event.FocusEvent;

import static org.websync.PsiUtil.*;

public class WebSyncEditorFactoryListener implements EditorFactoryListener {
    @Override
    public void editorCreated(@NotNull EditorFactoryEvent event) {
        Editor editor = event.getEditor();
        if(editor instanceof EditorEx) {
            ((EditorEx) editor).addFocusListener(focusGainedListener);
        }
    }

    private final FocusChangeListener focusGainedListener = new FocusChangeListener() {
        @Override
        public void focusGained(@NotNull Editor editor) {
            // ignored
        }

        @Override
        public void focusGained(@NotNull Editor editor, @NotNull FocusEvent event) {
            Project project = editor.getProject();
            if(event.getOppositeComponent() == null || project == null || DumbService.getInstance(project).isDumb()) {
                // ignore this event since it is on the editor which has just lost focus
                // or indexing is in progress
                return;
            }
            PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
            sendShowInPageEditor(findPsiClass(psiFile));
        }

        private void sendShowInPageEditor(PsiClass psiClass) {
            boolean isComponent = isComponent(psiClass);
            if (isComponent || isPage(psiClass)) {
                String type = isComponent ? "component" : "page";
                WebSyncService webSyncService = ServiceManager.getService(WebSyncService.class);
                webSyncService.getBrowserConnectionManager().sendShowInPageEditor(type, psiClass.getQualifiedName());
            }
        }
    };
}
