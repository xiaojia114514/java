import request from '@/utils/request'

// 查询考试成绩单列表
export function listScore(query) {
  return request({
    url: '/score/score/list',
    method: 'get',
    params: query
  })
}

// 查询考试成绩单详细
export function getScore(scoreId) {
  return request({
    url: '/score/score/' + scoreId,
    method: 'get'
  })
}

// 新增考试成绩单
export function addScore(data) {
  return request({
    url: '/score/score',
    method: 'post',
    data: data
  })
}

// 修改考试成绩单
export function updateScore(data) {
  return request({
    url: '/score/score',
    method: 'put',
    data: data
  })
}

// 删除考试成绩单
export function delScore(scoreId) {
  return request({
    url: '/score/score/' + scoreId,
    method: 'delete'
  })
}
