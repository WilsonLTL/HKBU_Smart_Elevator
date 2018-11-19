package AppKickstarter.myThreads;

import AppKickstarter.misc.*;
import AppKickstarter.AppKickstarter;
import AppKickstarter.timer.Timer;
import json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Thread_Elevator_Panel extends AppThread{
    private final int sleepTime = 5;
    public Thread_Elevator_Panel(String id, AppKickstarter appKickstarter) {
        super(id, appKickstarter);
    }
    public void run(){
        log.info(id + ": starting...");
        Timer.setSimulationTimer(id, mbox,sleepTime);
        String msgToServer;

        for (boolean quit = false; !quit;) {
            ArrayList<Integer> worklist ;
            JSONObject res ,req;
            Msg msg = mbox.receive();

            // log.info(id + ": message received: [" + msg + "].");

            switch (msg.getType()) {
                case TimesUp:
                    log.info(id + ": receiving timesup at " + appKickstarter.getSimulationTimeStr());
                    res = new JSONObject();
                    worklist = new ArrayList<Integer>();
                    res.put("LNO",1);
                    res.put("Current_Floor",1);       //current_floor
                    res.put("Dir",0);       //Direction 0 to up, 1 to down
                    res.put("Work_List",worklist);

                    // check elev arr or dep
                    //if arr
                        log.info(id + ": send request Elev_Arr to Thread_Server");
                        AppThread thdARR = appKickstarter.getThread("Thread_Server"); //thread 1,2,3,4,5
                        MBox thdARRMBox = thdARR.getMBox();
                        thdARRMBox.send(new Msg(id, mbox, Msg.Type.Elev_Arr, res,null));

                    //else if dep
                        log.info(id + ": send request Elev_Dep to Thread_Server");
                        AppThread thdDEP = appKickstarter.getThread("Thread_Server"); //thread 1,2,3,4,5
                        MBox thdDEPMBox = thdDEP.getMBox();
                        thdDEPMBox.send(new Msg(id, mbox, Msg.Type.Elev_Dep, res,null));
                    Timer.setSimulationTimer(id, mbox, sleepTime);
                    break;


                case Elev_Job:
                    log.info(id + ": [" + msg.getSender() + "]: message received: [" + msg + "] ");
                    req = msg.getDetails();
                    worklist = new ArrayList<Integer>();
                    int dstFNO = Integer.parseInt(req.get("dstFNO").toString());

                    // process in elev class

                    res = new JSONObject();
                    res.put("LNO",1);
                    res.put("Current_Floor",1);       //current_floor
                    res.put("Dir",0);       //Direction 0 to up, 1 to down
                    res.put("Work_List",worklist);

                    // reply to server
                    log.info(id + ": send request Elev_Reply to Thread_Server");
                    AppThread thdK = appKickstarter.getThread(msg.getSender()); //thread 1,2,3,4,5
                    MBox thdKioskMBox = thdK.getMBox();
                    thdKioskMBox.send(new Msg(id, mbox, Msg.Type.Elev_Reply, res,null));
                    break;

                case Terminate:
                    quit = true;
                    break;

                default:
                    log.severe(id + ": unknown message type!!");
                    break;
            }
        }

        // declaring our departure
        appKickstarter.unregThread(this);
        log.info(id + ": terminating...");
    }
}
