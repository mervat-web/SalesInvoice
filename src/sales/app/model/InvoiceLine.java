

package sales.app.model;


public class InvoiceLine {
    
     private String item;
	private double price;
	private int count;
        private InvoiceHeader inv;

    public InvoiceLine() {
    }

    public InvoiceLine(String item, double price, int count ,InvoiceHeader inv) {
        this.item = item;
        this.price = price;
        this.count = count;
        this.inv = inv;
    }
//Provide Setter and Getter For Variables To Enable Access ouside Class
    public InvoiceHeader getInv() {
        return inv;
    }

    public void setInv(InvoiceHeader inv) {
        this.inv = inv;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "InvoiceLine{" + "item=" + item + ", price=" + price + ", count=" + count + '}';
    }

   
 
        
    public double getTotal(){
        double total=count*price;
        return total;
    }   
    public String getAsCSV() {
        return inv.getInvoiceNum()+ "," + item + "," + price + "," + count;
    }
}
