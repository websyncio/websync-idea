package org.websync.jdi;

import com.epam.jdi.light.elements.pageobjects.annotations.JSite;
import com.epam.jdi.light.elements.pageobjects.annotations.Url;

@JSite("https://test-page.com/test/")
public class InitializedSetOfClasses {
    @Url("base-test-page.html")
    public BasePageObject publicPageObject;

    @Url("inherited-page.html")
    protected InheritedPageObject protectedPageObject;

    @Url("another-inherited-page.html")
    InheritedPageObject2 packagePrivatePageObject;

    @Url("page-with-complex-elements.html")
    private ComplexElementsInitialization privatePageObject;

    @Url("another-inherited-page.html")
    static PageObjectWithDifferentModificators staticPageObject;
}
