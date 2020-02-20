package org.websync.supportedframeworks.jdi;


import com.epam.jdi.light.elements.common.Label;
import com.epam.jdi.light.elements.pageobjects.annotations.*;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.*;

public class AttributesInitialization {

    @Css(".testCss")
    private Label initializedWithCss;
    @XPath("//testXpath")
    private Label initializedWithXpath;
    @ByText("test")
    private Label initializedWithByText;
    @UI("test")
    private Label initializedWithUi;
    @WithText("test")
    private Label initializedWithWithText;
    @FindBy(id = "test")
    private Label initializedWithFindBy;
    @FindBys({@FindBy(id = "test"), @FindBy(id = "test1")})
    private Label initializedWithFindBys;
    @Frame("test")
    private Label initializedWithFrame;
    @Name("test")
    private Label initializedWithName;
    @Title("test")
    private Label initializedWithTitle;


    @XPath("//testXpath")
    @Css(".testCss")
    private Label overlappedCssAndXpath;
    @Css("//testCss")
    private Label wrongCssFormat;
    @XPath(".testXpath")
    private Label wrongXpathFormat;

    public String getLabelValueByCss() {
        return initializedWithCss.getValue();
    }

    public String getLabelValueByXpath() {
        return initializedWithXpath.getValue();
    }

    public String getLabelValueByText() {
        return initializedWithByText.getValue();
    }

    public String getLabelValueByUi() {
        return initializedWithUi.getValue();
    }

    public String getLabelValueByWithText() {
        return initializedWithWithText.getValue();
    }

    public String getLabelValueByFindBy() {
        return initializedWithFindBy.getValue();
    }

    public String getLabelValueByFindBys() {
        return initializedWithFindBys.getValue();
    }

    public String getLabelValueByFrame() {
        return initializedWithFrame.getValue();
    }

    public String getLabelValueByName() {
        return initializedWithName.getValue();
    }

    public String getLabelValueByTitle() {
        return initializedWithTitle.getValue();
    }


    public String getLabelValueByOverlappedCssAndXpath() {
        return overlappedCssAndXpath.getValue();
    }

    public String getLabelValueByWrongCssFormat() {
        return wrongCssFormat.getValue();
    }

    public String getLabelValueByWrongXpathFormat() {
        return wrongXpathFormat.getValue();
    }
}
