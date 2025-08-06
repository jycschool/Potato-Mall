// ...existing code...

/**
 * 抽取盲盒
 */
exports.drawMysteryBox = async (req, res) => {
  try {
    const { id: itemId } = req.params;
    const userId = req.user.id;
    
    // 检查物品是否存在且属于当前用户
    const repositoryItem = await db.RepositoryItem.findOne({
      where: { 
        id: itemId,
        userId,
        type: 'MYSTERY_BOX',
        status: 'ACTIVE'
      }
    });
    
    if (!repositoryItem) {
      return res.json({
        code: 400,
        message: '盲盒不存在或已被抽取'
      });
    }
    
    // 获取盲盒对应的可能物品
    const mysteryBox = await db.Product.findByPk(repositoryItem.productId);
    if (!mysteryBox || !mysteryBox.possibleItems || mysteryBox.possibleItems.length === 0) {
      return res.json({
        code: 400,
        message: '盲盒数据异常'
      });
    }
    
    // 抽取逻辑 - 基于概率权重进行随机抽取
    const drawnItem = drawItemFromMysteryBox(mysteryBox.possibleItems);
    
    // 获取抽中物品的详细信息
    const product = await db.Product.findByPk(drawnItem.productId);
    if (!product) {
      return res.json({
        code: 400,
        message: '抽取的物品不存在'
      });
    }
    
    // 更新仓库记录 - 将盲盒转换为具体物品
    await repositoryItem.update({
      productId: product.id,
      type: product.type,
      name: product.name,
      description: product.description,
      imageUrl: product.imageUrl,
      status: 'ACTIVE',
      metadata: {
        ...repositoryItem.metadata,
        drawnFrom: mysteryBox.id,
        drawnAt: new Date(),
      }
    });
    
    // 构建返回数据
    const result = {
      id: product.id,
      name: product.name,
      description: product.description,
      imageUrl: product.imageUrl,
      rarity: drawnItem.rarity,
      rarityLabel: getRarityLabel(drawnItem.rarity),
    };
    
    return res.json({
      code: 200,
      message: '抽取成功',
      data: result
    });
    
  } catch (error) {
    console.error('抽取盲盒出错:', error);
    return res.json({
      code: 500,
      message: '服务器错误'
    });
  }
};

/**
 * 根据概率权重随机抽取盲盒中的物品
 */
function drawItemFromMysteryBox(possibleItems) {
  // 计算总权重
  const totalWeight = possibleItems.reduce((sum, item) => sum + (item.weight || 1), 0);
  
  // 生成随机数
  const random = Math.random() * totalWeight;
  
  // 根据权重选择物品
  let weightSum = 0;
  for (const item of possibleItems) {
    weightSum += (item.weight || 1);
    if (random <= weightSum) {
      return item;
    }
  }
  
  // 保底返回第一个物品
  return possibleItems[0];
}

/**
 * 获取稀有度标签
 */
function getRarityLabel(rarity) {
  const rarityMap = {
    'COMMON': '普通',
    'UNCOMMON': '优质',
    'RARE': '稀有',
    'EPIC': '史诗',
    'LEGENDARY': '传说'
  };
  
  return rarityMap[rarity] || '普通';
}

// ...existing code...
