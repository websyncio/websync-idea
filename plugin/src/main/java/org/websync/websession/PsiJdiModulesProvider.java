package org.websync.websession;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.packaging.impl.artifacts.JarArtifactType;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.AnnotatedElementsSearch;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.websync.jdi.JdiFramework;
import org.websync.logger.LoggerUtils;
import org.websync.websession.models.JdiModule;
import org.websync.websession.psimodels.PsiComponentType;
import org.websync.websession.psimodels.PsiJdiModule;
import org.websync.websession.psimodels.PsiPageType;
import org.websync.websession.psimodels.PsiWebsite;

import java.util.*;
import java.util.stream.Collectors;

import static org.websync.jdi.JdiAttribute.JDI_JSITE;
import static org.websync.jdi.JdiElement.JDI_UI_BASE_ELEMENT;
import static org.websync.jdi.JdiElement.JDI_WEB_PAGE;

public class PsiJdiModulesProvider implements JdiModulesProvider {
    private final List<String> moduleNames = new ArrayList<>();

    @Override
    public List<String> getJdiModuleNames() {
        return new ArrayList<>(moduleNames);
    }

    @Override
    public JdiModule getJdiModule(String name) {
        return createJdiModule(name);
    }

    private JdiModule createJdiModule(String name) {
        final Module module = findByFullName(name);
        Collection<PsiWebsite> websites = getWebsites(module);
        Collection<PsiPageType> pages = getPages(module);
        Collection<PsiComponentType> components = getCustomComponents(module);
//        components.addAll(getFrameworkComponents(module));
        return new PsiJdiModule(name, websites, components, pages);
    }

    @Override
    public Module findByFullName(String fullName) {
        if (!moduleNames.contains(fullName)) {
            throw new IllegalArgumentException("Unmanaged project and module: " + fullName);
        }
        int slash = fullName.indexOf('/');
        if (slash <= 0) {
            throw new IllegalArgumentException("Bad module full name: " + fullName);
        }
        String projectName = fullName.substring(0, slash);
        for (Project project : ProjectManager.getInstance().getOpenProjects()) {
            if (!project.getName().equals(projectName)) {
                continue;
            }
            String moduleName = fullName.substring(slash + 1);
            Module module = ModuleManager.getInstance(project).findModuleByName(moduleName);
            if (module != null) {
                return module;
            }
            throw new IllegalArgumentException("Module not found: " + moduleName);
        }
        throw new IllegalArgumentException("Project not found: " + projectName);
    }

    @Override
    public void addProject(Project project) {
        for (Module module : ModuleManager.getInstance(project).getModules()) {
            if (module.getName().endsWith(".main")) {
                // TODO workaround since IDEA returns 3 modules for a single module folder: module itself, .main and .test
                moduleNames.add(project.getName() + "/" + module.getName());
            }
        }
    }

    @Override
    public void removeProject(Project project) {
        moduleNames.removeIf(moduleName -> moduleName.startsWith(project.getName() + "/"));
    }

    private Collection<PsiWebsite> getWebsites(Module module) {
        long startTime = System.nanoTime();

        PsiClass jdiSiteAnnotation = findPsiClass(module.getProject(), JDI_JSITE.className);

        SearchScope scope = GlobalSearchScope.moduleScope(module);
        Collection<PsiWebsite> websites = AnnotatedElementsSearch.searchPsiClasses(jdiSiteAnnotation, scope).findAll()
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

    private Collection<PsiComponentType> getFrameworkComponents(Module module) {
        long startTime = System.nanoTime();

        ProjectFileIndex projectFileIndex = ProjectRootManager.getInstance(module.getProject()).getFileIndex();


        // get the modules on which it depends
        Module[] dependencies = ModuleRootManager.getInstance(module).getDependencies();

        // get the libraries on which it depends
//        ModuleRootManager.getInstance(module).getModifiableModel().getModuleLibraryTable();

        Collection<PsiClass> psiClasses = new ArrayList<PsiClass>();
        ModuleRootManager.getInstance(module).orderEntries() .forEachModule(m->{
            return true;
        });
        ModuleRootManager.getInstance(module).orderEntries().forEachLibrary(library -> {
            if(library.getName().contains(JdiFramework.ELEMENTS_MODULE.value)){

                GlobalSearchScope scope = GlobalSearchScope.filesScope(
                        module.getProject(),
                        Arrays.asList(library.getFiles(OrderRootType.SOURCES)));
                psiClasses.addAll(getDerivedPsiClasses(module.getProject(), JDI_UI_BASE_ELEMENT.className, scope));
                return false;
            }
            return true;
        });
        Collection<PsiComponentType> components = getComponents(psiClasses,false);

        long endTime = System.nanoTime();
        LoggerUtils.print(String.format("Getting PSI classes for framework components. Time = %.3f s.",
                (double) (endTime - startTime) / 1000000000));
        return components;
    }

    private Collection<PsiComponentType> getCustomComponents(Module module) {
        long startTime = System.nanoTime();
        Collection<PsiClass> psiClasses = getDerivedPsiClasses(
                module.getProject(),
                JDI_UI_BASE_ELEMENT.className,
                GlobalSearchScope.allScope(module.getProject()));
        Collection<PsiComponentType> components = getComponents(psiClasses, true);

        long endTime = System.nanoTime();
        LoggerUtils.print(String.format("Getting PSI classes for custom components. Time = %.3f s.",
                (double) (endTime - startTime) / 1000000000));
        return components;
    }

    private Collection<PsiComponentType> getComponents(Collection<PsiClass> psiClasses, boolean isCustom) {
        return psiClasses.stream().map(c -> {
            PsiComponentType component = new PsiComponentType(c, isCustom);
            component.fill();
            return component;
        }).collect(Collectors.toList());
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
