package org.websync.connection.commands;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiDirectory;
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
        PsiDirectory rootDirectory = PsiUtils.getRootDirectory(module);
        String fileContent = getComponentTypeFileContent(commandData.name, commandData.baseUrl);
        String fileName = commandData.name + ".java";
        PsiUtils.createJavaFileIn(module, fileName, fileContent, rootDirectory);
        return null;
    }

    private String getComponentTypeFileContent(String typeName, String url) {
        return StringUtil.join(
            "import com.epam.jdi.light.elements.pageobjects.annotations.JSite;\n",
                "import com.epam.jdi.light.elements.pageobjects.annotations.Url;\n\n",
            "@JSite(\""+url+"\")\n"+
            "public class " + typeName +"{\n",
                "}"
        );
    }
}
