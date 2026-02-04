<template>
  <div class="stats-management">
    <!-- 客户统计页面 -->
    <el-card v-if="currentPage === 'customer'">
      <template #header>
        <div class="card-header">
          <span>客户统计</span>
          <div class="header-actions">
            <el-button type="success" @click="handleExport">
              <el-icon><Download /></el-icon>
              <span>导出报表</span>
            </el-button>
          </div>
        </div>
      </template>
      
      <!-- 统计卡片 -->
      <div class="stats-cards">
        <el-card shadow="hover" class="stats-card">
          <div class="card-content">
            <div class="card-title">总客户数</div>
            <div class="card-value">{{ 330 }}</div>
            <div class="card-change positive">+12.5%</div>
          </div>
        </el-card>
        <el-card shadow="hover" class="stats-card">
          <div class="card-content">
            <div class="card-title">本月新增</div>
            <div class="card-value">{{ 25 }}</div>
            <div class="card-change positive">+8.2%</div>
          </div>
        </el-card>
        <el-card shadow="hover" class="stats-card">
          <div class="card-content">
            <div class="card-title">合作客户</div>
            <div class="card-value">{{ 210 }}</div>
            <div class="card-change positive">+15.8%</div>
          </div>
        </el-card>
        <el-card shadow="hover" class="stats-card">
          <div class="card-content">
            <div class="card-title">流失客户</div>
            <div class="card-value">{{ 35 }}</div>
            <div class="card-change negative">+3.1%</div>
          </div>
        </el-card>
      </div>
      
      <!-- 图表区域 -->
      <div class="charts-container">
        <!-- 客户统计图表 -->
        <el-card class="chart-card">
          <template #header>
            <div class="chart-header">
              <span>客户增长趋势</span>
              <el-select v-model="customerStatsType" @change="handleCustomerStatsTypeChange">
                <el-option label="按时间" value="time"></el-option>
                <el-option label="按分类" value="category"></el-option>
                <el-option label="按部门" value="dept"></el-option>
              </el-select>
            </div>
          </template>
          <div ref="customerChartRef" class="chart"></div>
        </el-card>
      </div>
    </el-card>
    
    <!-- 销售统计页面 -->
    <el-card v-else-if="currentPage === 'sales'">
      <template #header>
        <div class="card-header">
          <span>销售统计</span>
          <div class="header-actions">
            <el-button type="success" @click="handleExport">
              <el-icon><Download /></el-icon>
              <span>导出报表</span>
            </el-button>
          </div>
        </div>
      </template>
      
      <!-- 统计卡片 -->
      <div class="stats-cards">
        <el-card shadow="hover" class="stats-card">
          <div class="card-content">
            <div class="card-title">本月销售额</div>
            <div class="card-value">¥{{ 1250000.00 }}</div>
            <div class="card-change positive">+8.2%</div>
          </div>
        </el-card>
        <el-card shadow="hover" class="stats-card">
          <div class="card-content">
            <div class="card-title">本季度销售额</div>
            <div class="card-value">¥{{ 3800000.00 }}</div>
            <div class="card-change positive">+12.5%</div>
          </div>
        </el-card>
        <el-card shadow="hover" class="stats-card">
          <div class="card-content">
            <div class="card-title">年度目标完成率</div>
            <div class="card-value">{{ 32 }}%</div>
            <div class="card-change positive">+5.2%</div>
          </div>
        </el-card>
        <el-card shadow="hover" class="stats-card">
          <div class="card-content">
            <div class="card-title">销售冠军</div>
            <div class="card-value">管理员</div>
            <div class="card-change">¥1,200,000</div>
          </div>
        </el-card>
      </div>
      
      <!-- 图表区域 -->
      <div class="charts-container">
        <!-- 销售业绩统计图表 -->
        <el-card class="chart-card">
          <template #header>
            <div class="chart-header">
              <span>销售业绩统计</span>
              <el-select v-model="salesStatsType" @change="handleSalesStatsTypeChange">
                <el-option label="按员工" value="employee"></el-option>
                <el-option label="按部门" value="dept"></el-option>
                <el-option label="按时间" value="time"></el-option>
              </el-select>
            </div>
          </template>
          <div ref="salesChartRef" class="chart"></div>
        </el-card>
      </div>
    </el-card>
    
    <!-- 商机统计页面 -->
    <el-card v-else-if="currentPage === 'opportunity'">
      <template #header>
        <div class="card-header">
          <span>商机统计</span>
          <div class="header-actions">
            <el-button type="success" @click="handleExport">
              <el-icon><Download /></el-icon>
              <span>导出报表</span>
            </el-button>
          </div>
        </div>
      </template>
      
      <!-- 统计卡片 -->
      <div class="stats-cards">
        <el-card shadow="hover" class="stats-card">
          <div class="card-content">
            <div class="card-title">总商机数</div>
            <div class="card-value">{{ 220 }}</div>
            <div class="card-change positive">+8.2%</div>
          </div>
        </el-card>
        <el-card shadow="hover" class="stats-card">
          <div class="card-content">
            <div class="card-title">本月新增</div>
            <div class="card-value">{{ 35 }}</div>
            <div class="card-change negative">-3.1%</div>
          </div>
        </el-card>
        <el-card shadow="hover" class="stats-card">
          <div class="card-content">
            <div class="card-title">预计总金额</div>
            <div class="card-value">¥{{ 58000000.00 }}</div>
            <div class="card-change positive">+15.8%</div>
          </div>
        </el-card>
        <el-card shadow="hover" class="stats-card">
          <div class="card-content">
            <div class="card-title">成功率</div>
            <div class="card-value">{{ 45 }}%</div>
            <div class="card-change positive">+2.5%</div>
          </div>
        </el-card>
      </div>
      
      <!-- 图表区域 -->
      <div class="charts-container">
        <!-- 商机统计图表 -->
        <el-card class="chart-card">
          <template #header>
            <div class="chart-header">
              <span>商机统计</span>
              <el-select v-model="opportunityStatsType" @change="handleOpportunityStatsTypeChange">
                <el-option label="按阶段" value="stage"></el-option>
                <el-option label="按时间" value="time"></el-option>
              </el-select>
            </div>
          </template>
          <div ref="opportunityChartRef" class="chart"></div>
        </el-card>
      </div>
    </el-card>
    
    <!-- 回款统计页面 -->
    <el-card v-else-if="currentPage === 'payment'">
      <template #header>
        <div class="card-header">
          <span>回款统计</span>
          <div class="header-actions">
            <el-button type="success" @click="handleExport">
              <el-icon><Download /></el-icon>
              <span>导出报表</span>
            </el-button>
          </div>
        </div>
      </template>
      
      <!-- 统计卡片 -->
      <div class="stats-cards">
        <el-card shadow="hover" class="stats-card">
          <div class="card-content">
            <div class="card-title">本月回款</div>
            <div class="card-value">¥{{ 980000.00 }}</div>
            <div class="card-change positive">+15.8%</div>
          </div>
        </el-card>
        <el-card shadow="hover" class="stats-card">
          <div class="card-content">
            <div class="card-title">本季度回款</div>
            <div class="card-value">¥{{ 2800000.00 }}</div>
            <div class="card-change positive">+12.5%</div>
          </div>
        </el-card>
        <el-card shadow="hover" class="stats-card">
          <div class="card-content">
            <div class="card-title">回款率</div>
            <div class="card-value">{{ 85 }}%</div>
            <div class="card-change positive">+3.2%</div>
          </div>
        </el-card>
        <el-card shadow="hover" class="stats-card">
          <div class="card-content">
            <div class="card-title">待回款</div>
            <div class="card-value">¥{{ 1200000.00 }}</div>
            <div class="card-change negative">+5.1%</div>
          </div>
        </el-card>
      </div>
      
      <!-- 图表区域 -->
      <div class="charts-container">
        <!-- 回款统计图表 -->
        <el-card class="chart-card">
          <template #header>
            <div class="chart-header">
              <span>回款统计</span>
              <el-select v-model="paymentStatsType" @change="handlePaymentStatsTypeChange">
                <el-option label="按时间" value="time"></el-option>
                <el-option label="按方式" value="method"></el-option>
              </el-select>
            </div>
          </template>
          <div ref="paymentChartRef" class="chart"></div>
        </el-card>
      </div>
    </el-card>
    
    <!-- 默认统计页面 -->
    <el-card v-else>
      <template #header>
        <div class="card-header">
          <span>数据统计分析</span>
          <div class="header-actions">
            <el-button type="success" @click="handleExport">
              <el-icon><Download /></el-icon>
              <span>导出报表</span>
            </el-button>
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              @change="handleDateRangeChange"
              style="margin-left: 10px"
            ></el-date-picker>
          </div>
        </div>
      </template>
      
      <!-- 统计卡片 -->
      <div class="stats-cards">
        <el-card shadow="hover" class="stats-card">
          <div class="card-content">
            <div class="card-title">今日新增客户</div>
            <div class="card-value">{{ todayNewCustomers }}</div>
            <div class="card-change positive">+12.5%</div>
          </div>
        </el-card>
        <el-card shadow="hover" class="stats-card">
          <div class="card-content">
            <div class="card-title">本月销售业绩</div>
            <div class="card-value">¥{{ monthlySales.toFixed(2) }}</div>
            <div class="card-change positive">+8.2%</div>
          </div>
        </el-card>
        <el-card shadow="hover" class="stats-card">
          <div class="card-content">
            <div class="card-title">本月新增商机</div>
            <div class="card-value">{{ monthlyOpportunities }}</div>
            <div class="card-change negative">-3.1%</div>
          </div>
        </el-card>
        <el-card shadow="hover" class="stats-card">
          <div class="card-content">
            <div class="card-title">本月回款金额</div>
            <div class="card-value">¥{{ monthlyPayments.toFixed(2) }}</div>
            <div class="card-change positive">+15.8%</div>
          </div>
        </el-card>
      </div>
      
      <!-- 图表区域 -->
      <div class="charts-container">
        <!-- 客户统计 -->
        <el-card class="chart-card">
          <template #header>
            <div class="chart-header">
              <span>客户统计</span>
              <el-select v-model="customerStatsType" @change="handleCustomerStatsTypeChange">
                <el-option label="按时间" value="time"></el-option>
                <el-option label="按分类" value="category"></el-option>
                <el-option label="按部门" value="dept"></el-option>
              </el-select>
            </div>
          </template>
          <div ref="customerChartRef" class="chart"></div>
        </el-card>
        
        <!-- 销售业绩统计 -->
        <el-card class="chart-card">
          <template #header>
            <div class="chart-header">
              <span>销售业绩统计</span>
              <el-select v-model="salesStatsType" @change="handleSalesStatsTypeChange">
                <el-option label="按员工" value="employee"></el-option>
                <el-option label="按部门" value="dept"></el-option>
                <el-option label="按时间" value="time"></el-option>
              </el-select>
            </div>
          </template>
          <div ref="salesChartRef" class="chart"></div>
        </el-card>
        
        <!-- 商机统计 -->
        <el-card class="chart-card">
          <template #header>
            <div class="chart-header">
              <span>商机统计</span>
              <el-select v-model="opportunityStatsType" @change="handleOpportunityStatsTypeChange">
                <el-option label="按阶段" value="stage"></el-option>
                <el-option label="按时间" value="time"></el-option>
              </el-select>
            </div>
          </template>
          <div ref="opportunityChartRef" class="chart"></div>
        </el-card>
        
        <!-- 回款统计 -->
        <el-card class="chart-card">
          <template #header>
            <div class="chart-header">
              <span>回款统计</span>
              <el-select v-model="paymentStatsType" @change="handlePaymentStatsTypeChange">
                <el-option label="按时间" value="time"></el-option>
                <el-option label="按方式" value="method"></el-option>
              </el-select>
            </div>
          </template>
          <div ref="paymentChartRef" class="chart"></div>
        </el-card>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch, nextTick, computed } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Download } from '@element-plus/icons-vue'
