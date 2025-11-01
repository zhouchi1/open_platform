package com.zhouzhou.cloud.common.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;


@Data
public class BaseStringReq implements Serializable {
    private static final long serialVersionUID = -6916454601322177663L;

    /**
     * 字典编号
     **/
    @Schema(name = "入参字符串（实体复用）")
    private String str;
}
