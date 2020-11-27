package com.fclub.utils;


import com.fclub.pojo.TreeNode;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TreeUtils {
    public static <T extends TreeNode> List<T> buildTree(List<T> nodes) {
        Map<Integer, List<TreeNode>> sub = nodes.stream().filter(node -> node.getPid() != 0).collect(Collectors.groupingBy(node -> node.getPid()));
        nodes.forEach(node -> node.setSub(sub.get(node.getNid())));
        return nodes.stream().filter(node -> node.getPid() == 0).collect(Collectors.toList());
    }
}
