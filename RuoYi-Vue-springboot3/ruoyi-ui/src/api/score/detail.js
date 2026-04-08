import request from '@/utils/request'

// 查询成绩细目列表
export function listDetail(query) {
  return request({
    url: '/score/detail/list',
    method: 'get',
    params: query
  })
}

// 查询成绩细目详细
export function getDetail(scoreDetailId) {
  return request({
    url: '/score/detail/' + scoreDetailId,
    method: 'get'
  })
}

// 新增成绩细目
export function addDetail(data) {
  return request({
    url: '/score/detail',
    method: 'post',
    data: data
  })
}

// 修改成绩细目
export function updateDetail(data) {
  return request({
    url: '/score/detail',
    method: 'put',
    data: data
  })
}

// 删除成绩细目
export function delDetail(scoreDetailId) {
  return request({
    url: '/score/detail/' + scoreDetailId,
    method: 'delete'
  })
}
