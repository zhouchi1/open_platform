package com.zhouzhou.cloud.common.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class BasePageOrderReq extends BasePageReq {

  @Schema(name = "排序字段")
  protected Set<String> orderFields;

  @Schema(name = "升降序")
  protected boolean isAsc;

}
