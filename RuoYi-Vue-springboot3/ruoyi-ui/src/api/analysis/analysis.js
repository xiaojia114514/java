import request from '@/utils/request'

// 查询达成度分析列表
export function listAnalysis(query) {
  return request({
    url: '/analysis/analysis/list',
    method: 'get',
    params: query
  })
}

// 查询达成度分析详细
export function getAnalysis(analysisId) {
  return request({
    url: '/analysis/analysis/' + analysisId,
    method: 'get'
  })
}

// 新增达成度分析
export function addAnalysis(data) {
  return request({
    url: '/analysis/analysis',
    method: 'post',
    data: data
  })
}

// 修改达成度分析
export function updateAnalysis(data) {
  return request({
    url: '/analysis/analysis',
    method: 'put',
    data: data
  })
}

// 删除达成度分析
export function delAnalysis(analysisId) {
  return request({
    url: '/analysis/analysis/' + analysisId,
    method: 'delete'
  })
}
