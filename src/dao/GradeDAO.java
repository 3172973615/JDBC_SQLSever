package dao;

import entity.Grade;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradeDAO implements BaseDAO<Grade> {

    @Override
    public boolean add(Grade grade) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            conn = DBUtil.getConnection();
            
            // 检查学生是否存在
            String checkStudentSql = "SELECT COUNT(*) FROM student WHERE sno = ?";
            pstmt = conn.prepareStatement(checkStudentSql);
            pstmt.setString(1, grade.getSno());
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) == 0) {
                throw new Exception("学号不存在，无法添加成绩");
            }
            
            // 检查课程是否存在
            pstmt.close();
            rs.close();
            String checkCourseSql = "SELECT COUNT(*) FROM course WHERE cno = ?";
            pstmt = conn.prepareStatement(checkCourseSql);
            pstmt.setString(1, grade.getCno());
            rs = pstmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) == 0) {
                throw new Exception("课程号不存在，无法添加成绩");
            }
            
            // 成绩必须在0-100之间（这里也可以依赖数据库的CHECK约束）
            if (grade.getScore() < 0 || grade.getScore() > 100) {
                throw new Exception("成绩必须在0-100之间");
            }
            
            // 执行添加操作
            pstmt.close();
            rs.close();
            String sql = "INSERT INTO grade(sno, cno, score) VALUES(?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, grade.getSno());
            pstmt.setString(2, grade.getCno());
            pstmt.setInt(3, grade.getScore());
            
            int rows = pstmt.executeUpdate();
            result = rows > 0;
        } catch (SQLException e) {
            throw new Exception("添加成绩信息失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt, null);
        }
        
        return result;
    }

    @Override
    public boolean update(Grade grade) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            conn = DBUtil.getConnection();
            
            // 成绩必须在0-100之间
            if (grade.getScore() < 0 || grade.getScore() > 100) {
                throw new Exception("成绩必须在0-100之间");
            }
            
            String sql = "UPDATE grade SET score = ? WHERE sno = ? AND cno = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, grade.getScore());
            pstmt.setString(2, grade.getSno());
            pstmt.setString(3, grade.getCno());
            
            int rows = pstmt.executeUpdate();
            result = rows > 0;
        } catch (SQLException e) {
            throw new Exception("更新成绩信息失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt, null);
        }
        
        return result;
    }

    @Override
    public boolean delete(Object... ids) throws Exception {
        if (ids.length != 2) {
            throw new IllegalArgumentException("删除成绩需要提供学号和课程号");
        }
        
        String sno = (String) ids[0];
        String cno = (String) ids[1];
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM grade WHERE sno = ? AND cno = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sno);
            pstmt.setString(2, cno);
            
            int rows = pstmt.executeUpdate();
            result = rows > 0;
        } catch (SQLException e) {
            throw new Exception("删除成绩信息失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt, null);
        }
        
        return result;
    }

    @Override
    public Grade getById(Object... ids) throws Exception {
        if (ids.length != 2) {
            throw new IllegalArgumentException("查询成绩需要提供学号和课程号");
        }
        
        String sno = (String) ids[0];
        String cno = (String) ids[1];
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Grade grade = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM grade WHERE sno = ? AND cno = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sno);
            pstmt.setString(2, cno);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                grade = new Grade();
                String studentNo = rs.getString("sno");
                grade.setSno(studentNo != null ? studentNo.trim() : null);
                
                String courseNo = rs.getString("cno");
                grade.setCno(courseNo != null ? courseNo.trim() : null);
                
                grade.setScore(rs.getInt("score"));
            }
        } catch (SQLException e) {
            throw new Exception("查询成绩信息失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        
        return grade;
    }

    @Override
    public List<Grade> getAll() throws Exception {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Grade> gradeList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM grade";
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Grade grade = new Grade();
                String studentNo = rs.getString("sno");
                grade.setSno(studentNo != null ? studentNo.trim() : null);
                
                String courseNo = rs.getString("cno");
                grade.setCno(courseNo != null ? courseNo.trim() : null);
                
                grade.setScore(rs.getInt("score"));
                gradeList.add(grade);
            }
        } catch (SQLException e) {
            throw new Exception("查询所有成绩信息失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        
        return gradeList;
    }
    
    // 根据学生查询成绩
    public List<Grade> getByStudent(String sno) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Grade> gradeList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM grade WHERE sno = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sno);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Grade grade = new Grade();
                String studentNo = rs.getString("sno");
                grade.setSno(studentNo != null ? studentNo.trim() : null);
                
                String courseNo = rs.getString("cno");
                grade.setCno(courseNo != null ? courseNo.trim() : null);
                
                grade.setScore(rs.getInt("score"));
                gradeList.add(grade);
            }
        } catch (SQLException e) {
            throw new Exception("查询学生成绩信息失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        
        return gradeList;
    }
    
    // 根据课程查询成绩
    public List<Grade> getByCourse(String cno) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Grade> gradeList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM grade WHERE cno = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cno);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Grade grade = new Grade();
                String studentNo = rs.getString("sno");
                grade.setSno(studentNo != null ? studentNo.trim() : null);
                
                String courseNo = rs.getString("cno");
                grade.setCno(courseNo != null ? courseNo.trim() : null);
                
                grade.setScore(rs.getInt("score"));
                gradeList.add(grade);
            }
        } catch (SQLException e) {
            throw new Exception("查询课程成绩信息失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        
        return gradeList;
    }
}