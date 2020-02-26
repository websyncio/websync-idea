package org.websync.supportedframeworks.jdi;

import com.epam.jdi.light.elements.complex.table.BaseTable;
import com.epam.jdi.light.elements.pageobjects.annotations.FindBy;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.Css;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.XPath;
import org.openqa.selenium.By;

public class CustomBaseTable extends BaseTable {
    @FindBy(xpath = "//tr[%s]/td")
    public By customRowLocator;
    @XPath("//tr/td[%s]")
    public By customColumnLocator;
    @FindBy(xpath = "//tr[{1}]/td[{0}]")
    public By customCellLocator;
    @Css("td")
    public By allCellsLocator;
}
