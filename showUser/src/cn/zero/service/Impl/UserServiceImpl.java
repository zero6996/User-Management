package cn.zero.service.Impl;


import cn.zero.dao.Impl.UserDaoImpl;
import cn.zero.dao.UserDao;
import cn.zero.domain.AdminUser;
import cn.zero.domain.PageBean;
import cn.zero.domain.User;
import cn.zero.service.UserService;

import java.util.List;
import java.util.Map;

// 主要业务逻辑层
public class UserServiceImpl implements UserService {
    private UserDao dao = new UserDaoImpl();
    @Override
    public List<User> findAll() {
        // 调用Dao完成查询
        return dao.findAll();
    }

    @Override
    public User findUser(String id) {
        return dao.findUser(Integer.parseInt(id));
    }

    @Override
    public int addUser(User user) {
        return dao.addUser(user);
    }

    @Override
    public int delUser(String id) {
        return dao.delUser(id);
    }

    @Override
    public int updateUser(User user) {
        return dao.updateUser(user);
    }

    @Override
    public AdminUser login(AdminUser user) {
        return dao.login(user);
    }

    @Override
    public void delUser(String[] uids) {
        if (uids != null && uids.length > 0){ // 进行非空校验，防止空指针异常
            // 1. 遍历id
            for (String uid : uids) {
                // 2. 调用dao的删除方法
                dao.delUser(Integer.parseInt(uid));
            }
        }
    }

    @Override
    public PageBean<User> findUserByPage(int currentPage, int rows, Map<String, String[]> condition) {
        // 1. 创建一个PageBean对象
        PageBean<User> pageBean = new PageBean<User>();
        // 2. 设置当前页码属性和rows属性
        pageBean.setCurrentPage(currentPage);
        pageBean.setRows(rows);
        // 3. 调用dao查询totalCount总记录数
        int totalCount = dao.findTotalCount(condition);
        // 设置总记录数
        pageBean.setTotalCount(totalCount);
        // 4. 计算开始索引
        int start = (currentPage - 1) * rows;
        // 5. 调用dao查询list集合
        List<User> list = dao.SearchByPage(start, rows,condition);
        // 设置list集合
        pageBean.setList(list);
        // 6. 计算总页码
        int totalPage = totalCount % rows == 0 ? totalCount / rows : totalCount / rows + 1;
        // 设置总页码
        pageBean.setTotalPage(totalPage);
        // 7. 返回PageBean对象
        return pageBean;
    }
}
