package org.websync.websession.psimodels.jdi;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiNameValuePair;
import com.intellij.psi.impl.source.tree.java.PsiLiteralExpressionImpl;
import org.websync.jdi.JdiAttribute;
import org.websync.websession.psimodels.jdi.annotations.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Locator {
    private PsiAnnotation annotation;
    private List<PsiNameValuePair> attributes;

    public Locator(PsiAnnotation annotation) {
        this.annotation = annotation;
        this.attributes = Arrays.asList(annotation.getParameterList().getAttributes());
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
                Arrays.asList(JDropdown.class.getFields()).stream().forEach(
                        f -> {
                            String fieldName = f.getName();
                            try {
                                f.set(locator, getAnnotationValue(fieldName));
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                );
//                    locator.root = getAnnotationValue("root");
//                    locator.value = getAnnotationValue("value");
//                    locator.expand = getAnnotationValue("expand");
//                    locator.list = getAnnotationValue("list");
//                    locator.autoclose = getAnnotationValue("autoclose");
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

    private Object locator;

    public Object get() {
        return locator;
    }
}