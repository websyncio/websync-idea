package org.websync.jdi.components;

import com.epam.jdi.light.elements.common.Label;
import com.epam.jdi.light.elements.common.UIElement;
import com.epam.jdi.light.elements.pageobjects.annotations.FindBy;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.Css;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.XPath;

public class CustomElement extends Label {
    @FindBy(xpath = "//test1")
    public UIElement initializedCustomUIElement;
    @XPath("//test2")
    public UIElement initializedCustomXpathElement;
    @Css("test3")
    public UIElement initializedCustomCssElement;
}
