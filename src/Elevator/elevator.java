package Elevator;

import java.util.ArrayList;
public class elevator {

    private int elevator_id;
    private int currentFloor;
    private int distinFloor;
    private int eleStatus;
    private boolean doorClosed;
    private ArrayList<Integer> workList;
    private int workListTime;
    private elevatorTime eleTimer;
    private Thread t = new Thread(() -> {
        try {
            while (this.workListTime > 0) {
                Thread.sleep(1000);
                this.workListTime--;
            }
        } catch(InterruptedException ie){
            System.out.println(ie);
        }
    });

    public elevator(){
        this.eleTimer = new elevatorTime();
      //  this.eleTimer = new elevatorTime("aaa.cfg");
    }

    public void openDoor(){
        if(this.doorClosed!=false)
            this.doorClosed = false;
    }

    public void closeDoor(){
        if(this.doorClosed!=true)
            this.doorClosed = true;
    }

    public void waitDoor(){
        try {
            Thread.sleep((int)eleTimer.DoorWait*1000);
        }
        catch(InterruptedException ie){
            System.out.println(ie);
        }
    }

    public void checkWorkList(){
        if(workList.size()<0){
            setDistinFloor(0);
        }else{
            setDistinFloor(workList.get(0));
            workList.remove(0);
        }
    }

    public boolean checkFloor(){
        if(distinFloor==currentFloor)
                return true;
            else
                return false;
    }

    public boolean existedFloor(int floor){
        if(workList.indexOf(floor)>0)
            return true;
        else
            return false;
    }


    public boolean addFloorToWorkList(int floor){
        if(workList.size()<0){
            setDistinFloor(floor);
            if(countWaitTime(this.currentFloor,this.distinFloor)){
                if(t.isAlive()!=true)
                    t.start();
                return true;
            }else{
                System.out.println("Fail when add Floor");
                return false;
            }
        }else{
            if(countWaitTime(workList.get(workList.size()),floor)){
                if(t.isAlive()!=true)
                    t.start();
                workList.add(floor);
                return true;
            }{
                System.out.println("Fail when add Floor");
                return false;
            }
        }
    }

    public boolean countWaitTime(int curren,int distin){
        if(curren>distin){        //down
            int upFloorTime = Math.round(eleTimer.UpOneFloor*(curren-distin));
            this.workListTime += Math.round(1000*(eleTimer.DoorClose+
                    eleTimer.AccDown+eleTimer.DecDown+eleTimer.DoorOpen+
                    eleTimer.DoorWait+upFloorTime));
            // start the thread

            return true;
        }else if(curren<distin){   //up
            int downFloorTime = Math.round(eleTimer.DownOneFloor*(distin-curren));
            this.workListTime += Math.round(1000*(eleTimer.DoorClose+
                    eleTimer.AccUp+eleTimer.DecUp+eleTimer.DoorOpen+
                    eleTimer.DoorWait+downFloorTime));
            return true;
        }else{
            System.out.println("Current floor is the requested floor");
            return false;
        }
    }

    public void resetWorkListTime(){
        this.workListTime = 0;
    }
    public int getElevator_id() {
        return elevator_id;
    }

    public void setElevator_id(int elevator_id) {
        this.elevator_id = elevator_id;
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

    public int getEleStatus() {
        return eleStatus;
    }

    public void setEleStatus(int eleStatus) {
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
