package life.majiang.community.interceptor;

import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author a123
 */
@Service
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;

    /**
     * 这个方法在所有客户端进入之前执行 意义是将用户的数据放到session中
     * 预处理回调方法，实现处理器的预处理（如登录检查）
     * preHandler是在请求到达Controller之前执行的，然后对你的请求进行了封装，你可以对前台传过来的客户端的请求在拦截器中进行加工然后再提交给控制器。
     * @param request 请求
     * @param response 响应
     * @param handler 处理
     * @return true表示继续流程（如调用下一个拦截器或处理器） false表示流程中断（如登录检查失败）不会调用其他拦截器和处理器handler
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //根据请求获取cookie[]
        Cookie[] cookies = request.getCookies();
        //对获取的cookie做非空判断
        if (cookies !=null && cookies.length!=0) {
            //对获取的cookie遍历
            for (Cookie cookie : cookies) {
                //这里我们只需要key为"token"的cookie
                if ("token".equals(cookie.getName())) {
                    //获取到"token" 对应的UUID token
                    String token = cookie.getValue();
                    //根据token的值 从数据库查出对应的用户
                    User user = userMapper.findByToken(token);
                    if (user != null) {
                        //将获取到的用户保存到Session中
                        //这里的Session主要的作用就是在页面上显示跟用户相关的信息
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
