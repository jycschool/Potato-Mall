// src/api/cart.ts
import { axios } from '../utils/request'

// 获取购物车列表
export function getCartList() {
    const username = sessionStorage.getItem('username')
    return axios({
        url: '/api/cart',
        method: 'get',
        params: { username }
    })
}

// 添加商品到购物车
export function addToCart(data: {
    productId: number,
    quantity: number
}) {
    const username = sessionStorage.getItem('username')
    return axios({
        url: '/api/cart',
        method: 'post',
        params: { username, productId: data.productId, quantity: data.quantity },
        data
    })
}

// 从购物车中移除商品
export function removeCartItem(productId: number) {
    const username = sessionStorage.getItem('username')
    return axios({
        url: `/api/cart/${productId}`,
        method: 'delete',
        params: { username }
    })
}

// 更新购物车商品数量
export function updateCartItem(productId: number, quantity: number) {
    const username = sessionStorage.getItem('username')
    return axios({
        url: `/api/cart/${productId}`,
        method: 'put',
        params: { username, quantity },
        data: { quantity }
    })
}

// 清空购物车
export function clearCart() {
    const username = sessionStorage.getItem('username')
    return axios({
        url: '/api/cart/clear',
        method: 'delete',
        params: { username }
    })
}

// 选择/取消选择购物车商品
export function selectCartItem(productId: number, selected: boolean) {
    const username = sessionStorage.getItem('username')
    return axios({
        url: `/api/cart/select/${productId}`,
        method: 'put',
        params: { username },
        data: { selected }
    })
}

// 获取已选择的购物车商品
export function getSelectedCartItems() {
    const username = sessionStorage.getItem('username')
    return axios({
        url: '/api/cart/selected',
        method: 'get',
        params: { username }
    })
}

// 获取购物车数量
export function getCartItemsCount(username) {
    return axios({
        url: `/api/cart/count?username=${username}`,
        method: 'get'
    })
}