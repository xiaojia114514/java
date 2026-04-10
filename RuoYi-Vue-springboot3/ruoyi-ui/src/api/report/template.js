import request from '@/utils/request'

// 查询课程教学质量分析报告模板管理列表
export function listTemplate(query) {
  return request({
    url: '/report/template/list',
    method: 'get',
    params: query
  })
}

// 查询课程教学质量分析报告模板管理详细
export function getTemplate(reportTemplateId) {
  return request({
    url: '/report/template/' + reportTemplateId,
    method: 'get'
  })
}

// 新增课程教学质量分析报告模板管理
export function addTemplate(data) {
  return request({
    url: '/report/template',
    method: 'post',
    data: data
  })
}

// 修改课程教学质量分析报告模板管理
export function updateTemplate(data) {
  return request({
    url: '/report/template',
    method: 'put',
    data: data
  })
}

// 删除课程教学质量分析报告模板管理
export function delTemplate(reportTemplateId) {
  return request({
    url: '/report/template/' + reportTemplateId,
    method: 'delete'
  })
}
