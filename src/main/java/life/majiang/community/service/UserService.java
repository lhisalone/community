package life.majiang.community.service;

import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author a123
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 原来的代码发现每次登陆的时候 即使登陆的用户都是一样的 但是没有做随机生成的token做判断 所有每次登陆都会重新往数据库创建新的用户
     * 查询数据库中是否存在user 验证的方式就是从GitHub上返回的AccountID 拿它来跟数据库对比就可以知道当前用户是否存在
     * 存在就update token 把从新获得的UUID.token 更新到数据库
     * 不存在就create
     * 这里以来 就解决了 每次登陆都会重新在数据库创建数据的问题
     * @param user
     */
    public void createOrUpdate(User user) {
        User dbUser =  userMapper.findByAccountId(user.getAccountId());
        if (dbUser == null) {
            //insert
            //System.currentTimeMillis()当时的时间 包括秒
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else {
            //update
            //更新修改时间
            dbUser.setGmtModified(System.currentTimeMillis());
            //可能头像图片会发生改变
            dbUser.setAvatarUrl(user.getAvatarUrl());
            //上同理
            dbUser.setName(user.getName());
            //将新获得的uuid token重新赋值给查出来dbUser
            dbUser.setToken(user.getToken());
            userMapper.update(dbUser);
        }
    }
}
