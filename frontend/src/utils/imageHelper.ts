/**
 * 获取商品图片URL
 * 如果没有提供有效的图片URL，则返回占位图片
 * @param imageUrl 商品图片URL
 * @returns 处理后的图片URL
 */
export function getProductImageUrl(imageUrl: string | null | undefined): string {
  // 如果有有效的图片URL并且不是阿里云的URL，则返回该URL
  if (imageUrl &&
      typeof imageUrl === 'string' &&
      imageUrl.trim() !== '' &&
      !imageUrl.includes('img.alicdn.com')) {
    return imageUrl;
  }

  // 否则返回占位图片
  return '/placeholder.jpg';
}
