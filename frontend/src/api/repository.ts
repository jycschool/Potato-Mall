// src/api/repository.ts
import { axios } from '../utils/request'

// 获取用户购买记录
export function getUserPurchases() {
    return axios({
        url: '/api/user-repository',
        method: 'get'
    })
}

// 从购物车购买商品
export function purchaseFromCart(data: {
    address: string,
    contactPhone: string
}) {
    return axios({
        url: '/api/user-repository/purchase',
        method: 'post',
        params: data  // 使用params而不是data，因为后端使用@RequestParam
    })
}

// 评价商品
export function rateProduct(repositoryId: number, data: {
    rating: number,
    content: string
}) {
    return axios({
        url: `/api/user-repository/rate/${repositoryId}`,
        method: 'post',
        data
    })
}

// 抽取盲盒
export function drawMysteryBox(itemId: number) {
    return axios({
        url: `/api/user-repository/draw-mystery-box/${itemId}`,
        method: 'post'
    })
}
