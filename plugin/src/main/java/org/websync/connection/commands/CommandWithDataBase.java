package org.websync.connection.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import org.websync.WebSyncException;
import org.websync.psi.JdiProjectsProvider;
import org.websync.utils.PsiUtils;

import java.util.Arrays;
import java.util.List;

public abstract class CommandWithDataBase<T> extends CommandBase{
    public CommandWithDataBase(JdiProjectsProvider projectsProvider) {
        super(projectsProvider);
    }

    public abstract Object execute(T commandData) throws WebSyncException;

    protected T parseCommandData(String commandDataJson, Class<T> commandClass) {
        if(commandDataJson==null){
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        try {
            return mapper.readValue(commandDataJson, commandClass);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid command data received: " + commandDataJson, e);
        }
    }

    protected static PsiField findPsiField(Module module, String containerClassName, int fieldIndex) throws WebSyncException {
        PsiClass containerPsiClass = PsiUtils.findClass(module, containerClassName);
        if (containerPsiClass == null) {
            throw new WebSyncException("Field container class was not found: " + containerClassName);
        }

        List<PsiField> fieldsList = Arrays.asList(containerPsiClass.getFields());
//        PsiField psiField = componentPsiClass.findFieldByName(fieldName, false);
        if (fieldIndex >= fieldsList.size()) {
            throw new WebSyncException("Field with index " + fieldIndex + " not found in component: " + containerClassName);
        }
        return fieldsList.get(fieldIndex);
    }

    protected String getNameFromId(String componentType) {
        return componentType.substring(componentType.lastIndexOf('.') + 1);
    }
}
