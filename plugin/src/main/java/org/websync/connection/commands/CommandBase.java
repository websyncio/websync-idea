package org.websync.connection.commands;

import org.websync.psi.SeleniumProjectsProvider;

public abstract class CommandBase implements Command  {
    protected SeleniumProjectsProvider projectsProvider;

    public CommandBase(SeleniumProjectsProvider projectsProvider){
        this.projectsProvider = projectsProvider;
    }
}
