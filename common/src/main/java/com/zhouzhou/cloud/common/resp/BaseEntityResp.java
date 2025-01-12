package com.zhouzhou.cloud.common.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class BaseEntityResp extends BaseAMO {
    private static final long serialVersionUID = 8504075453295815549L;
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("创建人")
    private String createNo;

    @ApiModelProperty("记录插入时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("更新人")
    private String updateNo;

    @ApiModelProperty("记录最后更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
