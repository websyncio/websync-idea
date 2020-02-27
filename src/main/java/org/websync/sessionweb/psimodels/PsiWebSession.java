package org.websync.sessionweb.psimodels;

import org.websync.sessionweb.models.WebSession;

import java.util.Collection;
import java.util.stream.Collectors;

public class PsiWebSession extends WebSession {
    public PsiWebSession() {
        super();
    }

    public PsiWebSession(Collection<PsiWebsite> websites, Collection<PsiComponent> components,
                         Collection<PsiPage> pages) {

        this.websites = websites.stream()
                .collect(Collectors.toMap(PsiWebsite::getId, website -> website));

        this.components = components.stream()
                .collect(Collectors.toMap(PsiComponent::getId, component -> component));

        this.pages = pages.stream()
                .collect(Collectors.toMap(PsiPage::getId, page -> page));
    }
}
