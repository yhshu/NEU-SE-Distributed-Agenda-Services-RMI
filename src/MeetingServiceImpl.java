import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MeetingServiceImpl extends java.rmi.server.UnicastRemoteObject implements MeetingService {
    private int nextMeetingID = 0;
    private SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private List<User> userList = new ArrayList<>();
    private List<Meeting> meetingList = new ArrayList<>();


    MeetingServiceImpl() throws RemoteException {
        // 要抛出RemoteException异常，必须显式写出构造方法
    }


    public boolean register(String username, String password) {
        if (getUser(username) != null) // 用户已存在
            return false;
        userList.add(new User(username, password));
        return true;
    }


    public int addMeeting(String username, String password, String otherUserName, String start, String end, String title) throws ParseException {
        User sponser = getUser(username); // 发起者
        User otherUser = getUser(otherUserName); // 受邀者
        Date startDate = date.parse(start);
        Date endDate = date.parse(end);
        if (!checkUser(username, password)) return 0;
        if (username.equals(otherUserName)) return -1;
        if (getUser(otherUserName) == null) return -2;
        if (timeOverlap(sponser, startDate, endDate)) return -3;
        if (timeOverlap(otherUser, startDate, endDate)) return -4;

        Meeting meeting = new Meeting(sponser, otherUser, startDate, endDate, title);
        meeting.setID(nextMeetingID++);
        meetingList.add(meeting);
        sponser.getSchedule().add(meeting);
        otherUser.getSchedule().add(meeting);
        return 1; // 增加成功返回1
    }


    public String[][] queryMeeting(String username, String password, String start, String end) throws ParseException {
        if (!checkUser(username, password))
            return null;
        User user = getUser(username);
        Date startDate = date.parse(start);
        Date endDate = date.parse(end);
        ArrayList<Meeting> retList = new ArrayList<Meeting>();
        for (Meeting meeting : meetingList) {
            if (meeting.getStart().compareTo(startDate) >= 0 && meeting.getStart().before(endDate))
                retList.add(meeting);
        }
        sort(retList);
        int size = (retList == null) ? 0 : retList.size();
        String[][] ret = new String[size][5];
        for (int j = 0; j < retList.size(); j++) {
            ret[j][0] = String.valueOf(retList.get(j).getID());
            ret[j][1] = date.format(retList.get(j).getStart());
            ret[j][2] = date.format(retList.get(j).getEnd());
            ret[j][3] = retList.get(j).getTitle();
            ret[j][4] = retList.get(j).getSponsor().getUsername() + ", " + retList.get(j).getOtherUser().getUsername();
        }
        return ret;
    }


    private void sort(ArrayList<Meeting> list) {
        // 将会议列表按开始时间升序排序
        for (int i = 0; i < list.size(); i++) {
            for (int j = i; j < list.size(); j++) {
                if (list.get(j).getStart().before(list.get(i).getStart())) {
                    swap(list.get(j), list.get(i));
                }
            }
        }
    }

    private void swap(Meeting m1, Meeting m2) {
        User user = m1.getSponsor();
        m1.setSponsor(m2.getSponsor());
        m2.setSponsor(user);

        user = m1.getOtherUser();
        m1.setOtherUser(m2.getOtherUser());
        m2.setOtherUser(user);

        Date date = m1.getStart();
        m1.setStart(m2.getStart());
        m2.setStart(date);

        date = m1.getEnd();
        m1.setEnd(m2.getEnd());
        m2.setEnd(date);

        String title = m1.getTitle();
        m1.setTitle(m2.getTitle());
        m2.setTitle(title);
    }

    public boolean deleteMeeting(String username, String password, int meetingID) {
        if (!checkUser(username, password))
            return false;
        User user = getUser(username);
        for (int i = 0; i < meetingList.size(); i++) {
            if (meetingList.get(i).getSponsor().equals(user) && meetingList.get(i).getID() == meetingID) {
                meetingList.get(i).getSponsor().getSchedule().remove(meetingList.get(i));
                meetingList.get(i).getOtherUser().getSchedule().remove(meetingList.get(i));
                meetingList.remove(meetingList.get(i));
                return true;
            }
        }
        return false;
    }


    public boolean clearMeeting(String username, String password) {
        if (!checkUser(username, password))
            return false;
        User user = getUser(username);
        boolean clearFlag = false;
        List<Meeting> deleted = new ArrayList<>();
        for (Meeting meeting : meetingList) {
            if (meeting.getSponsor().equals(user))
                deleted.add(meeting);
        }
        for (Meeting meeting : deleted) {
            meetingList.remove(meeting);
            clearFlag = true;
        }
        return clearFlag;
    }

    private User getUser(String username) {
        for (User u : userList) {
            if (u.getUsername().equals(username))
                return u;
        }
        return null;
    }

    private boolean checkUser(String username, String password) {
        // 帐号是否存在且密码正确
        if (getUser(username) == null) return false;
        else return getUser(username).getPassword().equals(password);

    }

    private boolean timeOverlap(User user, Date start, Date end) {
        // 检查某用户会议时间是否重叠
        if (user.getSchedule().size() == 0)
            return false;
        for (Meeting m : user.getSchedule()) {
            Date s = m.getStart().after(start) ? m.getStart() : start;
            Date e = m.getEnd().before(end) ? m.getEnd() : end;
            if (s.compareTo(e) <= 0)
                return true;
        }
        return false;
    }
}
