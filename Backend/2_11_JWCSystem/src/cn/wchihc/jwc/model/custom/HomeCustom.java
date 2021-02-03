package cn.wchihc.jwc.model.custom;

import java.util.List;

public class HomeCustom<T> {
    private int studentNum;
    private int teacherNum;
    private int facultyNum;
    private List<T> data;

    public HomeCustom() {
    }

    public HomeCustom(int studentNum, int teacherNum, int facultyNum, List<T> data) {
        this.studentNum = studentNum;
        this.teacherNum = teacherNum;
        this.facultyNum = facultyNum;
        this.data = data;
    }

    public int getFacultyNum() {
        return facultyNum;
    }

    public void setFacultyNum(int facultyNum) {
        this.facultyNum = facultyNum;
    }

    public int getTeacherNum() {
        return teacherNum;
    }

    public void setTeacherNum(int teacherNum) {
        this.teacherNum = teacherNum;
    }

    public int getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(int studentNum) {
        this.studentNum = studentNum;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
