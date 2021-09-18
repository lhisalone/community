package life.majiang.community.controller;

import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.dto.GithubUser;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import life.majiang.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author a123
 * controller根据 GitHub oauth2的授权
 * 1.请求用户的GitHub的授权
 * 当用户点击登陆的时候会跳转到https://github.com/login/oauth/authorize？
 * 2.这时 GitHub 会根据发送的get请求重定向到你的站点 获得临时的code代码
 * 3.根据获得的code发送post请求获得access_token
 * access_token=gho_16C7e42F292c6912E7710c838347Ae178B4a&token_type=bearer
 * 4.然后在使用获得的token向API提出请求
 * https://api.github.com/user？token
 * 5.然后会返回用户消息来做出登陆
 *
 */
@Controller
public class AuthorizeController {

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.client.uri}")
    private String clientUri;

    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    private UserMapper userMapper;

    /**
     * @GetMapping会获取localhost：8777/callback后的参数
     * @param code
     * @param state
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response){
        //创建AccessTokenDTO对象
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        //对对象进行赋值
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(clientUri);
        accessTokenDTO.setState(state);
        //调用方法将对象传入发送post请求  获得Token
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        //根据上面获得的token 发送请求获取用户对象
        GithubUser githubUser = githubProvider.githubUser(accessToken);
        //如果得到用户不为空
        if (githubUser != null) {
            //创建user实例对象
            User user = new User();
            //创建随机的token
            String token = UUID.randomUUID().toString();
            //赋值给user对象
            user.setToken(token);
            user.setName(githubUser.getName());
            //GitHub中的AccessId为String User的Id为int 需要强转
            user.setAccountId(String.valueOf(githubUser.getId()));
            //System.currentTimeMillis()当时的时间 包括秒
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            //将获取GitHub的头像url赋值给user
            user.setAvatarUrl(githubUser.getAvatar_url());
            //插入数据库中
            userMapper.insert(user);
            //向服务器发送cookie key为token value为刚刚创建的随机token
            response.addCookie(new Cookie("token",token));
            //重定向回到首页
            return "redirect:/";
        }else {
            //登陆失败，重新登陆
            return "redirect:/";
        }
    }
}
