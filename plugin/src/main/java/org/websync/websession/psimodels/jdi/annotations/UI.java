package org.websync.websession.psimodels.jdi.annotations;

import org.websync.websession.psimodels.PsiComponentInstance;

import java.util.List;

public class UI {
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
