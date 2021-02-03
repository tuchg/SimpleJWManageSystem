package cn.wchihc.jwc.model.other;

/**
 * 选课表
 */
public class Elective {

    private Integer id;
    private Integer stId;
    private Integer csId;
    private Double score;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getStId() {
        return stId;
    }

    public void setStId(Integer stId) {
        this.stId = stId;
    }


    public Integer getCsId() {
        return csId;
    }

    public void setCsId(Integer csId) {
        this.csId = csId;
    }


    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Elective{" +
                "id=" + id +
                ", stId=" + stId +
                ", csId=" + csId +
                ", score=" + score +
                '}';
    }
}
