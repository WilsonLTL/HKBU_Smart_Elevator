package AppKickstarter.myThreads;

import AppKickstarter.misc.*;
import AppKickstarter.AppKickstarter;
import AppKickstarter.timer.Timer;
import json.JSONObject;

//======================================================================
// AppKickstarter.myThreads.Thread_Central_Control_Panel
public class Thread_Central_Control_Pane_old extends AppThread {
    private final int sleepTime = 5;
	// ------------------------------------------------------------
	public Thread_Central_Control_Pane_old(String id, AppKickstarter appKickstarter) {
		super(id, appKickstarter);
	} // AppKickstarter.myThreads.Thread_Central_Control_Panel

	// ------------------------------------------------------------
	// run
	public void run() {
		log.info(id + ": starting...");
        Timer.setSimulationTimer(id, mbox,sleepTime);
		String msgToServer;

		// public void GUIOnClick() {
		// GUI onclick (Start/Stop), assign a string to msgToServer.
		// msgToServer = "{["id":1],["id":2]}" etc..
		// }

		for (boolean quit = false; !quit;) {
			Msg msg = mbox.receive();

			// log.info(id + ": message received: [" + msg + "].");

			switch (msg.getType()) {
                case TimesUp:
                    log.info(id + ": receiving timesup at " + appKickstarter.getSimulationTimeStr());

                    // send the message to server
                    log.info(id + ": send request Admin_Req to Thread_Server");
                    AppThread thdS = appKickstarter.getThread("Thread_Server");
                    MBox thdServerMBox = thdS.getMBox();
                    thdServerMBox.send(new Msg(id, mbox, Msg.Type.Admin_Req, null,null));

                    // check any start or stop order??
                    // if yes
                    JSONObject req = new JSONObject();
                    req.put("LNO",12312);   // the id of the stop/start elev
                    req.put("Status",12);   // the following status of the elev PS: 11=start, 12=ready to stop

                    //send the message to server
                    log.info(id + ": send request Admin_Alert to Thread_Server :"+ req.toString());
                    AppThread thdS2 = appKickstarter.getThread("Thread_Server");
                    MBox thdServerMBox2 = thdS2.getMBox();
                    thdServerMBox2.send(new Msg(id, mbox, Msg.Type.Admin_Alert, req,null));
                    Timer.setSimulationTimer(id, mbox, sleepTime);
                    break;

				case Admin_Reply:
				    log.info(id + ": [" + msg.getSender() + "]: message received: [" + msg + "] ");
                    JSONObject res = msg.getDetails();
                    // get back the result
                    // sturture:
//                     {
//                    	"result":[{
//                    		"LNO":1,
//                    		"Current_Floor":1,
//                    		"Next_Floot":1,
//                    		"Status":1
//                    	},{"LNO":2,
//                    		"Current_Floor":1,
//                    		"Next_Floot":1,
//                    		"Status":1} ....
//                    	]
//                    }

				// display the info of lifts (GUI)

				// convert msg (String) to object?

				// for testing
				// for testing

				// // send reply to msg sender
				// MBox mbox = msg.getSenderMBox();
				// mbox.send(new Msg(id, mbox, Msg.Type.HiHi, "HiHi, this is Thread B!"));
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
