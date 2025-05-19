package dao;

import entity.Dorm;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DormDAO implements BaseDAO<Dorm> {
    
    @Override
    public boolean add(Dorm dorm) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO dorm(dormno, tele) VALUES(?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dorm.getDormno());
            pstmt.setString(2, dorm.getTele());
            
            int rows = pstmt.executeUpdate();
            result = rows > 0;
        } catch (SQLException e) {
            throw new Exception("添加宿舍信息失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt, null);
        }
        
        return result;
    }

    @Override
    public boolean update(Dorm dorm) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE dorm SET tele = ? WHERE dormno = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dorm.getTele());
            pstmt.setString(2, dorm.getDormno());
            
            int rows = pstmt.executeUpdate();
            result = rows > 0;
        } catch (SQLException e) {
            throw new Exception("更新宿舍信息失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt, null);
        }
        
        return result;
    }

    @Override
    public boolean delete(Object... ids) throws Exception {
        if (ids.length != 1) {
            throw new IllegalArgumentException("删除宿舍需要提供宿舍编号");
        }
        
        String dormno = (String) ids[0];
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean result = false;
        
        try {
            conn = DBUtil.getConnection();
            
            // 检查是否有学生住在这个宿舍
            String checkSql = "SELECT COUNT(*) FROM student WHERE dormno = ?";
            pstmt = conn.prepareStatement(checkSql);
            pstmt.setString(1, dormno);
            rs = pstmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                throw new Exception("无法删除宿舍：仍有学生住在该宿舍");
            }
            
            // 执行删除
            pstmt.close();
            String sql = "DELETE FROM dorm WHERE dormno = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dormno);
            
            int rows = pstmt.executeUpdate();
            result = rows > 0;
        } catch (SQLException e) {
            throw new Exception("删除宿舍信息失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        
        return result;
    }

    @Override
    public Dorm getById(Object... ids) throws Exception {
        if (ids.length != 1) {
            throw new IllegalArgumentException("查询宿舍需要提供宿舍编号");
        }
        
        String dormno = (String) ids[0];
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Dorm dorm = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM dorm WHERE dormno = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dormno);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                dorm = new Dorm();
                String dormNo = rs.getString("dormno");
                dorm.setDormno(dormNo != null ? dormNo.trim() : null);
                
                String tele = rs.getString("tele");
                dorm.setTele(tele != null ? tele.trim() : null);
            }
        } catch (SQLException e) {
            throw new Exception("查询宿舍信息失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        
        return dorm;
    }

    @Override
    public List<Dorm> getAll() throws Exception {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Dorm> dormList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM dorm";
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Dorm dorm = new Dorm();
                String dormno = rs.getString("dormno");
                dorm.setDormno(dormno != null ? dormno.trim() : null);
                
                String tele = rs.getString("tele");
                dorm.setTele(tele != null ? tele.trim() : null);
                
                dormList.add(dorm);
            }
        } catch (SQLException e) {
            throw new Exception("查询所有宿舍信息失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        
        return dormList;
    }
}
