package org.websync.supportedframeworks.jdi;


import com.epam.jdi.light.elements.common.Label;
import com.epam.jdi.light.elements.composite.WebPage;
import com.epam.jdi.light.elements.pageobjects.annotations.*;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.*;

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
