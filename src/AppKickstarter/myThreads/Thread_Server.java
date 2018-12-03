package AppKickstarter.myThreads;

import AppKickstarter.misc.*;
import AppKickstarter.AppKickstarter;
import json.JSONArray;
import json.JSONObject;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Random;

public class Thread_Server extends AppThread {
    private final int sleepTime = 5;
    private Random rand = new Random(); // for test only
    private ArrayList<Thread_kiosk_panel> Thread_kiosk_panel_list = new ArrayList<Thread_kiosk_panel>();
    private ArrayList<Thread_Elevator_Panel> Thread_Elevator_Panel_list = new ArrayList<Thread_Elevator_Panel>();
    private JSONArray elev_list = new JSONArray();
    private JSONObject obj,result = new JSONObject();
    private Thread_Central_Control_Panel Thread_Central_Control_Panel;
    private Thread_Socket_Server Thread_Socket_Server;
    private ServerSocket server;
    private final int elev_num ,floor_num,ServerPort;

    public Thread_Server(String id, AppKickstarter appKickstarter) {
        super(id, appKickstarter);
        elev_num = Integer.parseInt(appKickstarter.getProperty("Bldg.NElevators"));
        floor_num = Integer.parseInt(appKickstarter.getProperty("Bldg.MaxFloorNumber"));
        ServerPort = Integer.parseInt(appKickstarter.getProperty("Bldg.MaxFloorNumber"));

        for (int i = 1; i<= floor_num; i++){
            Thread_kiosk_panel_list.add(new Thread_kiosk_panel("Thread_kiosk_Panel" + i,appKickstarter));
        }
        for (int i = 1; i<= elev_num; i++){
            Thread_Elevator_Panel_list.add(new Thread_Elevator_Panel("Thread_Elevator_Panel" + i,appKickstarter));
            JSONObject obj = new JSONObject();
            obj.put("LNO",i);
            obj.put("Current_Floor",0);
            obj.put("Next_Floor",0);
            obj.put("Work_List",new ArrayList<Integer>());
            obj.put("Status",8);
            obj.put("Direction",-1);
            elev_list.put(obj);
        }

        Thread_Central_Control_Panel = new Thread_Central_Control_Panel("Thread_Central_Control_Panel",appKickstarter);
        new Admin_Panel_UI(Thread_Central_Control_Panel);
        Thread_Socket_Server = new Thread_Socket_Server("Thread_Socket_Server",appKickstarter);
        new Thread(Thread_Socket_Server).start();
        for (Thread_kiosk_panel tkp: Thread_kiosk_panel_list){
            new Thread(tkp).start();
        }
        for (Thread_Elevator_Panel tep: Thread_Elevator_Panel_list){
            new Thread(tep).start();
        }
        new Thread(Thread_Central_Control_Panel).start();
    }

