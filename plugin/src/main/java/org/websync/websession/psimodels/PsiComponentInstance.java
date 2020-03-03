package org.websync.websession.psimodels;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiNameValuePair;
import org.websync.jdi.JdiAttribute;
import org.websync.websession.models.ComponentInstance;

import java.util.Arrays;
import java.util.List;

import static org.websync.jdi.JdiAttribute.*;

public class PsiComponentInstance extends PsiModelWithId<PsiComponentInstance> implements ComponentInstance {

    public enum LocatorClassName {
        BY_TEXT(JDI_BY_TEXT.value),
        BY_CSS(JDI_CSS.value),
        BY_JDROPDOWN(JDI_JDROPDOWN.value),
        BY_JTABLE(JDI_JTABLE.value),
        BY_JMENU(JDI_JMENU.value),
        BY_UI(JDI_UI.value),
        BY_WITH_TEXT(JDI_WITH_TEXT.value),
        BY_XPATH(JDI_XPATH.value);

        private String value;

        LocatorClassName(String value) {
            this.value = value;
        }
    }

    public static class ByText {
        public String value;
    }

    public static class Css {
        public String value;
    }

    public static class JDropdown {
        public String root;
        public String value;
        public String list;
        public String expand;
        public boolean autoclose;
    }

    public static class JMenu {
        public String[] value;
        public String group;
    }

    public static class JTable {
        public String root;
        public String[] header;
        public String headers;
        public String filter;
        public String row;
        public String column;
        public String cell;
        public String allCells;
        public String rowHeader;
        public String fromCellToRow;
        public int size;
        public int count;
        public int firstColumnIndex;
        public int[] columnsMapping;
    }

    public static class UI {
        public String value;
        public String group;
        public UI[] list;
    }

    public static class WithText {
        public String value;
    }

    public static class XPath {
        public String value;
    }

    public static class Locator {
        public Class<?> clazz;
        public Object locator;
    }

    private PsiField psiFiled;

    public PsiComponentInstance(PsiField psiFiled) {
        this.psiFiled = psiFiled;
    }

    public void fill() {
        id = psiFiled.toString();
    }

    public String getAttributes() {
        List<PsiAnnotation> psiAnnotations = Arrays.asList(psiFiled.getAnnotations());
        final String[] result = {""};
        psiAnnotations.stream().forEach(
                anno -> {
                    result[0] += anno.getQualifiedName();
                    List<PsiNameValuePair> attributes = Arrays.asList(anno.getParameterList().getAttributes());
                    attributes.stream().forEach(attr -> result[0] += " = '" + attr.getLiteralValue() + "'");
                }
        );
        return result[0];
    }

    public Locator getLocator() {
        Locator locator = new Locator();

        List<PsiAnnotation> psiAnnotations = Arrays.asList(psiFiled.getAnnotations());
        psiAnnotations.stream().forEach(
                anno -> {
                    List<PsiNameValuePair> attributes = Arrays.asList(anno.getParameterList().getAttributes());

                    String className = anno.getQualifiedName();
                    try {
                        locator.clazz = Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    switch (JdiAttribute.valueOfStr(className)) {
                        case JDI_BY_TEXT:
                            attributes.get(0).getLiteralValue();
                            locator.locator = new ByText();
                            ((ByText)locator.locator).value = attributes.get(0).getLiteralValue();
                            break;
                        case JDI_CSS:
                            attributes.get(0).getLiteralValue();
                            locator.locator = new Css();
                            ((Css)locator.locator).value = attributes.get(0).getLiteralValue();
                            break;
                        case JDI_JDROPDOWN:
                            attributes.get(0).getLiteralValue();
                            locator.locator = new JDropdown();
                            ((JDropdown)locator.locator).value = attributes.get(0).getLiteralValue();
                            break;
                        case JDI_JMENU:
                            attributes.get(0).getLiteralValue();
                            locator.locator = new JMenu();
                            ((JMenu)locator.locator).value[0] = attributes.get(0).getLiteralValue();
                            break;
                        case JDI_JTABLE:
                            attributes.get(0).getLiteralValue();
                            locator.locator = new JTable();
                            ((JTable)locator.locator).root = attributes.get(0).getLiteralValue();
                            break;
                        case JDI_UI:
                            attributes.get(0).getLiteralValue();
                            locator.locator = new UI();
                            ((UI)locator.locator).value = attributes.get(0).getLiteralValue();
                            break;
                        case JDI_WITH_TEXT:
                            attributes.get(0).getLiteralValue();
                            locator.locator = new WithText();
                            ((WithText)locator.locator).value = attributes.get(0).getLiteralValue();
                            break;
                        case JDI_XPATH:
                            attributes.get(0).getLiteralValue();
                            locator.locator = new XPath();
                            ((XPath)locator.locator).value = attributes.get(0).getLiteralValue();
                            break;
                        default:
                            break;
                    }
                }
        );
        return locator;
    }
}
