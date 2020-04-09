package org.websync;

import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.io.FileUtil;
import org.jetbrains.idea.maven.MavenTestCase;
import org.jetbrains.idea.maven.server.MavenServerManager;

import java.io.File;

public class AnalyzerTestMaven extends MavenTestCase {
    String srcPath = "C:\\Users\\Vitalii_Balitckii\\IdeaProjects\\jdi-light-testng-template\\src\\";

    @Override
    protected void setUp() throws Exception {
//        VfsRootAccess.allowRootAccess(getTestRootDisposable(), PathManager.getConfigPath());

        super.setUp();
    }

    @Override
    protected void setUpInWriteAction() throws Exception {
        super.setUpInWriteAction();
        removeFromLocalRepository("test");
    }

    protected void removeFromLocalRepository(String relativePath) {
        if (SystemInfo.isWindows) {
            MavenServerManager.getInstance().shutdown(true);
        }
        FileUtil.delete(new File(getRepositoryPath(), relativePath));
    }

    public void test0() {
        //TODO Content has been never used
    }

}
