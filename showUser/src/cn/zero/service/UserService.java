package cn.zero.service;

import cn.zero.domain.AdminUser;
import cn.zero.domain.PageBean;
import cn.zero.domain.User;

import java.util.List;
import java.util.Map;

// 用户管理的业务接口
public interface UserService {
    /**
     * 查询所有用户信息
     * @return
     */
    public List<User> findAll();

    /**
     * 查询单个用户信息
     * @param id
     * @return
     */
    public User findUser(String id);
    /**
     * 添加用户
     * @param user
     * @return
     */
    public int addUser(User user);

    /**
     * 删除用户
     * @param id
     * @return
     */
    public int delUser(String id);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    public int updateUser(User user);

    /**
     * 管理员登录
     * @param user
     * @return
     */
    public AdminUser login(AdminUser user);

    /**
     * 批量删除用户
     * @param uids
     */
    void delUser(String[] uids);

    /**
     * 分页条件查询
     * @param currentPage
     * @param rows
     * @param condition
     * @return 分页对象
     */
    PageBean<User> findUserByPage(int currentPage, int rows, Map<String, String[]> condition);
}
