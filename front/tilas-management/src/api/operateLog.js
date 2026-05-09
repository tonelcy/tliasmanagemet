import request from '@/utils/request'

export function pageOperateLog(params) {
  return request({
    url: '/operateLog',
    method: 'get',
    params
  })
}
