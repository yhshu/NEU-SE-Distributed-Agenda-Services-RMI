import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
    public static void main(String args[]) {
        System.out.println("Looking for meeting service");
        try {
            MeetingService meetingService = (MeetingService) Naming.lookup("DAS"); // 获取远程对象，类型转换
            printMenu();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String command = reader.readLine();
                String arg[];
                arg = command.split(",");
                if (command.startsWith("register")) {
                    if (!checkCommand(arg, 3)) continue;
                    if (meetingService.register(arg[1], arg[2]))
                        System.out.println("[Successful] User " + arg[1] + " got registered.");
                    else
                        System.out.println("[Failed] User " + arg[1] + " has been registered.");
                } else if (command.startsWith("add")) {
                    if (!checkCommand(arg, 7)) continue;
                    int addRes = meetingService.addMeeting(arg[1], arg[2], arg[3], arg[4], arg[5], arg[6]);
                    switch (addRes) {
                        case 0:
                            System.out.println("[Failed] Username or password wrong.");
                            break;
                        case -1:
                            System.out.println("[Failed] The user hasn't been registered.");
                            break;
                        case -2:
                            System.out.println("[Failed] The sponsor of the meeting hasn't been registered.");
                            break;
                        case -3:
                            System.out.println("[Failed] There's conflict in the schedule of the sponsor.");
                            break;
                        case -4:
                            System.out.println("[Failed] There's conflict in the schedle of the invitee.");
                            break;
                        case 1:
                            System.out.println("[Successful] The meeting has benn added.");
                            break;
                        default:
                            break;
                    }
                } else if (command.startsWith("query")) {
                    if (!checkCommand(arg, 5)) continue;
                    String[][] list = meetingService.queryMeeting(arg[1], arg[2], arg[3], arg[4]);
                    if (list != null) {
                        for (String[] m : list) {
                            System.out.print("ID: " + m[0] + " ");
                            System.out.print("start: " + m[1] + " ");
                            System.out.print("end: " + m[2] + " ");
                            System.out.print("title: " + m[3] + " ");
                            System.out.println("participant: " + m[4] + " ");
                        }
                    } else {
                        System.out.println("[Failed] Querying failed.");
                    }
                } else if (command.startsWith("delete")) {
                    if (!checkCommand(arg, 4)) continue;
                    if (meetingService.deleteMeeting(arg[1], arg[2], Integer.parseInt(arg[3]))) {
                        System.out.println("[Successful] The meeting has been deleted.");
                    } else {
                        System.out.println("[Failed] Deleting failed.");
                    }
                } else if (command.startsWith("clear")) {
                    if (!checkCommand(arg, 3)) continue;
                    if (meetingService.clearMeeting(arg[1], arg[2])) {
                        System.out.println("[Successful] All the meeting has been cleared");
                    } else {
                        System.out.println("[Failed] Clearing meetings failed.");
                    }
                } else if (command.toLowerCase().startsWith("menu")) {
                    printMenu();
                } else {
                    System.out.println("[Error] Please check your command.");
                }
            }
        } catch (NotBoundException nbe) {
            System.out.println("No service available.");
        } catch (RemoteException re) {
            System.err.println("Remote Error - " + re);
        } catch (Exception e) {
            System.err.println("Error - " + e);
        }
    }

    private static void printMenu() {
        System.out.println("------------------------------Menu------------------------------");
        System.out.println("register,[username],[password]");
        System.out.println("add,[username],[password],[otherusername],[start],[end],[title]");
        System.out.println("query,[username],[password],[start],[end]");
        System.out.println("delete,[username],[password],[meetingid]");
        System.out.println("clear,[username],[password]");
    }

    private static boolean checkCommand(String[] arg, int length) {
        if (arg.length != length) {
            System.out.println("The command is illegal. Please retry.");
            printMenu();
            return false;
        }
        return true;
    }
}