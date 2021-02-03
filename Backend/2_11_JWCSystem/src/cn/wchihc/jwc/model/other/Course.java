package cn.wchihc.jwc.model.other;


import java.sql.Timestamp;

public class Course {

    private Integer id;
    private String cname;
    private String mark;
    private Integer tId;
    private Integer roomId;
    private Integer maxChooseNum;
    private Integer selectedNum;
    private Timestamp startTime;
    private Timestamp endTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }


    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }


    public Integer getTId() {
        return tId;
    }

    public void setTId(Integer tId) {
        this.tId = tId;
    }


    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }


    public Integer getMaxChooseNum() {
        return maxChooseNum;
    }

    public void setMaxChooseNum(Integer maxChooseNum) {
        this.maxChooseNum = maxChooseNum;
    }


    public Integer getSelectedNum() {
        return selectedNum;
    }

    public void setSelectedNum(Integer selectedNum) {
        this.selectedNum = selectedNum;
    }


    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
}
