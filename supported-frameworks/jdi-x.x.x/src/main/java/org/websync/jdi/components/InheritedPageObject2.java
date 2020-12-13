package org.websync.jdi.components;

import com.epam.jdi.light.elements.pageobjects.annotations.FindBy;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.*;
import com.epam.jdi.light.ui.html.elements.common.*;

public class InheritedPageObject2 extends InheritedPageObject {

    @Css(".testCss")
    public Button initializedButton;
    @XPath("//testXpath")
    public Checkbox initializedCheckbox;
    @ByText("test")
    public Image initializedImage;
    @UI("test")
    public Link initializedLink;
    @WithText("test")
    public TextField initializedTextField;
    @FindBy(id = "test")
    public Icon initializedIcon;
    @UI("#custom-element")
    public CustomElement initializedCustomElement;
    @UI("#custom-base-element")
    public CustomBaseElement initializedCustomBaseElement;

}
