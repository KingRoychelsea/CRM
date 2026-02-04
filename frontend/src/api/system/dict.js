import request from '../axios'
import { API_CONFIG } from '../../config/api.config'

// 数据字典API
const dictApi = {
  // 获取字典类型列表
  getDictList: (params) => {
    return request({
      url: API_CONFIG.SYSTEM.DICT.LIST,
      method: 'get',
      params
    })
  },
  
  // 获取字典类型详情
  getDictInfo: (id) => {
    return request({
      url: `${API_CONFIG.SYSTEM.DICT.DETAIL}/${id}`,
      method: 'get'
    })
  },
  
  // 创建字典类型
  createDict: (data) => {
    return request({
      url: API_CONFIG.SYSTEM.DICT.CREATE,
      method: 'post',
      data
    })
  },
  
  // 更新字典类型
  updateDict: (id, data) => {
    return request({
      url: `${API_CONFIG.SYSTEM.DICT.UPDATE}/${id}`,
      method: 'put',
      data
    })
  },
  
  // 删除字典类型
  deleteDict: (id) => {
    return request({
      url: `${API_CONFIG.SYSTEM.DICT.DELETE}/${id}`,
      method: 'delete'
    })
  },
  
  // 获取字典项列表
  getDictItemList: (dictId, params) => {
    return request({
      url: `${API_CONFIG.SYSTEM.DICT.ITEMS}/list/${dictId}`,
      method: 'get',
      params
    })
  },
  
  // 获取字典项详情
  getDictItemInfo: (id) => {
    return request({
      url: `${API_CONFIG.SYSTEM.DICT.ITEMS}/${id}`,
      method: 'get'
    })
  },
  
  // 创建字典项
  createDictItem: (data) => {
    return request({
      url: API_CONFIG.SYSTEM.DICT.ITEMS,
      method: 'post',
      data
    })
  },
  
  // 更新字典项
  updateDictItem: (id, data) => {
    return request({
      url: `${API_CONFIG.SYSTEM.DICT.ITEMS}/${id}`,
      method: 'put',
      data
    })
  },
  
  // 删除字典项
  deleteDictItem: (id) => {
    return request({
      url: `${API_CONFIG.SYSTEM.DICT.ITEMS}/${id}`,
      method: 'delete'
    })
  }
}

export default dictApi