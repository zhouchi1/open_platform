package com.zhouzhou.cloud.common.service.interfaces;

import com.zhouzhou.cloud.common.entity.Promotion;
import com.zhouzhou.cloud.common.entity.ShopOrder;
import com.zhouzhou.cloud.common.entity.UserInfo;

import java.util.List;

public interface PromotionRpcServer {

    List<Promotion> getAvailablePromotions(UserInfo user, ShopOrder order);

    Promotion getPromotionById(String promotionId);
}
