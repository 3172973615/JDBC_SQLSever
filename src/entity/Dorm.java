package entity;

public class Dorm {
    private String dormno;
    private String tele;
    
    public Dorm() {}
    
    public Dorm(String dormno, String tele) {
        this.dormno = dormno;
        this.tele = tele;
    }
    
    //基本函数
    public String getDormno() {
        return dormno;
    }
    
    public void setDormno(String dormno) {
        this.dormno = dormno;
    }
    
    public String getTele() {
        return tele;
    }
    
    public void setTele(String tele) {
        this.tele = tele;
    }
    
    @Override
    public String toString() {
        return "宿舍编号：" + dormno + "，电话：" + tele;
    }
}