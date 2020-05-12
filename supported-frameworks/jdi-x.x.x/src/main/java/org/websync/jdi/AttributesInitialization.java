package org.websync.jdi;

import com.epam.jdi.light.elements.common.Label;
import com.epam.jdi.light.elements.composite.WebPage;
import com.epam.jdi.light.elements.pageobjects.annotations.*;

public class AttributesInitialization extends WebPage {

    @Css(".testCss")
    public Label initializedWithCss;
    @XPath("//testXpath")
    public Label initializedWithXpath;
    @ByText("test")
    public Label initializedWithByText;
    @UI("test")
    public Label initializedWithUi;
    @WithText("test")
    public Label initializedWithWithText;
    @FindBy(id = "test")
    public Label initializedWithFindBy;
    @FindBys({@FindBy(id = "test"), @FindBy(id = "test1")})
    public Label initializedWithFindBys;
    @Frame("test")
    public Label initializedWithFrame;
    @Name("test")
    public Label initializedWithName;
    @Title("test")
    public Label initializedWithTitle;


    @XPath("//testXpath")
    @Css(".testCss")
    public Label overlappedCssAndXpath;
    @Css("//testCss")
    public Label wrongCssFormat;
    @XPath(".testXpath")
    public Label wrongXpathFormat;
}
