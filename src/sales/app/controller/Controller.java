
package sales.app.controller;

import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import sales.app.model.HeadersTableModel;
import sales.app.model.InvoiceHeader;
import sales.app.model.InvoiceLine;
import sales.app.model.LinesTableModel;
import sales.app.view.AppGui;
import sales.app.view.InvoiceHeaderDialog;
import sales.app.view.ItemDialog;


public class Controller implements ActionListener  , ListSelectionListener{

     private AppGui frame;
    
       private InvoiceHeaderDialog invDialog;
       private ItemDialog lineDialog;
       private HeadersTableModel invModel;
     public Controller( AppGui frame){
         this.frame=frame;
     }
    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        System.out.println("Action Taken Is : " + actionCommand);
        
        
        
        
        
         switch (actionCommand) {
            case "Load Files":
                loadFile();
                break;
            case "Save Files":
                saveFile();
                break;
            case "Create Invoice":
                createNewInvoice();
                break;
            case "Delete Invoice":
                deleteInvoice();
                break;
            case "Add Item":
                createNewItem();
                break;
            case "Delete Item":
                deleteItem();
                break;
                case "createInvoiceOK":
               createInvoiceOK();
                break;
            case "createInvoiceCancel":
              cancelCreateInvoice();
                break;
                
              case "createLineOK":
             createLineOk();
                break;
            case "createLineCancel":
              createLineCancel();
                break;
                
               
               
           
        }
     
       
    }
    
    
      @Override
    public void valueChanged(ListSelectionEvent e) {
        System.out.println("Data Display of Selected Row");
         int selectedIndex =  frame.getInvoiceTable().getSelectedRow();
        if (selectedIndex != -1) {
            InvoiceHeader currentInvoice = frame.getInvoices().get(selectedIndex);
            frame.getInvoiceNumLbl().setText("" + currentInvoice.getInvoiceNum());
            frame.getInvoiceDateLbl().setText(currentInvoice.getInvoiceDate());
            frame.getCustomerLbl().setText(currentInvoice.getCustomerName());
            frame.getTotalLbl().setText("" + currentInvoice.getTotal());
            LinesTableModel linesTableModel = new LinesTableModel(currentInvoice.getLines());
            frame.getItemTable().setModel(linesTableModel);
            linesTableModel.fireTableDataChanged();
       
    }
    }

    private void loadFile() {
        
        //This To Open Window To Select File 
       JFileChooser fc = new JFileChooser();
        try {
            int result = fc.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fc.getSelectedFile();
                Path headerPath = Paths.get(headerFile.getAbsolutePath());
                 
               ArrayList <String> headerLines= (ArrayList <String>) Files.readAllLines(headerPath);
                System.out.println("Invoices have been read Successfully");
               //make Loop into array of Data Of Invoices and defin each Variable By Split Each row 
                ArrayList<InvoiceHeader> invoicesArray = new ArrayList<>();
                for (String headerLine : headerLines) {
                    try {
                        String[] headerParts = headerLine.split(",");
                        int invoiceNum = Integer.parseInt(headerParts[0]);
                        String invoiceDate = headerParts[1];
                        String customerName = headerParts[2];

                        InvoiceHeader invoice = new InvoiceHeader(invoiceNum, invoiceDate, customerName);
                        invoicesArray.add(invoice);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Error in line format", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
               //open Dialog Second Time to Upload Items File
                result = fc.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File lineFile = fc.getSelectedFile();
                    Path linePath = Paths.get(lineFile.getAbsolutePath());
                    ArrayList <String> lineLines = (ArrayList <String>) Files.readAllLines(linePath);
                    System.out.println("items have been read successfully");
                    for (String lineLine : lineLines) {
                        try {
                            String lineParts[] = lineLine.split(",");
                            int invoiceNum = Integer.parseInt(lineParts[0]);
                            String itemName = lineParts[1];
                            double itemPrice = Double.parseDouble(lineParts[2]);
                            int count = Integer.parseInt(lineParts[3]);
                            InvoiceHeader inv = null;
                            for (InvoiceHeader invoice : invoicesArray) {
                                if (invoice.getInvoiceNum() == invoiceNum) {
                                    inv = invoice;
                                    break;
                                }
                            }

                            InvoiceLine line = new InvoiceLine(itemName, itemPrice, count, inv);
                            inv.getLines().add(line);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame, "Error in line format", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    
                    
                }
                

                frame.setInvoices(invoicesArray);

                HeadersTableModel invoicesTableModel = new HeadersTableModel(invoicesArray);
                frame.setInvoiceTableModel(invoicesTableModel);
                frame.getInvoiceTable().setModel(invoicesTableModel);
                frame.getInvoiceTableModel().fireTableDataChanged();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Cannot read file", "Error", JOptionPane.ERROR_MESSAGE);
        }
            
           
       
    }
//This Method To Save Changes After Upload Files and Change it
    private void saveFile() {
        ArrayList<InvoiceHeader> invoices = frame.getInvoices();
        String headers = "";
        String lines = "";
        for (InvoiceHeader invoice : invoices) {
            String invCSV = invoice.getAsCSV();
            headers += invCSV;
            headers += "\n";

            for (InvoiceLine line : invoice.getLines()) {
                String lineCSV = line.getAsCSV();
                lines += lineCSV;
                lines += "\n";
            }
        }
        System.out.println("Save Changes Into Two Files");
        try {
            JFileChooser fc = new JFileChooser();
            int result = fc.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fc.getSelectedFile();
                FileWriter hfw = new FileWriter(headerFile);
                hfw.write(headers);
                hfw.flush();
                hfw.close();
                result = fc.showSaveDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File lineFile = fc.getSelectedFile();
                    FileWriter lfw = new FileWriter(lineFile);
                    lfw.write(lines);
                    lfw.flush();
                    lfw.close();
                }
            }
        } catch (Exception ex) {
ex.printStackTrace();
        }
    }
//Method To Open Item Dialog When User Click Create Invoice  
    private void createNewInvoice() {
        invDialog=new InvoiceHeaderDialog(frame);
        invDialog.setVisible(true);
        
        
    }
//This Method To Delete Invoice  From  List and Show Message  if User Click Delete Without Select Row 
    private void deleteInvoice() {
        int selectedRow=frame.getInvoiceTable().getSelectedRow();
       
        if (selectedRow != -1) {
            frame.getInvoices().remove(selectedRow);
            frame.getInvoiceTableModel().fireTableDataChanged();
           
            JOptionPane.showMessageDialog(frame, "Delete Done Successfully", "Success ", JOptionPane.INFORMATION_MESSAGE);
        }else{
             JOptionPane.showMessageDialog(frame, "Please Select Invoice to Be Deleted", "Error", JOptionPane.ERROR_MESSAGE);
        }
 
    }
//Method To Open Item Dialog When User Click Add Item
    private void createNewItem() {
    
         lineDialog = new ItemDialog(frame);
        lineDialog.setVisible(true);
    }
//This Method To Delete Item From Item List and Show Message  if User Click Delete Without Select Data
    private void deleteItem() {
        int selectedRow = frame.getItemTable().getSelectedRow();

        if (selectedRow != -1) {
            LinesTableModel linesTableModel = (LinesTableModel) frame.getItemTable().getModel();
            linesTableModel.getLines().remove(selectedRow);
            linesTableModel.fireTableDataChanged();
      
           frame.getInvoiceTableModel().fireTableDataChanged();
                 JOptionPane.showMessageDialog(frame, "Delete Done Successfully", "Success ", JOptionPane.INFORMATION_MESSAGE);
        }else{
             JOptionPane.showMessageDialog(frame, "Please Select Item To be Deleted", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
//Method If User Click Ok To Save Changes and Add New Invoice Also Check Date Format
    private void createInvoiceOK() {
       String date = invDialog.getInvDateField().getText();
        String customer = invDialog.getCustNameField().getText();
        int num = frame.getNextInvoiceNum();
        try {
            String[] dateParts = date.split("-")  ;  // this split date to validate it's format
            if (dateParts.length !=3) {
                JOptionPane.showMessageDialog(frame, "Please Check that Date Format must be with Three Seoerate Parts Only  ", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                int day = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);
                if (day > 31 || month > 12) {
                    JOptionPane.showMessageDialog(frame, "Invalid   Day or Month", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    
                    InvoiceHeader invoice = new InvoiceHeader(num, date, customer);
                    frame.getInvoices().add(invoice);
                    frame.getInvoiceTableModel().fireTableDataChanged();
                    invDialog.setVisible(false);
                    invDialog.dispose();
                    invDialog = null;
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }


//This Method If User Cancel Changes After Enter Invoice Data Data
    private void cancelCreateInvoice() {
        invDialog.setVisible(false);
        invDialog.dispose();
        invDialog = null;
    }
    
    
    //Method If User Click Ok To Save Changes and Add New Item It Will Check If User Didn't Select Invoice also check if Item Price or Count Is Negative Number
    private void createLineOk() {
         String item = lineDialog.getItemNameField().getText();
        String countStr = lineDialog.getItemCountField().getText();
        String priceStr = lineDialog.getItemPriceField().getText();
        int count = Integer.parseInt(countStr);
        double price = Double.parseDouble(priceStr);
        int selectedInvoice = frame.getInvoiceTable().getSelectedRow();
        //this to Check If User Didn't Select Invoice
        if (selectedInvoice != -1) {
            //this to Check if Price or Count Is Negative Numbers
            if(price<1 || count<1){
                JOptionPane.showMessageDialog(frame, "Can't Add item Please Enter Postive Item Price and Postive Count ", "Error", JOptionPane.ERROR_MESSAGE); 
            } else {
            InvoiceHeader invoice = frame.getInvoices().get(selectedInvoice);
            InvoiceLine line = new InvoiceLine(item, price, count, invoice);
            invoice.getLines().add(line);
            LinesTableModel linesTableModel = (LinesTableModel) frame.getItemTable().getModel();
          
            linesTableModel.fireTableDataChanged();
            frame.getInvoiceTableModel(). fireTableDataChanged();
        }}else {
            JOptionPane.showMessageDialog(frame, "Can't Add Data Please Upload Files If Not Exist and Select Invoice to add Items in ", "Error", JOptionPane.ERROR_MESSAGE);
        }
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
    }
//This Method If User Cancel Changes After Enter Item Data
    private void createLineCancel() {
           lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
    }

    
    
}
