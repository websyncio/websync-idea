package org.websync.connection.commands;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiDirectory;
import org.websync.WebSyncException;
import org.websync.connection.ProjectUpdatesQueue;
import org.websync.connection.messages.browser.CreatePageTypeMessage;
import org.websync.frameworks.jdi.JdiAttribute;
import org.websync.frameworks.jdi.JdiElement;
import org.websync.psi.SeleniumProjectsProvider;
import org.websync.utils.PsiUtils;
import org.websync.utils.StringUtils;

public class CreatePageTypeCommand extends CommandWithDataBase<CreatePageTypeMessage>{
    private ProjectUpdatesQueue projectUpdatesQueue;

    public CreatePageTypeCommand(SeleniumProjectsProvider projectsProvider, ProjectUpdatesQueue projectUpdatesQueue) {
        super(projectsProvider);
        this.projectUpdatesQueue = projectUpdatesQueue;
    }


    @Override
    public Object execute(String commandDataString) throws WebSyncException {
        return execute(parseCommandData(commandDataString, CreatePageTypeMessage.class));
    }

    @Override
    public Object execute(CreatePageTypeMessage commandData) throws WebSyncException {
        final Module module = projectsProvider.findProject(commandData.projectName);
        try {
            this.projectUpdatesQueue.captureProjectState(module.getProject());
            String fileContent = getComponentTypeFileContent(commandData.name, commandData.baseType);
            PsiDirectory parentPsiDirectory = PsiUtils.getClassDirectory(module, commandData.website);
            String fileName = commandData.name + ".java";
            // Create page object class
            PsiUtils.createJavaFileIn(module, fileName, fileContent, parentPsiDirectory);
            // Add page object instance to website

            PsiUtils.addFieldToClass(module,
                    commandData.name,
                    StringUtils.toCamelCase(commandData.name),
                    JdiAttribute.JDI_URL.className,
                    commandData.absoluteUrl,
                    commandData.website);
        }finally {
            this.projectUpdatesQueue.releaseProjectState(module.getProject());
        }
        return null;
    }

    private String getComponentTypeFileContent(String typeName, String baseType) {
        baseType = StringUtil.isEmpty(baseType) ? JdiElement.JDI_WEB_PAGE.className : baseType;
        return StringUtil.join(
                        "import com.epam.jdi.light.elements.composite.WebPage;\n",
                        "import com.epam.jdi.light.elements.pageobjects.annotations.Url;\n",
                        "import com.epam.jdi.light.elements.pageobjects.annotations.locators.UI;\n\n",
                        "public class " + typeName + " extends " + baseType + " {\n",
                        "}"
        );
    }
}
