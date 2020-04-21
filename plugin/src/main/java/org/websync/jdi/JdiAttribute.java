package org.websync.jdi;

import org.jetbrains.annotations.Nullable;

public enum JdiAttribute {
    JDI_BY_TEXT("locators.ByText"),
    JDI_CSS("locators.Css"),
    JDI_JDROPDOWN("locators.JDropdown"),
    JDI_JTABLE("locators.JTable"),
    JDI_JMENU("locators.JMenu"),
    JDI_JSITE("JSite"),
    JDI_UI("locators.UI"),
    JDI_UI_LIST("locators.UI.List"),
    JDI_WITH_TEXT("locators.WithText"),
    JDI_XPATH("locators.XPath"),
    JDI_CLICK_AREA("ClickArea"),
    JDI_FIND_BY("FindBy"),
    JDI_FIND_BYS("FindBys"),
    JDI_FRAME("Frame"),
    JDI_GET_ANY("GetAny"),
    JDI_GET_SHOW_IN_VIEW("GetShowInView"),
    JDI_GET_TEXT_AS("GetTextAs"),
    JDI_GET_VISIBLE("GetVisible"),
    JDI_GET_VISIBLE_ENABLED("GetVisibleEnabled"),
    JDI_MANDATORY("Mandatory"),
    JDI_NAME("Name"),
    JDI_NO_CACHE("NoCache"),
    JDI_NO_WAIT("NoWait"),
    JDI_ROOT("Root"),
    JDI_PAGE_NAME("PageName"),
    JDI_SET_TEXT_AS("SetTextAs"),
    JDI_TITLE("Title"),
    JDI_URL("Url"),
    JDI_VISUAL_CHECK("VisualCheck"),
    JDI_WAIT_TIMEOUT("WaitTimeout"),
    JDI_SCLASS("smart.SClass"),
    JDI_SID("smart.SId"),
    JDI_SMART("smart.Smart"),
    JDI_SNAME("smart.SName"),
    JDI_STEXT("smart.SText");

    public final String className;

    JdiAttribute(final String name) {
        this.className = BASE_PACKAGE + "." + name;
    }

    private static final String BASE_PACKAGE = "com.epam.jdi.light.elements.pageobjects.annotations";

    public static boolean isJdiAnnotation(@Nullable String fqn) {
        return fqn != null && fqn.startsWith(BASE_PACKAGE);
    }

    public static JdiAttribute getByClassName(@Nullable String className) {
        for (JdiAttribute attr : values()) {
            if(attr.className.equals(className)) {
                return attr;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return className;
    }

    public String getShortName() {
        return className.substring(className.lastIndexOf(".") + 1);
    }

    public static String getQualifiedNameByShortName(String shortName) {
        for (JdiAttribute attr : values()) {
            if(attr.getShortName().equals(shortName)) {
                return attr.className;
            }
        }
        return null;
    }
}
