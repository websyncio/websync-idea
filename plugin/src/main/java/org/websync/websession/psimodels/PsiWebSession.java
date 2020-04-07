package org.websync.websession.psimodels;

import org.websync.websession.models.WebSession;

import java.util.Collection;
import java.util.stream.Collectors;

public class PsiWebSession extends WebSession {
    public PsiWebSession() {
        super();
    }

    public PsiWebSession(Collection<PsiWebsite> websites, Collection<PsiComponentType> components,
                         Collection<PsiPageType> pages) {

        this.websites = websites.stream()
                .collect(Collectors.toMap(PsiWebsite::getId, website -> website));

        this.componentTypes = components.stream()
                .collect(Collectors.toMap(PsiComponentType::getId, component -> component));

        this.pageTypes = pages.stream()
                .collect(Collectors.toMap(PsiPageType::getId, page -> page));
    }

    public void updateComponentInstanceInType(String id, String oldName, String newName) {
        componentTypes.get(id).updateComponentInstance(oldName, newName);
    }

    public void updateComponentInstanceInPage(String id, String oldName, String newName) {
        pageTypes.get(id).updateComponentInstance(oldName, newName);
    }

}