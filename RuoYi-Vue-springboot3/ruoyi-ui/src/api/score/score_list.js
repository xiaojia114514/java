import request from '@/utils/request'

// 查询考试成绩单列表
export function listScore_list(query) {
  return request({
    url: '/score/score_list/list',
    method: 'get',
    params: query
  })
}

// 查询考试成绩单详细
export function getScore_list(scoreId) {
  return request({
    url: '/score/score_list/' + scoreId,
    method: 'get'
  })
}

// 新增考试成绩单
export function addScore_list(data) {
  return request({
    url: '/score/score_list',
    method: 'post',
    data: data
  })
}

// 修改考试成绩单
export function updateScore_list(data) {
  return request({
    url: '/score/score_list',
    method: 'put',
    data: data
  })
}

// 删除考试成绩单
export function delScore_list(scoreId) {
  return request({
    url: '/score/score_list/' + scoreId,
    method: 'delete'
  })
}
