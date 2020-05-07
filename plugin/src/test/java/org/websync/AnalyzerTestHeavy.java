package org.websync;

import com.intellij.ide.projectWizard.ImportFromSourcesTestCase;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.impl.FileDocumentManagerImpl;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.jdom.JDOMException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AnalyzerTestHeavy extends ImportFromSourcesTestCase {

    File file = new File(srcPath);

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
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
