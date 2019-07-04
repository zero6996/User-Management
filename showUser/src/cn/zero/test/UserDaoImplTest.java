package cn.zero.test;

import cn.zero.dao.Impl.UserDaoImpl;
import cn.zero.domain.AdminUser;
import org.junit.Test;

public class UserDaoImplTest {
    @Test
    public void testLogin(){
        AdminUser adminUser = new AdminUser();
        adminUser.setUser("admin");
        adminUser.setPassword("admin123");
        UserDaoImpl dao = new UserDaoImpl();
        AdminUser user = dao.login(adminUser);
        System.out.println(user);
    }
}
