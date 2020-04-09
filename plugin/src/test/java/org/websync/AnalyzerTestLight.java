package org.websync;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassOwner;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import org.junit.Test;
import org.websync.logger.Logger;
import org.websync.websession.PsiWebSessionProvider;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AnalyzerTestLight extends LightJavaCodeInsightFixtureTestCase {

    // see https://www.jetbrains.org/intellij/sdk/docs/basics/testing_plugins/test_project_and_testdata_directories.html
    // first paragraph about 'src'
    private String srcPath = "C:\\Users\\Vitalii_Balitckii\\IdeaProjects\\jdi-light-testng-template\\src\\";
    private String javaPath = "main\\java\\";
    private String classPath = "org\\mytests\\uiobjects\\example\\site\\pages\\";
//    String exampleClassFile = "DatesPage.java";
//    Path path = Paths.get(srcPath);

    @Override
    public String getTestDataPath() {
        return srcPath;
    }

    @Test
    public void test0() {
        Logger.print(String.format("TempDir = %s", this.myFixture.getTempDirPath()));

        long startTime = System.nanoTime();
        myFixture.copyDirectoryToProject("", "");
        long endTime = System.nanoTime();
        Logger.print(
                String.format("Copy from directory contained java classes to virtual project. Time = %.3f.",
                        (double)(endTime - startTime) / 1000000000));

        Logger.print("Filenames:");
        Arrays.stream(FilenameIndex.getAllFilenames(getProject())).forEach(f -> {
            Logger.print("- " + f);
        });

        GlobalSearchScope projectScope = GlobalSearchScope.allScope(getProject());

        Logger.print("PsiFiles:");
        Arrays.stream(FilenameIndex.getAllFilenames(getProject())).forEach(f -> {
            Arrays.stream(FilenameIndex.getFilesByName(getProject(), f, projectScope)).forEach(psiFile -> {
                Logger.print("- " + psiFile.getVirtualFile().getPath());
            });
        });

        Logger.print("Classes:");
        Arrays.stream(PsiShortNamesCache.getInstance(getProject()).getAllClassNames()).forEach(c -> {
            Logger.print("- " + c);
        });

        Logger.print("PsiClasses:");
        Arrays.stream(FilenameIndex.getAllFilenames(getProject()))
                .filter(f -> f.endsWith(".java"))
                .forEach(f -> {
                    Arrays.stream(FilenameIndex.getFilesByName(getProject(), f, projectScope))
                            .forEach(psiFile -> {
                                PsiClass psiClass = ((PsiClassOwner) psiFile).getClasses()[0];
                                Logger.print("- " + psiClass.getQualifiedName());
                            });
                });

        // Start Analyzer of JDI Project structure
        // ...

        // Some tests
        // ...
    }

    @Test
    public void test01() {
        long startTime = System.nanoTime();
        myFixture.copyDirectoryToProject("", "");
        long endTime = System.nanoTime();
        Logger.print(
                String.format("Copy time from directory contained java classes to virtual project. Time = %.3f.",
                        (double)(endTime - startTime) / 1000000000));

        // To get inheritors of WebPage class
        List<PsiClass> psiClassList = FilenameIndex.getAllFilesByExt(getProject(), "java").stream()
                .map(f -> {
                    PsiFile psiFile = myFixture.getPsiManager().findFile(f);
                    PsiClass psiClass = ((PsiClassOwner) psiFile).getClasses()[0];
                    return psiClass;
                }).collect(Collectors.toList());

        List<PsiClass> psiWebPages = psiClassList.stream().filter(c -> Arrays.stream(c.getSuperTypes())
                .filter(s -> s.getClassName().equals("WebPage"))
                .count() > 0)
                .collect(Collectors.toList());

        psiWebPages.forEach(c -> {
            Logger.print(c.getQualifiedName());

            Logger.print(
                    String.format("     SuperTypes[]: %s",
                            String.join(", ", Arrays.stream(c.getSuperTypes())
                                    .map(s -> s.getClassName())
                                    .collect(Collectors.toList()))));

            Logger.print(String.format("     SuperClassType: %s", c.getSuperClassType().getName()));

            Logger.print(String.format("     SuperClass: %s", c.getSuperClass()));

            Logger.print(
                    String.format("     SuperTypes[]: %s",
                            String.join(", ", Arrays.stream(c.getSupers())
                                    .map(s -> s.getName())
                                    .collect(Collectors.toList()))));
        });
    }

    @Test
    public void test02() {
        long startTime = System.nanoTime();
        myFixture.copyDirectoryToProject("", "");
        long endTime = System.nanoTime();
        Logger.print(
                String.format("Copy time from directory contained java classes to virtual project. Time = %.3f.",
                        (double)(endTime - startTime) / 1000000000));

        new PsiWebSessionProvider().getWebSessions(false);
    }

    @Test
    public void test1() {
        long startTime = System.nanoTime();
        PsiFile[] psiFiles = myFixture.configureByFiles(
                javaPath + classPath + "ContactFormPage.java",
                javaPath + classPath + "ContactsPage.java",
                javaPath + classPath + "DatesPage.java",
                javaPath + classPath + "HomePage.java",
                javaPath + classPath + "Html5Page.java",
                javaPath + classPath + "JDIPerformancePage.java",
                javaPath + classPath + "UsersPage.java"
        );
        long endTime = System.nanoTime();
        Logger.print(String.format("%.3f", (double)(endTime - startTime) / 1000000000));

        // example
        PsiClass clazz = myFixture.findClass("org.mytests.uiobjects.example.site.pages.ContactFormPage");

        // example
        PsiShortNamesCache.getInstance(getProject()).getAllClassNames();

        // Some tests
        // ...
    }

    public void test2() {
        long startTime = System.nanoTime();
        VirtualFile virtualFile = myFixture.copyFileToProject(javaPath + classPath + "ContactFormPage.java");

        long endTime = System.nanoTime();
        Logger.print(String.format("%.3f", (double)(endTime - startTime) / 1000000000));

        // example
        PsiShortNamesCache.getInstance(getProject()).getAllClassNames();

        // Some tests
        // ...
    }
}
