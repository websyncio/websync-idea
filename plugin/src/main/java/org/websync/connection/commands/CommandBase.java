package org.websync.connection.commands;

import org.websync.psi.JdiProjectsProvider;

public abstract class CommandBase implements Command  {
    protected JdiProjectsProvider projectsProvider;

    public CommandBase(JdiProjectsProvider projectsProvider){
        this.projectsProvider = projectsProvider;
    }
}
