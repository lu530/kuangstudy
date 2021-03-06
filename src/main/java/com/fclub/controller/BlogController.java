package com.fclub.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fclub.cache.ViewsCache;
import com.fclub.pojo.*;
import com.fclub.service.BlogCategoryService;
import com.fclub.service.BlogService;
import com.fclub.service.CommentService;
import com.fclub.service.ThumbsService;
import com.fclub.utils.KuangUtils;
import com.fclub.vo.CommentTreeNode;
import com.fclub.vo.QuestionWriteForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 遇见狂神说
 * @since 2020-06-29
 */
@Controller
public class BlogController {

    @Autowired
    BlogCategoryService blogCategoryService;
    @Autowired
    BlogService blogService;
    @Autowired
    CommentService commentService;


    @Autowired
    ThumbsService thumbsService;

    @Autowired
    ViewsCache viewsCache;

    // 列表展示
    @GetMapping("/blog")
    public String blogList(String kws, Model model){
        QueryWrapper<Blog> query = new QueryWrapper();
        Page<Blog> pageParam = new Page<>(1, 10);
        if(!StringUtils.isEmpty(kws)){
            query.like("title", kws).or().like("content", kws);
        }
        query.orderByDesc("gmt_create");
        blogService.page(pageParam,query);

        // 结果
        List<Blog> blogList = pageParam.getRecords();
        model.addAttribute("blogList",blogList);
        model.addAttribute("pageParam",pageParam);

        // 分类信息
        List<BlogCategory> categoryList = blogCategoryService.list(null);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("kws", KuangUtils.toString(kws));

        return "blog/list";
    }

    @GetMapping("/blog/{page}/{limit}")
    public String blogListPage(
            String kws,
            @PathVariable int page,
            @PathVariable int limit,
            Model model){
        QueryWrapper<Blog> query = new QueryWrapper();

        if (page < 1){
            page = 1;
        }

        if(!StringUtils.isEmpty(kws)){
            query.like("title", kws).or().like("content", kws);
        }

        query.orderByDesc("gmt_create");
        Page<Blog> pageParam = new Page<>(page, limit);
        blogService.page(pageParam,query);

        // 结果
        List<Blog> blogList = pageParam.getRecords();
        model.addAttribute("blogList",blogList);
        model.addAttribute("pageParam",pageParam);

        // 分类信息
        List<BlogCategory> categoryList = blogCategoryService.list(null);
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("kws",KuangUtils.toString(kws));
        return "blog/list";
    }

    // 写文章
    @GetMapping("/blog/write")
    public String toWrite(Model model){
        List<BlogCategory> categoryList = blogCategoryService.list(null);
        model.addAttribute("categoryList",categoryList);
        return "blog/write";
    }

    @PostMapping("/blog/write")
    public synchronized String write(QuestionWriteForm questionWriteForm){
        // 构建问题对象
        Blog blog = new Blog();

        blog.setBid(KuangUtils.getUuid());
        blog.setTitle(questionWriteForm.getTitle());
        blog.setContent(questionWriteForm.getContent());

        String contentWithOutPic = questionWriteForm.getContent().replaceAll("!\\[\\]\\(.*\\)", "");

        String subTitle = contentWithOutPic.length() < 130? contentWithOutPic:contentWithOutPic.replaceAll("\n","").substring(0,130);
        blog.setSubtitle(subTitle);
        blog.setSort(0);
        blog.setViews(0);

        blog.setAuthorId(questionWriteForm.getAuthorId());
        blog.setAuthorName(questionWriteForm.getAuthorName());
        blog.setAuthorAvatar(questionWriteForm.getAuthorAvatar());

        BlogCategory category = blogCategoryService.getById(questionWriteForm.getCategoryId());
        blog.setCategoryId(questionWriteForm.getCategoryId());
        blog.setCategoryName(category.getCategory());
        blog.setGmtCreate(KuangUtils.getTime());
        blog.setGmtUpdate(KuangUtils.getTime());
        // 存储对象
        blogService.save(blog);

        // 重定向到列表页面
        return "redirect:/blog";
    }