import * as echarts from 'echarts'

// 获取当前路由
const route = useRoute()

// 根据路由计算当前页面类型
const currentPage = computed(() => {
  const path = route.path
  if (path === '/stats/customer') {
    return 'customer'
  } else if (path === '/stats/sales') {
    return 'sales'
  } else if (path === '/stats/opportunity') {
    return 'opportunity'
  } else if (path === '/stats/payment') {
    return 'payment'
  } else {
    return 'default'
  }
})

// 日期范围
const dateRange = ref([])

// 统计卡片数据
const todayNewCustomers = ref(12)
const monthlySales = ref(1250000.00)
const monthlyOpportunities = ref(35)
const monthlyPayments = ref(980000.00)

// 统计类型
const customerStatsType = ref('time')
const salesStatsType = ref('employee')
const opportunityStatsType = ref('stage')
const paymentStatsType = ref('time')

// 图表引用
const customerChartRef = ref(null)
const salesChartRef = ref(null)
const opportunityChartRef = ref(null)
const paymentChartRef = ref(null)

// 图表实例
let customerChart = null
let salesChart = null
let opportunityChart = null
let paymentChart = null

// 客户统计数据
const customerStatsData = {
  time: {
    xAxis: ['1月', '2月', '3月', '4月', '5月', '6月'],
    yAxis: [45, 62, 38, 59, 72, 65]
  },
  category: {
    xAxis: ['潜在客户', '意向客户', '合作客户', '流失客户'],
    yAxis: [120, 85, 210, 35]
  },
  dept: {
    xAxis: ['销售一部', '销售二部', '销售三部', '市场部'],
    yAxis: [85, 62, 45, 38]
  }
}

