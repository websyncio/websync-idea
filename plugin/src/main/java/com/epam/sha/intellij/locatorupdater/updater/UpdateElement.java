package com.epam.sha.intellij.locatorupdater.updater;

import com.epam.sha.intellij.locatorupdater.UserKeys;
import com.epam.sha.intellij.locatorupdater.model.RequestData;
import com.epam.sha.intellij.locatorupdater.utils.PsiClassUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContextWrapper;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiJavaFile;
import org.apache.http.util.Asserts;

import java.util.Arrays;

/**
 *
 */
public class UpdateElement extends AnAction {

    private static final String PAGE_AWARE_BY_ANNOTATION = "com.epam.sha.selenium.PageAwareFindBy";

    @Override
    public void actionPerformed(AnActionEvent e) {
        //TODO: window should show old locator value, new locator value, question: replace old locator?, YES NO buttons.
        // If Yes - perform replacement. If No - close window and no nothing.
        //Messages.showMessageDialog(e.getProject(), "some message info", "PSI Info", null);

        final PsiJavaFile element = (PsiJavaFile) e.getData(CommonDataKeys.PSI_FILE);
        final RequestData data = new DataContextWrapper(e.getDataContext()).getUserData(UserKeys.CUSTOM_DATA);

        Asserts.check(element != null, "Failed to get target file data");
        Asserts.check(data != null, "No request data present");

        PsiClassUtil.getClass(element).stream()
                .flatMap(it -> Arrays.stream(it.getFields()))
                .filter(m -> m.hasAnnotation(PAGE_AWARE_BY_ANNOTATION))
                .filter(field -> {
                    PsiAnnotation annotation = field.getAnnotation(PAGE_AWARE_BY_ANNOTATION);
                    String value = annotation.findAttributeValue("findBy").getText();
                    String pageName = annotation.findAttributeValue("page").getText();
                    return value.contains(data.getOldLocator()) && pageName.contains(data.getTarget());
                })
                .forEach(it -> {
                    final String PageAwareLinkText = String.format("@PageAwareFindBy(page = \"%s\", findBy = @FindBy(css = \"%s\"))", data.getTarget(), data.getNewLocator());
                    final PsiAnnotation tmsLink = createAnnotation(PageAwareLinkText, it);
                    final Project project = it.getProject();
                    CommandProcessor.getInstance().executeCommand(project, () -> ApplicationManager.getApplication().runWriteAction(() -> {
                        it.getAnnotation(PAGE_AWARE_BY_ANNOTATION).delete();
                        it.getModifierList().add(tmsLink);
                    }), "Updated locator in PageAwareFindBy annotation", null);
                });
    }

    /**
     * @param annotation
     * @param context
     * @return
     */
    private PsiAnnotation createAnnotation(final String annotation, final PsiElement context) {
        final PsiElementFactory factory = PsiElementFactory.getInstance(context.getProject());
        return factory.createAnnotationFromText(annotation, context);
    }

}
