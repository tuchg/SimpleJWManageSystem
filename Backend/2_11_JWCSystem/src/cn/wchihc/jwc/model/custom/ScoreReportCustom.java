package cn.wchihc.jwc.model.custom;

import cn.wchihc.jwc.model.other.Course;

/**
 * 成绩单
 */
public class ScoreReportCustom extends Course {
    private Double score;
    private int sNum;
    private long sRank;


    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public int getSNum() {
        return sNum;
    }

    public void setSNum(int sNum) {
        this.sNum = sNum;
    }

    public long getSRank() {
        return sRank;
    }

    public void setSRank(long sRank) {
        this.sRank = sRank;
    }
}
