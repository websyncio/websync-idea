package org.websync.websession.psimodels.jdi.annotations;

import java.util.List;

public class JMenu {
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
