package org.websync.websession.psimodels;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiNameValuePair;
import lombok.Getter;
import org.websync.jdi.JdiAttribute;
import org.websync.websession.models.ComponentInstance;

import java.util.Arrays;
import java.util.List;

public class PsiComponentInstance extends PsiModelWithId<PsiComponentInstance> implements ComponentInstance {

    public enum LocatorAnno {
        BYTEXT(ByText.class),
        CSS(Css.class),
        JDROPDOWN(JDropdown.class),
        JMENU(JMenu.class),
        JTABLE(JTable.class),
        UI(UI.class),
        WITHTEXT(WithText.class),
        XPATH(XPath.class);

        @Getter
        private Class<?> clazz;

        LocatorAnno(Class<?> clazz) {
            this.clazz = clazz;
        }

        public static LocatorAnno valueOfClass(Class<?> locatorClass) {
            return Arrays.asList(LocatorAnno.values())
                    .stream()
                    .filter(l -> l.getClazz().equals(locatorClass))
                    .findFirst().get();
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
        @Getter
        private Object locator;

        public Class<?> getLocatorClass() {
            return locator.getClass();
        }
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

                    switch (JdiAttribute.valueOfStr(className)) {
                        case JDI_BY_TEXT:
                            locator.locator = new ByText();
                            ((ByText) locator.locator).value = attributes.get(0).getLiteralValue();
                            break;
                        case JDI_CSS:
                            locator.locator = new Css();
                            ((Css) locator.locator).value = attributes.get(0).getLiteralValue();
                            break;
                        case JDI_JDROPDOWN:
                            locator.locator = new JDropdown();
                            ((JDropdown) locator.locator).value = attributes.get(0).getLiteralValue();
                            break;
                        case JDI_JMENU:
                            locator.locator = new JMenu();
                            ((JMenu) locator.locator).value[0] = attributes.get(0).getLiteralValue();
                            break;
                        case JDI_JTABLE:
                            locator.locator = new JTable();
                            ((JTable) locator.locator).root = attributes.get(0).getLiteralValue();
                            break;
                        case JDI_UI:
                            locator.locator = new UI();
                            ((UI) locator.locator).value = attributes.get(0).getLiteralValue();
                            break;
                        case JDI_WITH_TEXT:
                            locator.locator = new WithText();
                            ((WithText) locator.locator).value = attributes.get(0).getLiteralValue();
                            break;
                        case JDI_XPATH:
                            locator.locator = new XPath();
                            ((XPath) locator.locator).value = attributes.get(0).getLiteralValue();
                            break;
                        default:
                            break;
                    }
                }
        );
        return locator;
    }
}
