package life.majiang.community.controller;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author a123
 * index的控制层
 */
@Controller
public class IndexController {

    @Resource
    private QuestionService questionService;

    /**
     * 主要是显示主页上的问题，登陆 问题页面展示，还有右边未开发的功能
     * v1：关于主页分页的展示
     * 1.将questionDTO这个类封装到PaginationDTO中 把PaginationDTO添加到model中
     * 提供给页面做分页的对象信息
     * @param model 将对象赋值到model 用于页面上的展示
     * @param page 表示当前页面上的页码 defaultValue 默认值为1
     * @param size 表示当前页面上一页展示多少个问题
     */
    @GetMapping("/")
    public String index(Model model,
                        HttpServletRequest request,
                        //将请求参数绑定到你控制器的方法参数上 如果没有参数就使用默认值
                        @RequestParam(name="page",defaultValue = "1")Integer page,
                        @RequestParam(name="size",defaultValue = "5")Integer size){

        //这里将查出 带User对象的Question集合
        //v1这里的改动：PaginationDTO 是实现分页的对象 里面封装了questionDTO 加上关于分页的信息
        PaginationDTO pagination = questionService.getPaginationDTO(page,size);
        //这时候往model里赋值的question就带有user对象属性
        model.addAttribute("pagination",pagination);
        return "index";
    }
}
