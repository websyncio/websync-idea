package org.websync.connection.commands;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiDirectory;
import org.websync.WebSyncException;
import org.websync.connection.ProjectUpdatesQueue;
import org.websync.connection.messages.browser.CreateComponentTypeMessage;
import org.websync.frameworks.jdi.JdiElement;
import org.websync.psi.JdiProjectsProvider;
import org.websync.utils.PsiUtils;

public class CreateComponentTypeCommand extends CommandWithDataBase<CreateComponentTypeMessage>{
    private ProjectUpdatesQueue projectUpdatesQueue;

    public CreateComponentTypeCommand(JdiProjectsProvider projectsProvider, ProjectUpdatesQueue projectUpdatesQueue) {
        super(projectsProvider);
        this.projectUpdatesQueue = projectUpdatesQueue;
    }

    @Override
    public Object execute(String commandDataString) throws WebSyncException {
        return execute(parseCommandData(commandDataString, CreateComponentTypeMessage.class));
    }

    @Override
    public Object execute(CreateComponentTypeMessage commandData) throws WebSyncException {
        final Module module = projectsProvider.findProject(commandData.projectName);
        try {
            this.projectUpdatesQueue.captureProjectState(module.getProject());
            String fileContent = getComponentTypeFileContent(commandData.name, commandData.baseType);
            String fileName = commandData.name + ".java";
            PsiDirectory parentPsiDirectory = PsiUtils.getClassDirectory(module, commandData.parentType);
            PsiUtils.createJavaFileIn(module, fileName, fileContent, parentPsiDirectory);
        } finally {
            this.projectUpdatesQueue.releaseProjectState(module.getProject());
        }
        return null;
    }

    private String getComponentTypeFileContent(String typeName, String baseType) {
        baseType = StringUtil.isEmpty(baseType) ? JdiElement.JDI_UI_BASE_ELEMENT.className+"<TextAssert>" : baseType;
        return StringUtil.join(
                "import com.epam.jdi.light.asserts.generic.TextAssert;\n"+
                "import com.epam.jdi.light.elements.base.UIBaseElement;\n"+
                "import com.epam.jdi.light.elements.pageobjects.annotations.locators.UI;\n",
                "public class " + typeName + " extends " + baseType + " {\n",
                "}"
        );
    }
}
