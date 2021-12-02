package org.websync;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiTreeChangeAdapter;
import com.intellij.psi.PsiTreeChangeEvent;
import org.jetbrains.annotations.NotNull;
import org.websync.connection.ProjectUpdatesQueue;
import org.websync.utils.LoggerUtils;

public class WebSyncPsiTreeChangeListener extends PsiTreeChangeAdapter {
    private ProjectUpdatesQueue projectUpdatesQueue;

    public WebSyncPsiTreeChangeListener(ProjectUpdatesQueue projectUpdatesQueue) {
        this.projectUpdatesQueue = projectUpdatesQueue;
    }

    @Override
    public void childAdded(@NotNull PsiTreeChangeEvent event) {
        LoggerUtils.logeTreeChangeEvent(event.toString());
        PsiElement child = event.getChild();
        if (child == null || child instanceof PsiFile) {
            // . we should ignore file adding event because
            // at this stage we can not get new version of project data
            // since file still does not exist
            return;
        }
        System.out.println("child added: " + child.getContainingFile().getName());
        projectUpdatesQueue.addStructureUpdate(child.getContainingFile());
    }

    @Override
    public void childRemoved(@NotNull PsiTreeChangeEvent event) {
        LoggerUtils.logeTreeChangeEvent(event.toString());
        PsiElement child = event.getChild();
        if (child == null || !(child instanceof PsiFile)) {
            // . we are interested only in file structure changes
            return;
        }
        System.out.println("child removed: " + child.getContainingFile().getName());
        projectUpdatesQueue.addStructureUpdate(child.getContainingFile());
    }

//    @Override
//    public void childReplaced(@NotNull PsiTreeChangeEvent event) {
    // looks like a duplication for childrenChanged()
//        handle(event);
//    }

    @Override
    public void childMoved(@NotNull PsiTreeChangeEvent event) {
        LoggerUtils.logeTreeChangeEvent(event.toString());
        PsiElement child = event.getChild();
        if (child == null || !(child instanceof PsiFile)) {
            // . we are interested only in file structure changes
            return;
        }
        System.out.println("child moved: " + child.getContainingFile().getName());
        projectUpdatesQueue.addStructureUpdate(child.getContainingFile());
    }

    @Override
    public void childrenChanged(@NotNull PsiTreeChangeEvent event) {
        LoggerUtils.logeTreeChangeEvent(event.toString());
        projectUpdatesQueue.addContentUpdate(event.getFile());
    }

    // PsiFile is null for this event
//    @Override
//    public void propertyChanged(@NotNull PsiTreeChangeEvent event) {
//        handle(event);
//    }

}
