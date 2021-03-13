package org.websync.jdi.components;

import com.epam.jdi.light.elements.common.Label;
import com.epam.jdi.light.elements.complex.Menu;
import com.epam.jdi.light.elements.complex.dropdown.Dropdown;
import com.epam.jdi.light.elements.complex.dropdown.DropdownExpand;
import com.epam.jdi.light.elements.complex.table.DataTable;
import com.epam.jdi.light.elements.complex.table.Table;
import com.epam.jdi.light.elements.composite.Section;
import com.epam.jdi.light.elements.pageobjects.annotations.*;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.*;

public class ComplexInitializationAttributesComponent extends Section {
    @JDropdown("test")
    public Dropdown initializedDropdownnewvalue;
    @JDropdown(root = "test", value = "test1", list = "test2", expand = ".expand", autoclose = true)
    public DropdownExpand initializedDropdownExpand;

    @JTable(root = "#test", rowHeader = "test")
    public DataTable initializedDataTable;
    @JTable(root = "#root", rowHeader = "header", filter = "filter", fromCellToRow = "testString", count = 5, firstColumnIndex = 2, columnsMapping = {1, 2, 3})
    public DataTable initializedDataTable2;
    @JTable(
            root = "#test1",
            row = "//tr[%s]/td",
            column = "//tr/td[%s]",
            cell = "//tr[{1}]/td[{0}]",
            allCells = "td",
            headers = "th",
            header = {"Test1", "Test2", "Test3", "Test4"},
            rowHeader = "Test5",
            size = 4
    )
    public Table initializedTableWithAllParameters;

    @JTable(root = "#test1")
    public Table initializedTable;

    @JMenu("test")
    public Menu initializedMenu;

    @JMenu(value = "test", group = "group")
    public Menu initializedMenuWithAllParameters;
}
