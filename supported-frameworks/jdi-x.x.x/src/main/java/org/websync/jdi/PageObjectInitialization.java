package org.websync.jdi;

import com.epam.jdi.light.elements.composite.WebPage;
import com.epam.jdi.light.elements.pageobjects.annotations.FindBy;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.ByText;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.Css;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.XPath;
import com.epam.jdi.light.ui.html.elements.common.*;

public class PageObjectInitialization extends WebPage {
    @Css(".testCss")
    public Button initializedButton;
    @XPath("//testXpath")
    protected Image initializedImage;
    @ByText("test")
    Text initializedText;
    @Css(".testCss")
    private TextField initializedTextField;
    @FindBy(xpath = "//test")
    static Range initializedRange;
}
