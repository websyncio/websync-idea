package org.websync.supportedframeworks.jdi;

import com.epam.jdi.light.elements.common.*;

import com.epam.jdi.light.elements.common.Label;
import com.epam.jdi.light.elements.complex.dropdown.Dropdown;
import com.epam.jdi.light.elements.complex.dropdown.DropdownExpand;
import com.epam.jdi.light.elements.complex.dropdown.DropdownSelect;
import com.epam.jdi.light.elements.complex.table.BaseTable;
import com.epam.jdi.light.elements.complex.table.CacheAll;
import com.epam.jdi.light.elements.complex.table.Column;
import com.epam.jdi.light.elements.composite.WebPage;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.ByText;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.XPath;
import com.epam.jdi.light.ui.html.elements.common.*;
import com.epam.jdi.light.ui.html.elements.common.Button;
import com.epam.jdi.light.ui.html.elements.common.Image;
import com.epam.jdi.light.ui.html.elements.common.TextArea;
import com.epam.jdi.light.ui.html.elements.common.TextField;


import java.awt.*;
import java.awt.Checkbox;

public class SupportedJDIElementsInit extends WebPage {
    //common
    @XPath("//testXpath")
    public Alerts InitializedAlert;

    @XPath("//testXpath")
    public Cookies InitializedCookie;

    @XPath("//testXpath")
    public Keyboard InitializedKeyboard;

    @XPath("//testXpath")
    public Mouse InitializedMouse;

    @XPath("//testXpath")
    public WindowsManager InitializedWindowsManager;

    @XPath("//testXpath")
    public Button InitializedButton;

    @XPath("//testXpath")
    public FileInput InitializedFileInput;

    @XPath("//testXpath")
    public Checkbox InitializedCheckbox;

    @XPath("//testXpath")
    public ColorPicker InitializedColorPicker;

    @XPath("//testXpath")
    public DateTimeSelector InitializedDateTimeSelector;

    @XPath("//testXpath")
    public Icon InitializedIcon;

    @XPath("//testXpath")
    public Image InitializedImage;

    @XPath("//testXpath")
    public Label InitializedLabel;

    @XPath("//testXpath")
    public Link InitializedLink;

    @XPath("//testXpath")
    public NumberSelector  InitializedNumberSelector;

    @XPath("//testXpath")
    public ProgressBar InitializedInitialized;

    @XPath("//testXpath")
    public Range InitializedRange;

    @XPath("//testXpath")
    public Text InitializedText;

    @XPath("//testXpath")
    public TextArea InitializedTextArea;

    @XPath("//testXpath")
    public TextField InitializedTextField;

    @XPath("//testXpath")
    public UIElement InitializedUIElement;

    @XPath("//testXpath")
    public DropdownSelect InitializedDropdownSelect;

    @XPath("//testXpath")
    public BaseTable InitializedBaseTable;

    @XPath("//testXpath")
    public Dropdown InitializedDropdown;

    @XPath("//testXpath")
    public DropdownExpand InitializedDropdownExpand;

    @XPath("//testXpath")
    public CacheAll InitializedCacheAll;

    @XPath("//testXpath")
    public Column InitializedColumn;




}
