package com.epam.sha.intellij.websync.jdi;

public enum JdiAttribute {
    JDI_BY_TEXT("com.epam.jdi.light.elements.pageobjects.annotations.locators.ByText"),
    JDI_CSS("com.epam.jdi.light.elements.pageobjects.annotations.locators.Css"),
    JDI_JDROPDOWN("com.epam.jdi.light.elements.pageobjects.annotations.locators.JDropdown"),
    JDI_JTABLE("com.epam.jdi.light.elements.pageobjects.annotations.locators.JTable"),
    JDI_JMENU("com.epam.jdi.light.elements.pageobjects.annotations.locators.JMenu"),
    JDI_JSITE("com.epam.jdi.light.elements.pageobjects.annotations.JSite"),
    JDI_UI("com.epam.jdi.light.elements.pageobjects.annotations.UI"),
    JDI_WITH_TEXT("com.epam.jdi.light.elements.pageobjects.annotations.locators.WithText"),
    JDI_XPATH("com.epam.jdi.light.elements.pageobjects.annotations.locators.XPath"),
    JDI_CLICK_AREA("com.epam.jdi.light.elements.pageobjects.annotations.ClickArea"),
    JDI_FIND_BY("com.epam.jdi.light.elements.pageobjects.annotations.FindBy"),
    JDI_FIND_BYS("com.epam.jdi.light.elements.pageobjects.annotations.FindBys"),
    JDI_FRAME("com.epam.jdi.light.elements.pageobjects.annotations.Frame"),
    JDI_GET_ANY("com.epam.jdi.light.elements.pageobjects.annotations.GetAny"),
    JDI_GET_SHOW_IN_VIEW("com.epam.jdi.light.elements.pageobjects.annotations.GetShowInView"),
    JDI_GET_TEXT_AS("com.epam.jdi.light.elements.pageobjects.annotations.GetTextAs"),
    JDI_GET_VISIBLE("com.epam.jdi.light.elements.pageobjects.annotations.GetVisible"),
    JDI_GET_VISIBLE_ENABLED("com.epam.jdi.light.elements.pageobjects.annotations.GetVisibleEnabled"),
    JDI_MANDATORY("com.epam.jdi.light.elements.pageobjects.annotations.Mandatory"),
    JDI_NAME("com.epam.jdi.light.elements.pageobjects.annotations.Name"),
    JDI_NO_CACHE("com.epam.jdi.light.elements.pageobjects.annotations.NoCache"),
    JDI_NO_WAIT("com.epam.jdi.light.elements.pageobjects.annotations.NoWait"),
    JDI_ROOT("com.epam.jdi.light.elements.pageobjects.annotations.Root"),
    JDI_PAGE_NAME("com.epam.jdi.light.elements.pageobjects.annotations.PageName"),
    JDI_SET_TEXT_AS("com.epam.jdi.light.elements.pageobjects.annotations.SetTextAs"),
    JDI_TITLE("com.epam.jdi.light.elements.pageobjects.annotations.Title"),
    JDI_URL("com.epam.jdi.light.elements.pageobjects.annotations.Url"),
    JDI_VISUAL_CHECK("com.epam.jdi.light.elements.pageobjects.annotations.VisualCheck"),
    JDI_WAIT_TIMEOUT("com.epam.jdi.light.elements.pageobjects.annotations.WaitTimeout"),
    JDI_SCLASS("com.epam.jdi.light.elements.pageobjects.annotations.smart.SClass"),
    JDI_SID("com.epam.jdi.light.elements.pageobjects.annotations.smart.SId"),
    JDI_SMART("com.epam.jdi.light.elements.pageobjects.annotations.smart.Smart"),
    JDI_SNAME("com.epam.jdi.light.elements.pageobjects.annotations.smart.SName"),
    JDI_STEXT("com.epam.jdi.light.elements.pageobjects.annotations.smart.SText");

    public final String value;

    JdiAttribute(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * @throws EnumConstantNotPresentException
     */
    public static JdiAttribute valueOfStr(String value) {
        for (JdiAttribute v : values()) {
            if (v.value.equals(value)) {
                return v;
            }
        }
        throw new EnumConstantNotPresentException(JdiAttribute.class, "No enum const " + JdiAttribute.class + "@value." + value);
    }
}