// 销售业绩数据
const salesStatsData = {
  employee: {
    xAxis: ['管理员', '销售1', '销售2', '销售3', '销售4'],
    yAxis: [1200000, 850000, 620000, 450000, 380000]
  },
  dept: {
    xAxis: ['销售一部', '销售二部', '销售三部', '市场部'],
    yAxis: [1800000, 1200000, 950000, 450000]
  },
  time: {
    xAxis: ['1月', '2月', '3月', '4月', '5月', '6月'],
    yAxis: [950000, 1200000, 850000, 1100000, 1350000, 1250000]
  }
}

// 商机统计数据
const opportunityStatsData = {
  stage: {
    xAxis: ['初步接洽', '需求确认', '方案报价', '合同签订'],
    yAxis: [85, 62, 45, 28]
  },
  time: {
    xAxis: ['1月', '2月', '3月', '4月', '5月', '6月'],
    yAxis: [35, 42, 38, 45, 32, 35]
  }
}

// 回款统计数据
const paymentStatsData = {
  time: {
    xAxis: ['1月', '2月', '3月', '4月', '5月', '6月'],
    yAxis: [850000, 920000, 780000, 850000, 980000, 950000]
  },
  method: {
    xAxis: ['银行转账', '支付宝', '微信支付', '现金'],
    yAxis: [2800000, 1200000, 950000, 350000]
  }
}

