package com.fclub.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 遇见狂神说
 * @since 2020-11-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ks_thumbs")
@ApiModel(value="Thumbs对象", description="")
public class Thumbs implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "评论唯一id")
    private String thumbsId;

    @ApiModelProperty(value = "1博客 2问答 3资源")
    private Integer topicCategory;

    @ApiModelProperty(value = "评论主题id")
    private String topicId;

    @ApiModelProperty(value = "评论者id")
    private String userId;

    @ApiModelProperty(value = "-1踩 1赞同")
    private Integer thumbsFlag;

    @ApiModelProperty(value = "评论创建时间")
    private Date gmtCreate;


}
