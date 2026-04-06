import request from '@/utils/request'

// 查询试卷实质审查列表
export function listReview(query) {
  return request({
    url: '/review/review/list',
    method: 'get',
    params: query
  })
}

// 查询试卷实质审查详细
export function getReview(reviewId) {
  return request({
    url: '/review/review/' + reviewId,
    method: 'get'
  })
}

// 新增试卷实质审查
export function addReview(data) {
  return request({
    url: '/review/review',
    method: 'post',
    data: data
  })
}

// 修改试卷实质审查
export function updateReview(data) {
  return request({
    url: '/review/review',
    method: 'put',
    data: data
  })
}

// 删除试卷实质审查
export function delReview(reviewId) {
  return request({
    url: '/review/review/' + reviewId,
    method: 'delete'
  })
}
