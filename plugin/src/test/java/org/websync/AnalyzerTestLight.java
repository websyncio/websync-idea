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
import org.websync.websession.PsiWebSessionProvider;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AnalyzerTestLight extends LightJavaCodeInsightFixtureTestCase {

    // see https://www.jetbrains.org/intellij/sdk/docs/basics/testing_plugins/test_project_and_testdata_directories.html
    // first paragraph about 'src'
    String srcPath = "C:\\Users\\Vitalii_Balitckii\\IdeaProjects\\jdi-light-testng-template\\src\\";
    String javaPath = "main\\java\\";
    String classPath = "org\\mytests\\uiobjects\\example\\site\\pages\\";
//    String exampleClassFile = "DatesPage.java";
//    Path path = Paths.get(srcPath);

    @Override
    public String getTestDataPath() {
        return srcPath;
    }

//    @Override
//    public String getBasePath() {
//        return srcPath;
//    }
//
//    //    @Override
//    protected Path getProjectDirOrFile() {
//        return path;
//    }

    @Test
    public void test0() {
        System.out.println(String.format("TempDir = %s", this.myFixture.getTempDirPath()));

        long startTime = System.nanoTime();
        myFixture.copyDirectoryToProject("", "");
        long endTime = System.nanoTime();
        System.out.println(
                String.format("%s - the time of coping from directory contained java classes to virtual project",
                        endTime - startTime));

        System.out.println();
        System.out.println("Filenames:");
        Arrays.stream(FilenameIndex.getAllFilenames(getProject())).forEach(f -> {
            System.out.println("- " + f);
        });

        GlobalSearchScope projectScope = GlobalSearchScope.allScope(getProject());

        System.out.println();
        System.out.println("PsiFiles:");
        Arrays.stream(FilenameIndex.getAllFilenames(getProject())).forEach(f -> {
            Arrays.stream(FilenameIndex.getFilesByName(getProject(), f, projectScope)).forEach(psiFile -> {
                System.out.println("- " + psiFile.getVirtualFile().getPath());
            });
        });

        System.out.println();
        System.out.println("Classes:");
        Arrays.stream(PsiShortNamesCache.getInstance(getProject()).getAllClassNames()).forEach(c -> {
            System.out.println("- " + c);
        });

        System.out.println();
        System.out.println("PsiClasses:");
        Arrays.stream(FilenameIndex.getAllFilenames(getProject()))
                .filter(f -> f.endsWith(".java"))
                .forEach(f -> {
                    Arrays.stream(FilenameIndex.getFilesByName(getProject(), f, projectScope))
                            .forEach(psiFile -> {
                                PsiClass psiClass = ((PsiClassOwner) psiFile).getClasses()[0];
                                System.out.println("- " + psiClass.getQualifiedName());
                            });
                });

        // Start Analyzer of JDI Project structure
        // ...

        // Some tests
        // ...

        System.out.println();
    }

    @Test
    public void test01() {
        long startTime = System.nanoTime();
        myFixture.copyDirectoryToProject("", "");
        long endTime = System.nanoTime();
        System.out.println(
                String.format("%s - the copy time from directory contained java classes to virtual project",
                        endTime - startTime));

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
            System.out.println(c.getQualifiedName());

            System.out.println(
                    String.format("     SuperTypes[]: %s",
                            String.join(", ", Arrays.stream(c.getSuperTypes())
                                    .map(s -> s.getClassName())
                                    .collect(Collectors.toList()))));

            System.out.println(String.format("     SuperClassType: %s", c.getSuperClassType().getName()));

            System.out.println(String.format("     SuperClass: %s", c.getSuperClass()));

            System.out.println(
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
        System.out.println(
                String.format("%s - the copy time from directory contained java classes to virtual project",
                        endTime - startTime));

        new PsiWebSessionProvider(getProject()).getWebSessions(false);
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
        System.out.println(endTime - startTime);

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
        PsiFile psiFile = myFixture.getPsiManager().findFile(virtualFile);

        long endTime = System.nanoTime();
        System.out.println(endTime - startTime);

        // example
        PsiClass clazz = myFixture.findClass("org.mytests.uiobjects.example.site.pages.ContactFormPage");

        // example
        PsiShortNamesCache.getInstance(getProject()).getAllClassNames();

        // Some tests
        // ...
    }
}
