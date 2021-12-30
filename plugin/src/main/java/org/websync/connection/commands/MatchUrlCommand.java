package org.websync.connection.commands;

import com.intellij.util.Url;
import org.websync.WebSyncException;
import org.websync.connection.messages.browser.MatchUrlMessage;
import org.websync.connection.messages.idea.UrlMatchResult;
import org.websync.models.PageInstance;
import org.websync.models.WebSite;
import org.websync.psi.SeleniumProjectsProvider;
import org.websync.psi.models.PsiPageInstance;
import org.websync.psi.models.PsiWebsite;
import org.websync.utils.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class MatchUrlCommand extends CommandWithDataBase<MatchUrlMessage>{
    public MatchUrlCommand(SeleniumProjectsProvider projectsProvider) {
        super(projectsProvider);
    }

    @Override
    public Object execute(String commandDataString) throws WebSyncException {
        return execute(parseCommandData(commandDataString, MatchUrlMessage.class));
    }

    @Override
    public Object execute(MatchUrlMessage commandData) throws WebSyncException {
        Collection<WebSite> websites = projectsProvider.getWebsites(commandData.projectName);
        String url = commandData.url;
        PsiWebsite matchingWebsite = matchWebsite(websites, url);
        if (matchingWebsite == null) {
            return new UrlMatchResult(null, new ArrayList<>());
        }
        String absolutePath = commandData.url.substring(matchingWebsite.getUrl().length());
        List<PageInstance> matchingPages = matchPages(matchingWebsite.getPageInstances(), absolutePath);

        return new UrlMatchResult(matchingWebsite.getId(), matchingPages.stream().map(p -> p.getId()).collect(Collectors.toList()));
    }

    private List<PageInstance> matchPages(List<PageInstance> pageInstances, String absolutePath) {
        return pageInstances.stream().filter(p -> pageMatchUrl(p, absolutePath)).collect(Collectors.toList());
    }

    private boolean pageMatchUrl(PageInstance page, String absolutePath) {
        // Probably should rename PsiPageInstance to JdiPageInstance
        String urlPattern = ((PsiPageInstance) page).getUrlPattern();
        if (urlPattern == null || urlPattern.trim().isEmpty()) {
            final String LEADING_SLASH = "/";
            absolutePath = StringUtils.cutFirst(absolutePath,LEADING_SLASH);
            String pageUrl = StringUtils.cutFirst(page.getUrl(),LEADING_SLASH);
            return absolutePath.equals(pageUrl);
        } else {
            // Probably we should try to match twice - one time with leading slash and another time - without
            return absolutePath.matches(urlPattern);
        }
    }

    private PsiWebsite matchWebsite(Collection<WebSite> websites, String urlString) {
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            return null;
        }
        return (PsiWebsite) websites.stream().filter(ws -> {
            URL websiteUrl;
            try {
                websiteUrl = new URL(ws.getUrl());
            } catch (MalformedURLException e) {
                return false;
            }
            return url.getAuthority().equals(websiteUrl.getAuthority())
                    && url.getPath().startsWith(websiteUrl.getPath());
        }).collect(Collectors.toList()).stream().findFirst().orElse(null);
    }
}
