package com.epam.sha.intellij.websync.sessionweb.psimodels;

import com.epam.sha.intellij.websync.sessionweb.models.SessionWeb;

import java.util.List;
import java.util.stream.Collectors;

public class PsiSessionWeb extends SessionWeb {
    public PsiSessionWeb(List<PsiWebiteType> websites, List<PsiComponentType> components, List<PsiPageType> pages) {
        websiteTypes = websites.stream()
                .collect(Collectors.toMap(PsiWebiteType::getId, website -> website));

        componentTypes = components.stream()
                .collect(Collectors.toMap(PsiComponentType::getId, component -> component));

        pageTypes = pages.stream()
                .collect(Collectors.toMap(PsiPageType::getId, page -> page));
    }
}
