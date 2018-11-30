package AppKickstarter.myThreads;

import AppKickstarter.misc.*;
import AppKickstarter.AppKickstarter;
import AppKickstarter.timer.Timer;
import json.JSONObject;

public class Thread_kiosk_panel extends AppThread {
    private final int sleepTime = 5;
    public Thread_kiosk_panel(String id, AppKickstarter appKickstarter) {
        super(id, appKickstarter);
    } // AppKickstarter.myThreads.Thread_Central_Control_Panel

    public void run() {
        log.info(id + ": starting...");
        Timer.setSimulationTimer(id, mbox,sleepTime);
        String msgToServer;


        for (boolean quit = false; !quit;) {
            Msg msg = mbox.receive();

            switch (msg.getType()) {
                case TimesUp:
                    log.info(id + ": receiving timesup at " + appKickstarter.getSimulationTimeStr());

                    // check is there are any new request send to server
                    if(true){
                        JSONObject req = new JSONObject();
                        req.put("PID","Passenger-XXXX"); //the id of the kiosk panel, xxxx is a 4 digit number
                        req.put("srcFNO",1); // current floor
                        req.put("dstFNO",(int)Math.floor(Math.random() * 60 + 1)); // to which floor

                        log.info("Send kiosk panel request to Thread_Server");
                        // send the message to server
                        AppThread thdS = appKickstarter.getThread("Thread_Server");
                        MBox thdServerMBox = thdS.getMBox();
                        thdServerMBox.send(new Msg(id, mbox, Msg.Type.Svc_Req, req,null));
                        Timer.setSimulationTimer(id, mbox, sleepTime);
                        break;
                    }else{
                        // if no then wait
                        Timer.setSimulationTimer(id, mbox, sleepTime);
                        break;
                    }




                case Svc_Reply:
                    // reply msg from server
                    log.info(id + ": [" + msg.getSender() + "]: message received: [" + msg + "] ");
                    JSONObject res = msg.getDetails();
                    String PID = res.get("PID").toString();
                    int srcFNO = Integer.parseInt(res.get("srcFNO").toString());
                    int dstFNO = Integer.parseInt(res.get("dstFNO").toString());
                    int LNO = Integer.parseInt(res.get("LNO").toString());  //邊部呢派左比this req
                    log.info(id + ": Update kiosk panel information");
//                    update info in panel
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
    } // run
} // ThreadB
