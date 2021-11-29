package org.websync.connection.commands;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiPackage;
import org.websync.WebSyncException;
import org.websync.WebSyncService;
import org.websync.connection.messages.browser.CreateWebsiteMessage;
import org.websync.utils.PsiUtils;

public class CreateWebsiteCommand extends CommandWithDataBase<CreateWebsiteMessage> {
    public CreateWebsiteCommand(WebSyncService webSyncService) {
        super(webSyncService);
    }

    @Override
    public Object execute(String commandDataString) throws WebSyncException {
        return execute(parseCommandData(commandDataString, CreateWebsiteMessage.class));
    }

    @Override
    public Object execute(CreateWebsiteMessage commandData) throws WebSyncException {
        final Module module = webSyncService.getModulesProvider().findProject(commandData.projectName);
        PsiPackage rootPackage = PsiUtils.getRootPackage(module);
        PsiDirectory[] directories = rootPackage.getDirectories();
        if (directories.length == 0) {
            throw new WebSyncException("Unable to define root directory.");
        }
        PsiDirectory rootDirectory = directories[0];
        String fileContent = getComponentTypeFileContent(rootPackage.getQualifiedName(), commandData.name, commandData.baseUrl);
        String fileName = commandData.name + ".java";
        PsiUtils.createJavaFileIn(module, fileName, fileContent, rootDirectory);
        return null;
    }

    private String getComponentTypeFileContent(String packageName, String typeName, String url) {
        return StringUtil.join(
                "package " + packageName + ";\n" +
                        "import com.epam.jdi.light.elements.pageobjects.annotations.JSite;\n",
                "import com.epam.jdi.light.elements.pageobjects.annotations.Url;\n\n",
                "@JSite(\"" + url + "\")\n" +
                        "public class " + typeName + "{\n",
                "}"
        );
    }
}
