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

import java.util.Arrays;

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
        final Module module = webSyncService.getModulesProvider().findProject(commandData.projectName);
        String packageName = TypeNameUtils.getNamespaceFromFullName(commandData.parentType);
        String fileContent = getComponentTypeFileContent(commandData.typeName, packageName, commandData.baseType);
        String fileName = commandData.typeName + ".java";
        WriteAction.runAndWait(() -> {
            PsiClass parentPsiClass = findClass(module, commandData.parentType);
            if (parentPsiClass == null) {
                throw new WebSyncException("Parent type was not found: " + commandData.parentType);
            }
            PsiDirectory parentPsiDirectory = parentPsiClass.getContainingFile().getContainingDirectory();
            PsiFile[] files = parentPsiDirectory.getFiles();
            boolean duplicatesExist = Arrays.stream(files).anyMatch(f -> f.getName().equals(fileName));
            if(duplicatesExist) {
                throw new WebSyncException("File '" + fileName + "' already exists in '" + packageName + "'");
            }
            WriteCommandAction.runWriteCommandAction(module.getProject(),
                    "Create component type "+commandData.type,
                    "WebSyncAction",
                    () -> {
                        PsiFile typePsiFile = createJavaPsiFile(module.getProject(), fileName, fileContent);
                        parentPsiDirectory.add(typePsiFile);
                    });
        });
        return null;
    }

    private String getComponentTypeFileContent(String typeName, String packageName, String baseType) {
        baseType = StringUtil.isEmpty(baseType) ? JdiElement.JDI_UI_BASE_ELEMENT.className+"<UIAssert>" : baseType;
        return StringUtil.join(
                "package " + packageName + ";\n",
                "import com.epam.jdi.light.asserts.generic.UIAssert;\n"+
                "import com.epam.jdi.light.elements.base.UIBaseElement;\n"+
                "import com.epam.jdi.light.elements.pageobjects.annotations.locators.UI;\n",
                "public class " + typeName + " extends " + baseType + " {\n",
                "}"
        );
    }

    private PsiFile createJavaPsiFile(Project project, String fileName, String fileContent) {
        PsiFileFactory fileFactory = PsiFileFactory.getInstance(project);
        return fileFactory.createFileFromText(fileName, JavaLanguage.INSTANCE, fileContent);
    }
}
