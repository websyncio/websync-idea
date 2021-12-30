package org.websync.psi;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.AnnotatedElementsSearch;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import org.websync.frameworks.jdi.JdiElementType;
import org.websync.models.SeleniumProject;
import org.websync.models.WebSite;
import org.websync.utils.ModuleStructureUtils;
import org.websync.psi.models.PsiComponentType;
import org.websync.psi.models.PsiJdiProject;
import org.websync.psi.models.PsiPageType;
import org.websync.psi.models.PsiWebsite;
import org.websync.utils.LoggerUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.websync.frameworks.jdi.JdiAttribute.JDI_JSITE;
import static org.websync.frameworks.jdi.JdiElement.JDI_UI_BASE_ELEMENT;
import static org.websync.frameworks.jdi.JdiElement.JDI_WEB_PAGE;

public class PsiJdiProjectsProvider implements SeleniumProjectsProvider {
    private final List<String> projectNames = new ArrayList<>();

    @Override
    public List<String> getModuleNames() {
        return new ArrayList<>(projectNames);
    }

    public SeleniumProject getProject(String projectName) {
        return getJdiProject(projectName, findProject(projectName));
    }

    @Override
    public Collection<WebSite> getWebsites(String projectName) {
        return (Collection) getWebsites(findProject(projectName));
    }

    public SeleniumProject getJdiProject(String projectName, Module module) {
        Collection<PsiWebsite> websites = getWebsites(module);
        Collection<PsiPageType> pages = getPages(module);
        Collection<PsiComponentType> components = getComponents(module);
        return new PsiJdiProject(projectName, websites, components, pages);
    }

    @Override
    public Module findProject(String projectName) {
        String mainModuleName = ModuleStructureUtils.getMainModuleName(projectName);
        for (Project project : ProjectManager.getInstance().getOpenProjects()) {
            if (project.getName().equals(projectName)) {
                Module module = ModuleManager.getInstance(project).findModuleByName(mainModuleName);
                if (module == null) {
                    module = ModuleManager.getInstance(project).findModuleByName(projectName);
                }
                if(module==null){
                    throw new IllegalArgumentException("Module not found in the project. Module: " + mainModuleName + ", Project: " + projectName);
                }
                return module;
            }
        }
        throw new IllegalArgumentException("Project not found: " + projectName);
    }

    @Override
    public void addProject(Project project) {
        projectNames.add(project.getName());
//        for (Module module : ModuleManager.getInstance(project).getModules()) {
//            if (module.getName().endsWith("main")) {
//                // TODO workaround since IDEA returns 3 modules for a single module folder: module itself, .main and .test
//                projectNames.add(project.getName() + "/" + module.getName());
//            }
//        }
    }

    @Override
    public void removeProject(Project project) {
        projectNames.remove(project.getName());
    }

    private Collection<PsiWebsite> getWebsites(Module module) {
        long startTime = System.nanoTime();

        PsiClass jdiSiteAnnotation = findPsiClass(module.getProject(), JDI_JSITE.className);
        if(jdiSiteAnnotation==null){
            throw new RuntimeException("Unable to find class: "+ JDI_JSITE.className);
        }
        Collection<PsiWebsite> websites = AnnotatedElementsSearch.searchPsiClasses(jdiSiteAnnotation, module.getModuleScope()).findAll()
                .stream().map(c -> {
                    PsiWebsite website = new PsiWebsite(c);
                    website.fill();
                    return website;
                }).collect(Collectors.toList());

        long endTime = System.nanoTime();
        LoggerUtils.print(String.format("Getting website PSI classes. Time = %.3f s.",
                (double) (endTime - startTime) / 1000000000));
        return websites;
    }

    private Collection<PsiPageType> getPages(Module module) {
        long startTime = System.nanoTime();
        Collection<PsiClass> psiClasses = getDerivedPsiClasses(module, JDI_WEB_PAGE.className);
        Collection<PsiPageType> pages = psiClasses.stream().map(c -> {
            PsiPageType page = new PsiPageType(c);
            page.fill();
            return page;
        }).collect(Collectors.toList());

        long endTime = System.nanoTime();
        LoggerUtils.print(String.format("Getting page PSI classes. Time = %.3f s.",
                (double) (endTime - startTime) / 1000000000));
        return pages;
    }

//    private Collection<PsiComponentType> getFrameworkComponents(Module module) {
//        long startTime = System.nanoTime();
//
//        ProjectFileIndex projectFileIndex = ProjectRootManager.getInstance(module.getProject()).getFileIndex();
//
//
//        // get the modules on which it depends
//        Module[] dependencies = ModuleRootManager.getInstance(module).getDependencies();
//
//        // get the libraries on which it depends
////        ModuleRootManager.getInstance(module).getModifiableModel().getModuleLibraryTable();
//
//        Collection<PsiClass> psiClasses = new ArrayList<PsiClass>();
//        ModuleRootManager.getInstance(module).orderEntries() .forEachModule(m->{
//            return true;
//        });
//        ModuleRootManager.getInstance(module).orderEntries().forEachLibrary(library -> {
//            if(library.getName().contains(JdiFramework.ELEMENTS_MODULE.value)){
//
//                GlobalSearchScope scope = GlobalSearchScope.filesScope(
//                        module.getProject(),
//                        Arrays.asList(library.getFiles(OrderRootType.SOURCES)));
//                psiClasses.addAll(getDerivedPsiClasses(module.getProject(), JDI_UI_BASE_ELEMENT.className, scope));
//                return false;
//            }
//            return true;
//        });
//        Collection<PsiComponentType> components = getComponents(psiClasses);
//
//        long endTime = System.nanoTime();
//        LoggerUtils.print(String.format("Getting PSI classes for framework components. Time = %.3f s.",
//                (double) (endTime - startTime) / 1000000000));
//        return components;
//    }

    private Collection<PsiComponentType> getComponents(Module module) {
        long startTime = System.nanoTime();
        Collection<PsiClass> psiClasses = getDerivedPsiClasses(
                module.getProject(),
                JDI_UI_BASE_ELEMENT.className,
                GlobalSearchScope.allScope(module.getProject()));
        Collection<PsiComponentType> components = getComponents(psiClasses);

        long endTime = System.nanoTime();
        LoggerUtils.print(String.format("Getting PSI classes for custom components. Time = %.3f s.",
                (double) (endTime - startTime) / 1000000000));
        return components;
    }

    private Collection<PsiComponentType> getComponents(Collection<PsiClass> psiClasses) {
        return psiClasses.stream().map(c -> {
            PsiComponentType component = new PsiComponentType(c);
            component.fill();
            return component;
        }).filter(c -> !c.isFrameworkElement ||
                c.frameworkElementType == JdiElementType.Common ||
                c.frameworkElementType == JdiElementType.Complex
        ).collect(Collectors.toList());
    }

    private Collection<PsiClass> getDerivedPsiClasses(Module module, String classQualifiedName) {
        return getDerivedPsiClasses(module.getProject(), classQualifiedName, module.getModuleScope());
    }

    private Collection<PsiClass> getDerivedPsiClasses(Project project, String classQualifiedName, GlobalSearchScope scope) {
        PsiClass psiClass = findPsiClass(project, classQualifiedName);
        if (psiClass == null) {
            return Collections.emptyList();
        }
        return ClassInheritorsSearch.search(psiClass, scope, true).findAll();
    }

    private PsiClass findPsiClass(Project project, String fullName) {
        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        return javaPsiFacade.findClass(fullName, scope);
    }
}
