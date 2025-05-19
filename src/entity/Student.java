package entity;

public class Student {
    private String sno;
    private String sname;
    private String sex;
    private int sage;
    private String dno;
    private String dormno;
    
    public Student() {}
    
    public Student(String sno, String sname, String sex, int sage, String dno, String dormno) {
        this.sno = sno;
        this.sname = sname;
        this.sex = sex;
        this.sage = sage;
        this.dno = dno;
        this.dormno = dormno;
    }
    
    // Getters and Setters
    public String getSno() {
        return sno;
    }
    
    public void setSno(String sno) {
        this.sno = sno;
    }
    
    public String getSname() {
        return sname;
    }
    
    public void setSname(String sname) {
        this.sname = sname;
    }
    
    public String getSex() {
        return sex;
    }
    
    public void setSex(String sex) {
        this.sex = sex;
    }
    
    public int getSage() {
        return sage;
    }
    
    public void setSage(int sage) {
        this.sage = sage;
    }
    
    public String getDno() {
        return dno;
    }
    
    public void setDno(String dno) {
        this.dno = dno;
    }
    
    public String getDormno() {
        return dormno;
    }
    
    public void setDormno(String dormno) {
        this.dormno = dormno;
    }
    
    @Override
    public String toString() {
        return "学号：" + sno + "，姓名：" + sname + "，性别：" + sex + "，年龄：" + sage 
                + "，院系号：" + dno + "，宿舍号：" + dormno;
    }
}