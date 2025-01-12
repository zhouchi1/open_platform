package com.zhouzhou.cloud.common.resp;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author sunqingrui
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
@Builder
public class BaseUrlResp extends BaseAMO {


    private static final long serialVersionUID = -6788312595539982331L;


    @ApiModelProperty(value = "地址")
    protected List<String> urls;

}
