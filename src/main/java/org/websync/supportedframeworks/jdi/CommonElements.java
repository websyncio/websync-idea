package org.websync.supportedframeworks.jdi;


import com.epam.jdi.light.common.TextTypes;
import com.epam.jdi.light.elements.common.Label;
import com.epam.jdi.light.elements.pageobjects.annotations.*;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.*;

import static com.epam.jdi.light.common.ElementArea.CENTER;
import static com.epam.jdi.light.common.SetTextTypes.SET_TEXT;

public class CommonElements {

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
    @ClickArea(CENTER)
    public Label initializedWithClickArea;
    @FindBy(id = "test")
    public Label initializedWithFindBy;
    @FindBys({@FindBy(id = "test"), @FindBy(id = "test1")})
    public Label initializedWithFindBys;
    @Frame("test")
    public Label initializedWithFrame;
    @GetAny
    public Label initializedWithGetAny;
    @GetShowInView
    public Label initializedWithGetShowInView;
    @GetTextAs(TextTypes.LABEL)
    public Label initializedWithGetTextAs;
    @GetVisible
    public Label initializedWithGetVisible;
    @GetVisibleEnabled
    public Label initializedWithGetVisibleEnabled;
    @Mandatory
    public Label initializedWithMandatory;
    @Name("test")
    public Label initializedWithName;
    @NoCache
    public Label initializedWithNoCache;
    @NoWait
    public Label initializedWithNoWait;
    @PageName("test")
    public Label initializedWithPageName;
    @Root
    public Label initializedWithRoot;
    @SetTextAs(SET_TEXT)
    public Label initializedWithSetTextAs;
    @Title("test")
    public Label initializedWithTitle;
    @Url("test")
    public Label initializedWithUrl;
    @VisualCheck
    public Label initializedWithVisualCheck;
    @WaitTimeout(2)
    public Label initializedWithWaitTimeout;


    @XPath("//testXpath")
    @Css(".testCss")
    public Label overlappedCssAndXpath;
    @Css("//testCss")
    public Label wrongCssFormat;
    @XPath(".testXpath")
    public Label wrongXpathFormat;
}
