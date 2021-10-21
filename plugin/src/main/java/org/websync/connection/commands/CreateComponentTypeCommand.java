package org.websync.connection.commands;

import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import org.websync.WebSyncException;
import org.websync.WebSyncService;
import org.websync.connection.messages.browser.CreateTypeMessage;
import org.websync.frameworks.jdi.JdiElement;
import org.websync.utils.TypeNameUtils;

public class CreateComponentTypeCommand extends CommandWithDataBase<CreateTypeMessage>{
    public CreateComponentTypeCommand(WebSyncService webSyncService) {
        super(webSyncService);
    }

    @Override
    public Object execute(String commandDataString) throws WebSyncException {
        return execute(parseCommandData(commandDataString, CreateTypeMessage.class));
    }

    @Override
    public Object execute(CreateTypeMessage commandData) throws WebSyncException {
        final Module module = webSyncService.getProvider().findByFullName(commandData.projectName);
        String packageName = TypeNameUtils.getNamespaceFromFullName(commandData.parentType);
        String fileContent = getComponentTypeFileContent(commandData.typeName, packageName, commandData.baseType);
        WriteAction.runAndWait(() -> {
            PsiClass parentPsiClass = findClass(module, commandData.parentType);
            if (parentPsiClass == null) {
                throw new WebSyncException("Parent type was not found: " + commandData.parentType);
            }
            PsiDirectory parentPsiDirectory = parentPsiClass.getContainingFile().getContainingDirectory();
            WriteCommandAction.runWriteCommandAction(module.getProject(),
                    "Create component type "+commandData.type,
                    "WebSyncAction",
                    () -> {
                        PsiFile typePsiFile = createTypePsiFile(module.getProject(), commandData.typeName, fileContent);
                        parentPsiDirectory.add(typePsiFile);
                    });
        });
        return null;
    }

    private String getComponentTypeFileContent(String typeName, String packageName, String baseType) {
        baseType = StringUtil.isEmpty(baseType) ? JdiElement.JDI_UI_ELEMENT.className : baseType;
        return StringUtil.join(
                "package " + packageName + ";\n",
                "import com.epam.jdi.light.elements.pageobjects.annotations.locators.UI;\n",
                "import com.epam.jdi.light.elements.common.UIElement;\n\n",
                "public class " + typeName + " extends " + baseType + " {\n",
                "}"
        );
    }

    private PsiFile createTypePsiFile(Project project, String typeName, String fileContent) {
        PsiFileFactory fileFactory = PsiFileFactory.getInstance(project);
        String fileName = typeName + ".java";
        return fileFactory.createFileFromText(fileName, JavaLanguage.INSTANCE, fileContent);
    }
}
