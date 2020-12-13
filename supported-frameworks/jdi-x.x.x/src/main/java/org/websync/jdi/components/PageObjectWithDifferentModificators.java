package org.websync.jdi.components;

import com.epam.jdi.light.elements.composite.WebPage;
import com.epam.jdi.light.elements.pageobjects.annotations.FindBy;
import com.epam.jdi.light.elements.pageobjects.annotations.Title;
import com.epam.jdi.light.elements.pageobjects.annotations.Url;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.ByText;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.Css;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.XPath;
import com.epam.jdi.light.ui.html.elements.common.*;

@Url("/index.html")
@Title("Test Page")
public class PageObjectWithDifferentModificators extends WebPage {

    @Css(".testCss")
    public Link publicLink;
    @XPath("//testXpath")
    protected DateTimeSelector protectedDateTimeSelector;
    private @ByText("test")
    TextArea packagePrivateTextArea;
    @Css(".testCss")
    private FileInput privateFileInput;
    @FindBy(xpath = "//test")
    private NumberSelector staticNumberSelector;
}
