package com.zhouzhou.cloud.common.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class BaseEntityResp extends BaseAMO {
    private static final long serialVersionUID = 8504075453295815549L;
    @Schema(name = "id")
    private Long id;

    @Schema(name = "创建人")
    private String createNo;

    @Schema(name = "记录插入时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(name = "更新人")
    private String updateNo;

    @Schema(name = "记录最后更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
