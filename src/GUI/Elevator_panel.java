package GUI;

import AppKickstarter.Elevator.Elevator;

import javax.swing.*;

public class Elevator_panel {
    private JFrame frame;
    private JLabel ELEVATOR_PANEL_LABEL;
    private JTextField CURRENT_FLOOR_NUM2;
    private JLabel CURRENT_FLOOR_LABEL;
    private JLabel NEXT_ARRIVE_FLOOR_LABEL;
    private JTextField NEXT_ARRIVE_FLOOR_NUM;
    private JLabel ELEVATOR_STATUS_LABEL;
    private JTextField ELEVATOR_STATUS;
    private JButton OPEN_BTN;
    private JButton CLOSE_BTN;

    public JFrame getframe(){
        return this.frame;
    }

    public Elevator_panel(Elevator elevator){
        createUIComponents(elevator);
    }

    private void createUIComponents(Elevator elevator) {
        // TODO: place custom component creation code here
        initForm(elevator);
    }
    private void initForm(Elevator elevator) {
        frame = new JFrame("Elevator : "+elevator.getElevator_id());
        frame.setBounds(300, 200, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        CURRENT_FLOOR_LABEL = new JLabel("Current Floor: ");
        CURRENT_FLOOR_LABEL.setBounds(40, 65, 120, 25);
        frame.getContentPane().add(CURRENT_FLOOR_LABEL);

        CURRENT_FLOOR_NUM2 = new JTextField(elevator.getCurrentFloor());
        CURRENT_FLOOR_NUM2.setBounds(180, 65, 120, 25);
        frame.getContentPane().add(CURRENT_FLOOR_NUM2);
        CURRENT_FLOOR_NUM2.setColumns(10);

        NEXT_ARRIVE_FLOOR_LABEL = new JLabel("Next Arrive Floor: ");
        NEXT_ARRIVE_FLOOR_LABEL.setBounds(40, 118, 120, 25);
        frame.getContentPane().add(NEXT_ARRIVE_FLOOR_LABEL);

        NEXT_ARRIVE_FLOOR_NUM = new JTextField();
        NEXT_ARRIVE_FLOOR_NUM.setBounds(180, 118, 120, 25);
        frame.getContentPane().add(NEXT_ARRIVE_FLOOR_NUM);
        NEXT_ARRIVE_FLOOR_NUM.setColumns(10);

        ELEVATOR_STATUS_LABEL = new JLabel("Elevator Status: ");
        ELEVATOR_STATUS_LABEL.setBounds(40, 160, 120, 25);
        frame.getContentPane().add(ELEVATOR_STATUS_LABEL);

        ELEVATOR_STATUS = new JTextField();
        ELEVATOR_STATUS.setBounds(180, 160, 120, 25);
        frame.getContentPane().add(ELEVATOR_STATUS);
        ELEVATOR_STATUS.setColumns(10);





    }

    public JTextField getELEVATOR_STATUS() {
        return ELEVATOR_STATUS;
    }

    public JTextField getCURRENT_FLOOR_NUM2() {
        return CURRENT_FLOOR_NUM2;
    }

    public JTextField getNEXT_ARRIVE_FLOOR_NUM() {
        return NEXT_ARRIVE_FLOOR_NUM;
    }

//    public void setCURRENT_FLOOR_NUM2(JTextField CURRENT_FLOOR_NUM2) {
//        this.CURRENT_FLOOR_NUM2 = CURRENT_FLOOR_NUM2;
//    }
//
//    public void setNEXT_ARRIVE_FLOOR_NUM(JTextField NEXT_ARRIVE_FLOOR_NUM) {
//        this.NEXT_ARRIVE_FLOOR_NUM = NEXT_ARRIVE_FLOOR_NUM;
//    }
}
