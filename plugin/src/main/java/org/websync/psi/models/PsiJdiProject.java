package org.websync.psi.models;

import org.websync.connection.dto.ComponentTypeDto;
import org.websync.connection.dto.JdiProjectDto;
import org.websync.connection.dto.PageTypeDto;
import org.websync.connection.dto.WebsiteDto;
import org.websync.models.SeleniumProject;

import java.util.Collection;
import java.util.stream.Collectors;

public class PsiJdiProject extends SeleniumProject {
    public PsiJdiProject() {
        super();
    }

    public PsiJdiProject(String projectName, Collection<PsiWebsite> websites, Collection<PsiComponentType> components,
                         Collection<PsiPageType> pages) {

        this.name = projectName;

        this.websites = websites.stream()
                .collect(Collectors.toMap(PsiWebsite::getId, website -> website));

        this.componentTypes = components.stream()
                .collect(Collectors.toMap(PsiComponentType::getId, component -> component));

        this.pageTypes = pages.stream()
                .collect(Collectors.toMap(PsiPageType::getId, page -> page));
    }

    @Override
    public JdiProjectDto getDto() {
        JdiProjectDto dto = new JdiProjectDto();
        dto.project = name;
        dto.pageTypes = getPageTypes().values().stream()
                .map(PageTypeDto::new).collect(Collectors.toList());
        dto.componentTypes = getComponentTypes().values().stream()
                .map(ComponentTypeDto::new).collect(Collectors.toList());
        dto.webSites = getWebsites().values().stream()
                .map(WebsiteDto::new).collect(Collectors.toList());
        return dto;
    }
}