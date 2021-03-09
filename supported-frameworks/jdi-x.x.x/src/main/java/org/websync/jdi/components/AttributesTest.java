package org.websync.jdi.components;

import com.epam.jdi.light.elements.common.Label;
import com.epam.jdi.light.elements.composite.Section;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.*;

public class AttributesTest extends Section {
    @ByText(value = "value")
    private Label byText;
    @Css(value = "value")
    private Label css1;
    @JDropdown(root = "root1", value = "value1", expand = "expand1", list = "list1", autoclose = true)
    private Label jDropdown;
    @JMenu(value = {"value1", "value2"}, group = "group1")
    private Label jMenu;
    @JTable(
            root = "root1", header = {"header1", "header2"}, headers = "headers1", filter = "filter1", row = "row1",
            column = "column1", cell = "cell1", allCells = "allCells1", rowHeader = "rowHeader1",
            fromCellToRow = "fromCellToRow1", size = 1, count = 1, firstColumnIndex = 1, columnsMapping = {1,2,3})
    private Label jTable;
    @UI(value = "value1", group = "group1")
    private Label ui;
    @UI.List({@UI(value = "value1", group = "group1"), @UI(value = "value1", group = "group1")})
    private Label uiList;
    @WithText(value = "value1")
    private Label withText;
    @XPath(value = "value1")
    private Label xpath;
}
