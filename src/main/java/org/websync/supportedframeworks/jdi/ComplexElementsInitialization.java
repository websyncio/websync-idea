package org.websync.supportedframeworks.jdi;


import com.epam.jdi.light.elements.complex.*;
import com.epam.jdi.light.elements.complex.dropdown.Dropdown;
import com.epam.jdi.light.elements.complex.dropdown.DropdownExpand;
import com.epam.jdi.light.elements.complex.dropdown.DropdownSelect;
import com.epam.jdi.light.elements.complex.table.*;
import com.epam.jdi.light.elements.composite.WebPage;
import com.epam.jdi.light.elements.pageobjects.annotations.FindBy;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.*;
import com.epam.jdi.light.ui.html.elements.complex.DataListOptions;
import com.epam.jdi.light.ui.html.elements.complex.MultiSelector;
import com.epam.jdi.light.ui.html.elements.complex.RadioButtons;
import com.epam.jdi.light.ui.html.elements.complex.Tabs;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ComplexElementsInitialization extends WebPage {
    @Css(".testCss")
    public DataListOptions initializedDataListOptions;
    @XPath("//testXpath")
    public MultiSelector initializedMultiSelector;
    @ByText("test")
    public RadioButtons initializedRadioButtons;
    @Css(".testCss")
    public Tabs initializedTabs;
    @JDropdown("test")
    public Dropdown initializedDropdown;
    @FindBy(xpath = "//test")
    public DropdownExpand initializedDropdownExpand;
    @Css(".testCss")
    public DropdownSelect initializedDropdownSelect;
    @ByText("test")
    public Column initializedColumn;
    @JTable(root = "#test", rowHeader = "test")
    public DataTable initializedDataTable;
    @WithText("test")
    public Line initializedLine;
    @FindBy(xpath = "//test")
    public NameNum initializedNameNum;
    @XPath("//testXpath")
    public Row initializedRow;
    @JTable(root = "#test1")
    public Table initializedTable;
    @WithText("test")
    public TableMatcher initializedTableMatcher;
    @Css(".testCss")
    public Checklist initializedChecklist;
    @XPath("//testXpath")
    public Combobox initializedCombobox;
    @ByText("test")
    public DataList initializedDataList;
    @Css(".testCss")
    public JList initializedJList;
    @JMenu("test")
    public Menu initializedMenu;
    @FindBy(xpath = "//test")
    public Selector initializedSelector;
    @Css(".testCss")
    public SelectWrapper initializedSelectWrapper;
    @XPath("//testXpath")
    public WebList initializedWebList;


    public String getDataListOptionsByCss() {
        return initializedDataListOptions.getValue();
    }

    public String getMultiSelectorValueByXpath() {
        return initializedMultiSelector.getValue();
    }

    public String getRadioButtonsValueByXpath() {
        return initializedRadioButtons.getValue();
    }

    public String getTabsByCss() {
        return initializedTabs.getValue();
    }

    public String getDropdownByJDropdown() {
        return initializedDropdown.getValue();
    }

    public String getDropdownExpandByFindBy() {
        return initializedDropdownExpand.getValue();
    }

    public String getDropdownSelectByCss() {
        return initializedDropdownSelect.getValue();
    }

    public String getColumnByJTable() {
        return initializedColumn.name;
    }

    public String getDataTableByCss() {
        return initializedDataTable.getValue();
    }

    public String getLineByWithText() {
        return initializedLine.getValue();
    }

    public Boolean isNameNumByFindBy() {
        return initializedNameNum.hasName();
    }

    public String getRowByXpath() {
        return initializedRow.name;
    }

    public String getTableByJTable() {
        return initializedTable.getValue();
    }

    public String getTableMatcherByWithText() {
        return initializedTableMatcher.toString();
    }

    public String getChecklistByCss() {
        return initializedChecklist.getValue();
    }

    public String getComboboxByXpath() {
        return initializedCombobox.getValue();
    }

    public String getDataListByByText() {
        return initializedDataList.getValue();
    }

    public String getJListByCss() {
        return initializedJList.getValue();
    }

    public String getMenuByJMenu() {
        return initializedMenu.getValue();
    }

    public String getSelectorByFindBy() {
        return initializedSelector.getValue();
    }

    public List<WebElement> getSelectWrapperByCss() {
        return initializedSelectWrapper.getOptions();
    }

    public String getWebListByXpath() {
        return initializedWebList.getValue();
    }
}
