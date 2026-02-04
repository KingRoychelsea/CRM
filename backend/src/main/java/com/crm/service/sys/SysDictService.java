package com.crm.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.entity.sys.SysDict;
import com.crm.entity.sys.SysDictItem;
import com.crm.dto.sys.SysDictDTO;
import com.crm.dto.sys.SysDictItemDTO;
import com.crm.vo.sys.SysDictVO;

import java.util.List;

/**
 * 数据字典服务接口
 */
public interface SysDictService extends IService<SysDict> {

    /**
     * 获取字典列表
     */
    List<SysDictVO> getDictList(SysDictDTO dto);

    /**
     * 创建字典
     */
    boolean createDict(SysDictDTO dto);

    /**
     * 更新字典
     */
    boolean updateDict(SysDictDTO dto);

    /**
     * 删除字典
     */
    boolean deleteDict(Long id);

    /**
     * 更新字典状态
     */
    boolean updateDictStatus(Long id, String status);

    /**
     * 获取字典项列表
     */
    List<SysDictItem> getDictItems(Long dictId);

    /**
     * 创建字典项
     */
    boolean createDictItem(SysDictItemDTO dto);

    /**
     * 更新字典项
     */
    boolean updateDictItem(SysDictItemDTO dto);

    /**
     * 删除字典项
     */
    boolean deleteDictItem(Long id);

    /**
     * 根据字典类型获取字典项
     */
    List<SysDictItem> getDictItemsByType(String dictType);

    /**
     * 获取字典类型列表
     */
    List<?> listType(Integer pageNum, Integer pageSize, String keyword, String status, Integer orderNum);

    /**
     * 根据ID获取字典类型
     */
    SysDictVO getTypeById(Long id);

    /**
     * 添加字典类型
     */
    boolean addType(SysDictDTO dto);

    /**
     * 更新字典类型
     */
    boolean updateType(SysDictDTO dto);

    /**
     * 删除字典类型
     */
    boolean deleteType(Long id);

    /**
     * 批量删除字典类型
     */
    boolean batchDeleteType(List<Long> ids);

    /**
     * 更新字典类型状态
     */
    boolean updateTypeStatus(Long id, Integer status);

    /**
     * 获取字典项列表
     */
    List<?> listItem(Long dictId);

    /**
     * 添加字典项
     */
    boolean addItem(SysDictItemDTO dto);

    /**
     * 更新字典项
     */
    boolean updateItem(SysDictItemDTO dto);

    /**
     * 删除字典项
     */
    boolean deleteItem(Long id);

    /**
     * 批量删除字典项
     */
    boolean batchDeleteItem(List<Long> ids);

    /**
     * 获取字典数据
     */
    List<SysDictItem> getDictData(String dictType);

    /**
     * 获取所有字典类型
     */
    List<SysDictVO> getAllType();
}
