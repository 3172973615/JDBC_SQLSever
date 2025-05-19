package dao;

import entity.Department;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO implements BaseDAO<Department> {
    
    @Override
    public boolean add(Department dept) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO department(dno, dname, head) VALUES(?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dept.getDno());
            pstmt.setString(2, dept.getDname());
            pstmt.setString(3, dept.getHead());
            
            int rows = pstmt.executeUpdate();
            result = rows > 0;
        } catch (SQLException e) {
            throw new Exception("添加院系信息失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt, null);
        }
        
        return result;
    }

    @Override
    public boolean update(Department dept) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE department SET dname = ?, head = ? WHERE dno = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dept.getDname());
            pstmt.setString(2, dept.getHead());
            pstmt.setString(3, dept.getDno());
            
            int rows = pstmt.executeUpdate();
            result = rows > 0;
        } catch (SQLException e) {
            throw new Exception("更新院系信息失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt, null);
        }
        
        return result;
    }

    @Override
    public boolean delete(Object... ids) throws Exception {
        if (ids.length != 1) {
            throw new IllegalArgumentException("删除院系需要提供院系编号");
        }
        
        String dno = (String) ids[0];
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean result = false;
        
        try {
            conn = DBUtil.getConnection();
            
            // 检查是否有学生关联到此院系
            String checkSql = "SELECT COUNT(*) FROM student WHERE dno = ?";
            pstmt = conn.prepareStatement(checkSql);
            pstmt.setString(1, dno);
            rs = pstmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                throw new Exception("无法删除院系：存在关联到此院系的学生");
            }
            
            // 执行删除
            pstmt.close();
            String sql = "DELETE FROM department WHERE dno = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dno);
            
            int rows = pstmt.executeUpdate();
            result = rows > 0;
        } catch (SQLException e) {
            throw new Exception("删除院系信息失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        
        return result;
    }

    @Override
    public Department getById(Object... ids) throws Exception {
        if (ids.length != 1) {
            throw new IllegalArgumentException("查询院系需要提供院系编号");
        }
        
        String dno = (String) ids[0];
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Department dept = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM department WHERE dno = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dno);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                dept = new Department();
                String deptNo = rs.getString("dno");
                dept.setDno(deptNo != null ? deptNo.trim() : null);
                
                String dname = rs.getString("dname");
                dept.setDname(dname != null ? dname.trim() : null);
                
                String head = rs.getString("head");
                dept.setHead(head != null ? head.trim() : null);
            }
        } catch (SQLException e) {
            throw new Exception("查询院系信息失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        
        return dept;
    }

    @Override
    public List<Department> getAll() throws Exception {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Department> deptList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM department";
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Department dept = new Department();
                String dno = rs.getString("dno");
                dept.setDno(dno != null ? dno.trim() : null);
                
                String dname = rs.getString("dname");
                dept.setDname(dname != null ? dname.trim() : null);
                
                String head = rs.getString("head");
                dept.setHead(head != null ? head.trim() : null);
                
                deptList.add(dept);
            }
        } catch (SQLException e) {
            throw new Exception("查询所有院系信息失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        
        return deptList;
    }
}