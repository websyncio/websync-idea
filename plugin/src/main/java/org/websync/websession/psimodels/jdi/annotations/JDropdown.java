package org.websync.websession.psimodels.jdi.annotations;

public class JDropdown {
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
