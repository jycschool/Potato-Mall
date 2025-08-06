// src/api/comment.ts
import { axios } from '../utils/request'

// 获取商品评论列表
export function getCommentsByProductId(productId: string) {
    return axios({
        url: `/api/comments/product/${productId}`,
        method: 'get'
    })
}

// 获取用户评论列表
export function getCommentsByUsername(username: string) {
    return axios({
        url: `/api/comments/user/${username}`,
        method: 'get'
    })
}

// 添加评论
export function addComment(data: {
    productId: number,
    username: string,
    content: string,
    rating: number
}) {
    return axios({
        url: '/api/comments',
        method: 'post',
        data
    })
}

// 更新评论
export function updateComment(data: {
    id: number,
    productId: number,
    username: string,
    content: string,
    rating: number
}) {
    return axios({
        url: `/api/comments/${data.id}`,
        method: 'put',
        data
    })
}

// 删除评论
export function deleteComment(id: number) {
    return axios({
        url: `/api/comments/${id}`,
        method: 'delete'
    })
}