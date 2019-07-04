package cn.zero.dao.Impl;

import cn.zero.dao.UserDao;
import cn.zero.domain.AdminUser;
import cn.zero.domain.User;
import cn.zero.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

// 数据访问层
public class UserDaoImpl implements UserDao {
    private JdbcTemplate template = new JdbcTemplate((JDBCUtils.getDataSource()));

    /**
     * 查询所有用户方法
     * @return UserBean
     */
    @Override
    public List<User> findAll() {
        // 使用JDBC操作数据库...
        // 1. 定义sql
        String sql = "select * from user";
        List<User> users = template.query(sql, new BeanPropertyRowMapper<User>(User.class));
        return users;
    }

    /**
     * 查询用户方法
     * @param id
     * @return 查询到的用户对象
     */
    @Override
    public User findUser(int id) {
        return template.queryForObject("select * from user where id=?",new BeanPropertyRowMapper<User>(User.class),id);
    }

    /**
     * 添加用户方法
     * @param addUser
     * @return 影响的行数
     */
    @Override
    public int addUser(User addUser) {
        if (addUser.getName()!=null && addUser.getName()!=""){
            return template.update("insert into user values(?,?,?,?,?,?,?)",
                    null,addUser.getName(),addUser.getGender(),addUser.getAge(),
                    addUser.getAddress(),addUser.getQq(),addUser.getEmail());
        }
        return -1;
    }

    /**
     * 删除用户方法
     * @param id
     * @return 成功返回正数，失败返回负数
     */
    @Override
    public int delUser(String id) {
        if (id != null && id != ""){
            return template.update("delete from user where id = ? ", id);
        }
        return -1;
    }


    @Override
    public void delUser(int id) {
        template.update("delete from user where id = ? ",id);
    }

    /**
     * 修改用户方法
     * @param user
     * @return 成功返回正数，失败返回负数
     */
    @Override
    public int updateUser(User user) {
        if (user.getAge() > 0){
            return template.update("update user set name = ?, gender = ?, age = ?, address = ?, qq = ?, email = ? where id = ?",
                    user.getName(),user.getGender(),user.getAge(),user.getAddress(),user.getQq(),user.getEmail(),user.getId());
        }
        return -1;
    }

    /**
     * 登录方法
     * @param user
     * @return
     */
    @Override
    public AdminUser login(AdminUser user) {
        try{
//            if (user.getUser() != null && user.getUser() != "" && user.getPassword() != null && user.getPassword() != "")
            return template.queryForObject("select * from adminuser where user = ? and password = ?",new BeanPropertyRowMapper<AdminUser>(AdminUser.class),user.getUser(), user.getPassword());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询总记录数
     * @return 总记录数
     * @param condition
     */
    @Override
    public int findTotalCount(Map<String, String[]> condition) {
        // 1. 定义模板初始化sql
        String sql = "select count(*) from user where 1 = 1 ";
        StringBuilder sb = new StringBuilder(sql);
        String value = null;
        if (condition.containsKey("search")){ // 判断map中有search，在取值
            value = condition.get("search")[0];
        }
        List<Object> params = new ArrayList<>();
        if (value != null && !"".equals(value)){
            sb.append("and name like ? OR gender like ? OR age like ? OR address like ? OR qq like ? OR email like ? ");
            params.add("%" + value + "%");
            params.add("%" + value + "%");
            params.add("%" + value + "%");
            params.add("%" + value + "%");
            params.add("_" + value + "%");
            params.add("%" + value + "%");
        }
        System.out.println(sb.toString());
        System.out.println(params);
        return template.queryForObject(sb.toString(),Integer.class,params.toArray());
        // 2. 遍历map
//        Set<String> keySet = condition.keySet();
//        // 定义存放参数的集合
//        List<Object> params = new ArrayList<>();
//        for (String key : keySet) {
//            // 排除分页条件参数
//            if ("currentPage".equals(key) || "rows".equals(key) || "search".equals(key)){
//                continue;
//            }
//            // 获取value
//            String value = condition.get(key)[0];
//            // 判断value是否有值
//            if (value != null && !"".equals(value)){
//                sb.append(" and "+key+" like ?");
//                params.add("%"+value+"%"); // 将key对应值传入
//            }
//        }
//        System.out.println(sb.toString());
//        System.out.println(params);
//        return template.queryForObject(sb.toString(), Integer.class,params.toArray());
    }

    @Override
    public int searchTotalCount(Map<String, String[]> condition) {
        String sql = "select count(*) from user where 1 = 1 ";
        StringBuilder sb = new StringBuilder(sql);
        return 1;
    }

    /**
     * 分页查询list
     * @param start
     * @param rows
     * @param condition
     * @return 返回该分页数据对象
     */
    @Override
    public List<User> findByPage(int start, int rows, Map<String, String[]> condition) {
        String sql = "select * from user where 1 = 1 ";
        StringBuilder sb = new StringBuilder(sql);
        // 2. 遍历map
        Set<String> keySet = condition.keySet();
        // 定义参数的集合
        List<Object> params = new ArrayList<>();
        for (String key : keySet) {
            // 排除分页条件参数
            if ("currentPage".equals(key) || "rows".equals(key)) {
                continue;
            }
            // 获取value
            String value = condition.get(key)[0];
            // 判断value是否有值
            if (value != null && !"".equals(value)) {
                sb.append(" and " + key + " like ?");
                params.add("%" + value + "%"); // 将key对应值传入
            }
        }
        // 添加分页查询
        sb.append(" limit ?,?");
        // 添加分页查询参数值
        params.add(start);
        params.add(rows);
        System.out.println(sb.toString());
        System.out.println(params);
        return template.query(sb.toString(),new BeanPropertyRowMapper<User>(User.class),params.toArray());
    }

    @Override
    public List<User> SearchByPage(int start, int rows, Map<String, String[]> condition) {
        String sql = "select * from user where 1 = 1 ";
        StringBuilder sb = new StringBuilder(sql);
        String value = null;
        if (condition.containsKey("search")){ // 判断map中有search，在取值
            value = condition.get("search")[0];
        }
        List<Object> params = new ArrayList<>();
        if (value != null && !"".equals(value)){
            sb.append("and name like ? OR gender like ? OR age like ? OR address like ? OR qq like ? OR email like ? ");
            params.add("%" + value + "%"); // name
            params.add("%" + value + "%"); // gender
            params.add("%" + value + "%"); // age
            params.add("%" + value + "%"); // address
            params.add("_" + value + "%"); // qq,原因是查询age时，如果qq中有包含age的字段，使用%%模糊查询就会将qq中包含有age查询条件的结果返回，
            // 故使用_%，确保查询age时不会出现将qq中包含age值的结果返回
            params.add("%" + value + "%"); // email
        }
        sb.append("limit ?,?");
        params.add(start);
        params.add(rows);
        System.out.println(sb.toString());
        System.out.println(params);
        return template.query(sb.toString(),new BeanPropertyRowMapper<User>(User.class),params.toArray());
    }
}
