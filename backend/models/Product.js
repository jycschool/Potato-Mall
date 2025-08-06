// ...existing code...

module.exports = (sequelize, DataTypes) => {
  const Product = sequelize.define('Product', {
    // ...existing code...
    
    type: {
      type: DataTypes.STRING,
      allowNull: false,
      validate: {
        isIn: [['NORMAL', 'VIRTUAL', 'MYSTERY_BOX']]  // 添加MYSTERY_BOX类型
      }
    },
    
    // 盲盒可能包含的物品列表
    possibleItems: {
      type: DataTypes.JSON,
      defaultValue: [],
      // 格式: [
      //   {productId: 1, weight: 10, rarity: 'COMMON'},
      //   {productId: 2, weight: 5, rarity: 'UNCOMMON'},
      //   {productId: 3, weight: 3, rarity: 'RARE'},
      //   {productId: 4, weight: 1, rarity: 'LEGENDARY'}
      // ]
    },
    
    // ...existing code...
  });

  // ...existing code...

  return Product;
};
