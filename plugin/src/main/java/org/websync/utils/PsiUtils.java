package org.websync.utils;

import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.impl.source.PsiFieldImpl;
import com.intellij.psi.impl.source.tree.ChildRole;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.InheritanceUtil;
import com.intellij.psi.util.PsiUtil;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;
import org.websync.WebSyncException;
import org.websync.frameworks.jdi.JdiElement;

import java.util.Arrays;

import static org.websync.frameworks.jdi.JdiAttribute.JDI_JSITE;

@UtilityClass
public class PsiUtils {
    @Nullable
    public static PsiClass findPsiClass(@Nullable PsiFile psiFile) {
        if(!(psiFile  instanceof PsiJavaFile)) {
            return null;
        }
        return Arrays.stream(((PsiJavaFile)psiFile).getClasses())
                .filter(c -> c.getModifierList() != null && c.getModifierList().hasModifierProperty(PsiModifier.PUBLIC))
                .findFirst().orElse(null);
    }

    public static boolean isWebsite(@Nullable PsiClass psiClass) {
        return psiClass != null && psiClass.getAnnotation(JDI_JSITE.className) != null;
    }

    public static boolean isComponent(@Nullable PsiType psiType) {
        return isComponent(PsiUtil.resolveClassInType(psiType));
    }

    public static boolean isComponent(@Nullable PsiClass psiClass) {
        return isInheritor(psiClass, JdiElement.JDI_UI_BASE_ELEMENT);
    }

    public static boolean isPage(@Nullable PsiType psiType) {
        return isPage(PsiUtil.resolveClassInType(psiType));
    }

    public static boolean isPage(@Nullable PsiClass psiClass) {
        return isInheritor(psiClass, JdiElement.JDI_WEB_PAGE);
    }

    private static boolean isInheritor(@Nullable PsiClass psiClass, JdiElement jdiElement) {
        return psiClass != null && Arrays.stream(psiClass.getSuperTypes())
                .anyMatch(s -> InheritanceUtil.isInheritor(s, jdiElement.className));
    }

    public static PsiFile createJavaPsiFile(Project project, String fileName, String fileContent) {
        PsiFileFactory fileFactory = PsiFileFactory.getInstance(project);
        return fileFactory.createFileFromText(fileName, JavaLanguage.INSTANCE, fileContent);
    }

    public static PsiClass findClass(Module module, String className) {
        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(module.getProject());
        return javaPsiFacade.findClass(className, GlobalSearchScope.moduleScope(module));
    }

    public static void createJavaFileIn(Module module, String fileName, String fileContent, PsiDirectory parentPsiDirectory) throws WebSyncException {
        WriteAction.runAndWait(() -> {
            PsiFile[] files = parentPsiDirectory.getFiles();
            boolean duplicatesExist = Arrays.stream(files).anyMatch(f -> f.getName().equals(fileName));
            if (duplicatesExist) {
                throw new WebSyncException("File '" + fileName + "' already exists in '" + parentPsiDirectory.getName() + "'");
            }
            WriteCommandAction.runWriteCommandAction(module.getProject(),
                    "Create java file " + fileName,
                    "WebSyncAction",
                    () -> {
                        PsiFile typePsiFile = PsiUtils.createJavaPsiFile(module.getProject(), fileName, fileContent);
                        parentPsiDirectory.add(typePsiFile);
                    });
        });
    }

    public static PsiDirectory getClassDirectory(Module module, String parentType) throws WebSyncException {
        PsiClass parentPsiClass = findClass(module, parentType);
        if (parentPsiClass == null) {
            throw new WebSyncException("Parent type was not found: " + parentType);
        }
        return parentPsiClass.getContainingFile().getContainingDirectory();
    }

