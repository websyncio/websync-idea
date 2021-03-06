package org.websync.jdi.pages.Inheritance;


import com.epam.jdi.light.elements.complex.dropdown.DropdownSelect;
import com.epam.jdi.light.elements.complex.table.Line;
import com.epam.jdi.light.elements.complex.table.NameNum;
import com.epam.jdi.light.elements.composite.WebPage;
import com.epam.jdi.light.elements.pageobjects.annotations.FindBy;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.ByText;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.Css;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.WithText;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.XPath;
import com.epam.jdi.light.ui.html.elements.complex.DataListOptions;
import com.epam.jdi.light.ui.html.elements.complex.MultiSelector;
import com.epam.jdi.light.ui.html.elements.complex.RadioButtons;
import com.epam.jdi.light.ui.html.elements.complex.Tabs;

public abstract class PageBase extends WebPage {
    @Css(".testCss")
    public DataListOptions initializedDataListOpt;
    @XPath("//testXpath")
    public MultiSelector initialedMultiSelector;
    @ByText("test")
    public RadioButtons initializedRadioButtons;
    @Css(".testCss")
    public Tabs initializedTabs;
    @Css(".testCss")
    public DropdownSelect initializedDropdownSelect;
    @WithText("test")
    public Line initializedLine;
    @FindBy(xpath = "//test")
    public NameNum initializedNameNum;
}
