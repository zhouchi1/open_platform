package com.zhouzhou.cloud.common.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@ApiModel
@Getter
@Setter
public class BasePageOrderReq extends BasePageReq {

  @ApiModelProperty("排序字段")
  protected Set<String> orderFields;

  @ApiModelProperty("升降序")
  protected boolean isAsc;

}
