package org.websync.jdi;

import com.epam.jdi.light.elements.pageobjects.annotations.JSite;
import com.epam.jdi.light.elements.pageobjects.annotations.Url;
import org.websync.jdi.components.*;

@JSite("https://test-page.com/test/")
public class JdiWebSite {
    @Url("base-test-page.html")
    public BasePageObject publicPageObject;

    @Url("inherited-page.html")
    protected InheritedPageObject protectedPageObject;

    @Url("another-inherited-page.html")
    private InheritedPageObject2 packagePrivatePageObject;

    @Url("page-with-complex-elements.html")
    private ComplexElementsInitialization privatePageObject;

    private @Url("another-inherited-page.html")
    static PageObjectWithDifferentModificators staticPageObject;

    @Url("app/#project{0}")
    public StackoverflowQuestionPage StackOverflowPage;
}
