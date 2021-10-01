package life.majiang.community.mapper;

import life.majiang.community.model.User;
import org.apache.ibatis.annotations.*;

/**
 * @author a123
 */
@Mapper
public interface UserMapper {

    /**
     * 登陆时把用户插入数据库
     * @param user
     */
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified,avatar_url) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);

    /**
     * 根据token获取用户
     * @param token
     */
    @Select("select * from user where token=#{token}")
    User findByToken(@Param("token") String token);

    /**
     * 根据ID查找用户
     * @param id 用户ID
     * @return
     */
    @Select("select * from user where id=#{id}")
    User findById(@Param("id")Integer id);

    /**
     * 根据account_id查找用户
     * @param accountId GitHub id
     * @return
     */
    @Select("select * from user where account_id = #{accountId}")
    User findByAccountId(@Param("accountId") String accountId);

    /**
     * 更新用户的名字 token 修改时间 还有头像地址
     * @param user
     * @return
     */
    @Update("update user set name= #{name},token=#{token},gmt_modified=#{gmtModified},avatar_url=#{AvatarUrl} where id = #{id}")
    User update(User user);
}
