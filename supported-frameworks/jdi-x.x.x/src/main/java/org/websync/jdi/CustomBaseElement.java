package org.websync.jdi;

import com.epam.jdi.light.elements.base.UIBaseElement;
import com.epam.jdi.light.elements.common.UIElement;
import com.epam.jdi.light.elements.pageobjects.annotations.FindBy;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.Css;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.XPath;
import org.openqa.selenium.By;

public class CustomBaseElement extends UIBaseElement {
    @FindBy(xpath = "//test")
    public UIElement customUIElement;
    @XPath("//test")
    public UIElement customXpathElement;
    @Css("test")
    public UIElement customCssElement;
}
