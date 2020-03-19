package org.websync.websession.psimodels.jdi.annotations;

import java.util.List;

public class JTable {
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
