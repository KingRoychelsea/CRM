package com.crm;

import com.crm.dto.customer.ContactDTO;
import com.crm.dto.customer.CustomerDTO;
import com.crm.dto.customer.FollowRecordDTO;
import com.crm.dto.sales.BusinessOpportunityDTO;
import com.crm.dto.sales.ContractDTO;
import com.crm.dto.sales.OrderDTO;
import com.crm.dto.sales.PaymentDTO;
import com.crm.dto.sys.SysDeptDTO;
import com.crm.dto.sys.SysDictDTO;
import com.crm.dto.sys.SysRoleDTO;
import com.crm.dto.sys.SysUserDTO;
import com.crm.service.customer.ContactService;
import com.crm.service.customer.CustomerService;
import com.crm.service.customer.FollowRecordService;
import com.crm.service.sales.BusinessOpportunityService;
import com.crm.service.sales.ContractService;
import com.crm.service.sales.OrderService;
import com.crm.service.sales.PaymentService;
import com.crm.service.sys.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;

/**
 * Test data generator
 * Used to generate test data for all functional modules of the system
 */
@Component
public class TestDataGenerator {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private FollowRecordService followRecordService;

    @Autowired
    private BusinessOpportunityService businessOpportunityService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Random random = new Random();

    public void run(String... args) throws Exception {
        System.out.println("Starting to generate test data...");

        // Generate system management module test data
        generateSystemTestData();

        // Generate customer management module test data
        generateCustomerTestData();

        // Generate sales management module test data
        generateSalesTestData();

        System.out.println("Test data generation completed!");
    }

    /**
     * Generate system management module test data
     */
    private void generateSystemTestData() throws Exception {
        System.out.println("Generating system management module test data...");

        // Generate department data
        generateDeptData();

        // Generate role data
        generateRoleData();

        // Generate user data
        generateUserData();

        // Generate dictionary data
        generateDictData();
    }

    /**
     * Generate department data
     */
    private void generateDeptData() throws Exception {
        System.out.println("Generating department data...");

        // Generate departments
        SysDeptDTO dept1 = new SysDeptDTO();
        dept1.setDeptName("Sales Dept");
        dept1.setParentId(0L);
        dept1.setOrderNum(1);
        dept1.setStatus("1");
        sysDeptService.add(dept1);

        SysDeptDTO dept2 = new SysDeptDTO();
        dept2.setDeptName("Marketing Dept");
        dept2.setParentId(0L);
        dept2.setOrderNum(2);
        dept2.setStatus("1");
        sysDeptService.add(dept2);

        SysDeptDTO dept3 = new SysDeptDTO();
        dept3.setDeptName("Tech Dept");
        dept3.setParentId(0L);
        dept3.setOrderNum(3);
        dept3.setStatus("1");
        sysDeptService.add(dept3);
    }

    /**
     * Generate role data
     */
    private void generateRoleData() throws Exception {
        System.out.println("Generating role data...");

        // Generate roles
        SysRoleDTO role1 = new SysRoleDTO();
        role1.setRoleName("Admin");
        role1.setRoleKey("admin");
        role1.setOrderNum(1);
        role1.setStatus("1");
        sysRoleService.add(role1);

        SysRoleDTO role2 = new SysRoleDTO();
        role2.setRoleName("Sales Manager");
        role2.setRoleKey("sales_manager");
        role2.setOrderNum(2);
        role2.setStatus("1");
        sysRoleService.add(role2);

        SysRoleDTO role3 = new SysRoleDTO();
        role3.setRoleName("User");
        role3.setRoleKey("user");
        role3.setOrderNum(3);
        role3.setStatus("1");
        sysRoleService.add(role3);
    }

    /**
     * Generate user data
     */
    private void generateUserData() throws Exception {
        System.out.println("Generating user data...");

        // Generate users
        SysUserDTO user1 = new SysUserDTO();
        user1.setUsername("admin");
        user1.setPassword(passwordEncoder.encode("123456")); // Password: 123456
        user1.setNickname("Admin");
        user1.setEmail("admin@example.com");
        user1.setPhone("13800138000");
        user1.setStatus("1");
        sysUserService.add(user1);

        SysUserDTO user2 = new SysUserDTO();
        user2.setUsername("sales");
        user2.setPassword(passwordEncoder.encode("123456")); // Password: 123456
        user2.setNickname("Sales Manager");
        user2.setEmail("sales@example.com");
        user2.setPhone("13800138001");
        user2.setStatus("1");
        sysUserService.add(user2);

        SysUserDTO user3 = new SysUserDTO();
        user3.setUsername("user");
        user3.setPassword(passwordEncoder.encode("123456")); // Password: 123456
        user3.setNickname("User");
        user3.setEmail("user@example.com");
        user3.setPhone("13800138002");
        user3.setStatus("1");
        sysUserService.add(user3);
    }

    /**
     * Generate dictionary data
     */
    private void generateDictData() throws Exception {
        System.out.println("Generating dictionary data...");

        // Generate dictionary types
        SysDictDTO dict1 = new SysDictDTO();
        dict1.setDictName("Customer Type");
        dict1.setDictType("customer_type");
        sysDictService.addType(dict1);

        SysDictDTO dict2 = new SysDictDTO();
        dict2.setDictName("Opportunity Status");
        dict2.setDictType("opportunity_status");
        sysDictService.addType(dict2);

        SysDictDTO dict3 = new SysDictDTO();
        dict3.setDictName("Contract Status");
        dict3.setDictType("contract_status");
        sysDictService.addType(dict3);
    }

    /**
     * Generate customer management module test data
     */
    private void generateCustomerTestData() throws Exception {
        System.out.println("Generating customer management module test data...");

        // Generate customer data
        for (int i = 1; i <= 10; i++) {
            CustomerDTO customer = new CustomerDTO();
            customer.setCustomerName("Customer" + i);
            customer.setCustomerType(1);
            customer.setPhone("138001380" + (i < 10 ? "0" + i : i));
            customer.setEmail("customer" + i + "@example.com");
            customer.setAddress("Beijing Chaoyang District " + i);
            customer.setStatus(1);
            customer.setOwnerId(1L);
            customerService.add(customer);
        }
    }

    /**
     * Generate sales management module test data
     */
    private void generateSalesTestData() throws Exception {
        System.out.println("Generating sales management module test data...");

        // Generate opportunity data
        for (int i = 1; i <= 5; i++) {
            BusinessOpportunityDTO opportunity = new BusinessOpportunityDTO();
            opportunity.setOpportunityName("Opportunity" + i);
            opportunity.setCustomerId((long) (i % 10 + 1));
            opportunity.setExpectedAmount(new java.math.BigDecimal(100000.00 * i));
            opportunity.setStage(1);
            opportunity.setOwnerId(1L);
            businessOpportunityService.add(opportunity);
        }
    }
}