    public void run() {
        log.info(id + ": starting...");
        for (boolean quit = false; !quit;) {
            JSONObject req;
            JSONObject res;
            String PID;
            int srcFNO,dstFNO,LNO,Status;
            Msg msg = mbox.receive();

            switch (msg.getType()) {
                case Svc_Req:
                    // receive req from kiosk
                    log.info(id + ": [" + msg.getSender() + "]: message received: [" + msg.getType() + "] ");
                    req = msg.getDetails();
                    PID = req.get("PID").toString();
                    srcFNO = Integer.parseInt(req.get("srcFNO").toString());
                    dstFNO = Integer.parseInt(req.get("dstFNO").toString());

                    // cal elev and assign
                    // then result ...

                    // to src Fno
                    LNO = (int)(Math.random() * 6 + 1);
                    System.out.println("LNO"+LNO);
                    res = new JSONObject();
                    res.put("PID",PID);
                    res.put("srcFNO",srcFNO);
                    res.put("dstFNO",srcFNO);
                    res.put("LNO",LNO); //1 to 5 random,
                    log.info("Send job request to Thread_Elevator_Panel"+LNO);
//                  send jobs to elev
                    AppThread thdK = appKickstarter.getThread("Thread_Elevator_Panel"+LNO); //thread 1,2,3,4,5
                    MBox thdKioskMBox = thdK.getMBox();
                    thdKioskMBox.send(new Msg(id, mbox, Msg.Type.Elev_Job, res,null));

                    //to dst Fno
                    res = new JSONObject();
                    res.put("PID",PID);
                    res.put("srcFNO",srcFNO);
                    res.put("dstFNO",dstFNO);
                    res.put("LNO",LNO); //1 to 5 random,
                    log.info("Send job request to Thread_Elevator_Panel"+LNO);
//                  send jobs to elev
                    AppThread thdK2 = appKickstarter.getThread("Thread_Elevator_Panel"+LNO); //thread 1,2,3,4,5
                    MBox thdKiosk2MBox = thdK2.getMBox();
                    thdKiosk2MBox.send(new Msg(id, mbox, Msg.Type.Elev_Job, res,null));


                    log.info("Receive kiosk panel request from "+msg.getSender());
                    // send the message to Svc
                    AppThread thdE = appKickstarter.getThread(msg.getSender()); //thread ....
                    MBox thdElevMBox = thdE.getMBox();
                    thdElevMBox.send(new Msg(id, mbox, Msg.Type.Svc_Reply, res,null));


                    break;

//                case Elev_Reply:
//                    log.info(id + ": [" + msg.getSender() + "]: message received: [" + msg.getType() + "] ");
//                    res = msg.getDetails();
//                    System.out.println("elev_list:"+elev_list.toString());
//                    obj = (JSONObject)elev_list.get(Integer.parseInt(msg.getSender().split("Thread_Elevator_Panel")[1])-1);
//                    if (!obj.get("Status").toString().equals("7") || !obj.get("Status").toString().equals("12")) {
//                        System.out.println("Enter");
//                        obj =new JSONObject();
//                        obj.put("LNO",res.getInt("LNO"));
//                        obj.put("Current_Floor",res.getInt("Current_Floor"));
//                        obj.put("Next_Floor",res.getInt("Dir"));
//                        if (res.getInt("Current_Floor") > res.getInt("Dir")){
//                            obj.put("Direction",1);
//                        }else{
//                            obj.put("Direction",0);
//                        }
//                        obj.put("Status",res.getString("Status"));
//                    }
//                    elev_list.put(Integer.parseInt(msg.getSender().split("Thread_Elevator_Panel")[1])-1,obj);
//                    log.info("Receive elevator reply from "+msg.getSender());
//                    break;

                case Elev_Arr:
                case Elev_Dep:
                case Elev_Reply:
                    log.info(id + ": [" + msg.getSender() + "]: message received: [" + msg.getType() + "] ");
                    res = msg.getDetails();

                    obj = (JSONObject)elev_list.get(Integer.parseInt(msg.getSender().split("Thread_Elevator_Panel")[1])-1);
                    if (!obj.get("Status").toString().equals("7") || !obj.get("Status").toString().equals("12")) {
                        obj =new JSONObject();
                        obj.put("LNO",res.getInt("LNO"));
                        obj.put("Current_Floor",res.getInt("Current_Floor"));
                        obj.put("Next_Floor",res.getInt("Dir"));
                        if (res.getInt("Current_Floor") > res.getInt("Dir")){
                            obj.put("Direction",1);
                            obj.put("Status","down");
                        }else{
                            obj.put("Direction",0);
                            obj.put("Status","up");
                        }

                    }
                    elev_list.put(Integer.parseInt(msg.getSender().split("Thread_Elevator_Panel")[1])-1,obj);
//                    System.out.println("elev_list:"+elev_list.toString());
                    log.info("Receive elevator update from "+msg.getSender());
                    result = new JSONObject().put("result",elev_list);
                    AppThread thdADR = appKickstarter.getThread("Thread_Central_Control_Panel"); // 1,2,3,4,5 thread
                    MBox thdADRMBox = thdADR.getMBox();
                    thdADRMBox.send(new Msg(id, mbox, Msg.Type.Admin_Reply, result,null));
                    break;

                case Admin_Req:
                    log.info(id + ": [" + msg.getSender() + "]: message received: [" + msg.getType() + "] ");

                    // send the message to Admin
                    // reply the work list
                    JSONArray Arr = new JSONArray();
                    JSONObject result = new JSONObject();

                    for (int i =0 ;i< Integer.parseInt(appKickstarter.getProperty("Bldg.NElevators"));i++){
                        JSONObject obj = new JSONObject();
                        obj.put("LNO",i+1);
                        obj.put("Current_Floor",i+1);
                        obj.put("Next_Floor",i+1);
                        obj.put("Status",2);
                        obj.put("Direction",0);
                        Arr.put(obj);
                    }
                    result.put("result",Arr);

                    AppThread thdEAR = appKickstarter.getThread(msg.getSender()); // 1,2,3,4,5 thread
                    MBox thdEARMBox = thdEAR.getMBox();
                    thdEARMBox.send(new Msg(id, mbox, Msg.Type.Admin_Reply, result,null));
                    break;

                case Admin_Alert:
                    res = msg.getDetails();
                    log.info(id + ": [" + msg.getSender() + "]: message received: [" + msg.getType() + "] :"+res.toString());
                    LNO = Integer.parseInt(res.get("LNO").toString());
                    Status = Integer.parseInt(res.get("Status").toString());
                    log.info("Receive admin panel alert from "+msg.getSender());

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
