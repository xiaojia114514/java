import request from '@/utils/request'

// 查询目标达成度列表
export function listAchievement(query) {
  return request({
    url: '/achievement/achievement/list',
    method: 'get',
    params: query
  })
}

// 查询目标达成度详细
export function getAchievement(achievementId) {
  return request({
    url: '/achievement/achievement/' + achievementId,
    method: 'get'
  })
}

// 新增目标达成度
export function addAchievement(data) {
  return request({
    url: '/achievement/achievement',
    method: 'post',
    data: data
  })
}

// 修改目标达成度
export function updateAchievement(data) {
  return request({
    url: '/achievement/achievement',
    method: 'put',
    data: data
  })
}

// 删除目标达成度
export function delAchievement(achievementId) {
  return request({
    url: '/achievement/achievement/' + achievementId,
    method: 'delete'
  })
}
