package com.fclub.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TreeNode {
    private int nid;
    private int pid;
    private String name;
    private List<TreeNode> sub = new ArrayList<>();
    public TreeNode(int nid, int pid) {
        this.nid = nid;
        this.pid = pid;
    }

    public TreeNode() {

    }



    public int getNid() {
        return nid;
    }

    public void setNid(int id) {
        this.nid = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TreeNode> getSub() {
        return sub;
    }

    public void setSub(List<TreeNode> sub) {
        this.sub = sub;
    }
}
