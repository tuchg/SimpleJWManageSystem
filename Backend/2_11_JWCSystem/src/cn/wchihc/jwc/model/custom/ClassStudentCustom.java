package cn.wchihc.jwc.model.custom;

/**
 * 班级学生信息
 */
public class ClassStudentCustom {
    private Integer cId;
    private Integer sId;
    private String sno;
    private Integer eId;
    //学生排名
    private long sRank;
    private String name;
    private String sex;
    private int fId;
    private Double score;


    public int getFId() {
        return fId;
    }

    public void setFId(Integer fId) {
        this.fId = fId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getSId() {
        return sId;
    }

    public void setSId(Integer sId) {
        this.sId = sId;
    }

    public long getSRank() {
        return sRank;
    }

    public void setSRank(Long sRank) {
        this.sRank = sRank;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public Integer getEId() {
        return eId;
    }

    public void setEId(Integer eId) {
        this.eId = eId;
    }

    public Integer getCId() {
        return cId;
    }

    public void setCId(Integer cId) {
        this.cId = cId;
    }
}
