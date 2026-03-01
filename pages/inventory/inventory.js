// pages/inventory/inventory.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    // 库存统计数据
    totalCount: 1286,
    todayInCount: 28,
    todayOutCount: 16,
    // 操作记录列表
    recordList: [
      { id: 1, time: '09:25', type: 'in', goodsName: '办公用品A', count: 10, status: 'success' },
      { id: 2, time: '08:42', type: 'out', goodsName: '生产原料B', count: 5, status: 'success' },
      { id: 3, time: '昨日', type: 'in', goodsName: '包装材料C', count: 20, status: 'success' },
      { id: 4, time: '昨日', type: 'out', goodsName: '成品D', count: 8, status: 'fail' },
      { id: 5, time: '昨日', type: 'in', goodsName: '配件E', count: 15, status: 'success' }
    ],
    // 扫码相关
    showScanModal: false,
    scanResult: '',
    currentOperType: '' // in:入库 out:出库
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    // 支持从扫码直接进入页面并自动触发对应操作
    if (options.qrCode) {
      // 解析二维码参数（示例：qrCode=in:123456 或 qrCode=out:123456）
      const [operType, goodsCode] = options.qrCode.split(':');
      if (operType && goodsCode) {
        this.setData({
          currentOperType: operType,
          scanResult: goodsCode,
          showScanModal: true
        });
      }
    }
  },

  /**
   * 返回上一页
   */
  handleBack() {
    const pages = getCurrentPages();
    if (pages.length > 1) {
      wx.navigateBack({
        delta: 1,
        fail: () => {
          wx.redirectTo({
            url: '/pages/index/index'
          });
        }
      });
    } else {
      wx.redirectTo({
        url: '/pages/index/index'
      });
    }
  },

  /**
   * 扫码操作（顶部扫码按钮）
   */
  handleScanCode() {
    wx.scanCode({
      onlyFromCamera: true,
      scanType: ['qrCode'],
      success: (res) => {
        // 解析扫码结果，这里假设二维码内容格式为：in:商品编码 或 out:商品编码
        const [operType, goodsCode] = res.result.split(':');
        if (operType && goodsCode) {
          this.setData({
            currentOperType: operType,
            scanResult: goodsCode,
            showScanModal: true
          });
        } else {
          wx.showToast({
            title: '二维码格式错误',
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        console.error('扫码失败', err);
        wx.showToast({
          title: '扫码失败，请重试',
          icon: 'none'
        });
      }
    });
  },

  /**
   * 扫码入库
   */
  handleInStock() {
    this.setData({
      currentOperType: 'in'
    });
    this.handleScanCode();
  },

  /**
   * 扫码出库
   */
  handleOutStock() {
    this.setData({
      currentOperType: 'out'
    });
    this.handleScanCode();
  },

  /**
   * 库存盘点
   */
  handleStockCheck() {
    wx.showToast({
      title: '库存盘点功能开发中',
      icon: 'none'
    });
  },

  /**
   * 查看全部记录
   */
  handleShowAllRecord() {
    wx.navigateTo({
      url: '/pages/inventoryRecord/inventoryRecord',
      fail: () => {
        wx.showToast({
          title: '功能暂未开发',
          icon: 'none'
        });
      }
    });
  },

  /**
   * 显示扫码弹窗
   */
  showScanModal() {
    this.setData({
      showScanModal: true
    });
  },

  /**
   * 隐藏扫码弹窗
   */
  hideScanModal() {
    this.setData({
      showScanModal: false,
      scanResult: '',
      currentOperType: ''
    });
  },

  /**
   * 阻止事件冒泡
   */
  stopPropagation() {},

  /**
   * 确认扫码操作
   */
  confirmScan() {
    wx.showLoading({
      title: '处理中...',
      mask: true
    });

    // 模拟接口请求处理出入库
    setTimeout(() => {
      wx.hideLoading();
      
      // 更新库存数据
      if (this.data.currentOperType === 'in') {
        this.setData({
          totalCount: this.data.totalCount + 1,
          todayInCount: this.data.todayInCount + 1
        });
        wx.showToast({
          title: '入库成功',
          icon: 'success'
        });
      } else if (this.data.currentOperType === 'out') {
        this.setData({
          totalCount: this.data.totalCount - 1,
          todayOutCount: this.data.todayOutCount + 1
        });
        wx.showToast({
          title: '出库成功',
          icon: 'success'
        });
      }

      // 添加操作记录
      const now = new Date();
      const hours = now.getHours().toString().padStart(2, '0');
      const minutes = now.getMinutes().toString().padStart(2, '0');
      const newRecord = {
        id: Date.now(),
        time: `${hours}:${minutes}`,
        type: this.data.currentOperType,
        goodsName: `商品${this.data.scanResult.slice(-4)}`,
        count: 1,
        status: 'success'
      };
      
      this.data.recordList.unshift(newRecord);
      // 只保留最近5条记录
      if (this.data.recordList.length > 5) {
        this.data.recordList = this.data.recordList.slice(0, 5);
      }
      this.setData({
        recordList: this.data.recordList,
        showScanModal: false
      });
    }, 1000);
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {},

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {},

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {},

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {},

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {},

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {},

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {
    return {
      title: '库存管理',
      path: '/pages/inventory/inventory'
    };
  }
});