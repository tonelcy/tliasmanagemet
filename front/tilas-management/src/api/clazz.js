import request from '@/utils/request'

export function pageClazz(params) {
  return request({
    url: '/clazzs',
    method: 'get',
    params
  })
}

export function listClazz() {
  return request({
    url: '/clazzs/list',
    method: 'get'
  })
}

export function getClazzById(id) {
  return request({
    url: `/clazzs/${id}`,
    method: 'get'
  })
}

export function saveClazz(data) {
  return request({
    url: '/clazzs',
    method: 'post',
    data
  })
}

export function updateClazz(data) {
  return request({
    url: '/clazzs',
    method: 'put',
    data
  })
}

export function deleteClazz(ids) {
  return request({
    url: '/clazzs',
    method: 'delete',
    data: ids
  })
}
