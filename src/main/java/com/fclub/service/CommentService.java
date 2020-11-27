package com.fclub.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.fclub.pojo.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fclub.vo.CommentTreeNode;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 遇见狂神说
 * @since 2020-06-30
 */
public interface CommentService extends IService<Comment> {

    List<CommentTreeNode> tree(Wrapper<Comment> queryWrapper);

}
