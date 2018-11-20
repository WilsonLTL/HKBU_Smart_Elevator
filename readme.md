# comp4007 - Smart Elevator
The smart Elevator system develop base on appkickstarter
Including 4 threading class - Thread_Server, Thread_kiosk_panel, Thread_Elevator_Panel, Thread_Central_Control_Panel

## The things that you guys need to do:

### Kiosk part
1. Make sure the panel connect to the main class
2. In Thread_kiosk_panel, finish the if else in line 28
3. Finish the Svc_Reply part that when info receive, update the panel.

### Elevator part:
1. Make sure the panel connect to the main class
2. In Thread_Elevator_Panel, finish the if else in line 39 and 45, who check which type of message should be send
3. In Thread_Elevatoe_Panel, finish the process in line 60, which will receive a job send by server, and reply the information your all things need in line 63 to 66
4. Do the function that to update the panel of elevator as u like! (since i believe that you already have your own idea to design it)

### Admin part:
1. Make sure the panel connect to the main class
2. In Thread_Central_Control_Panel, TimesUp case, you should check the stop/start btn click or not, and send me back the Admin_Alert, just modify as you like, remember to take a look of elevator status in the readme.md
3. In Thread_Central_Control_Panel, Admin_Reply, these the message that server send all info of all elevator, just update this info in to the admin panel, modify it as u like

### All
1. please test it to make sure the program is runnable after modify the code.

## Thread_Server
### Svc_Req
Receive from Thread_kiosk_panel, when user press the panel, the thread will send the request to server for assign a elevator for the user
Json format of Svc_Req<br >
```
{
    "PID":"Passenger-XXXX",
    "srcFNO":int,
    "dstFNO":int
}
```
PID: The unqiue ID of each passenger in 4 digital : String <br >
srcFNO: The current floor of the panel : Integer <br >
dstFNO: The floor that passenger want to go : Integer <br >

After process, server will send Svc_Reply and Elev_Job <br>

Json format of Svc_Reply and Elev_Job<br >
```
{
    "PID":"Passenger-XXXX",
    "srcFNO":int,
    "dstFNO":int,
    "LNO":int
}
```
PID: The unqiue ID of each passenger in 4 digital : String <br >
srcFNO: The current floor of the panel : Integer <br >
dstFNO: The floor that passenger want to go : Integer <br >
LNO : The elevator ID assign to the passenger <br >

### Elev_Reply
Receive from Thread_Elevator_Panel, after assign the Elev_Job to thread for reply message <br >
Json format of Elev_Reply<br >
```
{
    "LNO":int,
    "Current_Floor":,
    "Dir":,
    "Work_List":ArrayList<Integer>
}
```
LNO : The ID of the elevator : Integer <br >
Current_Floor : The current floor of the elevator : Integer <br >
Dir : The direction of elevator ,0=up, 1=down : Integer <br >
Work_List : The work list of the elevator : ArrayList<Integer> <br >

### Elev_Arr,Elev_Dep
Receive from Thread_Elevator_Panel, to update the schedule and information of each elevator
Json format of Elev_Arr,Elev_Dep<br >
```
{
    "LNO":int,
    "Current_Floor":,
    "Dir":,
    "Work_List":ArrayList<Integer>
}
```
LNO : The ID of the elevator : Integer <br >
Current_Floor : The current floor of the elevator : Integer <br >
Dir : The direction of elevator ,0=up, 1=down : Integer <br >
Work_List : The work list of the elevator : ArrayList<Integer> <br >

### Admin_Req
Receive from Thread_Central_Control_Panel, to alert to server to send the elevators information from admin panel
Json format of Admin_Req<br >
```
{
}
```
PS: the request is null <br >

### Admin_Alert
Receive from Thread_Central_Control_Panel, to alert the server that the elevator(s) need to stop or start
Json format of Admin_Alert<br >
```
{
    "LNO":int,
    "Status":int
}
```
LNO : The ID of the elevator : Integer <br >
Status : The new status of elevator : Integer <br >

## Thread_kiosk_panel
### TimesUp
When TimesUp message receive, kiosk will check the information in the panel, if true (user call the elevator), send Svc_Req to Thread_Server
Json format of Svc_Req<br >
```
{
    "PID":"Passenger-XXXX",
    "srcFNO":int,
    "dstFNO":int
}
```
PID: The unique ID of each passenger in 4 digital : String <br >
srcFNO: The current floor of the panel : Integer <br >
dstFNO: The floor that passenger want to go : Integer <br >

### Svc_Reply
Receive from Thread_Server, the message will include the old information send in Svc_Req and the assigned Elevator ID
Json format of Svc_Reply<br >
```
{
    "PID":"Passenger-XXXX",
    "srcFNO":int,
    "dstFNO":int,
    "LNO":int
}
```
PID: The unqiue ID of each passenger in 4 digital : String <br >
srcFNO: The current floor of the panel : Integer <br >
dstFNO: The floor that passenger want to go : Integer <br >
LNO : The elevator ID assign to the passenger <br >

## Thread_Elevator_Panel
### TimesUp
When TimesUp message receive, elevator will update the current information of itself and check it status to send Elev_Arr or Elev_Dep
Json format of Elev_Arr,Elev_Dep<br >
```
{
    "LNO":int,
    "Current_Floor":,
    "Dir":,
    "Work_List":ArrayList<Integer>
}
```
LNO : The ID of the elevator : Integer <br >
Current_Floor : The current floor of the elevator : Integer <br >
Dir : The direction of elevator ,0=up, 1=down : Integer <br >
Work_List : The work list of the elevator : ArrayList<Integer> <br >

### Elev_Job
Receive from Thread_Server, to assign a new job for elevator
Json format of Elev_Job<br >
```
{
    "PID":"Passenger-XXXX",
    "srcFNO":int,
    "dstFNO":int,
    "LNO":int
}
```
PID: The unique ID of each passenger in 4 digital : String <br >
srcFNO: The current floor of the panel : Integer <br >
dstFNO: The floor that passenger want to go : Integer <br >
LNO : The elevator ID assign to the passenger <br >

##Thread_Central_Control_Panel
### TimesUp
When TimesUp message receive, admin panel will send Admin_Req to Thread_Server, if any start or stop order detected, send Admin_Alert to Thread_Server too
Json format of Admin_Req<br >
```
{
}
```
PS: the request is null <br >

Json format of Admin_Alert<br >
```
{
    "LNO":int,
    "Status":int
}
```
LNO : The ID of the elevator : Integer <br >
Status : The new status of elevator : Integer <br >

### Admin_Reply
Receive from Thread_Server. which include all elevator current information <br >
Json format of Admin_Reply <br >
```
{
   "result":[{
        "LNO":1,
        "Current_Floor":1,
        "Next_Floot":1,
        "Status":1
   },{
        "LNO":2,
        "Current_Floor":1,
        "Next_Floot":1,
        "Status":1
        }
        ...
    ]
}
```
PID: The unique ID of each passenger in 4 digital : String <br >
Current_Floor : The current floor of the elevator : Integer <br >
Next_Floot : The next floor of the elevator : Integer <br >
Status : The Status of the elevator : Integer <br >

## The detail information of elevator status
```
AccUp : 0
AccDown : 1
DecUp : 2
DecDown : 3
DoorOpen : 4
DoorClose : 5
DoorWait : 6
Stop : 7
Free : 8
Up : 9
Down : 10
Start : 11
Ready_Stop : 12
```