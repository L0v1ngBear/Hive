// pages/index/index.js
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
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    // 页面加载时请求待办数据
    this.fetchTodoData();
    
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    // 页面重新显示时刷新数据（比如从其他页面返回）
    this.fetchTodoData();
  },

  /**
   * 核心：请求待办数据接口
   */
  fetchTodoData() {
    wx.showLoading({
      title: '加载中...',
    });

    // 替换为你的真实接口地址
    wx.request({
      url: 'https://你的接口域名/api/todo/list', 
      method: 'GET',
      header: {
        'content-type': 'application/json',
        // 如需token认证，取消注释并替换
        // 'Authorization': 'Bearer ' + wx.getStorageSync('token')
      },
      success: (res) => {
        wx.hideLoading();
        // 假设接口返回code=200为成功（根据实际接口调整）
        if (res.data.code === 200) {
          const { count, list } = res.data.data;
          // 更新页面数据
          this.setData({
            todoCount: count,
            todoList: list
          });
        } else {
          wx.showToast({
            title: res.data.msg || '加载失败',
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        wx.hideLoading();
        wx.showToast({
          title: '网络异常，请重试',
          icon: 'none'
        });
        console.error('待办数据请求失败：', err);
      }
    });
  },

  /**
   * 核心功能模块点击事件（保留原有逻辑，优化参数名）
   */
  handleFunctionTap(e) {
    // 注意：WXML中是data-function-type，这里要对应
    const type = e.currentTarget.dataset.functionType;
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