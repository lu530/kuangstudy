package com.fclub.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fclub.pojo.Blog;
import com.fclub.pojo.Question;
import com.fclub.service.BlogService;
import com.fclub.service.QuestionService;
import com.fclub.utils.KuangUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    BlogService blogService;

    @Autowired
    QuestionService questionService;

    // 问题列表展示
    @GetMapping("/search")
    public String search(String kws, Model model){
        QueryWrapper<Question> query = new QueryWrapper();
        QueryWrapper<Blog> blogQuery = new QueryWrapper();
        Page<Question> pageParam = new Page<>(1, 5);
        Page<Blog> blogPageParam = new Page<>(1, 5);

        if(!StringUtils.isEmpty(kws)){
            query.like("title", kws).or().like("content", kws);
            blogQuery.like("title", kws).or().like("content", kws);
        }
        query.orderByDesc("gmt_create");
        blogQuery.orderByDesc("gmt_create");

        questionService.page(pageParam, query);
        List<Question> questionList = pageParam.getRecords();

        blogService.page(blogPageParam, blogQuery);
        List<Blog> blogList = blogPageParam.getRecords();

        model.addAttribute("questionList", questionList);
        model.addAttribute("blogList", blogList);
        model.addAttribute("total", blogList.size() + questionList.size() );

        model.addAttribute("kws", KuangUtils.toString(kws));

        return "index";
    }
}
