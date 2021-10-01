package life.majiang.community.controller;

import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private QuestionService questionService;


    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }

    /**
     * 问题编辑功能 其实就是跳转回发布问题页面
     *
     * @param id 创建一个证明身份的id 用于辨识问题是否已经创建
     * @return 重定向回发布问题
     */
    @GetMapping("publish/{id}")
    public String edit(@PathVariable(name = "id") Integer id,
                       Model model) {
        QuestionDTO question = questionService.getById(id);
        //model返回数据到页面
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("id", question.getId());
        return "publish";
    }

    /**
     * 这个控制器会获取publish页面发送的post请求中的数据
     * 并把问题存入数据库中
     *
     * @param title       对应 publish.html中submit点击后中name为title的数据
     * @param description 对应 publish,html中from表单中的description
     * @param tag         对应 表单中的标签tag
     * @param id          id是从edit方法中创建的 用于辨识问题是否已经存在 创建问题的时候是没有id的 只有经过edit方法才会产生ID
     *                    required=false：请求路径中不需要一定包含该参数 不会报错
     * @return 返回到发布问题页面
     */
    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "id", required = false) Integer id,
            HttpServletRequest request,
            Model model
    ) {
        //model返回数据到页面
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        //根据title的内容返回到publish页面信息 提示错误
        if (title == null || "".equals(title)) {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (description == null || "".equals(description)) {
            model.addAttribute("error", "问题不能为空");
            return "publish";
        }
        if (tag == null || "".equals(tag)) {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }
        //取出之前session存的user
        User user = (User) request.getSession().getAttribute("user");
        //非空判断  我也不知道要抛什么异常 自定义异常还不会写 = =
        if (user == null) {
            model.addAttribute("error", "用户未登陆");
            return "publish";
        }
        //将上面获取到的值赋值给question对象
        Question question = new Question()
                .setTitle(title)
                .setDescription(description)
                .setTag(tag)
                .setCreator(user.getId())
                .setId(id);
        //将问题插入数据库或者更新原有的问题
        questionService.createOrUpdate(question);
        return "redirect:/";
    }
}