    public static PsiPackage getRootPackage(Module module) {
        return JavaPsiFacade.getInstance(module.getProject()).findPackage("");
//        Collection<VirtualFile> virtualFiles = FileTypeIndex.getFiles(JavaFileType.INSTANCE, GlobalSearchScope.moduleScope(module));
//        String rootPackage = null;
//        for (VirtualFile vf : virtualFiles) {
//            PsiFile psifile = PsiManager.getInstance(module.getProject()).findFile(vf);
//
//            if (psifile instanceof PsiJavaFile) {
//                PsiJavaFile psiJavaFile = (PsiJavaFile) psifile;
//                String packageName = psiJavaFile.getPackageName();
//                if (rootPackage==null||packageName.length() < rootPackage.length()) {
//                    rootPackage = packageName;
//                }
//            }
//        }
//        if (rootPackage.isEmpty()) {
//            throw new WebSyncException("Unable to define root package.");
//        }
//        return JavaPsiFacade.getInstance(module.getProject()).findPackage(rootPackage);
    }

    public static PsiDirectory getRootDirectory(Module module) throws WebSyncException {
        PsiPackage rootPackage = getRootPackage(module);
        PsiDirectory[] directories = rootPackage.getDirectories();
        if (directories.length == 0) {
            throw new WebSyncException("Unable to define root directory.");
        }
        return directories[0];
    }

    public static void addFieldToClass(Module module, String typeName, String fieldName, String attributeName, Object attributeValue, String containerClassName) {
        WriteCommandAction.runWriteCommandAction(module.getProject(),
                containerClassName + ": add field " + fieldName + "'",
                "WebSyncAction",
                () -> {
                    PsiClass containerClass = PsiUtils.findClass(module, containerClassName);

                    PsiElement anchorElement;
                    PsiField[] fields = containerClass.getFields();
                    boolean insertNewLineBeforeField = false;
                    if (fields.length > 0) {
                        anchorElement = fields[fields.length - 1];
                        insertNewLineBeforeField = true;
                    } else {
                        anchorElement = containerClass.getLBrace();
                    }

                    String newFieldText = "public " + typeName + " " + fieldName + ";";
                    PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(module.getProject());
                    PsiFieldImpl newField = (PsiFieldImpl) elementFactory.createFieldFromText(newFieldText, null);

                    String annotationText = "@" + attributeName + "(\"" + attributeValue + "\")";
                    PsiAnnotation newAnnotation = elementFactory.createAnnotationFromText(annotationText, newField);

                    // .add annotation to field
                    PsiModifierList modifierListElement = (PsiModifierList) newField.getNode().findChildByRoleAsPsiElement(ChildRole.MODIFIER_LIST);
                    modifierListElement.addBefore(newAnnotation, modifierListElement.getFirstChild());

                    // .add field after the last field in the class
                    PsiElement parent = anchorElement.getParent();
                    parent.addAfter(newField, anchorElement);
                    if (insertNewLineBeforeField) {
                        parent.addAfter(createNewLine(module.getProject()), anchorElement);
                    }

                    JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(module.getProject());
                    styleManager.shortenClassReferences(containerClass.getContainingFile());
                    //JavaCodeStyleManager.getInstance(module.getProject()).shortenClassReferences(newField);
//                        CodeStyleManager.getInstance(module.getProject()).reformat(containerClass);
                });
    }

    protected PsiElement createNewLine(Project project) {
        return PsiParserFacade.SERVICE.getInstance(project).createWhiteSpaceFromText("\n\n");
    }

    public static void addImportStatement(Module module, String componentTypeId, String containerClassName) {
        WriteCommandAction.runWriteCommandAction(module.getProject(),
                containerClassName + ": add import statement",
                "WebSyncAction",
                () -> {
                    PsiClass containerClass = PsiUtils.findClass(module, containerClassName);
                    PsiClass importedClass = PsiUtils.findClass(module, componentTypeId);
                    PsiJavaFile file = (PsiJavaFile) containerClass.getContainingFile();
                    file.importClass(importedClass);
                });
    }
}
