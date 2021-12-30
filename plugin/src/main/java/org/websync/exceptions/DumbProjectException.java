package org.websync.exceptions;

import com.intellij.openapi.project.Project;

public class DumbProjectException extends RuntimeException{
    public Project project;

    public DumbProjectException(Project project) {
        this.project = project;
    }
}
