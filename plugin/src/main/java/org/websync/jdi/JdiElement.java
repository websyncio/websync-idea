package org.websync.jdi;

public enum JdiElement {
    JDI_ALERTS("com.epam.jdi.light.elements.common.Alerts"),
    JDI_COOKIES("com.epam.jdi.light.elements.common.Cookies"),
    JDI_KEYBOARD("com.epam.jdi.light.elements.common.Keyboard"),
    JDI_MOUSE("com.epam.jdi.light.elements.common.Mouse"),
    WINDOWS_MANAGER("com.epam.jdi.light.elements.common.WindowsManager"),
    JDI_BUTTON("com.epam.jdi.light.ui.html.elements.common.Button"),
    JDI_CHECKBOX("com.epam.jdi.light.ui.html.elements.common.Checkbox"),
    JDI_COLOR_PICKER("com.epam.jdi.light.ui.html.elements.common.Colorpicker"),
    JDI_DATE_TIME_SELECTOR("com.epam.jdi.light.ui.html.elements.common.DateTimeSelector"),
    JDI_FILE_INPUT("com.epam.jdi.light.ui.html.elements.common.FileInput"),
    JDI_ICON("com.epam.jdi.light.ui.html.elements.common.Icon"),
    JDI_IMAGE("com.epam.jdi.light.ui.html.elements.common.Image"),
    JDI_LABEL("com.epam.jdi.light.elements.common.Label"),
    JDI_LINK("com.epam.jdi.light.ui.html.elements.common.Link"),
    JDI_NUMBER_SELECTOR("com.epam.jdi.light.ui.html.elements.common.NumberSelector"),
    JDI_PROGRESS_BAR("com.epam.jdi.light.ui.html.elements.common.ProgressBar"),
    JDI_RANGE("com.epam.jdi.light.ui.html.elements.common.Range"),
    JDI_TEXT("com.epam.jdi.light.ui.html.elements.common.Text"),
    JDI_TEXT_AREA("com.epam.jdi.light.ui.html.elements.common.TextArea"),
    JDI_TEXT_FIELD("com.epam.jdi.light.ui.html.elements.common.TextField"),
    JDI_UI_ELEMENT("com.epam.jdi.light.elements.common.UIElement"),
    JDI_DROPDOWN("com.epam.jdi.light.elements.complex.dropdown.Dropdown"),
    JDI_DROPDOWN_EXPAND("com.epam.jdi.light.elements.complex.dropdown.DropdownExpand"),
    JDI_DROPDOWN_SELECT("com.epam.jdi.light.elements.complex.dropdown.DropdownSelect"),
    JDI_BASE_TABLE("com.epam.jdi.light.elements.complex.table.BaseTable"),
    JDI_CACHE_ALL("com.epam.jdi.light.elements.complex.table.CacheAll"),
    JDI_COLUMN("com.epam.jdi.light.elements.complex.table.Column"),
    JDI_DATA_TABLE("com.epam.jdi.light.elements.complex.table.DataTable"),
    JDI_LINE("com.epam.jdi.light.elements.complex.table.Line"),
    JDI_NAME_NUM("com.epam.jdi.light.elements.complex.table.NameNum"),
    JDI_ROW("com.epam.jdi.light.elements.complex.table.Row"),
    JDI_SINGLE("com.epam.jdi.light.elements.complex.table.Single"),
    JDI_TABLE("com.epam.jdi.light.elements.complex.table.Table"),
    JDI_TABLE_MATCHER("com.epam.jdi.light.elements.complex.table.TableMatcher"),
    JDI_CHECKLIST("com.epam.jdi.light.elements.complex.Checklist"),
    JDI_COMBOBOX("com.epam.jdi.light.elements.complex.Combobox"),
    JDI_DATA_LIST("com.epam.jdi.light.elements.complex.DataList"),
    JDI_DATA_LIST_OPTIONS("com.epam.jdi.light.ui.html.elements.complex.DataListOptions"),
    JDI_JLIST("com.epam.jdi.light.elements.complex.JList"),
    JDI_LIST_BASE("com.epam.jdi.light.elements.complex.ListBase"),
    JDI_MENU("com.epam.jdi.light.elements.complex.Menu"),
    JDI_MULTI_SELECTOR("com.epam.jdi.light.ui.html.elements.complex.MultiSelector"),
    JDI_RADIO_BUTTONS("com.epam.jdi.light.ui.html.elements.complex.RadioButtons"),
    JDI_SELECTOR("com.epam.jdi.light.elements.complex.Selector"),
    JDI_SELECT_WRAPPER("com.epam.jdi.light.elements.complex.SelectWrapper"),
    JDI_TABS("com.epam.jdi.light.ui.html.elements.complex.Tabs"),
    JDI_WEB_LIST("com.epam.jdi.light.elements.complex.WebList"),
    JDI_FORM("com.epam.jdi.light.elements.composite.Form"),
    JDI_SECTION("com.epam.jdi.light.elements.composite.Section"),
    JDI_WEB_PAGE("com.epam.jdi.light.elements.composite.WebPage"),
    JDI_DRIVER_BASE("com.epam.jdi.light.elements.base.DriverBase"),
    JDI_JDI_BASE("com.epam.jdi.light.elements.base.JDIBase"),
    JDI_UI_BASE_ELEMENT("com.epam.jdi.light.elements.base.UIBaseElement"),
    JDI_UI_LIST_BASE("com.epam.jdi.light.elements.base.UIListBase");

    public final String className;

    JdiElement(final String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return className;
    }

}
