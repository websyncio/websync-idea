package org.websync.jdi;

import com.epam.jdi.light.elements.pageobjects.annotations.JSite;
import com.epam.jdi.light.elements.pageobjects.annotations.Url;
import org.websync.jdi.pages.*;
import org.websync.jdi.pages.Inheritance.InheritedPage;
import org.websync.jdi.pages.Inheritance.InheritedPage2;
import org.websync.jdi.pages.Inheritance.PageBase;
import org.websync.jdi.pages.InitializationAttributes.ComplexInitializationAttributesPage;
import org.websync.jdi.pages.InitializationAttributes.FieldsWithDifferentModificatorsPage;

@JSite("https://stackoverflow.com")
public class JdiWebSite {
    @Url("/base-test-page.html")
    public PageBase publicPageObject;

    @Url("/inherited-page.html")
    protected InheritedPage protectedPageObject;

    @Url("/another-inherited-page.html")
    private InheritedPage2 packagePrivatePageObject;

    @Url("/page-with-complex-elements.html")
    private ComplexInitializationAttributesPage privatePageObject;

    private @Url("/base-test-page.htm")
    static FieldsWithDifferentModificatorsPage staticPageObject;

    @Url("/questions/66936084/nodejs-pagination-recursive-promise-problem")
    public StackoverflowQuestionPage StackOverflowPage;
}
