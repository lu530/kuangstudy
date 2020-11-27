package com.fclub.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fclub.mapper.CommentMapper;
import com.fclub.pojo.Comment;
import com.fclub.service.CommentService;
import com.fclub.utils.TreeUtils;
import com.fclub.vo.CommentTreeNode;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 遇见狂神说
 * @since 2020-06-30
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {


    @Override
    public List<CommentTreeNode> tree(Wrapper<Comment> queryWrapper) {
        List<Comment> list = super.list(queryWrapper);
        List<CommentTreeNode> treeNodes = new ArrayList<>();
        list.forEach(o->{
            int pid = o.getParentId() == null ? 0:o.getParentId();
            CommentTreeNode treeNode = new CommentTreeNode(o.getId(), pid);
            BeanUtils.copyProperties(o, treeNode);
            treeNodes.add(treeNode);
        });
        List<CommentTreeNode> tree = TreeUtils.buildTree(treeNodes);
        return tree;
    }

}
