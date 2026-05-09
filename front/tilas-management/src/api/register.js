import request from '@/utils/request'

//注册
export function registerApi(data){
    return request.post('/register', data)
}