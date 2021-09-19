package life.majiang.community.mapper;

import life.majiang.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
     * @param id
     * @return
     */
    @Select("select * from user where id=#{id}")
    User findById(@Param("id")Integer id);
}
