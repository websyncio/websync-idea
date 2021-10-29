package org.websync.jdi.pages.Inheritance;

import com.epam.jdi.light.elements.pageobjects.annotations.FindBy;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.*;
import com.epam.jdi.light.ui.html.elements.common.*;
import org.websync.jdi.components.CustomComponentBase;
import org.websync.jdi.components.CustomElement;

public class InheritedPage2 extends InheritedPage {
    @Css(".testCss")
    public Text initializedButton;
    @XPath("//testXpath")
    public Checkbox initializedCheckbox;
    @ByText("test")
    public Image initializedImage;
    @UI("test")
    public Link initializedLink;
    @WithText("test")
    public TextField initializedTextField;
    @FindBy("test")
    public Image initializedIcon;
    @UI("#custom-element")
    public CustomElement initializedCustomElement;
    @UI("#custom-base-element")
    public CustomComponentBase initializedCustomBaseElement;

}
