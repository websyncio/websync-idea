package org.websync.connection.commands;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiPackage;
import org.websync.WebSyncException;
import org.websync.connection.ProjectUpdatesQueue;
import org.websync.connection.messages.browser.CreateWebsiteMessage;
import org.websync.psi.JdiProjectsProvider;
import org.websync.utils.PsiUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CreateWebsiteCommand extends CommandWithDataBase<CreateWebsiteMessage> {
    private ProjectUpdatesQueue projectUpdatesQueue;

    public CreateWebsiteCommand(JdiProjectsProvider projectsProvider, ProjectUpdatesQueue projectUpdatesQueue) {
        super(projectsProvider);
        this.projectUpdatesQueue = projectUpdatesQueue;
    }

    @Override
    public Object execute(String commandDataString) throws WebSyncException {
        return execute(parseCommandData(commandDataString, CreateWebsiteMessage.class));
    }

    @Override
    public Object execute(CreateWebsiteMessage commandData) throws WebSyncException {
        final Module module = projectsProvider.findProject(commandData.projectName);
        try {
            WriteAction.runAndWait(() -> {
                this.projectUpdatesQueue.captureProjectState(module.getProject());
                PsiPackage rootPackage = PsiUtils.getRootPackage(module);
                List<PsiDirectory> directories = Arrays.stream(rootPackage.getDirectories())
                        .filter(d -> d.toString().endsWith("\\src\\main\\java")).collect(Collectors.toList());
                if (directories.size() != 1) {
                    throw new WebSyncException("Unable to define root directory.");
                }
                PsiDirectory rootDirectory = directories.get(0);
                String fileContent = getComponentTypeFileContent(commandData.name, commandData.baseUrl);
                String fileName = commandData.name + ".java";
                PsiUtils.createJavaFileIn(module, fileName, fileContent, rootDirectory);
            });
        } finally {
            this.projectUpdatesQueue.releaseProjectState(module.getProject());
        }
        return null;
    }

    private String getComponentTypeFileContent(String typeName, String url) {
        return StringUtil.join(
                        "import com.epam.jdi.light.elements.pageobjects.annotations.JSite;\n",
                "import com.epam.jdi.light.elements.pageobjects.annotations.Url;\n\n",
                "@JSite(\"" + url + "\")\n" +
                        "public class " + typeName + "{\n",
                "}"
        );
    }
}
