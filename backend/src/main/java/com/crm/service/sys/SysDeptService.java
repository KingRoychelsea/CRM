package com.crm.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.entity.sys.SysDept;
import com.crm.dto.sys.SysDeptDTO;
import com.crm.vo.sys.SysDeptVO;

import java.util.List;

/**
 * 部门服务接口
 */
public interface SysDeptService extends IService<SysDept> {

    /**
     * 获取部门列表
     */
    List<SysDeptVO> getDeptList(SysDeptDTO dto);

    /**
     * 获取部门树
     */
    List<SysDeptVO> getDeptTree();

    /**
     * 创建部门
     */
    boolean createDept(SysDeptDTO dto);

    /**
     * 更新部门
     */
    boolean updateDept(SysDeptDTO dto);

    /**
     * 删除部门
     */
    boolean deleteDept(Long id);

    /**
     * 更新部门状态
     */
    boolean updateStatus(Long id, String status);

    /**
     * 根据部门ID获取子部门
     */
    List<SysDept> getChildDepts(Long deptId);

    /**
     * 检查部门是否有子部门
     */
    boolean hasChildDept(Long deptId);

    /**
     * 检查部门是否有用户
     */
    boolean hasUser(Long deptId);

    /**
     * 添加部门
     */
    boolean add(SysDeptDTO dto);

    /**
     * 更新部门
     */
    boolean update(SysDeptDTO dto);

    /**
     * 删除部门
     */
    boolean delete(Long id);

    /**
     * 批量删除部门
     */
    boolean batchDelete(List<Long> ids);

    /**
     * 排序部门
     */
    boolean sort(List<SysDeptDTO> dtos);

    /**
     * 获取选择树
     */
    List<SysDeptVO> getSelectTree();

    /**
     * 获取部门用户
     */
    List<?> getDeptUsers(Long deptId, Integer pageNum, Integer pageSize, String keyword, String status, Integer roleId);
}
