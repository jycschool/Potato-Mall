import { axios } from '../utils/request';

// 获取动态列表（分页）
export function getMoments(page = 0, size = 10) {
  return axios({
    url: '/api/moments',
    method: 'get',
    params: { page, size }
  });
}

// 获取用户的动态
export function getUserMoments(username: string) {
  return axios({
    url: `/api/moments/user/${username}`,
    method: 'get'
  });
}

// 发布动态
export function createMoment(content: string, images: string | null) {
  const formData = new FormData();
  formData.append('content', content);
  if (images) {
    formData.append('images', images);
  }

  return axios({
    url: '/api/moments',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}

// 删除动态
export function deleteMoment(momentId: number) {
  return axios({
    url: `/api/moments/${momentId}`,
    method: 'delete'
  });
}

// 点赞动态
export function likeMoment(momentId: number) {
  return axios({
    url: `/api/moments/${momentId}/like`,
    method: 'post'
  });
}
