package com.epam.sha.intellij.websync.utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;

import java.util.List;

public class FileParser {
    public void parse(List<String> lines) {
        if (lines == null || lines.size() == 0) {
            System.out.println("File is empty.");
            return;
        }
        Project currentProject = ProjectManager.getInstance().getOpenProjects()[0];
        switch (lines.get(0)) {
            case "a":
                System.out.println("Command 'a'");
                break;
            case "b":
                System.out.println("Command 'b'");
                break;
            case "print":
                System.out.println("Print");
                break;
            default:
                System.out.println(String.format("Any command '%s'", lines.get(0)));
        }
    }
}
