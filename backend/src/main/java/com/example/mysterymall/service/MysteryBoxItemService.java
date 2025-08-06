package com.example.mysterymall.service;

import com.example.mysterymall.po.MysteryBoxItem;
import com.example.mysterymall.vo.Response;

import java.util.List;
import java.util.Map;

/**
 * 盲盒内容服务接口
 */
public interface MysteryBoxItemService {

    /**
     * 根据产品ID获取盲盒可能的内容
     * @param productId 产品ID（盲盒ID）
     * @return 盲盒内容列表
     */
    Response<List<MysteryBoxItem>> getItemsByProductId(Long productId);

    /**
     * 添加盲盒内容
     * @param mysteryBoxItem 盲盒内容信息
     * @return 添加的内容信息
     */
    Response<MysteryBoxItem> addMysteryBoxItem(MysteryBoxItem mysteryBoxItem);

    /**
     * 更新盲盒内容
     * @param itemId 内容ID
     * @param mysteryBoxItem 更新的内容信息
     * @return 更新后的内容信息
     */
    Response<MysteryBoxItem> updateMysteryBoxItem(Long itemId, MysteryBoxItem mysteryBoxItem);

    /**
     * 删除盲盒内容
     * @param itemId 内容ID
     * @return 操作结果
     */
    Response<String> deleteMysteryBoxItem(Long itemId);

    /**
     * 从盲盒中随机抽取一个物品（基于概率）
     * @param productId 产品ID（盲盒ID）
     * @return 抽取结果，包含物品信息
     */
    Response<Map<String, Object>> drawRandomItem(Long productId);

    /**
     * 检查盲盒内容是否已配置
     * @param productId 产品ID���盲盒ID）
     * @return 是否已配置
     */
    Response<Boolean> isItemsConfigured(Long productId);

    /**
     * 验证盲盒物品概率总和是否合理
     * @param productId 产品ID（盲盒ID）
     * @return 验证结果，包含总概率值
     */
    Response<Map<String, Object>> validateProbabilities(Long productId);

    /**
     * 更新盲盒物品的库存数量
     * @param itemId 物品ID
     * @param stockChange 库存���化量（正数增加，负数减少）
     * @return 更新后的库存
     */
    Response<Integer> updateItemStock(Long itemId, Integer stockChange);
}
