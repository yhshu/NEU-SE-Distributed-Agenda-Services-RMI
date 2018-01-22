import java.rmi.RemoteException;
import java.text.ParseException;

public interface MeetingService extends java.rmi.Remote {
    // 每个方法都必须throws RemoteException

    boolean register(String username, String password) throws RemoteException;
    // 注册用户
    // 若用户名存在，返回false
    // 若用户名不存在，注册成功，返回true

    int addMeeting(String username, String password, String otherUserName, String start, String end, String title) throws RemoteException, ParseException;
    // 增加会议
    // 帐号密码错误返回0
    // 发起者、参与者是同一用户返回-1
    // 参与者未注册返回-2
    // 会议时间与发起者时间冲突，返回-3
    // 会议时间与参与者时间冲突，返回-4
    // 增加会议成功，返回1

    public String[][] queryMeeting(String username, String password, String start, String end) throws ParseException, RemoteException;
    // 查询会议
    // 包括注册用户主动召开的会议和被邀请参与的会议
    // 帐号密码错误返回null

    public boolean deleteMeeting(String username, String password, int meetingID) throws RemoteException;
    // 删除会议
    // 注册用户只能删除已创建的会议
    // 清除成功返回true，失败返回false

    public boolean clearMeeting(String username, String password) throws RemoteException;
    // 清除会议
    // 注册用户清除自己召开的所有会议
}