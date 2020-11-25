package com.kuang.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.kuang.pojo.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.pojo.TreeNode;
import com.kuang.vo.CommentTreeNode;

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
