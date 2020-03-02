package org.websync.jdi;

import com.epam.jdi.light.elements.composite.WebPage;
import com.epam.jdi.light.elements.pageobjects.annotations.FindBy;
import com.epam.jdi.light.elements.pageobjects.annotations.Title;
import com.epam.jdi.light.elements.pageobjects.annotations.Url;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.ByText;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.Css;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.XPath;
import com.epam.jdi.light.ui.html.elements.common.*;

@Url("/index.html") @Title("Test Page")
public class PageObjectInitialization2 extends WebPage {

    @Css(".testCss")
    public Link initializedLink;
    @XPath("//testXpath")
    protected DateTimeSelector initializedDateTimeSelector;
    @ByText("test")
    TextArea initializedTextArea;
    @Css(".testCss")
    private FileInput initializedFileInput;
    @FindBy(xpath = "//test")
    static NumberSelector initializedNumberSelector;
}
