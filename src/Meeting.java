import java.util.Date;

public class Meeting {
    private User sponsor;
    private User otherUser;
    private Date start;
    private Date end;
    private String title;
    private int ID;

    public User getSponsor() {
        return sponsor;
    }

    public void setSponsor(User sponsor) {
        this.sponsor = sponsor;
    }

    public User getOtherUser() {
        return otherUser;
    }

    public void setOtherUser(User otherUser) {
        this.otherUser = otherUser;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    Meeting(User sponsor, User otherUser, Date start, Date end, String title) {
        this.sponsor = sponsor;
        this.otherUser = otherUser;
        this.start = start;
        this.end = end;
        this.title = title;
    }
}
