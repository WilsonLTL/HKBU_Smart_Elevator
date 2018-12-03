package Server;
import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.AppThread;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;
import AppKickstarter.timer.Timer;
import json.JSONObject;

import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer extends Thread {
    private boolean OutServer = false;
    private ServerSocket server;
    private final int ServerPort = 54321;// 要監控的port
    private AppKickstarter appKickstarter;
    public SocketServer() {
        try {
            server = new ServerSocket(ServerPort);
        } catch (java.io.IOException e) {
            System.out.println("Socket啟動有問題 !");
            System.out.println("IOException :" + e.toString());
        }
    }
    public void run() {
        Socket socket;
        java.io.BufferedInputStream in;
        System.out.println("伺服器已啟動 !");
        while (!OutServer) {
            socket = null;
            try {
                synchronized (server) {
                    socket = server.accept();
                }
                System.out.println("取得連線 : InetAddress = "
                        + socket.getInetAddress());
// TimeOut時間
//                socket.setSoTimeout(15000);
                in = new java.io.BufferedInputStream(socket.getInputStream());
                byte[] b = new byte[1024];
                String data = "";
                int length = in.read(b);
                System.out.println("size:"+length);
                data = new String(b, 0, length);
                System.out.println("我取得的值:" + data);
                JSONObject req = new JSONObject();
                req.put("PID",data.split(" ")[1]); //the id of the kiosk panel, xxxx is a 4 digit number
                req.put("srcFNO",Integer.parseInt(data.split(" ")[2])); // current floor
                req.put("dstFNO",Integer.parseInt(data.split(" ")[3].split("\n")[0])); // to which floor

                in.close();
                in = null;
                socket.close();
            } catch (Exception e) {
                System.out.println("Socket連線有問題 !");
                System.out.println("IOException :" + e.toString());
            }
        }
    }
    public static void main(String args[]) {
        (new SocketServer()).start();
    }
}