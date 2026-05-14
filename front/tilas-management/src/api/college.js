import request from '@/utils/request'

export function listCollege() {
  return request({
    url: '/colleges',
    method: 'get'
  })
}

export function getCollegeById(id) {
  return request({
    url: `/colleges/${id}`,
    method: 'get'
  })
}

export function saveCollege(data) {
  return request({
    url: '/colleges',
    method: 'post',
    data
  })
}

export function updateCollege(data) {
  return request({
    url: '/colleges',
    method: 'put',
    data
  })
}

export function deleteCollege(ids) {
  return request({
    url: '/colleges',
    method: 'delete',
    data: ids
  })
}

export function countCollege() {
  return request({
    url: '/colleges/count',
    method: 'get'
  })
}
