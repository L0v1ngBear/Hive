// pages/index/index.js
// 引入多租户和功能权限工具类（核心新增）
const functionAuth = require('../../utils/function-auth.js');
const tenantUtil = require('../../utils/tenant.js');
const { request } = require('../../utils/request.js');

Page({
  /**
   * 页面的初始数据
   */
  data: {
    isPc: false,
    todoCount: 0, // 接口返回的待办总数
    todoList: [],  // 接口返回的待办列表
    userInfo: {    // 用户信息（可后续也从接口获取）
      name: "张三",
      dept: "技术部 - 前端开发工程师"
    },
    // 新增：功能启用状态映射（与WXML中的功能项对应）
    functionEnable: {
      attendance: false,
      order: false,
      inventory: false,
      approval: false,
      notice: false,
      file: false,
      contact: false,
      meeting: false,
      asset: false,
      more: false,
      badProduct: false
    },
    // 新增：是否有任意功能启用（用于空状态显示）
    hasAnyFunctionEnable: false
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    // 新增：初始化功能权限（优先于数据请求，保证UI渲染正确）
    this.initFunctionPermission();
    // 初始化用户信息（关联租户）
    this.initUserInfo();
    // 页面加载时请求待办数据（原有逻辑保留，改为调用封装请求）
    this.fetchTodoData();
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    // 新增：页面重新显示时刷新功能权限（防止功能开关修改后未刷新）
    this.initFunctionPermission();
    // 页面重新显示时刷新数据（比如从其他页面返回）（原有逻辑保留）
    this.fetchTodoData();
  },

  /**
   * 新增：初始化用户信息（关联租户）
   */
  initUserInfo() {
    // 获取当前租户信息
    const tenantInfo = tenantUtil.getTenantInfo();
    if (tenantInfo && tenantInfo.name) {
      // 租户信息合并到用户部门，区分不同租户
      this.setData({
        userInfo: {
          ...this.data.userInfo,
          dept: `${tenantInfo.name} - ${this.data.userInfo.dept}`
        }
      });
    }
  },

  /**
   * 新增：初始化功能权限（核心逻辑）
   */
  initFunctionPermission() {
    // 校验每个功能的启用状态（与function-auth.js关联）
    const functionEnable = {
      attendance: true,  // 强制显示考勤
      order: true,       // 强制显示订单
      inventory: true,   // 强制显示库存
      approval: true,
      notice: true,
      file: false,
      contact: false,
      meeting: false,
      asset: false,
      more: false,
      badProduct: true
      // TODO 接口还没实现
      // attendance: functionAuth.checkEnable('attendance'),
      // order: functionAuth.checkEnable('order'),
      // inventory: functionAuth.checkEnable('inventory'),
      // approval: functionAuth.checkEnable('approval'),
      // notice: functionAuth.checkEnable('notice'),
      // file: functionAuth.checkEnable('file'),
      // contact: functionAuth.checkEnable('contact'),
      // meeting: functionAuth.checkEnable('meeting'),
      // asset: functionAuth.checkEnable('asset'),
      // more: functionAuth.checkEnable('more'),
      // badProduct: functionAuth.checkEnable('badProduct')
      
    };

    // 判断是否有任意功能启用（用于空状态显示）
    const hasAnyFunctionEnable = Object.values(functionEnable).some(enable => enable === true);

    // 更新页面数据
    this.setData({
      functionEnable,
      hasAnyFunctionEnable
    });
  },

  /**
   * 核心：请求待办数据接口（改造：使用封装请求，自动带租户ID）
   */
  async fetchTodoData() {
    // 前置校验：租户ID是否存在
    const tenantId = tenantUtil.getTenantId();
    if (!tenantId) {
      wx.showToast({ title: '请先选择租户', icon: 'none' });
      return;
    }

    wx.showLoading({
      title: '加载中...',
    });

    try {
      // 替换原生wx.request为封装的request，自动带租户ID
      const res = await request({
        url: 'https://你的接口域名/api/todo/list',
        method: 'GET',
        // 无需手动加Tenant-Id，封装方法会自动添加
        // 如需token认证，可在request.js中统一配置，无需此处写
        // data: {} // GET请求无需传data，如需传参可加
      });

      // 接口成功逻辑（res已在封装方法中过滤，仅返回code=200的数据）
      const { count, list } = res.data;
      this.setData({
        todoCount: count,
        todoList: list
      });
    } catch (err) {
      // 异常已在封装方法中统一提示，此处仅记录日志
      console.error('待办数据请求失败：', err);
    } finally {
      // 无论成功/失败，都隐藏loading
      wx.hideLoading();
    }
  },

  /**
   * 核心功能模块点击事件（原有逻辑保留，新增权限兜底校验）
   */
  handleFunctionTap(e) {
    // 注意：WXML中是data-function-type，这里要对应
    const type = e.currentTarget.dataset.functionType;

    // 新增：权限兜底校验（防止禁用功能被触发）
    if (!this.data.functionEnable[type]) {
      wx.showToast({
        title: '该功能已禁用，请联系管理员',
        icon: 'none',
        duration: 2000
      });
      return;
    }

    // 原有逻辑保留
    wx.showToast({
      title: `即将进入${this.getFunctionName(type)}`,
      icon: "none",
      duration: 1500
    });
    // 实际项目中跳转对应页面
    wx.navigateTo({ url: `/pages/${type}/${type}` });
  },

  /**
   * 获取功能名称（辅助函数，保留原有逻辑）
   */
  getFunctionName(type) {
    const functionMap = {
      order: "订单管理",
      inventory: "库存管理",
      attendance: "考勤打卡",
      approval: "审批流程",
      notice: "企业公告",
      file: "内部文件",
      contact: "员工通讯录",
      meeting: "会议室预约",
      asset: "资产管理",
      more: "更多功能",
      badProduct: "次品管理"
    };
    return functionMap[type] || "未知功能";
  },

  /**
   * 跳转到待办列表页（新增：点击待办数量区域的跳转）
   */
  handleJumpToTodoList() {
    wx.navigateTo({
      url: '/pages/todoList/todoList' // 替换为实际待办列表页路径
    });
  },

  /**
   * 快捷待办查看更多（保留并优化）
   */
  handleMoreTodo() {
    wx.navigateTo({
      url: '/pages/todoList/todoList' // 替换为实际待办列表页路径
    });
    // 可选：保留提示
    // wx.showToast({
    //   title: "即将进入待办列表",
    //   icon: "none",
    //   duration: 1500
    // });
  },

  /**
   * 快捷待办项点击事件（保留原有逻辑）
   */
  handleTodoItemTap(e) {
    const todoId = e.currentTarget.dataset.todoId; // 对应WXML的data-todo-id
    wx.showToast({
      title: `查看待办项 ${todoId} 详情`,
      icon: "none",
      duration: 1500
    });
    // 实际项目中跳转待办详情页
    // wx.navigateTo({ url: `/pages/todoDetail/todoDetail?id=${todoId}` });
  },

  /**
   * 底部导航切换事件（保留并优化参数名）
   */
  switchTab(e) {
    const tab = e.currentTarget.dataset.tabPage; // 对应WXML的data-tab-page
    if (tab === "index") {
      // 已在首页，无需跳转
      return;
    } else if (tab === "mine") {
      // 跳转至“我的”页面
      wx.switchTab({
        url: "/pages/mine/mine"
      });
    }
  },

  /**
   * 搜索输入处理（新增：适配WXML的搜索框事件）
   */
  handleSearchInput(e) {
    console.log('搜索输入内容：', e.detail.value);
    // 可添加实时搜索逻辑
  },

  /**
   * 搜索确认处理（新增：适配WXML的搜索框事件）
   */
  handleSearchConfirm(e) {
    console.log('搜索确认内容：', e.detail.value);
    // 可添加搜索提交逻辑
  }
});