/**
 * RMI服务器
 *
 * @author 舒意恒
 */

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;


public class Server {
    public static void main(String args[]) {
        System.out.println("Loading RMI service");
        try {
            LocateRegistry.createRegistry(1099); // 1099 为RMI默认端口
            MeetingService meetingService = new MeetingServiceImpl();
            Naming.rebind("DAS", meetingService); // 将meetServiceImpl对象绑定到RMI服务器上
            System.out.println("[Successful] Distributed Agenda Service server is ready.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[Failed] Distributed Agenda Service server has failed.");
        }
    }
}
