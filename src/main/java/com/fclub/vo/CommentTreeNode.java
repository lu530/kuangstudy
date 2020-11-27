package com.fclub.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fclub.pojo.TreeNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class CommentTreeNode extends TreeNode {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "评论唯一id")
    private String commentId;

    @ApiModelProperty(value = "1博客 2问答")
    private Integer topicCategory;

    @ApiModelProperty(value = "评论主题id")
    private String topicId;

    @ApiModelProperty(value = "评论者id")
    private String userId;

    @ApiModelProperty(value = "评论者昵称")
    private String userName;

    @ApiModelProperty(value = "评论者头像")
    private String userAvatar;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "评论创建时间")
    private Date gmtCreate;

    @ApiModelProperty(value = "父节点ID")
    private Integer parentId;

    public CommentTreeNode(int nid, int pid, Integer id, String commentId, Integer topicCategory, String topicId, String userId, String userName, String userAvatar, String content, Date gmtCreate, Integer parentId) {
        super(nid, pid);
        this.id = id;
        this.commentId = commentId;
        this.topicCategory = topicCategory;
        this.topicId = topicId;
        this.userId = userId;
        this.userName = userName;
        this.userAvatar = userAvatar;
        this.content = content;
        this.gmtCreate = gmtCreate;
        this.parentId = parentId;
    }

    public CommentTreeNode(int nid, int pid){
        super(nid, pid);
    }

    public CommentTreeNode(){
        super();
    }






}
