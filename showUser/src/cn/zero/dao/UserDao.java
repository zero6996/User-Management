package cn.zero.dao;

import cn.zero.domain.AdminUser;
import cn.zero.domain.User;

import java.util.List;
import java.util.Map;

// 用户操作的Dao
public interface UserDao {
    public List<User> findAll();
    public User findUser(int id);
    public int addUser(User user);
    public int delUser(String id);
    public void delUser(int id);
    public int updateUser(User user);
    public AdminUser login(AdminUser user);
    int findTotalCount(Map<String, String[]> condition);
    int searchTotalCount(Map<String, String[]> condition);
    List<User> findByPage(int start, int rows, Map<String, String[]> condition);
    List<User> SearchByPage(int start, int rows, Map<String, String[]> condition);
}
