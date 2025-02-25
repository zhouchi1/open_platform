package com.zhouzhou.cloud.productservice.service.impl;

import com.google.common.collect.Sets;
import com.zhouzhou.cloud.productservice.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public void addProductInfoByCartesian() {
        Set<String> set1 = new HashSet<>(Arrays.asList("A", "B"));
        Set<Integer> set2 = new HashSet<>(Arrays.asList(1, 2));
        Set<String> set3 = new HashSet<>(Arrays.asList("C", "D"));
        Set<List<Object>> product = Sets.cartesianProduct(set1, set2, set3);
        System.out.println(product);
    }
}
