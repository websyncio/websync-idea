package com.epam.sha.intellij.websync.sessionweb.psimodels;

import com.epam.sha.intellij.websync.sessionweb.models.ComponentType;
import com.epam.sha.intellij.websync.sessionweb.models.PageType;
import com.epam.sha.intellij.websync.sessionweb.models.SessionWeb;
import com.epam.sha.intellij.websync.sessionweb.models.WebsiteType;

import java.util.List;

public class PsiSessionWeb extends SessionWeb {
    public PsiSessionWeb(List<PsiWebiteType> websites, List<PsiComponentType> components, List<PsiPageType> pages) {
        websiteTypes = (List<WebsiteType>) (List<?>) websites;
        pageTypes = (List<PageType>) (List<?>) pages;
        componentTypes = (List<ComponentType>) (List<?>) components;
    }
}
