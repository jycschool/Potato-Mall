// src/api/product.ts
import { axios } from '../utils/request'

// 获取商品列表
export function getProductList(params: {
    page?: number,
    size?: number,
    categoryId?: string,
    keyword?: string,
    sortBy?: string,
    sortDirection?: 'asc' | 'desc'
}) {
    return axios({
        url: '/api/products',
        method: 'get',
        params
    })
}

// 获取商品详情
export function getProductDetail(id: string) {
    return axios({
        url: `/api/products/${id}`,
        method: 'get'
    })
}

// 获取商品分类
export function getCategoryList() {
    return axios({
        url: '/api/categories',
        method: 'get'
    })
}

// 获取热门盲盒列表
export function getHotProducts(limit: number = 5) {
    return axios({
        url: '/api/products/hot',
        method: 'get',
        params: { limit }
    })
}

// 创建商品（商家角色）
export function createProduct(data: {
    name: string,
    price: number,
    stock: number,
    description: string,
    categoryId: string,
    images: string[],
    attributes?: Record<string, any>
}) {
    return axios({
        url: '/api/products',
        method: 'post',
        data
    })
}

// 更新商品（商家角色）
export function updateProduct(id: string, data: any) {
    return axios({
        url: `/api/products/${id}`,
        method: 'put',
        data
    })
}

// 删除商品（商家角色）
export function deleteProduct(id: string) {
    return axios({
        url: `/api/products/${id}`,
        method: 'delete'
    })
}