// 初始化客户图表
const initCustomerChart = () => {
  if (customerChart) {
    customerChart.dispose()
  }
  
  customerChart = echarts.init(customerChartRef.value)
  const data = customerStatsData[customerStatsType.value]
  
  const option = {
    title: {
      text: '客户统计',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: data.xAxis
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '客户数量',
        type: 'bar',
        data: data.yAxis,
        itemStyle: {
          color: '#409EFF'
        }
      }
    ]
  }
  
  customerChart.setOption(option)
}

// 初始化销售图表
const initSalesChart = () => {
  if (salesChart) {
    salesChart.dispose()
  }
  
  salesChart = echarts.init(salesChartRef.value)
  const data = salesStatsData[salesStatsType.value]
  
  const option = {
    title: {
      text: '销售业绩统计',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      },
      formatter: function(params) {
        return params[0].name + '<br/>' + params[0].seriesName + ': ¥' + params[0].value.toLocaleString()
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: data.xAxis
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        formatter: function(value) {
          return (value / 10000).toFixed(0) + '万'
        }
      }
    },
    series: [
      {
        name: '销售金额',
        type: 'bar',
        data: data.yAxis,
        itemStyle: {
          color: '#67C23A'
        }
      }
    ]
  }
  
  salesChart.setOption(option)
}

