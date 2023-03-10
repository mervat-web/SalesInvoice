
package sales.app.model;

import java.util.ArrayList;


public class InvoiceHeader {
     private int invoiceNum;
	private String  invoiceDate;
	private String customerName;
	private ArrayList<InvoiceLine> lines;

    public InvoiceHeader() {
    }

    public InvoiceHeader(int invoiceNum, String invoiceDate, String customerName) {
        this.invoiceNum = invoiceNum;
        this.invoiceDate = invoiceDate;
        this.customerName = customerName;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public int getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(int invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public ArrayList<InvoiceLine> getLines() {
        // Check if Items is null 
        if(lines==null){
            lines=new ArrayList<>();
      
        }
        return lines;
    }

    @Override
    public String toString() {
        return "InvoiceHeader{" + "invoiceNum=" + invoiceNum + ", invoiceDate=" + invoiceDate + ", customerName=" + customerName + '}';
    }

    public double getTotal(){
        double total=0.0;
        for (InvoiceLine  line:getLines()){
            total+=line.getTotal();
        }
        return total;
    } 
        
    public String getAsCSV() {
        return invoiceNum + "," + invoiceDate + "," + customerName;
    } 
}
