package org.websync.jdi.components;


import com.epam.jdi.light.elements.base.DriverBase;
import com.epam.jdi.light.elements.base.JDIBase;
import com.epam.jdi.light.elements.base.UIBaseElement;
import com.epam.jdi.light.elements.base.UIListBase;
import com.epam.jdi.light.elements.common.*;
import com.epam.jdi.light.elements.complex.*;
import com.epam.jdi.light.elements.complex.dropdown.Dropdown;
import com.epam.jdi.light.elements.complex.dropdown.DropdownExpand;
import com.epam.jdi.light.elements.complex.dropdown.DropdownSelect;
import com.epam.jdi.light.elements.complex.table.*;
import com.epam.jdi.light.elements.composite.Form;
import com.epam.jdi.light.elements.composite.Section;
import com.epam.jdi.light.elements.composite.WebPage;
import com.epam.jdi.light.elements.pageobjects.annotations.FindBy;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.ByText;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.Css;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.WithText;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.XPath;
import com.epam.jdi.light.ui.html.elements.common.*;
import com.epam.jdi.light.ui.html.elements.complex.DataListOptions;
import com.epam.jdi.light.ui.html.elements.complex.MultiSelector;
import com.epam.jdi.light.ui.html.elements.complex.RadioButtons;
import com.epam.jdi.light.ui.html.elements.complex.Tabs;

public class SupportedJdiElements extends WebPage {

    @XPath("//testXpath")
    public Alerts initializedAlert;

    @XPath("//testXpath")
    public Cookies initializedCookie;

    @XPath("//testXpath")
    public Keyboard initializedKeyboard;

    @XPath("//testXpath")
    public Mouse initializedMouse;

    @XPath("//testXpath")
    public WindowsManager initializedWindowsManager;

    @XPath("//testXpath")
    public Button initializedButton;

    @XPath("//testXpath")
    public FileInput initializedFileInput;

    @XPath("//testXpath")
    public Checkbox initializedCheckbox;

    @XPath("//testXpath")
    public ColorPicker initializedColorPicker;

    @XPath("//testXpath")
    public DateTimeSelector initializedDateTimeSelector;

    @XPath("//testXpath")
    public Icon initializedIcon;

    @XPath("//testXpath")
    public Image initializedImage;

    @XPath("//testXpath")
    public Label initializedLabel;

    @XPath("//testXpath")
    public Link initializedLink;

    @XPath("//testXpath")
    public NumberSelector initializedNumberSelector;

    @XPath("//testXpath")
    public ProgressBar initializedInitialized;

    @XPath("//testXpath")
    public Range initializedRange;

    @XPath("//testXpath")
    public Text initializedText;

    @XPath("//testXpath")
    public TextArea initializedTextArea;

    @XPath("//testXpath")
    public TextField initializedTextField;

    @XPath("//testXpath")
    public UIElement initializedUIElement;

    @XPath("//testXpath")
    public DropdownSelect initializedDropdownSelect;

    @XPath("//testXpath")
    public BaseTable initializedBaseTable;

    @XPath("//testXpath")
    public Dropdown initializedDropdown;

    @XPath("//testXpath")
    public DropdownExpand initializedDropdownExpand;

    @XPath("//testXpath")
    public CacheAll initializedCacheAll;

    @XPath("//testXpath")
    public Column initializedColumn;

    @XPath("//testXpath")
    public Row initializedRow;

    @XPath("//testXpath")
    public Single initializedSingle;

    @XPath("//testXpath")
    public Table initializedTable;

    @XPath("//testXpath")
    public TableMatcher initializedTableMatcher;

    @XPath("//testXpath")
    public DataTable initializedDataTable;

    @Css(".testCss")
    public DataListOptions initializedDataListOptions;

    @XPath("//testXpath")
    public MultiSelector initializedMultiSelector;

    @ByText("test")
    public RadioButtons initializedRadioButtons;

    @Css(".testCss")
    public Tabs initializedTabs;

    @WithText("test")
    public Line initializedLine;

    @FindBy(xpath = "//test")
    public NameNum initializedNameNum;

    @Css(".testCss")
    public Checklist initializedChecklist;

    @XPath("//testXpath")
    public Combobox initializedCombobox;

    @ByText("test")
    public DataList initializedDataList;

    @Css(".testCss")
    public JList initializedJList;

    @XPath("//testXpath")
    public Form initializedForm;

    @XPath("//testXpath")
    public Section initializedSection;

    @Css(".testCss")
    public Menu initializedMenu;

    @FindBy(xpath = "//test")
    public Selector initializedSelector;

    @XPath("//testXpath")
    public WebPage initializedWebPage;

    @Css(".testCss")
    public SelectWrapper initializedSelectWrapper;

    @XPath("//testXpath")
    public WebList initializedWebList;

    @XPath("//testXpath")
    public UIBaseElement initializedUiBaseElement;

    @XPath("//testXpath")
    public UIListBase initializedDriverBase;

    @XPath("//testXpath")
    public DriverBase initializedUiListBase;

    @XPath("//testXpath")
    public JDIBase initializedJdiBase;
}
