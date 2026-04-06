import request from '@/utils/request'

// 查询教学大纲列表
export function listSyllabus(query) {
  return request({
    url: '/syllabus/syllabus/list',
    method: 'get',
    params: query
  })
}

// 查询教学大纲详细
export function getSyllabus(syllabusId) {
  return request({
    url: '/syllabus/syllabus/' + syllabusId,
    method: 'get'
  })
}

// 新增教学大纲
export function addSyllabus(data) {
  return request({
    url: '/syllabus/syllabus',
    method: 'post',
    data: data
  })
}

// 修改教学大纲
export function updateSyllabus(data) {
  return request({
    url: '/syllabus/syllabus',
    method: 'put',
    data: data
  })
}

// 删除教学大纲
export function delSyllabus(syllabusId) {
  return request({
    url: '/syllabus/syllabus/' + syllabusId,
    method: 'delete'
  })
}