// 初始化商机图表
const initOpportunityChart = () => {
  if (opportunityChart) {
    opportunityChart.dispose()
  }
  
  opportunityChart = echarts.init(opportunityChartRef.value)
  const data = opportunityStatsData[opportunityStatsType.value]
  
  let chartType = 'bar'
  if (opportunityStatsType.value === 'stage') {
    chartType = 'pie'
  }
  
  let option = {}
  
  if (chartType === 'bar') {
    option = {
      title: {
        text: '商机统计',
        left: 'center'
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        data: data.xAxis
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: '商机数量',
          type: 'bar',
          data: data.yAxis,
          itemStyle: {
            color: '#E6A23C'
          }
        }
      ]
    }
  } else {
    option = {
      title: {
        text: '商机阶段分布',
        left: 'center'
      },
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b}: {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left',
        data: data.xAxis
      },
      series: [
        {
          name: '商机数量',
          type: 'pie',
          radius: '50%',
          data: data.xAxis.map((item, index) => ({
            value: data.yAxis[index],
            name: item
          })),
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    }
  }
  
  opportunityChart.setOption(option)
}

// 初始化回款图表
const initPaymentChart = () => {
  if (paymentChart) {
    paymentChart.dispose()
  }
  
  paymentChart = echarts.init(paymentChartRef.value)
  const data = paymentStatsData[paymentStatsType.value]
  
  const option = {
    title: {
      text: '回款统计',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      },
      formatter: function(params) {
        return params[0].name + '<br/>' + params[0].seriesName + ': ¥' + params[0].value.toLocaleString()
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: data.xAxis
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        formatter: function(value) {
          return (value / 10000).toFixed(0) + '万'
        }
      }
    },
    series: [
      {
        name: '回款金额',
        type: 'line',
        data: data.yAxis,
        itemStyle: {
          color: '#909399'
        },
        lineStyle: {
          width: 3
        },
        symbol: 'circle',
        symbolSize: 8
      }
    ]
  }
  
  paymentChart.setOption(option)
}

// 处理日期范围变化
const handleDateRangeChange = () => {
  // 模拟API请求
  // 实际项目中需要调用后端API
  setTimeout(() => {
    ElMessage.success('日期范围已更新')
    updateCharts()
  }, 500)
}

// 处理客户统计类型变化
const handleCustomerStatsTypeChange = () => {
  initCustomerChart()
}

// 处理销售统计类型变化
const handleSalesStatsTypeChange = () => {
  initSalesChart()
}

// 处理商机统计类型变化
const handleOpportunityStatsTypeChange = () => {
  initOpportunityChart()
}

// 处理回款统计类型变化
const handlePaymentStatsTypeChange = () => {
  initPaymentChart()
}

// 更新所有图表
const updateCharts = () => {
  initCustomerChart()
  initSalesChart()
  initOpportunityChart()
  initPaymentChart()
}

// 导出报表
const handleExport = () => {
  // 模拟API请求
  // 实际项目中需要调用后端API
  setTimeout(() => {
    ElMessage.success('导出报表成功')
  }, 500)
}

// 监听窗口大小变化
const handleResize = () => {
  customerChart?.resize()
  salesChart?.resize()
  opportunityChart?.resize()
  paymentChart?.resize()
}

// 组件挂载时
onMounted(() => {
  nextTick(() => {
    if (currentPage.value === 'customer' || currentPage.value === 'default') {
      initCustomerChart()
    }
    if (currentPage.value === 'sales' || currentPage.value === 'default') {
      initSalesChart()
    }
    if (currentPage.value === 'opportunity' || currentPage.value === 'default') {
      initOpportunityChart()
    }
    if (currentPage.value === 'payment' || currentPage.value === 'default') {
      initPaymentChart()
    }
  })
  
  window.addEventListener('resize', handleResize)
})
</script>

<style scoped>
.stats-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  align-items: center;
}

.stats-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.stats-card {
  transition: all 0.3s ease;
}

.stats-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.card-content {
  text-align: center;
  padding: 20px 0;
}

.card-title {
  font-size: 16px;
  color: #606266;
  margin-bottom: 10px;
}

.card-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 10px;
}

.card-change {
  font-size: 14px;
}

.card-change.positive {
  color: #67C23A;
}

.card-change.negative {
  color: #F56C6C;
}

.charts-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(500px, 1fr));
  gap: 20px;
}

.chart-card {
  height: 400px;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart {
  width: 100%;
  height: 320px;
}

@media (max-width: 1200px) {
  .charts-container {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .stats-cards {
    grid-template-columns: 1fr;
  }
  
  .charts-container {
    grid-template-columns: 1fr;
  }
  
  .chart-card {
    height: 350px;
  }
  
  .chart {
    height: 270px;
  }
}
</style>