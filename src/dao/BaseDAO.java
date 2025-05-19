package dao;

import java.util.List;

public interface BaseDAO<T> {
    // 添加记录
    boolean add(T t) throws Exception;
    
    // 更新记录
    boolean update(T t) throws Exception;
    
    // 删除记录（可能需要多个ID参数，如Grade表有复合主键）
    boolean delete(Object... ids) throws Exception;
    
    // 根据ID查询单个记录
    T getById(Object... ids) throws Exception;
    
    // 查询所有记录
    List<T> getAll() throws Exception;
}