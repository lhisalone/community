package life.majiang.community.mapper;

import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author a123
 */
@Mapper
public interface QuestionMapper {

    /**
     * 把问题插入数据库
     *
     * @param question 从html的from表单上获取到的问题赋值到question对象 在将对象插入数据库 持久化
     */
    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag) values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);

    /**
     * 全查问题 并做分页
     *
     * @param offSet limit 返回记录行的偏移量
     * @param size   limit 返回记录行的最大数目
     * @return 返回全部问题的集合
     */
    @Select("select * from question limit #{offset},#{size}")
    List<Question> list(@Param(value = "offset") Integer offSet, Integer size);

    /**
     * 查询问题总数
     *
     * @return 返回当前数据库中问题的总数量
     */
    @Select("select count(1) from question")
    Integer countQuestion();

    /**
     * 根据用户ID查询该用户发布的问题
     * 并做分页
     * @param userId 用户ID
     * @param offSet limit 返回记录行的偏移量
     * @param size limit 返回记录行的最大数目
     * @return
     */
    @Select("select * from question where creator=#{userId} limit #{offset},#{size}")
    List<Question> listByUserId(@Param("userId") Integer userId, @Param(value = "offset") Integer offSet, Integer size);

    /**
     * 根据用户的ID查询出用户发布过的问题数量
     * @param userId 用户ID
     * @return 返回问题数量
     */
    @Select("select count(1) from question where creator=#{userId}")
    Integer countQuestionByUserId(@Param("userId") Integer userId);

    /**
     * 根据用户ID查询用户发布过的问题
     * @param id 用户ID
     * @return 返回Question对象
     */
    @Select("select * from question where id=#{id}")
    Question getById(Integer id);
}
