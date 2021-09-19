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
     * 将model中的question查出
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
        //查询出问题的总数
        Integer totalCount = questionMapper.countQuestion();
        //调用方法  通过方法将paginationDTO中关于分页的数据赋值
        paginationDTO.setPagination(totalCount, page, size);
        //对当前的page做边界判断 page不能小于1也不能大于totalPage总数
        if (page < 1) {
            page = 1;
        }
        if (page > paginationDTO.getTotalPage()) {
            page = paginationDTO.getTotalPage();
        }
        //size*(page-1)
        //offSet相当与数据库查询语句中的 limit offset,size
        Integer offSet = size * (page - 1);
        //全查问题 把查询到的问题放到一个集合中
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


}
