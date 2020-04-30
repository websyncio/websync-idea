package org.websync.websession.psimodels;

import org.websync.websession.models.JdiModule;

import java.util.Collection;
import java.util.stream.Collectors;

public class PsiJdiModule extends JdiModule {
    public PsiJdiModule() {
        super();
    }

    public PsiJdiModule(String name, Collection<PsiWebsite> websites, Collection<PsiComponentType> components,
                        Collection<PsiPageType> pages) {

        this.name = name;

        this.websites = websites.stream()
                .collect(Collectors.toMap(PsiWebsite::getId, website -> website));

        this.componentTypes = components.stream()
                .collect(Collectors.toMap(PsiComponentType::getId, component -> component));

        this.pageTypes = pages.stream()
                .collect(Collectors.toMap(PsiPageType::getId, page -> page));
    }
}