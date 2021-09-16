package life.majiang.community.provider;

import com.alibaba.fastjson.JSON;
import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author a123
 * 这里的方法主要是根据AccessToken的对象发送请求 来获取GitHub的Token
 * 根据Token再次发送请求来获取 登陆用户的个人信息
 * Provider：提供者
 */
@Component
public class GithubProvider {

    /**
     * 使用okhttp可以post请求 根据AccessTokenDTO的属性可以作为Post的key和value来发送请求
     * 获取AccessToken来进行下一步的请求
     * @param accessTokenDTO
     * @return
     */
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        //根据request发送请求
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            //AccessToken:access_token=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&scope=user&token_type=bearer
            //拆分Token 先获取左边的数据 在获取=右边的数据
            String token=string.split("&")[0].split("=")[1];
            return token;
        } catch (Exception e) {
        }
        return null;
    }


    /**
     * 这里根据得到的AccessToken向网站发送get请求来获取用户的信息
     * @param accessToken
     * @return
     */
    public GithubUser githubUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .header("Authorization","token "+accessToken)
                .build();
        try {
            //发送请求
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            System.out.println(string);
            //这里使用fastjson来将收到的json格式转换为一个对象
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
        }
        return null;
    }
}

