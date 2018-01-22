import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RemoteRef;

public class Server {
    public static void main(String args[]) {
        System.out.println("Loading RMI service");
        try {
            LocateRegistry.createRegistry(1099); // 1099 为默认端口
            MeetingService meetingService = new MeetingServiceImpl();
            Naming.rebind("DAS", meetingService); // 将bulbService绑定到RMI服务器上
            System.out.println("Distributed Agenda Service server is ready.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Distributed Agenda Service server has failed.");
        }
    }
}
