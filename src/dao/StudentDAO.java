package dao;

import entity.Student;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO implements BaseDAO<Student> {
    
    @Override
    public boolean add(Student student) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean result = false;
        
        try {
            conn = DBUtil.getConnection();
            
            // 检查院系是否存在
            String checkDeptSql = "SELECT COUNT(*) FROM department WHERE dno = ?";
            pstmt = conn.prepareStatement(checkDeptSql);
            pstmt.setString(1, student.getDno());
            rs = pstmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) == 0) {
                throw new Exception("院系不存在");
            }
            
            // 检查宿舍是否存在
            pstmt.close();
            rs.close();
            String checkDormSql = "SELECT COUNT(*) FROM dorm WHERE dormno = ?";
            pstmt = conn.prepareStatement(checkDormSql);
            pstmt.setString(1, student.getDormno());
            rs = pstmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) == 0) {
                throw new Exception("宿舍不存在");
            }
            
            // 添加学生
            pstmt.close();
            String sql = "INSERT INTO student(sno, sname, sex, sage, dno, dormno) VALUES(?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, student.getSno());
            pstmt.setString(2, student.getSname());
            pstmt.setString(3, student.getSex());
            pstmt.setInt(4, student.getSage());
            pstmt.setString(5, student.getDno());
            pstmt.setString(6, student.getDormno());
            
            int rows = pstmt.executeUpdate();
            result = rows > 0;
        } catch (SQLException e) {
            throw new Exception("添加学生信息失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        
        return result;
    }

    @Override
    public boolean update(Student student) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean result = false;
        
        try {
            conn = DBUtil.getConnection();
            
            // 检查院系是否存在
            String checkDeptSql = "SELECT COUNT(*) FROM department WHERE dno = ?";
            pstmt = conn.prepareStatement(checkDeptSql);
            pstmt.setString(1, student.getDno());
            rs = pstmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) == 0) {
                throw new Exception("院系不存在");
            }
            
            // 检查宿舍是否存在
            pstmt.close();
            rs.close();
            String checkDormSql = "SELECT COUNT(*) FROM dorm WHERE dormno = ?";
            pstmt = conn.prepareStatement(checkDormSql);
            pstmt.setString(1, student.getDormno());
            rs = pstmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) == 0) {
                throw new Exception("宿舍不存在");
            }
            
            // 更新学生
            pstmt.close();
            String sql = "UPDATE student SET sname = ?, sex = ?, sage = ?, dno = ?, dormno = ? WHERE sno = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, student.getSname());
            pstmt.setString(2, student.getSex());
            pstmt.setInt(3, student.getSage());
            pstmt.setString(4, student.getDno());
            pstmt.setString(5, student.getDormno());
            pstmt.setString(6, student.getSno());
            
            int rows = pstmt.executeUpdate();
            result = rows > 0;
        } catch (SQLException e) {
            throw new Exception("更新学生信息失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        
        return result;
    }

    @Override
    public boolean delete(Object... ids) throws Exception {
        if (ids.length != 1) {
            throw new IllegalArgumentException("删除学生需要提供学号");
        }
        
        String sno = (String) ids[0];
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean result = false;
        
        try {
            conn = DBUtil.getConnection();
            
            // 检查是否有该学生的成绩记录
            String checkGradeSql = "SELECT COUNT(*) FROM grade WHERE sno = ?";
            pstmt = conn.prepareStatement(checkGradeSql);
            pstmt.setString(1, sno);
            rs = pstmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                // 删除该学生的所有成绩记录
                pstmt.close();
                String deleteGradeSql = "DELETE FROM grade WHERE sno = ?";
                pstmt = conn.prepareStatement(deleteGradeSql);
                pstmt.setString(1, sno);
                pstmt.executeUpdate();
            }
            
            // 删除学生
            pstmt.close();
            String sql = "DELETE FROM student WHERE sno = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sno);
            
            int rows = pstmt.executeUpdate();
            result = rows > 0;
        } catch (SQLException e) {
            throw new Exception("删除学生信息失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        
        return result;
    }

    @Override
    public Student getById(Object... ids) throws Exception {
        if (ids.length != 1) {
            throw new IllegalArgumentException("查询学生需要提供学号");
        }
        
        String sno = (String) ids[0];
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Student student = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM student WHERE sno = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sno);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                student = new Student();
                student.setSno(rs.getString("sno").trim());
                student.setSname(rs.getString("sname").trim());
                student.setSex(rs.getString("sex").trim());
                student.setSage(rs.getInt("sage"));
                student.setDno(rs.getString("dno").trim());
                student.setDormno(rs.getString("dormno").trim());
            }
        } catch (SQLException e) {
            throw new Exception("查询学生信息失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        
        return student;
    }

    @Override
    public List<Student> getAll() throws Exception {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Student> studentList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM student";
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Student student = new Student();
                
                // 安全地处理可能为null的字符串,不然无法查询所有学生，会空指针报错
                String sno = rs.getString("sno");
                student.setSno(sno != null ? sno.trim() : null);
                
                String sname = rs.getString("sname");
                student.setSname(sname != null ? sname.trim() : null);
                
                String sex = rs.getString("sex");
                student.setSex(sex != null ? sex.trim() : null);
                
                // 数字类型一般不会返回null，但如果列允许为null，要使用rs.getObject()方法
                student.setSage(rs.getInt("sage"));
                
                String dno = rs.getString("dno");
                student.setDno(dno != null ? dno.trim() : null);
                
                String dormno = rs.getString("dormno");
                student.setDormno(dormno != null ? dormno.trim() : null);
                
                studentList.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // 添加详细日志
            throw new Exception("查询所有学生信息失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        
        return studentList;
    }

    // 根据院系查询学生
    public List<Student> getByDepartment(String dno) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Student> studentList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM student WHERE dno = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dno);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Student student = new Student();
                
                // 安全地处理可能为null的字符串
                String sno = rs.getString("sno");
                student.setSno(sno != null ? sno.trim() : null);
                
                String sname = rs.getString("sname");
                student.setSname(sname != null ? sname.trim() : null);
                
                String sex = rs.getString("sex");
                student.setSex(sex != null ? sex.trim() : null);
                
                student.setSage(rs.getInt("sage"));
                
                String stdDno = rs.getString("dno");
                student.setDno(stdDno != null ? stdDno.trim() : null);
                
                String dormno = rs.getString("dormno");
                student.setDormno(dormno != null ? dormno.trim() : null);
                
                studentList.add(student);
            }
        } catch (SQLException e) {
            throw new Exception("根据院系查询学生信息失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        
        return studentList;
    }
    
    // 根据宿舍查询学生
    public List<Student> getByDorm(String dormno) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Student> studentList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM student WHERE dormno = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dormno);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Student student = new Student();
                
                // 安全地处理可能为null的字符串
                String sno = rs.getString("sno");
                student.setSno(sno != null ? sno.trim() : null);
                
                String sname = rs.getString("sname");
                student.setSname(sname != null ? sname.trim() : null);
                
                String sex = rs.getString("sex");
                student.setSex(sex != null ? sex.trim() : null);
                
                student.setSage(rs.getInt("sage"));
                
                String dno = rs.getString("dno");
                student.setDno(dno != null ? dno.trim() : null);
                
                String stdDormno = rs.getString("dormno");
                student.setDormno(stdDormno != null ? stdDormno.trim() : null);
                
                studentList.add(student);
            }
        } catch (SQLException e) {
            throw new Exception("根据宿舍查询学生信息失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        
        return studentList;
    }
}
