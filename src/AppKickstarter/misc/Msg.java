package AppKickstarter.misc;
import json.*;
//======================================================================
// Msg
public class Msg {
    private String sender, msg;
    private MBox senderMBox;
    private Type type;
    private JSONObject details;

    //------------------------------------------------------------
    // Msg
    public Msg(String sender, MBox senderMBox, Type type, JSONObject details, String msg) {
	this.sender = sender;
	this.senderMBox = senderMBox;
	this.type = type;
	this.details = details;
	this.msg = msg;
    } // Msg


    //------------------------------------------------------------
    // getters
    public String getSender()     { return sender; }
    public MBox   getSenderMBox() { return senderMBox; }
    public Type   getType()       { return type; }
    public JSONObject getDetails()    { return details; }
    public String getMessage() {return msg;}


    //------------------------------------------------------------
    // toString
    public String toString() {
	    return sender + " (" + type + ") -- " + msg;
    } // toString


    //------------------------------------------------------------
    // Msg Types
    public enum Type {
	    Terminate,	// Terminate the running thread
	    SetTimer,       // Set a timer
	    CancelTimer,    // Set a timer
	    Tick,           // Timer clock ticks
	    TimesUp,
        Svc_Req,        // Kiosk to server
        Svc_Reply,      // server to kiosk
        Elev_Arr,       // elev to server PS: page 4
        Elev_Dep,        // elev to server
        Elev_Job,       // jobs to elev
        Elev_Reply,     // elev to server
        Admin_Req,      // admin to server, ask for update elev infos
        Admin_Reply,    // server to admin, send infos
        Admin_Alert,    // admin to server, to alert elev start/stop
//	Hello,          // Hello -- a testing msg type
//	HiHi,           // HiHi -- a testing msg type
    } // Type
} // Msg
