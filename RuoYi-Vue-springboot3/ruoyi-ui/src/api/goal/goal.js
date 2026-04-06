import request from '@/utils/request'

// 查询课程目标列表
export function listGoal(query) {
  return request({
    url: '/goal/goal/list',
    method: 'get',
    params: query
  })
}

// 查询课程目标详细
export function getGoal(goalId) {
  return request({
    url: '/goal/goal/' + goalId,
    method: 'get'
  })
}

// 新增课程目标
export function addGoal(data) {
  return request({
    url: '/goal/goal',
    method: 'post',
    data: data
  })
}

// 修改课程目标
export function updateGoal(data) {
  return request({
    url: '/goal/goal',
    method: 'put',
    data: data
  })
}

// 删除课程目标
export function delGoal(goalId) {
  return request({
    url: '/goal/goal/' + goalId,
    method: 'delete'
  })
}
