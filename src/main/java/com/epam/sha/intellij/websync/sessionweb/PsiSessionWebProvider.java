package com.epam.sha.intellij.websync.sessionweb;

import com.epam.sha.intellij.websync.sessionweb.models.SessionWeb;
import com.epam.sha.intellij.websync.sessionweb.psimodels.PsiComponentType;
import com.epam.sha.intellij.websync.sessionweb.psimodels.PsiPageType;
import com.epam.sha.intellij.websync.sessionweb.psimodels.PsiSessionWeb;
import com.epam.sha.intellij.websync.sessionweb.psimodels.PsiWebiteType;
import com.intellij.openapi.project.Project;

import java.util.Arrays;
import java.util.List;

public class PsiSessionWebProvider implements SessionWebPovider {

    private Project project;
    private List<SessionWeb> cachedSessionWebs;

    public PsiSessionWebProvider(Project project) {
        this.project = project;
    }

    @Override
    public List<SessionWeb> GetSessionWeb(boolean useCache) {
        if (cachedSessionWebs == null || !useCache) {
            try {
                List<PsiWebiteType> websites = GetWebsites(project);
                List<PsiPageType> pages = GetPages(project);
                List<PsiComponentType> components = GetComponents(project);
                
                PsiSessionWeb sessionWeb = new PsiSessionWeb(websites, components, pages);
                List<SessionWeb> sessionWebs = Arrays.asList(sessionWeb);
                CacheSesionWebs(sessionWebs);
                return sessionWebs;
            } catch (Exception ex) {
                throw ex;
            }
        }
        return cachedSessionWebs;
    }

    private List<PsiWebiteType> GetWebsites(Project project) {
        return null;
    }

    private List<PsiPageType> GetPages(Project project) {
        return null;
    }

    private List<PsiComponentType> GetComponents(Project project) {
        return null;
    }

    private void CacheSesionWebs(List<SessionWeb> sessionWebs) {
        cachedSessionWebs = sessionWebs;
    }
}
