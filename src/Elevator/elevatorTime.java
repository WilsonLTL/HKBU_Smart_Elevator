package Elevator;

public class elevatorTime {

    public float UpOneFloor;
    public float DownOneFloor;
    public float AccUp;
    public float AccDown;
    public float DecUp;
    public float DecDown;
    public float DoorOpen;
    public float DoorClose;
    public float DoorWait;

//    public elevatorTime(string filename) {
//        //open file
//
//        UpOneFloor = file.upOneFloor;
//        DownOneFloor = file.downOneFloor;
//        AccUp = file.accUp;
//        AccDown = file.accDown;
//        DecUp = file.decUp;
//        DecDown = file.decDown;
//        DoorOpen = file.doorOpen;
//        DoorClose = file.doorClose;
//        DoorWait = file.doorWait;
//    }
public elevatorTime(int upOneFloor, int downOneFloor, int accUp, int accDown, int decUp, int decDown, int doorOpen, int doorClose, int doorWait) {
    this.UpOneFloor = upOneFloor;
    this.DownOneFloor = downOneFloor;
    this.AccUp = accUp;
    this.AccDown = accDown;
    this.DecUp = decUp;
    this.DecDown = decDown;
    this.DoorOpen = doorOpen;
    this.DoorClose = doorClose;
    this.DoorWait = doorWait;
}
    public elevatorTime() {
        this.UpOneFloor = 0.6f;
        this.DownOneFloor = 0.5f;
        this.AccUp = 1.2f;
        this.AccDown = 1f;
        this.DecUp = 1.2f;
        this.DecDown = 1f;
        this.DoorOpen = 1f;
        this.DoorClose = 1.5f;
        this.DoorWait = 5;
    }

}
