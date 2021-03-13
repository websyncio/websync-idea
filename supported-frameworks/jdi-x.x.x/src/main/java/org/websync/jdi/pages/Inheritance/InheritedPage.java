package org.websync.jdi.pages.Inheritance;


import com.epam.jdi.light.elements.complex.*;
import com.epam.jdi.light.elements.pageobjects.annotations.FindBy;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.ByText;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.Css;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.XPath;

public class InheritedPage extends PageBase {
    @Css(".testCss")
    public Checklist initializedChecklist;
    @XPath("//testXpath")
    public Combobox initializedCombobox;
    @ByText("test")
    public DataList initializedDataList1;
    @Css(".testCss")
    public JList initializedJList;
    @FindBy(xpath = "//test")
    public Selector initializedSelector;
    @Css(".testCss")
    public SelectWrapper initializedSelectWrapper;
    @XPath("//testXpath")
    public WebList initializedWebList;
}
