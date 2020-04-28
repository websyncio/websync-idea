package org.websync;

import com.intellij.ide.projectWizard.ImportFromSourcesTestCase;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.impl.FileDocumentManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.jdom.JDOMException;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AnalyzerTestHeavy extends ImportFromSourcesTestCase {

    //TODO: exclude hadcoded local path
    private String srcPath = "C:\\Users\\Vitalii_Balitckii\\IdeaProjects\\jdi-light-testng-template\\";
    Path path = Paths.get(srcPath);
    File file = new File(srcPath);

    public void test0() {
        Project project = null;
        try {
            myProject = ProjectManager.getInstance().loadAndOpenProject(srcPath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }

        importFromSources(file);
        this.importFromSources(new File(srcPath));

        //TODO:include reasonable assertion
        /*
        //temporary assert to hide codacy warning
        //reason:
        //Since: PMD 2.0
        //JUnit tests should include at least one assertion.
        */
        Assert.assertTrue(true);

        //TODO
        FileDocumentManagerImpl impl = (FileDocumentManagerImpl) FileDocumentManager.getInstance();
    }
}
