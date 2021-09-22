package life.majiang.community.service;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author a123
 * Question的逻辑层
 * 相当于controller跟model或DTO，VO中间的缓冲
 */
@Service
public class QuestionService {
    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private UserMapper userMapper;

    /**
     * QuestionDTO中的User对象的作用是获取里面avatar_url头像URL来给前端的页面显示图片
     * 创建QuestionDTO对象 因为里面有User对象 所有还要根据question中的creator查出User
     * 将数据库查到的question赋值给QuestionDTO运输层传输给前端页面
     * v1:关于分页功能的实现
     * 返回对象改为PaginationDTO 里面封装了一个QuestionDTO的对象 加上分页的信息
     * QuestionDTO里面封装了当前用户User对象
     * 调用方法在DTO中对PaginationDTO中的值进行赋值
     * 最后把方法返回到index 将PaginationDTO 添加到model实现对页面的改变
     * @param page 当前页码
     * @param size 当前页面显示多少问题
     */
    public PaginationDTO getPaginationDTO(Integer page, Integer size) {

        //这里的pagination对象封装了questionDTO 加上分页的数据
        PaginationDTO paginationDTO = new PaginationDTO();
        //创建总页数
        Integer totalPage;
        //查询出问题的总数
        Integer totalCount = questionMapper.countQuestion();
        //判断一共有多少页
        totalPage = (totalCount % size == 0) ? (totalCount / size) : (totalCount / size + 1);
        //对当前的page做边界判断 page不能小于1也不能大于totalPage总数
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        //调用方法  通过方法将paginationDTO中关于分页的数据赋值
        paginationDTO.setPagination(totalPage, page);
        //size*(page-1)
        //offSet相当与数据库查询语句中的 limit offset,size
        Integer offSet = size * (page - 1);
        //全查问题 把查询到的问题放到一个集合中
        // v1根据传过来的offset和size做出查询问题分页效果
        //所有这里的questions查出来的不是全部问题
        List<Question> questions = questionMapper.list(offSet, size);
        //创建QuestionDTO的集合对象
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        //这里做的目的是想把查出来的问题赋值给DTO类来负责向view传输数据
        for (Question question : questions) {
            //根据用户的ID来查出用户
            User user = userMapper.findById(question.getCreator());
            //创建一个DTO对象
            QuestionDTO questionDTO = new QuestionDTO();
            //BeanUtils.copyProperties 把一个对象赋值给另个对象，避免了重复get和set的操作
            BeanUtils.copyProperties(question, questionDTO);
            //把user对象赋值给questionDTO中的user对象
            questionDTO.setUser(user);
            //将上面获得的对象添加到questionDTOList中
            questionDTOList.add(questionDTO);
        }

        //把questionDTO封装到paginationDTO中
        paginationDTO.setQuestions(questionDTOList);
        //返回包含分页数据的paginationDTO对象
        return paginationDTO;
    }

    /**
     * 根据用户的ID查询出用户发出的问题数
     * 并且根据传来的page和size来设置当前的分页数据
     *
     * @param userId 用户的ID
     * @param page 当前页码
     * @param size 当前页码显示问题数量
     */
    public PaginationDTO list(Integer userId, Integer page, Integer size) {
        //这里的pagination对象封装了questionDTO 加上分页的数据
        PaginationDTO paginationDTO = new PaginationDTO();
        //创建总页数
        Integer totalPage;
        //根据用户ID查询出用户发布的问题数量
        Integer totalCount = questionMapper.countQuestionByUserId(userId);
        //判断一共有多少页
        totalPage = (totalCount % size == 0) ? (totalCount / size) : (totalCount / size + 1);
        //对当前的page做边界判断 page不能小于1也不能大于totalPage总数
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        //调用方法  通过方法将paginationDTO中关于分页的数据赋值
        paginationDTO.setPagination(totalPage, page);
        //size*(page-1)
        //offSet相当与数据库查询语句中的 limit offset,size
        Integer offSet = size * (page - 1);
        //根据用户的ID查询出用户发布的问题 把查询到的问题放到一个集合中
        //根据offset和size做出查询问题分页效果
        List<Question> questions = questionMapper.listByUserId(userId,offSet, size);
        //创建QuestionDTO的集合对象
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        //这里做的目的是想把查出来的问题赋值给DTO类来负责向view传输数据
        for (Question question : questions) {
            //根据用户的ID来查出用户
            User user = userMapper.findById(question.getCreator());
            //创建一个DTO对象
            QuestionDTO questionDTO = new QuestionDTO();
            //BeanUtils.copyProperties 把一个对象赋值给另个对象，避免了重复get和set的操作
            BeanUtils.copyProperties(question, questionDTO);
            //把user对象赋值给questionDTO中的user对象
            questionDTO.setUser(user);
            //将上面获得的对象添加到questionDTOList中
            questionDTOList.add(questionDTO);
        }

        //把questionDTO封装到paginationDTO中
        paginationDTO.setQuestions(questionDTOList);
        //返回包含分页数据的paginationDTO对象
        return paginationDTO;
    }


    /**
     * questionDTO一个包含user对象的DTO类
     * 查询出用户发布的问题 再将user传入QuestionDTO中
     * @param id 用户ID
     * @return 返回跟用户相关的questionDTO
     */
    public QuestionDTO getById(Integer id) {
        //创建question对象
        Question question = questionMapper.getById(id);
        //创建questionDTO对象
        QuestionDTO questionDTO = new QuestionDTO();
        //BeanUtils.copyProperties 把一个对象赋值给另个对象，避免了重复get和set的操作
        BeanUtils.copyProperties(question,questionDTO);
        //根据userId创建user将user对象赋值给questionDTO
        User user = userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }
}
