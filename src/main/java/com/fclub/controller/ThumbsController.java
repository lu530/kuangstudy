package com.fclub.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fclub.dto.ResultDTO;
import com.fclub.pojo.Thumbs;
import com.fclub.service.ThumbsService;
import com.fclub.utils.KuangUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 遇见狂神说
 * @since 2020-11-24
 */
@RestController
@RequestMapping("/thumbs")
public class ThumbsController {

    @Autowired
    ThumbsService thumbsService;

    @PostMapping("add")
    public Object thumb(Thumbs thumbs){
        // 存储评论
        thumbs.setThumbsId(KuangUtils.getUuid());
        thumbs.setGmtCreate(KuangUtils.getTime());

        int count = thumbsService.count(new QueryWrapper<Thumbs>().eq("topic_id", thumbs.getTopicId()).eq("user_id", thumbs.getUserId()));
        if(count > 0){
            return ResultDTO.errorOf(-1, "请勿对该内容重复点击踩赞！");
        }
        thumbsService.save(thumbs);
        return ResultDTO.okOf();
    }

}

