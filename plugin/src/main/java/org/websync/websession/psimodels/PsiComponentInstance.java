package org.websync.websession.psimodels;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiLiteralExpressionImpl;
import lombok.Getter;
import org.websync.jdi.JdiAttribute;
import org.websync.websession.models.ComponentInstance;

import java.util.ArrayList;
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
        private PsiAnnotation annotation;
        private List<PsiNameValuePair> attributes;

        private Locator(PsiAnnotation annotation) {
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

    public static class LiteralExpression {
        @Getter
        private final Class<?> clazz;
        @Getter
        private final Object value;

        public LiteralExpression(Object value) {
            this.value = value;
            this.clazz = (value != null) ? value.getClass() : null;
        }

        @Override
        public String toString() {
            return "LiteralExpression{" +
                    "clazz=" + clazz +
                    ", value=" + value +
                    '}';
        }
    }

    public static class NameValuePair {
        @Getter
        private final String identifier;
        @Getter
        private LiteralExpression literalExpression;
        @Getter
        private ArrayList<LiteralExpression> arrayInitializerMemberValue;

        public NameValuePair(String identifier, LiteralExpression literalExpression) {
            this.identifier = identifier;
            this.literalExpression = literalExpression;
        }

        public NameValuePair(String identifier, ArrayList<LiteralExpression> arrayInitializerMemberValue) {
            this.identifier = identifier;
            this.arrayInitializerMemberValue = arrayInitializerMemberValue;
        }

        @Override
        public String toString() {
            return "NameValuePair{" +
                    "identifier='" + identifier + '\'' +
                    ", literalExpression=" + literalExpression +
                    ", arrayInitializerMemberValue=" + arrayInitializerMemberValue +
                    '}';
        }
    }

    public static class Annotation {
        @Getter
        private final String codeReferenceElement;
        @Getter
        private final List<NameValuePair> annotationParameterList = new ArrayList<>();

        public Annotation(String javaCodeReferenceElement) {
            this.codeReferenceElement = javaCodeReferenceElement;
        }

        @Override
        public String toString() {
            return "Attribute{" +
                    "codeReferenceElement='" + codeReferenceElement + '\'' +
                    ", annotationParameterList=" + annotationParameterList +
                    '}';
        }
    }

    public Annotation getAttribute() {
        if (psiFiled.getAnnotations().length == 0) {
            return null;
        }
        PsiAnnotation annotation = psiFiled.getAnnotations()[0];

        String javaCodeReference = Arrays.asList(annotation.getChildren()).stream()
                .filter(psiElement -> PsiJavaCodeReferenceElement.class.isInstance(psiElement))
                .findFirst().get().getText();
        Annotation attribute = new Annotation(javaCodeReference);

        List<PsiNameValuePair> psiNameValuePairs = Arrays.asList(annotation.getParameterList().getAttributes());
        psiNameValuePairs.stream().forEach(
                psiNameValuePair -> {
                    String identifier = psiNameValuePair.getName();
                    List<PsiElement> psiElements = Arrays.asList(psiNameValuePair.getChildren());

                    PsiLiteralExpression psiLiteralExpression = (PsiLiteralExpression) psiElements.stream()
                            .filter(psiElement -> PsiLiteralExpression.class.isInstance(psiElement))
                            .findFirst().orElse(null);

                    if (psiLiteralExpression != null) {
                        Object value = psiLiteralExpression.getValue();
                        attribute.getAnnotationParameterList().add(
                                new NameValuePair(identifier, new LiteralExpression(value)));
                    }

                    PsiArrayInitializerMemberValue psiArrayInitializerMemberValue =
                            (PsiArrayInitializerMemberValue) psiElements.stream()
                                    .filter(psiElement -> PsiArrayInitializerMemberValue.class.isInstance(psiElement))
                                    .findFirst().orElse(null);

                    if (psiArrayInitializerMemberValue != null) {

                        ArrayList<LiteralExpression> arrayInitializerMemberValue = new ArrayList<>();

                        Arrays.asList(psiArrayInitializerMemberValue.getInitializers())
                                .forEach(psiAnnotationMemberValue -> {
                                    if (PsiAnnotation.class.isInstance(psiAnnotationMemberValue)) {
                                        LiteralExpression literalExpression = new LiteralExpression(null);
                                        arrayInitializerMemberValue.add(literalExpression);
                                    }
                                    if (PsiLiteralExpression.class.isInstance(psiAnnotationMemberValue)) {
                                        Object value = ((PsiLiteralExpression) psiAnnotationMemberValue).getValue();
                                        LiteralExpression literalExpression = new LiteralExpression(value);
                                        arrayInitializerMemberValue.add(literalExpression);
                                    }
                                });

                        attribute.getAnnotationParameterList().add(
                                new NameValuePair(identifier, arrayInitializerMemberValue));
                    }
                }
        );
        return attribute;
    }
}
