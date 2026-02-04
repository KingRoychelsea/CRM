package com.crm.service.impl.sys;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.entity.sys.SysDept;
import com.crm.entity.sys.SysUser;
import com.crm.mapper.sys.SysDeptMapper;
import com.crm.mapper.sys.SysUserMapper;
import com.crm.service.sys.SysDeptService;
import com.crm.dto.sys.SysDeptDTO;
import com.crm.vo.sys.SysDeptVO;
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
 * 部门服务实现类
 */
@Slf4j
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Override
    public List<SysDeptVO> getDeptList(SysDeptDTO dto) {
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        if (dto.getDeptName() != null) {
            wrapper.like(SysDept::getDeptName, dto.getDeptName());
        }
        if (dto.getStatus() != null) {
            wrapper.eq(SysDept::getStatus, dto.getStatus());
        }
        wrapper.eq(SysDept::getDelFlag, "0");
        wrapper.orderByAsc(SysDept::getParentId).orderByAsc(SysDept::getOrderNum);

        List<SysDept> deptList = deptMapper.selectList(wrapper);
        return buildDeptTree(deptList);
    }

    @Override
    public List<SysDeptVO> getDeptTree() {
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDept::getDelFlag, "0");
        wrapper.eq(SysDept::getStatus, "1");
        wrapper.orderByAsc(SysDept::getParentId).orderByAsc(SysDept::getOrderNum);

        List<SysDept> deptList = deptMapper.selectList(wrapper);
        return buildDeptTree(deptList);
    }

    @Transactional
    @Override
    public boolean createDept(SysDeptDTO dto) {
        // 检查部门名称是否已存在
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDept::getDeptName, dto.getDeptName());
        wrapper.eq(SysDept::getParentId, dto.getParentId());
        wrapper.eq(SysDept::getDelFlag, "0");
        SysDept existingDept = deptMapper.selectOne(wrapper);
        if (existingDept != null) {
            throw new BusinessException("部门名称已存在");
        }

        // 创建部门
        SysDept dept = new SysDept();
        BeanUtils.copyProperties(dto, dept);
        dept.setDelFlag("0");
        dept.setCreateBy(SecurityUtils.getUsername());
        dept.setCreateTime(new Date());
        dept.setUpdateBy(SecurityUtils.getUsername());
        dept.setUpdateTime(new Date());

        return save(dept);
    }

    @Transactional
    @Override
    public boolean updateDept(SysDeptDTO dto) {
        SysDept dept = getById(dto.getId());
        if (dept == null) {
            throw new BusinessException("部门不存在");
        }

        // 检查部门名称是否已存在
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDept::getDeptName, dto.getDeptName());
        wrapper.eq(SysDept::getParentId, dto.getParentId());
        wrapper.ne(SysDept::getId, dto.getId());
        wrapper.eq(SysDept::getDelFlag, "0");
        SysDept existingDept = deptMapper.selectOne(wrapper);
        if (existingDept != null) {
            throw new BusinessException("部门名称已存在");
        }

        // 检查是否会导致循环引用
        if (dto.getParentId().equals(dto.getId())) {
            throw new BusinessException("父部门不能是自身");
        }

        // 更新部门
        BeanUtils.copyProperties(dto, dept);
        dept.setUpdateBy(SecurityUtils.getUsername());
        dept.setUpdateTime(new Date());

        return updateById(dept);
    }

    @Transactional
    @Override
    public boolean deleteDept(Long id) {
        // 检查是否有子部门
        if (hasChildDept(id)) {
            throw new BusinessException("存在子部门，无法删除");
        }

        // 检查是否有用户
        if (hasUser(id)) {
            throw new BusinessException("部门下存在用户，无法删除");
        }

        SysDept dept = getById(id);
        if (dept == null) {
            throw new BusinessException("部门不存在");
        }

        // 逻辑删除部门
        dept.setDelFlag("1");
        dept.setUpdateBy(SecurityUtils.getUsername());
        dept.setUpdateTime(new Date());

        return updateById(dept);
    }

    @Override
    public boolean updateStatus(Long id, String status) {
        SysDept dept = getById(id);
        if (dept == null) {
            throw new BusinessException("部门不存在");
        }

        // 更新状态
        dept.setStatus(status);
        dept.setUpdateBy(SecurityUtils.getUsername());
        dept.setUpdateTime(new Date());

        boolean updated = updateById(dept);
        if (updated && "0".equals(status)) {
            // 递归更新子部门状态
            List<SysDept> childDepts = getChildDepts(id);
            for (SysDept childDept : childDepts) {
                childDept.setStatus(status);
                childDept.setUpdateBy(SecurityUtils.getUsername());
                childDept.setUpdateTime(new Date());
                updateById(childDept);
            }
        }

        return updated;
    }

    @Override
    public List<SysDept> getChildDepts(Long deptId) {
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDept::getParentId, deptId);
        wrapper.eq(SysDept::getDelFlag, "0");
        List<SysDept> childDepts = deptMapper.selectList(wrapper);

        List<SysDept> allChildDepts = new ArrayList<>(childDepts);
        for (SysDept childDept : childDepts) {
            allChildDepts.addAll(getChildDepts(childDept.getId()));
        }

        return allChildDepts;
    }

    @Override
    public boolean hasChildDept(Long deptId) {
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDept::getParentId, deptId);
        wrapper.eq(SysDept::getDelFlag, "0");
        long count = deptMapper.selectCount(wrapper);
        return count > 0;
    }

    @Override
    public boolean hasUser(Long deptId) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getDeptId, deptId);
        wrapper.eq(SysUser::getDelFlag, "0");
        long count = userMapper.selectCount(wrapper);
        return count > 0;
    }

    @Override
    public boolean add(SysDeptDTO dto) {
        return createDept(dto);
    }

    @Override
    public boolean update(SysDeptDTO dto) {
        return updateDept(dto);
    }

    @Override
    public boolean delete(Long id) {
        return deleteDept(id);
    }

    @Override
    public boolean batchDelete(List<Long> ids) {
        for (Long id : ids) {
            deleteDept(id);
        }
        return true;
    }

    @Override
    public boolean sort(List<SysDeptDTO> dtos) {
        for (SysDeptDTO dto : dtos) {
            SysDept dept = getById(dto.getId());
            if (dept != null) {
                dept.setOrderNum(dto.getOrderNum());
                dept.setUpdateBy(SecurityUtils.getUsername());
                dept.setUpdateTime(new Date());
                updateById(dept);
            }
        }
        return true;
    }

    @Override
    public List<SysDeptVO> getSelectTree() {
        return getDeptTree();
    }

    @Override
    public List<?> getDeptUsers(Long deptId, Integer pageNum, Integer pageSize, String keyword, String status, Integer roleId) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (deptId != null) {
            wrapper.eq(SysUser::getDeptId, deptId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(SysUser::getUsername, keyword).or().like(SysUser::getNickname, keyword));
        }
        if (status != null) {
            wrapper.eq(SysUser::getStatus, status);
        }
        wrapper.eq(SysUser::getDelFlag, "0");
        wrapper.orderByAsc(SysUser::getId);
        
        return userMapper.selectList(wrapper);
    }

    /**
     * 构建部门树
     */
    private List<SysDeptVO> buildDeptTree(List<SysDept> deptList) {
        List<SysDeptVO> voList = new ArrayList<>();
        List<SysDeptVO> parentList = new ArrayList<>();

        // 转换为VO
        for (SysDept dept : deptList) {
            SysDeptVO vo = new SysDeptVO();
            BeanUtils.copyProperties(dept, vo);
            vo.setChildren(new ArrayList<>());
            vo.setHasChildren(hasChildDept(dept.getId()));
            voList.add(vo);
            if (dept.getParentId() == 0) {
                parentList.add(vo);
            }
        }

        // 构建树
        for (SysDeptVO parent : parentList) {
            buildChildDeptTree(parent, voList);
        }

        return parentList;
    }

    /**
     * 递归构建子部门树
     */
    private void buildChildDeptTree(SysDeptVO parent, List<SysDeptVO> allDepts) {
        for (SysDeptVO dept : allDepts) {
            if (dept.getParentId().equals(parent.getId())) {
                parent.getChildren().add(dept);
                buildChildDeptTree(dept, allDepts);
            }
        }
    }
}
