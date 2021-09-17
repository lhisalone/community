package life.majiang.community.controller;

import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author a123
 * 关于发布问题的控制器方法
 */
@Controller
public class PublishController {

    @Autowired
    private QuestionMapper questionMapper;


    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }

    /**
     * 这个控制器会获取publish页面发送的post请求中的数据
     * 并把问题存入数据库中
     *
     * @param title       对应 publish.html中submit点击后中name为title的数据
     * @param description
     * @param tag
     * @return
     */
    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            HttpServletRequest request,
            Model model
    ) {
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        if (title == null || "".equals(title)) {
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if (description == null || "".equals(description)) {
            model.addAttribute("error","问题不能为空");
            return "publish";
        }
        if (tag== null || "".equals(tag)) {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }
        //取出之前session存的user
        User user = (User) request.getSession().getAttribute("user");
        //非空判断  我也不知道要抛什么异常 自定义异常还不会写 = =
        if (user == null) {
            model.addAttribute("error","用户未登陆");
            return "publish";
        }
        //将上面获取到的值赋值给question对象
        Question question = new Question()
                .setTitle(title)
                .setDescription(description)
                .setTag(tag)
                .setCreator(user.getId())
                .setGmtCreate(System.currentTimeMillis())
                .setGmtModified(System.currentTimeMillis());
        //将问题插入数据库
        questionMapper.create(question);
        return "publish";
    }
}






