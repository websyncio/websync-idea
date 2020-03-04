package org.websync.websession.psimodels;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiNameValuePair;
import com.intellij.psi.impl.source.tree.java.PsiLiteralExpressionImpl;
import lombok.Getter;
import org.websync.jdi.JdiAttribute;
import org.websync.websession.models.ComponentInstance;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PsiComponentInstance extends PsiModelWithId<PsiComponentInstance> implements ComponentInstance {

    public static class ByText {
        public String value;

        @Override
        public String toString() {
            return "ByText{" +
                    "value='" + value + '\'' +
                    '}';
        }
    }

    public static class Css {
        public String value;

        @Override
        public String toString() {
            return "Css{" +
                    "value='" + value + '\'' +
                    '}';
        }
    }

    public static class JDropdown {
        public String root;
        public String value;
        public String list;
        public String expand;
        public boolean autoclose;

        @Override
        public String toString() {
            return "JDropdown{" +
                    "root='" + root + '\'' +
                    ", value='" + value + '\'' +
                    ", list='" + list + '\'' +
                    ", expand='" + expand + '\'' +
                    ", autoclose=" + autoclose +
                    '}';
        }
    }

    public static class JMenu {
        public List<String> value;
        public String group;

        @Override
        public String toString() {
            return "JMenu{" +
                    "value=" + value +
                    ", group='" + group + '\'' +
                    '}';
        }
    }

    public static class JTable {
        public String root;
        public List<String> header;
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
        public List<Integer> columnsMapping;

        @Override
        public String toString() {
            return "JTable{" +
                    "root='" + root + '\'' +
                    ", header=" + header +
                    ", headers='" + headers + '\'' +
                    ", filter='" + filter + '\'' +
                    ", row='" + row + '\'' +
                    ", column='" + column + '\'' +
                    ", cell='" + cell + '\'' +
                    ", allCells='" + allCells + '\'' +
                    ", rowHeader='" + rowHeader + '\'' +
                    ", fromCellToRow='" + fromCellToRow + '\'' +
                    ", size=" + size +
                    ", count=" + count +
                    ", firstColumnIndex=" + firstColumnIndex +
                    ", columnsMapping=" + columnsMapping +
                    '}';
        }
    }

    public static class UI {
        public String value;
        public String group;
        public List<UI> list;

        @Override
        public String toString() {
            return "UI{" +
                    "value='" + value + '\'' +
                    ", group='" + group + '\'' +
                    ", list=" + list +
                    '}';
        }
    }

    public static class WithText {
        public String value;

        @Override
        public String toString() {
            return "WithText{" +
                    "value='" + value + '\'' +
                    '}';
        }
    }

    public static class XPath {
        public String value;

        @Override
        public String toString() {
            return "XPath{" +
                    "value='" + value + '\'' +
                    '}';
        }
    }

    public static class Locator {
        @Getter
        private PsiAnnotation annotation;
        private List<PsiNameValuePair> attributes;

        private Locator(PsiAnnotation annotation) {
            this.annotation = annotation;
            attributes = Arrays.asList(annotation.getParameterList().getAttributes());
            setup();
        }

        private <T> T getAnnotationValue(String name) {
            PsiAnnotationMemberValue value = attributes.stream()
                    .filter(a -> a.getName().equals(name))
                    .findFirst().get().getValue();
            return (T) ((PsiLiteralExpressionImpl) value).getValue();
        }

        private <T> List<T> getAnnotationValues(String name) {
            PsiAnnotationMemberValue value = attributes.stream()
                    .filter(a -> a.getName().equals(name))
                    .findFirst().get().getValue();

            List<T> list = Arrays.asList(value.getChildren()).stream()
                    .filter(c -> c instanceof PsiLiteralExpressionImpl)
                    .map(c -> (T) ((PsiLiteralExpressionImpl) c).getValue())
                    .collect(Collectors.toList());
            return list;
        }

        private void setup() {
            JdiAttribute attr;
            try {
                attr = JdiAttribute.valueOfStr(annotation.getQualifiedName());
            } catch (Exception e) {
                return;
            }
            switch (attr) {
                case JDI_BY_TEXT: {
                    ByText locator = new ByText();
                    locator.value = getAnnotationValue("value");
                    this.locator = locator;
                    break;
                }
                case JDI_CSS: {
                    Css locator = new Css();
                    locator.value = getAnnotationValue("value");
                    this.locator = locator;
                    break;
                }
                case JDI_JDROPDOWN: {
                    JDropdown locator = new JDropdown();
                    locator.root = getAnnotationValue("root");
                    locator.value = getAnnotationValue("value");
                    locator.expand = getAnnotationValue("expand");
                    locator.list = getAnnotationValue("list");
                    locator.autoclose = getAnnotationValue("autoclose");
                    this.locator = locator;
                    break;
                }
                case JDI_JMENU: {
                    JMenu locator = new JMenu();
                    locator.value = getAnnotationValues("value");
                    locator.group = getAnnotationValue("group");
                    this.locator = locator;
                    break;
                }
                case JDI_JTABLE: {
                    JTable locator = new JTable();
                    locator.root = getAnnotationValue("root");
                    locator.header = getAnnotationValues("header");
                    locator.headers = getAnnotationValue("headers");
                    locator.filter = getAnnotationValue("filter");
                    locator.row = getAnnotationValue("row");
                    locator.column = getAnnotationValue("column");
                    locator.cell = getAnnotationValue("cell");
                    locator.allCells = getAnnotationValue("allCells");
                    locator.rowHeader = getAnnotationValue("rowHeader");
                    locator.fromCellToRow = getAnnotationValue("fromCellToRow");
                    locator.size = getAnnotationValue("size");
                    locator.count = getAnnotationValue("count");
                    locator.firstColumnIndex = getAnnotationValue("firstColumnIndex");
                    locator.columnsMapping = getAnnotationValues("columnsMapping");
                    this.locator = locator;
                    break;
                }
                case JDI_UI: {
                    UI locator = new UI();
                    locator.value = getAnnotationValue("value");
                    locator.group = getAnnotationValue("group");
                    this.locator = locator;
                    break;
                }
                case JDI_WITH_TEXT: {
                    WithText locator = new WithText();
                    locator.value = getAnnotationValue("value");
                    this.locator = locator;
                    break;
                }
                case JDI_XPATH: {
                    XPath locator = new XPath();
                    locator.value = getAnnotationValue("value");
                    this.locator = locator;
                    break;
                }
                default:
                    break;
            }
        }

        @Getter
        private Object locator;
    }

    private PsiField psiFiled;

    public PsiComponentInstance(PsiField psiFiled) {
        this.psiFiled = psiFiled;
    }

    public void fill() {
        id = psiFiled.toString();
    }

    public Locator getLocator() {
        if (psiFiled.getAnnotations().length == 0) {
            return null;
        }
        return new Locator(psiFiled.getAnnotations()[0]);
    }
}