    // 阅读文章
    @GetMapping("/blog/read/{bid}")
    public String read(@PathVariable("bid") String bid,Model model, HttpServletRequest req){
        Blog blog = blogService.getOne(new QueryWrapper<Blog>().eq("bid", bid));
        // 防止阅读重复
        String ip = KuangUtils.getIP(req);
        if(StringUtils.isEmpty(viewsCache.getIpBid(ip + "_" + bid))){
            blog.setViews(blog.getViews()+1);
            viewsCache.putIpBid(ip + "_" + bid, bid);
        }
        blogService.updateById(blog);
        model.addAttribute("blog",blog);
       // List<Comment> commentList = commentService.list(new QueryWrapper<Comment>().eq("topic_id", bid).orderByDesc("gmt_create"));
        List<CommentTreeNode> commentList = commentService.tree(new QueryWrapper<Comment>().eq("topic_id", bid).orderByDesc("gmt_create"));

        List<Map<String, Object>> maps = thumbsService.listMaps(new QueryWrapper<Thumbs>().select("thumbs_flag", "count(1) as count").eq("topic_id", bid).groupBy("thumbs_flag"));

        Map<String, Object> thumbs
                = maps.stream().collect(Collectors.toMap(item -> StringUtils.toString(item.get("thumbs_flag")), item -> StringUtils.toString(item.get("count")), (oldValue, newValue) -> newValue));

        model.addAttribute("commentList",commentList);
        model.addAttribute("thumbs", thumbs);
        return "blog/read";
    }

    // 编辑问题
    @GetMapping("/blog/editor/{uid}/{bid}")
    public synchronized String toEditor(@PathVariable("uid") String uid,
                           @PathVariable("bid") String bid,Model model){

        Blog blog = blogService.getOne(new QueryWrapper<Blog>().eq("bid", bid));

        if (!blog.getAuthorId().equals(uid)){
            KuangUtils.print("禁止非法编辑");
            return "redirect:/blog";
        }

        model.addAttribute("blog",blog);

        List<BlogCategory> categoryList = blogCategoryService.list(null);
        model.addAttribute("categoryList",categoryList);

        return "blog/editor";
    }

    @PostMapping("/blog/editor")
    public String editor(Blog blog){
        Blog queryBlog = blogService.getOne(new QueryWrapper<Blog>().eq("bid", blog.getBid()));

        queryBlog.setTitle(blog.getTitle());
        queryBlog.setCategoryId(blog.getCategoryId());
        queryBlog.setContent(blog.getContent());
        queryBlog.setGmtUpdate(KuangUtils.getTime());
        String contentWithOutPic = blog.getContent().replaceAll("!\\[\\]\\(.*\\)", "");
        String subTitle = contentWithOutPic.length() < 130? contentWithOutPic:contentWithOutPic.replaceAll("\n","").substring(0,130);
        queryBlog.setSubtitle(subTitle);
        blogService.updateById(queryBlog);

        return "redirect:/blog/read/"+blog.getBid();
    }

    // 删除问题
    @GetMapping("/blog/delete/{uid}/{bid}")
    public String delete(@PathVariable("uid") String uid,
                         @PathVariable("bid") String bid){

        Blog blog = blogService.getOne(new QueryWrapper<Blog>().eq("bid", bid));

        if (!blog.getAuthorId().equals(uid)){
            KuangUtils.print("禁止非法删除");
            return "redirect:/blog";
        }

        blogService.removeById(blog);
        // 重定向到列表页面
        return "redirect:/blog";
    }

    // 评论
    @PostMapping("/blog/comment/{bid}")
    public String comment(@PathVariable("bid") String bid, Comment comment){
        // 存储评论
        comment.setCommentId(KuangUtils.getUuid());
        comment.setTopicCategory(1);
        comment.setGmtCreate(KuangUtils.getTime());
        commentService.save(comment);
        // 重定向到列表页面   难道需要写一篇炸弹式的文章吸引流量吗？
        return "redirect:/blog/read/"+bid;
    }




}

