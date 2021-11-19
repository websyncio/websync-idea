package org.websync.connection.commands;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiDirectory;
import org.websync.WebSyncException;
import org.websync.WebSyncService;
import org.websync.connection.messages.browser.CreatePageTypeMessage;
import org.websync.frameworks.jdi.JdiAttribute;
import org.websync.frameworks.jdi.JdiElement;
import org.websync.utils.PsiUtils;
import org.websync.utils.StringUtils;
import org.websync.utils.TypeNameUtils;

public class CreatePageTypeCommand extends CommandWithDataBase<CreatePageTypeMessage>{
    public CreatePageTypeCommand(WebSyncService webSyncService) {
        super(webSyncService);
    }


    @Override
    public Object execute(String commandDataString) throws WebSyncException {
        return execute(parseCommandData(commandDataString, CreatePageTypeMessage.class));
    }

    @Override
    public Object execute(CreatePageTypeMessage commandData) throws WebSyncException {
        final Module module = webSyncService.getModulesProvider().findProject(commandData.projectName);
        String packageName = TypeNameUtils.getNamespaceFromFullName(commandData.website);
        String fileContent = getComponentTypeFileContent(packageName, commandData.name, commandData.baseType);
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
        return null;
    }

    private String getComponentTypeFileContent(String packageName, String typeName, String baseType) {
        baseType = StringUtil.isEmpty(baseType) ? JdiElement.JDI_WEB_PAGE.className : baseType;
        return StringUtil.join(
                "package " + packageName + ";\n"+
                        "import com.epam.jdi.light.elements.composite.WebPage;\n",
                        "import com.epam.jdi.light.elements.pageobjects.annotations.Url;\n",
                        "public class " + typeName + " extends " + baseType + " {\n",
                        "}"
        );
    }
}
