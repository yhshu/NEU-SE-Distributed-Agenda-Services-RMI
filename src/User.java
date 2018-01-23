/**
 * 描述会议服务中的用户
 *
 * @author 舒意恒
 * @see Server
 * @see Meeting
 */

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String username;
    private String password;
    private ArrayList<Meeting> schedule;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    protected String getPassword() {
        return password;
    }

    protected void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Meeting> getSchedule() {
        return schedule;
    }

    User(String username, String password) {
        super();
        this.username = username;
        this.password = password;
        this.schedule = new ArrayList<>();
    }
}