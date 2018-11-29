package AppKickstarter.myThreads;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import json.JSONObject;

public class Admin_Panel_UI {
    JFrame f;
    Thread_Central_Control_Panel thread_ccp;

    public Admin_Panel_UI(){}

    public Admin_Panel_UI(Thread_Central_Control_Panel thread_ccp) {

        this.thread_ccp = thread_ccp;

//        thread_ccp.print("[Thread_Central_Control_Panel] from Admin_Panel_UI: Connected");

        // Setup JFrame
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        f = new JFrame("Central Control Panel");
        f.setSize(400, 300);
        f.setLocationRelativeTo(null);
        Container cp = f.getContentPane();
        // cp.setLayout(null);

        // Build Elements
        MyTableModel mtm = new MyTableModel();
        
        JTable jt = new JTable(mtm);
        jt.setPreferredScrollableViewportSize(new Dimension(400, 300));

        CheckBoxModelListener cbml = new CheckBoxModelListener(thread_ccp);

        jt.getModel().addTableModelListener(cbml);

        // JComboBox jcb = new JComboBox();
        // jcb.addItem("O");
        // jcb.addItem("A");
        // jcb.addItem("B");
        // jcb.addItem("AB");
        // TableColumn column = jt.getColumnModel().getColumn(5);
        // column.setCellEditor(new DefaultCellEditor(jcb));
        cp.add(new JScrollPane(jt), BorderLayout.CENTER);
        f.setVisible(true);

        // Close JFrame
        f.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        f.addWindowListener(new WindowHandler(f));

        thread_ccp.setMTM(mtm);

        mtm.setValueAt("Updated", 0, 0);
    }
}

class CheckBoxModelListener implements TableModelListener {
    Thread_Central_Control_Panel thread_ccp;

    public CheckBoxModelListener(Thread_Central_Control_Panel thread_ccp){
        this.thread_ccp = thread_ccp;
    }

    public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int column = e.getColumn();
        if (column == 3) {
            TableModel model = (TableModel) e.getSource();
            String columnName = model.getColumnName(column);
            Boolean checked = (Boolean) model.getValueAt(row, column);

            JSONObject res = new JSONObject();

            if (checked) {
                System.out.println("LNO: " + (row+1) + ", " + columnName + ": " + "START");

                res.put("LNO", (model.getValueAt(row, 0)));
                res.put("Status", new Integer(11));

                thread_ccp.setStatus(res);
            } else {
                System.out.println("LNO: " + (row+1) + ", " + columnName + ": " + "STOP");

                res.put("LNO", (model.getValueAt(row, 0)));
                res.put("Status", new Integer(12));

                thread_ccp.setStatus(res);
            }
        }
        
    }
}

class WindowHandler extends WindowAdapter {
    JFrame f;

    public WindowHandler(JFrame f) {
        this.f = f;
    }

    public void windowClosing(WindowEvent e) {
        int result = JOptionPane.showConfirmDialog(f, "確定要結束程式嗎?", "確認訊息", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}

class MyTableModel extends AbstractTableModel {
    Object[][] data = { { "1", "1", "2", false, "", "O" },
            { "2", "1", "3", true, "", "O" } ,{ "1", "1", "2", false, "", "O" },
            { "2", "1", "3", true, "", "O" } ,{ "1", "1", "2", false, "", "O" },{ "1", "1", "2", false, "", "O" },
           };

    String[] columns = { "Lift No.", "Current Floor", "Next Floor", "Status", "x", "y" };

    public int getColumnCount() {
        return columns.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    public String getColumnName(int col) {
        return columns[col];
    }

    public Class getColumnClass(int col) {
        return getValueAt(0, col).getClass();
    }

    public boolean isCellEditable(int row, int col) {
        return true;
    }

    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }
}