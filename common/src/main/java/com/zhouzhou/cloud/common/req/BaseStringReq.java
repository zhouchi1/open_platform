package com.zhouzhou.cloud.common.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class BaseStringReq implements Serializable {
    private static final long serialVersionUID = -6916454601322177663L;

    /**
     * 字典编号
     **/
    @ApiModelProperty("入参字符串（实体复用）")
    private String str;
}
