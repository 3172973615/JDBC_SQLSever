package entity;

public class Grade {
    private String sno;
    private String cno;
    private int score;
    
    public Grade() {}
    
    public Grade(String sno, String cno, int score) {
        this.sno = sno;
        this.cno = cno;
        this.score = score;
    }
    
    //基本函数
    public String getSno() {
        return sno;
    }
    
    public void setSno(String sno) {
        this.sno = sno;
    }
    
    public String getCno() {
        return cno;
    }
    
    public void setCno(String cno) {
        this.cno = cno;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    @Override
    public String toString() {
        return "学号：" + sno + "，课程号：" + cno + "，成绩：" + score;
    }
}