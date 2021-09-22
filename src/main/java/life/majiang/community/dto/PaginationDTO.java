package life.majiang.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 关于分页功能的DTO
 *
 * @author a123
 */
@Data
public class PaginationDTO {
    private List<QuestionDTO> questions;
    private Boolean showPrevious;
    private Boolean showFirstPage;
    private Boolean showNext;
    private Boolean showEndPage;
    /**
     * 当前页码
     */
    private Integer page;
    /**
     * pages：当前页面上显示的页面标签  3，4，5，6，7
     */
    private List<Integer> pages;
    /**
     * totalPage表示一共有多少页
     */
    private Integer totalPage;

    /**
     * 这个方法主要是根据问题的数量 来判断分页当前多少页 以及总页数是多少
     * 在第一页的时候 分页上没有显示上一页的按钮 所有要修改showFirstPage的boolean
     * 在最后一页的时候 分页上没有显示下一页的按钮 所有要修改showEndPage的boolean
     * 因为在页面中的分页标签中有回到首页和跳到末页的按钮
     * 所有还要做出 判断是否有回到首页和跳到末页 的方法
     * 分页中的页码也要做出逻辑上的判断 当在第一页的时候 分页上还要显示出 2 ，3，4页
     * 在中间页的时候 还要显示 前面几页跟后面几页
     *
     * @param totalPage  总页数
     * @param page       当前页码
     */
    public void setPagination(Integer totalPage, Integer page) {
        //pages显示的是当前分页上显示的页数 总共显示7页
        pages = new ArrayList<>();
        //将传过来的totalPage赋值给DTO中的totalPage
        this.totalPage=totalPage;
        //将传过来的page赋值给DTO中的page
        this.page = page;
        //把当前页码添加到pages集合中
        pages.add(page);
        //这里的p的意思是当前往前显示3页 往后显示3页 pages.size()=p*2+1
        int p = 3;
        //判断当前显示的页码
        for (int i = 1; i <= p; i++) {
            //往集合的头部插入页码 意思就是显示前3页
            if (page - i > 0) {
                pages.add(0, page - i);
            }
            //往集合的尾部插入页码 意思就是显示后3页
            if (page + i <= totalPage) {
                pages.add(page + i);
            }
        }

        //判断是否显示上一页
        showPrevious= !page.equals(1);
        //判断是否显示下一页
        showNext = !page.equals(totalPage);
        //判断是否展示回到第一页
        showFirstPage = !pages.contains(1);
        //判断是否展示跳到末页
        showEndPage = !pages.contains(totalPage);
    }
}
