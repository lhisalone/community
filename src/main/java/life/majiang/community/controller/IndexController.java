package life.majiang.community.controller;

import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author a123
 */
@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;


    @GetMapping("/")
    public String index(HttpServletRequest request){
        //根据请求获取cookie[]
        Cookie[] cookies = request.getCookies();
        //对获取的cookie遍历
        for (Cookie cookie : cookies) {
            //这里我们只需要key为"token"的cookie
            if (cookie.getName().equals("token")) {
                //获取到"token" 对应的UUID token
                String token = cookie.getValue();
                //根据token的值 从数据库查出对应的用户
                User user=userMapper.findByToken(token);
                if (user != null) {
                    //将获取到的用户保存到Session中
                    //这里的Session主要的作用就是在页面上显示跟用户相关的信息
                    request.getSession().setAttribute("user",user);
                }
                break;
            }
        }

        return "index";
    }


}
