package com.zhouzhou.cloud.common.resp;

import com.google.common.collect.Lists;
import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;


@EqualsAndHashCode(callSuper = true)
@Data
public class BaseListResp<T> extends BaseAMO {

    private static final long serialVersionUID = -8997670094316032406L;

    @Schema(name = "列表")
    private List<T> records = Lists.newArrayList();

    public static <T> BaseListResp<T> build(List<T> list) {
        BaseListResp<T> result = new BaseListResp<>();
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        result.setRecords(list);
        return result;
    }

    public static <T extends BaseAMO> BaseListResp<T> convertBuild(List<?> list, Class<T> rClass) {
        List<T> newList = list.stream().map(bean -> {
            T resp;
            try {
                resp = rClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            BeanUtils.copyProperties(bean, resp);
            return resp;
        }).collect(Collectors.toList());
        BaseListResp<T> result = new BaseListResp<>();
        if (CollectionUtils.isEmpty(newList)) {
            return result;
        }
        result.setRecords(newList);
        return result;
    }
}
