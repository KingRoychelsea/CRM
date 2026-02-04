package com.crm.service.impl.sys;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.entity.sys.SysDict;
import com.crm.entity.sys.SysDictItem;
import com.crm.mapper.sys.SysDictMapper;
import com.crm.mapper.sys.SysDictItemMapper;
import com.crm.service.sys.SysDictService;
import com.crm.dto.sys.SysDictDTO;
import com.crm.dto.sys.SysDictItemDTO;
import com.crm.vo.sys.SysDictVO;
import com.crm.exception.BusinessException;
import com.crm.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据字典服务实现类
 */
@Slf4j
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {

    @Autowired
    private SysDictMapper dictMapper;

    @Autowired
    private SysDictItemMapper dictItemMapper;

    @Override
    public List<SysDictVO> getDictList(SysDictDTO dto) {
        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<>();
        if (dto.getDictName() != null) {
            wrapper.like(SysDict::getDictName, dto.getDictName());
        }
        if (dto.getDictType() != null) {
            wrapper.like(SysDict::getDictType, dto.getDictType());
        }
        if (dto.getStatus() != null) {
            wrapper.eq(SysDict::getStatus, dto.getStatus());
        }
        wrapper.eq(SysDict::getDelFlag, "0");
        wrapper.orderByAsc(SysDict::getOrderNum);

        List<SysDict> dictList = dictMapper.selectList(wrapper);
        List<SysDictVO> voList = new ArrayList<>();

        for (SysDict dict : dictList) {
            SysDictVO vo = new SysDictVO();
            BeanUtils.copyProperties(dict, vo);
            // 获取字典项
            List<SysDictItem> dictItems = getDictItems(dict.getId());
            vo.setDictItems(dictItems);
            voList.add(vo);
        }

        return voList;
    }

    @Transactional
    @Override
    public boolean createDict(SysDictDTO dto) {
        // 检查字典类型是否已存在
        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDict::getDictType, dto.getDictType());
        wrapper.eq(SysDict::getDelFlag, "0");
        SysDict existingDict = dictMapper.selectOne(wrapper);
        if (existingDict != null) {
            throw new BusinessException("字典类型已存在");
        }

        // 创建字典
        SysDict dict = new SysDict();
        BeanUtils.copyProperties(dto, dict);
        dict.setDelFlag("0");
        dict.setCreateBy(SecurityUtils.getUsername());
        dict.setCreateTime(new Date());
        dict.setUpdateBy(SecurityUtils.getUsername());
        dict.setUpdateTime(new Date());

        return save(dict);
    }

    @Transactional
    @Override
    public boolean updateDict(SysDictDTO dto) {
        SysDict dict = getById(dto.getId());
        if (dict == null) {
            throw new BusinessException("字典不存在");
        }

        // 检查字典类型是否已存在
        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDict::getDictType, dto.getDictType());
        wrapper.ne(SysDict::getId, dto.getId());
        wrapper.eq(SysDict::getDelFlag, "0");
        SysDict existingDict = dictMapper.selectOne(wrapper);
        if (existingDict != null) {
            throw new BusinessException("字典类型已存在");
        }

        // 更新字典
        BeanUtils.copyProperties(dto, dict);
        dict.setUpdateBy(SecurityUtils.getUsername());
        dict.setUpdateTime(new Date());

        return updateById(dict);
    }

    @Transactional
    @Override
    public boolean deleteDict(Long id) {
        // 检查是否有字典项
        List<SysDictItem> dictItems = getDictItems(id);
        if (!dictItems.isEmpty()) {
            throw new BusinessException("字典下存在字典项，无法删除");
        }

        SysDict dict = getById(id);
        if (dict == null) {
            throw new BusinessException("字典不存在");
        }

        // 逻辑删除字典
        dict.setDelFlag("1");
        dict.setUpdateBy(SecurityUtils.getUsername());
        dict.setUpdateTime(new Date());

        return updateById(dict);
    }

    @Override
    public boolean updateDictStatus(Long id, String status) {
        SysDict dict = getById(id);
        if (dict == null) {
            throw new BusinessException("字典不存在");
        }

        // 更新状态
        dict.setStatus(status);
        dict.setUpdateBy(SecurityUtils.getUsername());
        dict.setUpdateTime(new Date());

        boolean updated = updateById(dict);
        if (updated && "0".equals(status)) {
            // 更新字典项状态
            List<SysDictItem> dictItems = getDictItems(id);
            for (SysDictItem dictItem : dictItems) {
                dictItem.setStatus(status);
                dictItem.setUpdateBy(SecurityUtils.getUsername());
                dictItem.setUpdateTime(new Date());
                dictItemMapper.updateById(dictItem);
            }
        }

        return updated;
    }

    @Override
    public List<SysDictItem> getDictItems(Long dictId) {
        LambdaQueryWrapper<SysDictItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDictItem::getDictId, dictId);
        wrapper.eq(SysDictItem::getDelFlag, "0");
        wrapper.orderByAsc(SysDictItem::getOrderNum);
        return dictItemMapper.selectList(wrapper);
    }

    @Transactional
    @Override
    public boolean createDictItem(SysDictItemDTO dto) {
        // 检查字典是否存在
        SysDict dict = getById(dto.getDictId());
        if (dict == null) {
            throw new BusinessException("字典不存在");
        }

        // 检查字典项值是否已存在
        LambdaQueryWrapper<SysDictItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDictItem::getDictId, dto.getDictId());
        wrapper.eq(SysDictItem::getItemValue, dto.getItemValue());
        wrapper.eq(SysDictItem::getDelFlag, "0");
        SysDictItem existingItem = dictItemMapper.selectOne(wrapper);
        if (existingItem != null) {
            throw new BusinessException("字典项值已存在");
        }

        // 创建字典项
        SysDictItem dictItem = new SysDictItem();
        BeanUtils.copyProperties(dto, dictItem);
        dictItem.setDelFlag("0");
        dictItem.setCreateBy(SecurityUtils.getUsername());
        dictItem.setCreateTime(new Date());
        dictItem.setUpdateBy(SecurityUtils.getUsername());
        dictItem.setUpdateTime(new Date());

        return dictItemMapper.insert(dictItem) > 0;
    }

    @Transactional
    @Override
    public boolean updateDictItem(SysDictItemDTO dto) {
        SysDictItem dictItem = dictItemMapper.selectById(dto.getId());
        if (dictItem == null) {
            throw new BusinessException("字典项不存在");
        }

        // 检查字典项值是否已存在
        LambdaQueryWrapper<SysDictItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDictItem::getDictId, dto.getDictId());
        wrapper.eq(SysDictItem::getItemValue, dto.getItemValue());
        wrapper.ne(SysDictItem::getId, dto.getId());
        wrapper.eq(SysDictItem::getDelFlag, "0");
        SysDictItem existingItem = dictItemMapper.selectOne(wrapper);
        if (existingItem != null) {
            throw new BusinessException("字典项值已存在");
        }

        // 更新字典项
        BeanUtils.copyProperties(dto, dictItem);
        dictItem.setUpdateBy(SecurityUtils.getUsername());
        dictItem.setUpdateTime(new Date());

        return dictItemMapper.updateById(dictItem) > 0;
    }

    @Transactional
    @Override
    public boolean deleteDictItem(Long id) {
        SysDictItem dictItem = dictItemMapper.selectById(id);
        if (dictItem == null) {
            throw new BusinessException("字典项不存在");
        }

        // 逻辑删除字典项
        dictItem.setDelFlag("1");
        dictItem.setUpdateBy(SecurityUtils.getUsername());
        dictItem.setUpdateTime(new Date());

        return dictItemMapper.updateById(dictItem) > 0;
    }

    @Override
    public List<SysDictItem> getDictItemsByType(String dictType) {
        // 查找字典
        LambdaQueryWrapper<SysDict> dictWrapper = new LambdaQueryWrapper<>();
        dictWrapper.eq(SysDict::getDictType, dictType);
        dictWrapper.eq(SysDict::getDelFlag, "0");
        dictWrapper.eq(SysDict::getStatus, "1");
        SysDict dict = dictMapper.selectOne(dictWrapper);

        if (dict == null) {
            return new ArrayList<>();
        }

        // 获取字典项
        return getDictItems(dict.getId());
    }

    @Override
    public List<?> listType(Integer pageNum, Integer pageSize, String keyword, String status, Integer orderNum) {
        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(SysDict::getDictName, keyword).or().like(SysDict::getDictType, keyword));
        }
        if (status != null) {
            wrapper.eq(SysDict::getStatus, status);
        }
        wrapper.eq(SysDict::getDelFlag, "0");
        wrapper.orderByAsc(SysDict::getOrderNum);
        
        List<SysDict> dictList = dictMapper.selectList(wrapper);
        return dictList.stream().map(dict -> {
            SysDictVO vo = new SysDictVO();
            BeanUtils.copyProperties(dict, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public SysDictVO getTypeById(Long id) {
        SysDict dict = getById(id);
        if (dict == null) {
            return null;
        }
        SysDictVO vo = new SysDictVO();
        BeanUtils.copyProperties(dict, vo);
        return vo;
    }

    @Override
    public boolean addType(SysDictDTO dto) {
        return createDict(dto);
    }

    @Override
    public boolean updateType(SysDictDTO dto) {
        return updateDict(dto);
    }

    @Override
    public boolean deleteType(Long id) {
        return deleteDict(id);
    }

    @Override
    public boolean batchDeleteType(List<Long> ids) {
        for (Long id : ids) {
            deleteDict(id);
        }
        return true;
    }

    @Override
    public boolean updateTypeStatus(Long id, Integer status) {
        return updateDictStatus(id, String.valueOf(status));
    }

    @Override
    public List<?> listItem(Long dictId) {
        return getDictItems(dictId);
    }

    @Override
    public boolean addItem(SysDictItemDTO dto) {
        return createDictItem(dto);
    }

    @Override
    public boolean updateItem(SysDictItemDTO dto) {
        return updateDictItem(dto);
    }

    @Override
    public boolean deleteItem(Long id) {
        return deleteDictItem(id);
    }

    @Override
    public boolean batchDeleteItem(List<Long> ids) {
        for (Long id : ids) {
            deleteDictItem(id);
        }
        return true;
    }

    @Override
    public List<SysDictItem> getDictData(String dictType) {
        return getDictItemsByType(dictType);
    }

    @Override
    public List<SysDictVO> getAllType() {
        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDict::getDelFlag, "0");
        wrapper.eq(SysDict::getStatus, "1");
        wrapper.orderByAsc(SysDict::getOrderNum);
        
        List<SysDict> dictList = dictMapper.selectList(wrapper);
        return dictList.stream().map(dict -> {
            SysDictVO vo = new SysDictVO();
            BeanUtils.copyProperties(dict, vo);
            return vo;
        }).collect(Collectors.toList());
    }
}
