package dao;

import entity.Course;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO implements BaseDAO<Course> {
    
    @Override
    public boolean add(Course course) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean result = false;
        
        try {
            conn = DBUtil.getConnection();
            
            // 如果有先修课程，检查先修课程是否存在
            if (course.getCpno() != null && !course.getCpno().isEmpty()) {
                String checkSql = "SELECT COUNT(*) FROM course WHERE cno = ?";
                pstmt = conn.prepareStatement(checkSql);
                pstmt.setString(1, course.getCpno());
                rs = pstmt.executeQuery();
                
                if (rs.next() && rs.getInt(1) == 0) {
                    throw new Exception("先修课程不存在");
                }
                pstmt.close();
            }
            
            // 添加课程
            String sql = "INSERT INTO course(cno, cname, cpno, credit, teacher) VALUES(?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, course.getCno());
            pstmt.setString(2, course.getCname());
            
            if (course.getCpno() != null && !course.getCpno().isEmpty()) {
                pstmt.setString(3, course.getCpno());
            } else {
                pstmt.setNull(3, Types.CHAR);
            }
            
            pstmt.setInt(4, course.getCredit());
            pstmt.setString(5, course.getTeacher());
            
            int rows = pstmt.executeUpdate();
            result = rows > 0;
        } catch (SQLException e) {
            throw new Exception("添加课程信息失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        
        return result;
    }

    @Override
    public boolean update(Course course) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean result = false;
        
        try {
            conn = DBUtil.getConnection();
            
            // 如果有先修课程，检查先修课程是否存在
            if (course.getCpno() != null && !course.getCpno().isEmpty()) {
                String checkSql = "SELECT COUNT(*) FROM course WHERE cno = ?";
                pstmt = conn.prepareStatement(checkSql);
                pstmt.setString(1, course.getCpno());
                rs = pstmt.executeQuery();
                
                if (rs.next() && rs.getInt(1) == 0) {
                    throw new Exception("先修课程不存在");
                }
                pstmt.close();
            }
            
            // 检查是否形成环
            if (course.getCpno() != null && !course.getCpno().isEmpty() && course.getCpno().equals(course.getCno())) {
                throw new Exception("课程不能将自己设为先修课程");
            }
            
            // 更新课程
            String sql = "UPDATE course SET cname = ?, cpno = ?, credit = ?, teacher = ? WHERE cno = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, course.getCname());
            
            if (course.getCpno() != null && !course.getCpno().isEmpty()) {
                pstmt.setString(2, course.getCpno());
            } else {
                pstmt.setNull(2, Types.CHAR);
            }
            
            pstmt.setInt(3, course.getCredit());
            pstmt.setString(4, course.getTeacher());
            pstmt.setString(5, course.getCno());
            
            int rows = pstmt.executeUpdate();
            result = rows > 0;
        } catch (SQLException e) {
            throw new Exception("更新课程信息失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        
        return result;
    }

    @Override
    public boolean delete(Object... ids) throws Exception {
        if (ids.length != 1) {
            throw new IllegalArgumentException("删除课程需要提供课程编号");
        }
        
        String cno = (String) ids[0];
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean result = false;
        
        try {
            conn = DBUtil.getConnection();
            
            // 检查是否有学生选修了这门课程
            String checkGradeSql = "SELECT COUNT(*) FROM grade WHERE cno = ?";
            pstmt = conn.prepareStatement(checkGradeSql);
            pstmt.setString(1, cno);
            rs = pstmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                throw new Exception("无法删除课程：有学生选修了该课程");
            }
            
            // 检查是否有其他课程以此课程为先修课程
            pstmt.close();
            rs.close();
            String checkPrereqSql = "SELECT COUNT(*) FROM course WHERE cpno = ?";
            pstmt = conn.prepareStatement(checkPrereqSql);
            pstmt.setString(1, cno);
            rs = pstmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                throw new Exception("无法删除课程：该课程是其他课程的先修课程");
            }
            
            // 执行删除
            pstmt.close();
            String sql = "DELETE FROM course WHERE cno = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cno);
            
            int rows = pstmt.executeUpdate();
            result = rows > 0;
        } catch (SQLException e) {
            throw new Exception("删除课程信息失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        
        return result;
    }

    @Override
    public Course getById(Object... ids) throws Exception {
        if (ids.length != 1) {
            throw new IllegalArgumentException("查询课程需要提供课程编号");
        }
        
        String cno = (String) ids[0];
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Course course = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM course WHERE cno = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cno);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                course = new Course();
                String courseNo = rs.getString("cno");
                course.setCno(courseNo != null ? courseNo.trim() : null);
                
                String cname = rs.getString("cname");
                course.setCname(cname != null ? cname.trim() : null);
                
                String cpno = rs.getString("cpno");
                course.setCpno(cpno != null ? cpno.trim() : null);
                
                course.setCredit(rs.getInt("credit"));
                
                String teacher = rs.getString("teacher");
                course.setTeacher(teacher != null ? teacher.trim() : null);
            }
        } catch (SQLException e) {
            throw new Exception("查询课程信息失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        
        return course;
    }

    @Override
    public List<Course> getAll() throws Exception {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Course> courseList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM course";
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Course course = new Course();
                String cno = rs.getString("cno");
                course.setCno(cno != null ? cno.trim() : null);
                
                String cname = rs.getString("cname");
                course.setCname(cname != null ? cname.trim() : null);
                
                String cpno = rs.getString("cpno");
                course.setCpno(cpno != null ? cpno.trim() : null);
                
                course.setCredit(rs.getInt("credit"));
                
                String teacher = rs.getString("teacher");
                course.setTeacher(teacher != null ? teacher.trim() : null);
                
                courseList.add(course);
            }
        } catch (SQLException e) {
            throw new Exception("查询所有课程信息失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        
        return courseList;
    }
}