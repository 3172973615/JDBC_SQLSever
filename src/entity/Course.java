package entity;

public class Course {
    private String cno;
    private String cname;
    private String cpno;  // 先修课程号
    private int credit;
    private String teacher;
    
    public Course() {}
    
    public Course(String cno, String cname, String cpno, int credit, String teacher) {
        this.cno = cno;
        this.cname = cname;
        this.cpno = cpno;
        this.credit = credit;
        this.teacher = teacher;
    }
    
    //基本函数
    public String getCno() {
        return cno;
    }
    
    public void setCno(String cno) {
        this.cno = cno;
    }
    
    public String getCname() {
        return cname;
    }
    
    public void setCname(String cname) {
        this.cname = cname;
    }
    
    public String getCpno() {
        return cpno;
    }
    
    public void setCpno(String cpno) {
        this.cpno = cpno;
    }
    
    public int getCredit() {
        return credit;
    }
    
    public void setCredit(int credit) {
        this.credit = credit;
    }
    
    public String getTeacher() {
        return teacher;
    }
    
    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
    
    @Override
    public String toString() {
        return "课程编号：" + cno + "，课程名称：" + cname + "，先修课程：" + (cpno == null ? "无" : cpno) 
                + "，学分：" + credit + "，教师：" + teacher;
    }
}