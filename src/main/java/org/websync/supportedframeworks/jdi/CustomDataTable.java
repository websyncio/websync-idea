package org.websync.supportedframeworks.jdi;


import com.epam.jdi.light.elements.complex.table.DataTable;
import com.epam.jdi.light.elements.pageobjects.annotations.FindBy;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.Css;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.XPath;
import org.openqa.selenium.By;

public class CustomDataTable extends DataTable {
    @FindBy(css = "th")
    public By customHeaderLocator;
    @XPath("../td")
    public By customFromCellToRow;
    @Css("th input[type=search],th input[type=text]")
    public By filterLocator;
}
