package AppKickstarter.myThreads;

import AppKickstarter.Elevator.Elevator;
import AppKickstarter.misc.*;
import AppKickstarter.AppKickstarter;
import AppKickstarter.timer.Timer;
import GUI.Elevator_panel;
import json.JSONObject;
import java.awt.EventQueue;
import java.util.ArrayList;

public class Thread_Elevator_Panel extends AppThread{
    private final int sleepTime = 5;
    public Elevator elevator;
    public Elevator_panel window;
    public Thread_Elevator_Panel(String id, AppKickstarter appKickstarter) {
        super(id, appKickstarter);
        //elevator = new Elevator(Character.getNumericValue(id.charAt(id.length() - 1)),log);

    }

    public void run(){
        log.info(id + ": starting...");
        Timer.setSimulationTimer(id, mbox,sleepTime);
        String msgToServer;
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    elevator = new Elevator(Character.getNumericValue(id.charAt(id.length() - 1)),log);
                    window = new Elevator_panel(elevator);
                    window.getframe().setVisible(true);
                    elevator.setElevator_panel(window);
                    elevator.setmBox(mbox);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        for (boolean quit = false; !quit;) {
            ArrayList<Integer> worklist ;
            JSONObject res ,req;
            Msg msg = mbox.receive();

            // log.info(id + ": message received: [" + msg + "].");

            switch (msg.getType()) {
                case Elev_Arr_FromElev:
                        res = msg.getDetails();
                        AppThread thdARR = appKickstarter.getThread("Thread_Server"); //thread 1,2,3,4,5
                        MBox thdARRMBox = thdARR.getMBox();
                       // System.out.println(" ARR "+res.toString());
                        thdARRMBox.send(new Msg(id, mbox, Msg.Type.Elev_Arr, res,null));
                        Timer.setSimulationTimer(id, mbox, sleepTime);
                    break;

                    case Elev_Dep_FromElev:
                        res = msg.getDetails();
                        AppThread thdDEP = appKickstarter.getThread("Thread_Server"); //thread 1,2,3,4,5
                        MBox thdDEPMBox = thdDEP.getMBox();
                       // System.out.println(" DEP "+res.toString());
                        thdDEPMBox.send(new Msg(id, mbox, Msg.Type.Elev_Dep, res,null));
                        Timer.setSimulationTimer(id, mbox, sleepTime);
                    break;


                case Elev_Job:
                    log.info(id + ": [" + msg.getSender() + "]: message received: [" + msg + "] ");
                    req = msg.getDetails();
                    worklist = new ArrayList<Integer>();
                    int dstFNO = Integer.parseInt(req.get("dstFNO").toString());

                    // process in elev class
                    if(!elevator.existedFloor(dstFNO)&&elevator.getCurrentFloor()!=dstFNO)
                    elevator.addFloorToWorkList(dstFNO);

                    res = new JSONObject();
                    res.put("LNO",elevator.getElevator_id());
                    res.put("Current_Floor",elevator.getCurrentFloor());       //current_floor
                    res.put("Dir",elevator.getWorkList().get(0));       //Direction 0 to up, 1 to down
                    res.put("Work_List",elevator.getWorkList());

                    // reply to server
                    log.info(id + ": send request Elev_Reply to Thread_Server");

                    log.info(res + ": Content");
                    AppThread thdK = appKickstarter.getThread(msg.getSender()); //thread 1,2,3,4,5
                    MBox thdKioskMBox = thdK.getMBox();
                    thdKioskMBox.send(new Msg(id, mbox, Msg.Type.Elev_Reply, res,null));

                    break;

                case Terminate:
                    quit = true;
                    break;
                case TimesUp:
                    break;

                default:
                    log.severe(id + ": unknown message type!!");
                    log.info(""+msg.getType());
                    break;
            }
        }

        // declaring our departure
        appKickstarter.unregThread(this);
        log.info(id + ": terminating...");
    }
}
