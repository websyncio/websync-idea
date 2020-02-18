package com.epam.websync.sessionweb.psimodels;

import com.epam.websync.sessionweb.models.SessionWeb;

import java.util.Collection;
import java.util.stream.Collectors;

public class PsiSessionWeb extends SessionWeb {
    public PsiSessionWeb() {
        super();
    }

    public PsiSessionWeb(Collection<PsiWebsite> websites, Collection<PsiComponentType> components,
                         Collection<PsiPageType> pages) {

        this.websites = websites.stream()
                .collect(Collectors.toMap(PsiWebsite::getId, website -> website));

        this.componentTypes = components.stream()
                .collect(Collectors.toMap(PsiComponentType::getId, component -> component));

        this.pageTypes = pages.stream()
                .collect(Collectors.toMap(PsiPageType::getId, page -> page));
    }
}
