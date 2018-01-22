import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
    public static void main(String args[]) {
        System.out.println("Looking for meeting service");
        try {
            MeetingService meetingService = (MeetingService) Naming.lookup("DAS");
            printMenu();


            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String command = reader.readLine();
                String arg[];
                arg = command.split(" ");
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
                            System.out.println("Username or password wrong.");
                            break;
                        case -1:
                            System.out.println("The user hasn't been registered.");
                            break;
                            
                    }
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
        System.out.println("-----Menu-----");
        System.out.println("register [username] [password]");
        System.out.println("add [username] [password] [otherusername] [start] [end] [title]");
        System.out.println("query [username] [password] [start] [end]");
        System.out.println("delete [username] [password] [meetingid]");
        System.out.println("clear [username] [password]");
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
