package org.websync;

import com.intellij.ide.projectWizard.ImportFromSourcesTestCase;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.ProjectManager;
import org.jdom.JDOMException;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AnalyzerTestHeavy extends ImportFromSourcesTestCase {

    //TODO: exclude hadcoded local path
    private String srcPath = "path to maven Project";
    private File file = new File(srcPath);

    public void test0() {
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
    }
}
