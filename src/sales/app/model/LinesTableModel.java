
package sales.app.model;

import static java.nio.file.Files.lines;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;


public class LinesTableModel  extends AbstractTableModel{
    private ArrayList<InvoiceLine> lines;
    private String[] columns = {"No.", "Item Name", "Item Price", "Count", "Item Total"};

    public LinesTableModel(ArrayList<InvoiceLine> lines) {
        this.lines = lines;
    }

    public ArrayList<InvoiceLine> getLines() {
        return lines;
    }
    
    @Override
    public int getRowCount() {
         return lines.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

        @Override
    public String getColumnName(int index) {
        return columns[index];
    }
     @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceLine line = lines.get(rowIndex);
     
        switch(columnIndex) {
            case 0: return line.getInv().getInvoiceNum();
            case 1: return line.getItem();
            case 2: return line.getPrice();
            case 3: return line.getCount();
            case 4: return line.getTotal();
            default : return "";
        }
   
    }
    
    
}
