import request from '@/utils/request'

// 查询课程教学质量分析报告列表
export function listReport(query) {
  return request({
    url: '/report/report/list',
    method: 'get',
    params: query
  })
}

// 查询课程教学质量分析报告详细
export function getReport(reportId) {
  return request({
    url: '/report/report/' + reportId,
    method: 'get'
  })
}

// 新增课程教学质量分析报告
export function addReport(data) {
  return request({
    url: '/report/report',
    method: 'post',
    data: data
  })
}

// 修改课程教学质量分析报告
export function updateReport(data) {
  return request({
    url: '/report/report',
    method: 'put',
    data: data
  })
}

// 删除课程教学质量分析报告
export function delReport(reportId) {
  return request({
    url: '/report/report/' + reportId,
    method: 'delete'
  })
}
