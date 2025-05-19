package entity;

public class Department {
    private String dno;
    private String dname;
    private String head;
    
    public Department() {}
    
    public Department(String dno, String dname, String head) {
        this.dno = dno;
        this.dname = dname;
        this.head = head;
    }
    
    // Getters and Setters
    public String getDno() {
        return dno;
    }
    
    public void setDno(String dno) {
        this.dno = dno;
    }
    
    public String getDname() {
        return dname;
    }
    
    public void setDname(String dname) {
        this.dname = dname;
    }
    
    public String getHead() {
        return head;
    }
    
    public void setHead(String head) {
        this.head = head;
    }
    
    @Override
    public String toString() {
        return "院系编号：" + dno + "，院系名称：" + dname + "，负责人：" + head;
    }
}