package org.websync.frameworks.jdi;

import java.util.Arrays;
import java.util.Optional;

//Not supported common elemtns:
//        ColorPicker,
//        DateTimeSelector,
//        NumberSelector,
//        Icon,
//        ProgressBar,
//        Range,
//        Title,
//        Label
//
//        Not supported complex elemtns:
//        DataList,
//        MultiDropdown,
//        MultiSelector
//        ColorPicker,
//        DateTimeSelector,
//        NumberSelector,
//        Icon,
//        ProgressBar,
//        Range,
//        Title,
//        Label,
//        Combobox

public enum JdiElement {
    JDI_TEXT(JdiElementType.Common,"com.epam.jdi.light.ui.html.elements.common.Text"),
    JDI_TEXT_AREA(JdiElementType.Common,"com.epam.jdi.light.ui.html.elements.common.TextArea"),
    JDI_TEXT_FIELD(JdiElementType.Common,"com.epam.jdi.light.ui.html.elements.common.TextField"),
    JDI_BUTTON(JdiElementType.Common,"com.epam.jdi.light.ui.html.elements.common.Button"),
    JDI_CHECKBOX(JdiElementType.Common,"com.epam.jdi.light.ui.html.elements.common.Checkbox"),
    JDI_IMAGE(JdiElementType.Common,"com.epam.jdi.light.ui.html.elements.common.Image"),
    JDI_LINK(JdiElementType.Common,"com.epam.jdi.light.ui.html.elements.common.Link"),
    JDI_FILE_INPUT(JdiElementType.Common,"com.epam.jdi.light.ui.html.elements.common.FileInput"),
    JDI_LABEL(JdiElementType.Common,"com.epam.jdi.light.elements.common.Label"),
    JDI_ICON(JdiElementType.Common,"com.epam.jdi.light.ui.html.elements.common.Icon"),
    JDI_UI_ELEMENT(JdiElementType.Common,"com.epam.jdi.light.elements.common.UIElement"),

    JDI_MENU(JdiElementType.Complex,"com.epam.jdi.light.elements.complex.Menu"),
    JDI_RADIO_BUTTONS(JdiElementType.Complex,"com.epam.jdi.light.ui.html.elements.complex.RadioButtons"),
    JDI_CHECKLIST(JdiElementType.Complex,"com.epam.jdi.light.elements.complex.Checklist"),
    JDI_TABLE(JdiElementType.Complex,"com.epam.jdi.light.elements.complex.table.Table"),
    JDI_DATA_TABLE(JdiElementType.Complex,"com.epam.jdi.light.elements.complex.table.DataTable"),
    JDI_DROPDOWN(JdiElementType.Complex,"com.epam.jdi.light.elements.complex.dropdown.Dropdown"),
    JDI_TABS(JdiElementType.Complex,"com.epam.jdi.light.ui.html.elements.complex.Tabs"),
    JDI_WEB_LIST(JdiElementType.Complex,"com.epam.jdi.light.elements.complex.WebList"),

    JDI_SECTION(JdiElementType.Composite,"com.epam.jdi.light.elements.composite.Section"),
    JDI_WEB_PAGE(JdiElementType.Composite, "com.epam.jdi.light.elements.composite.WebPage"),

    JDI_UI_BASE_ELEMENT(JdiElementType.Base, "com.epam.jdi.light.elements.base.UIBaseElement");

//    JDI_COLOR_PICKER("com.epam.jdi.light.ui.html.elements.common.Colorpicker"),
//    JDI_DATE_TIME_SELECTOR("com.epam.jdi.light.ui.html.elements.common.DateTimeSelector"),

//    JDI_NUMBER_SELECTOR("com.epam.jdi.light.ui.html.elements.common.NumberSelector"),
//    JDI_PROGRESS_BAR("com.epam.jdi.light.ui.html.elements.common.ProgressBar"),
//    JDI_RANGE("com.epam.jdi.light.ui.html.elements.common.Range"),
//    JDI_DROPDOWN_EXPAND("com.epam.jdi.light.elements.complex.dropdown.DropdownExpand"),
//    JDI_DROPDOWN_SELECT("com.epam.jdi.light.elements.complex.dropdown.DropdownSelect"),
//    JDI_BASE_TABLE("com.epam.jdi.light.elements.complex.table.BaseTable"),
//    JDI_CACHE_ALL("com.epam.jdi.light.elements.complex.table.CacheAll"),
//    JDI_COLUMN("com.epam.jdi.light.elements.complex.table.Column"),
//    JDI_LINE("com.epam.jdi.light.elements.complex.table.Line"),
//    JDI_NAME_NUM("com.epam.jdi.light.elements.complex.table.NameNum"),
//    JDI_ROW("com.epam.jdi.light.elements.complex.table.Row"),
//    JDI_SINGLE("com.epam.jdi.light.elements.complex.table.Single"),
//    JDI_TABLE_MATCHER("com.epam.jdi.light.elements.complex.table.TableMatcher"),
//    JDI_COMBOBOX("com.epam.jdi.light.elements.complex.Combobox"),
//    JDI_DATA_LIST("com.epam.jdi.light.elements.complex.DataList"),
//    JDI_DATA_LIST_OPTIONS("com.epam.jdi.light.ui.html.elements.complex.DataListOptions"),
//    JDI_JLIST("com.epam.jdi.light.elements.complex.JList"),
//    JDI_LIST_BASE("com.epam.jdi.light.elements.complex.ListBase"),
//    JDI_MULTI_SELECTOR("com.epam.jdi.light.ui.html.elements.complex.MultiSelector"),
//    JDI_SELECTOR("com.epam.jdi.light.elements.complex.Selector"),
//    JDI_SELECT_WRAPPER("com.epam.jdi.light.elements.complex.SelectWrapper"),
//    JDI_FORM("com.epam.jdi.light.elements.composite.Form"),
//    JDI_DRIVER_BASE("com.epam.jdi.light.elements.base.DriverBase"),
//    JDI_JDI_BASE("com.epam.jdi.light.elements.base.JDIBase"),
//    JDI_UI_LIST_BASE("com.epam.jdi.light.elements.base.UIListBase");

    public final String className;
    public final JdiElementType type;

    JdiElement(JdiElementType type, final String className) {
        this.type = type;
        this.className = className;
    }

    public static Optional<JdiElement> findElement(String className) {
        return Arrays.stream(values()).filter(v -> v.className.equals(className))
                .findFirst();
    }

    @Override
    public String toString() {
        return className;
    }
}

