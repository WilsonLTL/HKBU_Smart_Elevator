package AppKickstarter.myThreads;
import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.AppThread;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;
import AppKickstarter.timer.Timer;
import json.JSONArray;
import json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Thread_Socket_Server extends AppThread {
    private boolean OutServer = false;
    private Thread_Server thread_server;
    private JSONObject obj;
    private PrintWriter out;
    private ServerSocket server;
    private Socket socket;
    private boolean last_msg_status = false;
    private String last_msg = "";
    private final int ServerPort = 54321;// 要監控的port
    public Thread_Socket_Server(String id, AppKickstarter appKickstarter,Thread_Server thread_server) {
        super(id, appKickstarter);
        try {
            this.thread_server = thread_server;
            server = new ServerSocket(ServerPort,0, InetAddress.getByName("127.0.0.1"));
            socket = server.accept();
        } catch (java.io.IOException e) {
            System.out.println("Socket啟動有問題 !");
            System.out.println("IOException :" + e.toString());
        }
    }

    public void reply_message(JSONObject obj){

        String return_msg = "Svc_Reply "+obj.getString("PID")+" "+obj.getInt("srcFNO")+" "+obj.getInt("dstFNO");
        switch (obj.getInt("LNO")){
            case 1:
                return_msg+="A ";
                break;
            case 2:
                return_msg+="B ";
                break;
            case 3:
                return_msg+="C ";
                break;
            case 4:
                return_msg+="D ";
                break;
            case 5:
                return_msg+="E ";
                break;
            case 6:
                return_msg+="F ";
                break;

        }
        send_msg(return_msg);
    }

    public void send_arr_dep_msg(String type, String LNO, String FNO, String DIR, JSONArray LIST){
        String messageTemp = "";
        switch (type){
            case "Elev_Arr":
                 messageTemp = "Elev_Arr ";
                break;
            case "Elev_Dep":
                 messageTemp = "Elev_Dep ";
                break;
        }
        switch (LNO){
            case "1":
                messageTemp+="A ";
                break;
            case "2":
                messageTemp+="B ";
                break;
            case "3":
                messageTemp+="C ";
                break;
            case "4":
                messageTemp+="D ";
                break;
            case "5":
                messageTemp+="E ";
                break;
            case "6":
                messageTemp+="F ";
                break;

        }
        messageTemp +=FNO+ " "+  DIR + " ";
        for (Object o:LIST) {
            messageTemp += o.toString()+" ";
        }
        System.out.println("Msg:"+messageTemp);
        send_msg(messageTemp);
    }



    public void read_request(){
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String data = in.readLine();

            JSONObject req = new JSONObject();
            req.put("PID",data.split(" ")[1]); //the id of the kiosk panel, xxxx is a 4 digit number
            req.put("srcFNO",Integer.parseInt(data.split(" ")[2])); // current floor
            req.put("dstFNO",Integer.parseInt(data.split(" ")[3].split("\n")[0])); // to which floor

            AppThread thdS = appKickstarter.getThread("Thread_Server");
            MBox thdServerMBox = thdS.getMBox();
            thdServerMBox.send(new Msg(id, mbox, Msg.Type.Svc_Req, req,null));

        } catch (Exception e) {
            System.out.println("Socket連線有問題 !");
            System.out.println("IOException :" + e.toString());
        }
    }

    public void send_msg(String msg){
        try{
            out = new PrintWriter(socket.getOutputStream(), true);
            if(!last_msg_status){
//                out.println(last_msg);
                System.out.println("Resend msg successful:"+last_msg);
                last_msg_status = true;
            }else {
                out.println(msg);
            }
            log.info("Message Send: " + msg);
        }catch(SocketException e){
            last_msg = msg;
            last_msg_status = false;
            System.out.println("send message fail:"+e);
        }catch (Exception e){
            System.out.println("Exception:"+e);
        }
    }

    public void run() {
        System.out.println("伺服器已啟動 !");
        while (!OutServer) {
            read_request();
            Msg msg = mbox.receive();
            switch (msg.getType()) {
                case Svc_Reply:
                    obj = msg.getDetails();
                    reply_message(obj);
                    break;

                case Elev_Arr:
                    obj = msg.getDetails();
                    send_arr_dep_msg(obj.getString("type"),obj.getString("LNO"),obj.getString("FNO"),obj.getString("DIR"),obj.getJSONArray("LIST"));
                    break;

                case Elev_Dep:
                    obj = msg.getDetails();
                    send_arr_dep_msg(obj.getString("type"),obj.getString("LNO"),obj.getString("FNO"),obj.getString("DIR"),obj.getJSONArray("LIST"));
                    break;

                case Terminate:
                    OutServer = true;
                    break;

                default:
                    log.severe(id + ": unknown message type!!");
                    break;
            }
        }
    }
}
