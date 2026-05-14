import request from '@/utils/request'

export function pageStudent(params) {
  return request({
    url: '/students',
    method: 'get',
    params
  })
}

export function getStudentById(id) {
  return request({
    url: `/students/${id}`,
    method: 'get'
  })
}

export function saveStudent(data) {
  return request({
    url: '/students',
    method: 'post',
    data
  })
}

export function updateStudent(data) {
  return request({
    url: '/students',
    method: 'put',
    data
  })
}

export function deleteStudent(ids) {
  return request({
    url: '/students',
    method: 'delete',
    data: ids
  })
}

export function countStudent() {
  return request({
    url: '/students/count',
    method: 'get'
  })
}

export function getStudentGenderStats() {
  return request({
    url: '/students/statistics/gender',
    method: 'get'
  })
}

export function getStudentDegreeStats() {
  return request({
    url: '/students/statistics/degree',
    method: 'get'
  })
}

export function getStudentCollegeStats() {
  return request({
    url: '/students/statistics/college',
    method: 'get'
  })
}

export function getStudentClazzStats() {
  return request({
    url: '/students/statistics/clazz',
    method: 'get'
  })
}
