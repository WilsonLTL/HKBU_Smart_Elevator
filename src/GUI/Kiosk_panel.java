package GUI;

import javax.swing.*;

public class Kiosk_panel {
    private JTextField FLOOR_LABEL;
    private JButton N7_BTN;
    private JButton N8_BTN;
    private JButton N9_BTN;
    private JButton N4_BTN;
    private JButton N5_BTN;
    private JButton N6_BTN;
    private JButton N1_BTN;
    private JButton N2_BTN;
    private JButton N3_BTN;
    private JButton N0_BTN;
    private JButton CLEAR_BTN;
    private JButton SUBMIT_BTN;
    private JLabel KIOSK_PANEL_LABEL;


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    private JPanel panel;
    protected int scrFNo;
    protected int dstFNo;
    protected int LNo;


    public void setScrFNo(int scrFNo) {
        this.scrFNo = scrFNo;
    }

    public int getScrFNo() {
        return scrFNo;
    }

    public void setDstFNo(int dstFNo) {
        this.dstFNo = dstFNo;
    }

    public int getDstFNo() {
        return dstFNo;
    }

    public void setLNo(int LNo) {
        this.LNo = LNo;
    }

    public int getLNo() {
        return LNo;
    }

    public Kiosk_panel() {

        this.dstFNo = -1;
        this.LNo = -1;

        panel = new JPanel();
        panel.setVisible(true);

//        while (panel.setVisible(true)) {
//            if (dstFNo != -1) {
//                if (LNo != -1) {
//                    FLOOR_LABEL.replaceSelection(String.valueOf(this.LNo));
//                    reset();
//                } else {
//                    FLOOR_LABEL.replaceSelection("Please wait...");
//                }
//            } else{
//                FLOOR_LABEL.replaceSelection("Please enter the floor: ");
//            }
//        }
    }

    public void reset() {
        this.dstFNo = -1;
        this.LNo = -1;
    }

    public static void main(String[] args) {

        Kiosk_panel kiosk_panel = new Kiosk_panel();
    }
}
