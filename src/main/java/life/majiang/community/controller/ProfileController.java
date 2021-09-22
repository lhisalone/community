package life.majiang.community.controller;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.model.User;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author a123
 * 关于我的中心页面展示的控制器
 */
@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    /**
     *
     * @param action 获取url上的同名变量
     * @param page 当前页码
     * @param size 当前页码问题数量
     * @param request 代表客户端的请求 用于获取客户端发送的信息
     * @param model 后台要从控制层直接返回前端所需的数据
     * @return
     */
    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action")String action,
                          //将请求参数绑定到你控制器的方法参数上 如果没有参数就使用默认值
                          @RequestParam(name="page",defaultValue = "1")Integer page,
                          @RequestParam(name="size",defaultValue = "5")Integer size,
                          HttpServletRequest request,
                          Model model){
        //从request中获取session的值
        User user =(User) request.getSession().getAttribute("user");
        //如果没有用户就返回首页
        if (user == null) {
            return  "redirect:/";
        }
        //我的问题的标签数组
        //questions 我的问题 列表
        //replies 最新回复 列表
        String[] tag = {"questions","replies"};
        //从页面上返回的action的来判断当前页面是哪个页面 并返回相应的数据作为页面上的展示
        if (tag[0].equals(action)) {
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的问题");
        } else if (tag[1].equals(action)) {
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }
        //这个方法根据当前的用户ID查询出问题相关信息 并创建包含QuestionDTO和User对象的PaginationDTO
        PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
        //将查询出来关于用户的分页问题添加到model中 对客户端做出显示
        model.addAttribute("pagination",paginationDTO);
        return "profile";
    }

}
