package AppKickstarter.Elevator;

import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;
import GUI.Elevator_panel;
import json.JSONObject;

import java.util.ArrayList;

import java.util.logging.Logger;

public class Elevator {
public Logger log ;
    private int elevator_id;
    private int currentFloor;
    private int distinFloor;
    private String eleStatus;
    private boolean doorClosed;
    private ArrayList<Integer> workList;
    private static ElevatorTime eleTimer = new ElevatorTime("etc/SmartElevator.cfg");
    private Elevator_panel elevator_panel;
    private MBox mBox;
    private Thread t = new Thread(() -> {
        try {
            JSONObject res = new JSONObject();
            while (this.workList.size() > 0) {
                    elevator_panel.getNEXT_ARRIVE_FLOOR_NUM().setText(printWorkList());

                    closeDoor();
                    updateElevator_panel("Door Close");
                    res.put("LNO",elevator_id);
                    res.put("Current_Floor",currentFloor);       //current_floor
                    res.put("Dir",workList.get(0));       //Direction 0 to up, 1 to down
                    res.put("Work_List",workList);
                    mBox.send(new Msg("Thread_Elevator_Panel"+elevator_id, mBox, Msg.Type.Elev_Dep_FromElev, res,null));
                    Thread.sleep(ElevatorTime.getDoorClose());

                    setDistinFloor(this.workList.get(0));
                    while (!checkFloor()) {
                        if (isArr()) {
                            updateElevator_panel("Going Up");
                            Thread.sleep(ElevatorTime.getAccUp());
                            this.currentFloor++;
                        } else {
                            updateElevator_panel("Going Down");
                            Thread.sleep(ElevatorTime.getAccDown());
                            this.currentFloor--;
                        }
                    }
                    this.workList.remove(0);
                    updateElevator_panel();
                    openDoor();
                    updateElevator_panel("Door Opening");
                    res.put("LNO",elevator_id);
                    res.put("Current_Floor",currentFloor);       //current_floor
                    mBox.send(new Msg("Thread_Elevator_Panel"+elevator_id, mBox, Msg.Type.Elev_Arr_FromElev, res,null));
                    Thread.sleep(ElevatorTime.getDoorOpen());
                    waitDoor();
                    updateElevator_panel("Waiting");
                    Thread.sleep(ElevatorTime.getDoorWait());
                    updateElevator_panel();
                    if (this.workList.size() > 0) {
                        setDistinFloor(this.workList.get(0));
                        res.put("Dir",workList.get(0));       //Direction 0 to up, 1 to down
                        res.put("Work_List",workList);
                        mBox.send(new Msg("Thread_Elevator_Panel"+elevator_id, mBox, Msg.Type.Elev_Arr_FromElev, res,null));

                    }
                    while (!(this.workList.size() > 0)) {
                        Thread.sleep(100);
                        updateElevator_panel("Waiting [No job]");
                    }
                }

        } catch(Exception ie){
            System.out.println(ie);
        }
    });

    public Elevator(int id, Logger log){
        this.eleStatus = "free";
        this.currentFloor = 0;
        this.doorClosed = false;
        this.elevator_id = id;
        this.workList = new ArrayList<Integer>();
        this.log = log;
    }

    public void openDoor(){
        if(this.doorClosed!=false)
            this.doorClosed = false;
    }

    public void closeDoor(){
        //log.info("Door Closed");

        if(this.doorClosed!=true)
            this.doorClosed = true;
    }

    public void setElevator_panel(Elevator_panel elevator_panel) {
        this.elevator_panel = elevator_panel;
        updateElevator_panel();
    }
    private void updateElevator_panel(String eStatus) {
        updateElevator_panel();
        this.elevator_panel.getELEVATOR_STATUS().setText(eStatus);

    } private void updateElevator_panel() {
        this.elevator_panel.getCURRENT_FLOOR_NUM2().setText(""+this.currentFloor);
        this.elevator_panel.getNEXT_ARRIVE_FLOOR_NUM().setText(""+this.workList);

    }




    public void waitDoor(){

    }

    public void setmBox(MBox mBox) {
        this.mBox = mBox;
    }

    public boolean checkFloor(){
        return this.distinFloor == this.currentFloor;
    }

    public String printWorkList(){
        String res = "";
        for(int i = 0 ; i < this.workList.size(); i++)
        res +=" [ "+i+" ] "+ this.workList.get(i)+" ";
        return res;
    }

    public boolean existedFloor(int floor){
        return workList.indexOf(floor) > 0;
    }


    public void addFloorToWorkList(int floor){
        if (this.workList.size() > 0) {
            String logg = "  Elevator : "+elevator_id+
                    " ||  WorkList Size : "+this.workList.size()+" ||  Current Floor : "+this.currentFloor;
            for(int i = 0 ; i < this.workList.size();i++){
             logg +=  "   "+i+")"+this.workList.get(i);
            }
            if(this.elevator_id ==1)
            log.info(logg);
           // this.workList.add(floor);
        }else{
            if(this.elevator_id ==1)
            log.info("  Elevator : "+elevator_id+
                    " ||  WorkList Size : "+this.workList.size()+" ||  Current Floor : "+this.currentFloor+
                    " ||  First Dis  : "+floor);
        }
        this.workList.add(floor);
        if(t.getState() == Thread.State.NEW)
            t.start();
    }


    public boolean isArr(){
        return workList.get(0) > currentFloor;
    }

     public int getElevator_id() {
        return elevator_id;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getDistinFloor() {
        return distinFloor;
    }

    public void setDistinFloor(int distinFloor) {
        this.distinFloor = distinFloor;
    }

    public String getEleStatus() {
        return eleStatus;
    }

    public void setEleStatus(String eleStatus) {
        this.eleStatus = eleStatus;
    }

    public boolean isDoorClosed() {
        return doorClosed;
    }

    public void setDoorClosed(boolean doorClosed) {
        this.doorClosed = doorClosed;
    }

    public ArrayList<Integer> getWorkList() {
        return workList;
    }
}
