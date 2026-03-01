// 引入封装的请求工具 + 租户工具
const { request } = require('../../utils/request.js');
const tenantUtil = require('../../utils/tenant.js');

Page({
  data: {
    // 库存统计（布匹专用）
    totalRollCount: 120,    // 总匹数
    totalMeters: 3600.5,    // 总米数
    todayInMeters: 280,
    todayOutMeters: 160,

    recordList: [
      { id: 1, time: '09:25', type: 'in', model: 'C001', meters: 28.5 },
      { id: 2, time: '08:40', type: 'out', model: 'C002', meters: 32.0 },
    ],

    // 扫码
    barcode: '',
    showClothModal: false,
    clothInfo: {},         // 后端返回的布匹信息
    inputMeters: 1,

    // 打印相关
    showPrintModal: false, // 打印选择弹窗
    printData: {},         // 待打印的条码数据
    bluetoothDevices: [],  // 已配对的蓝牙设备
    connectedDeviceId: '', // 已连接的打印机设备ID
    isBluetoothOpen: false // 蓝牙是否开启
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.getInventoryStats();
    // 初始化蓝牙（可选：进入页面检测蓝牙状态）
    this.initBluetooth();
  },

  // 扫码（只识别 布匹条码）
  handleScanCode() {
    wx.scanCode({
      scanType: ['barCode'],
      success: res => {
        const barcode = res.result;
        this.setData({ barcode });
        this.getClothInfo(barcode);
      }
    })
  },

  // 查询布匹信息
  async getClothInfo(barcode) {
    const tenantId = tenantUtil.getTenantId();
    if (!tenantId) {
      wx.showToast({ title: '请先选择租户', icon: 'none' });
      return;
    }

    wx.showLoading({ title: '查询中...' })

    // 模拟逻辑（对接后端时替换为真实接口）
    setTimeout(() => {
      const mockCloth = {
        model: 'C001',
        meters: 28.5,
        isInStock: false, // 是否已入库
        color: '白色',
        width: '1.8m'
      }
      this.setData({ clothInfo: mockCloth, showClothModal: true })
      wx.hideLoading()
    }, 800)
  },

  // 输入米数
  onMetersInput(e) {
    this.setData({ inputMeters: Number(e.detail.value) })
  },

  // 入库（核心：入库成功后触发打印）
  async doIn() {
    const meters = this.data.inputMeters;
    const tenantId = tenantUtil.getTenantId();
    
    if (!tenantId) {
      wx.showToast({ title: '请先选择租户', icon: 'none' });
      return;
    }
    if (!meters || meters <= 0) {
      wx.showToast({ title: '请输入有效米数', icon: 'none' });
      return;
    }

    wx.showLoading({ title: '入库中...' })

    // 模拟入库接口（对接后端时替换）
    setTimeout(() => {
      wx.hideLoading();
      // 模拟后端返回的条码数据（实际由后端生成）
      const printData = {
        barcode: `CL${new Date().getFullYear().toString().slice(2)}${(new Date().getMonth()+1).toString().padStart(2, '0')}${new Date().getDate().toString().padStart(2, '0')}${Math.floor(Math.random()*10000).toString().padStart(4, '0')}`,
        model: this.data.clothInfo.model,
        meters: meters,
        color: this.data.clothInfo.color,
        width: this.data.clothInfo.width,
        tenantId: tenantId
      };
      // 入库成功后存储打印数据，打开打印选择弹窗
      this.setData({
        printData: printData,
        showPrintModal: true
      });
      wx.showToast({ title: '入库成功，准备打印条码', icon: 'success' });
      this.hideClothModal();
    }, 800);

    // 真实后端接口（对接时取消注释）
    /*
    try {
      const res = await request({
        url: 'https://你的域名/api/cloth/in',
        data: {
          barcode: this.data.barcode || '', // 为空则由后端生成
          model: this.data.clothInfo.model,
          meters: meters,
          color: this.data.clothInfo.color,
          width: this.data.clothInfo.width
        }
      });
      wx.hideLoading();
      // 存储后端返回的打印数据
      this.setData({
        printData: res.data.printData,
        showPrintModal: true
      });
      wx.showToast({ title: '入库成功，准备打印条码', icon: 'success' });
      this.hideClothModal();
    } catch (err) {
      wx.hideLoading();
      console.error('入库失败：', err);
    }
    */
  },

  // 出库
  async doOut() {
    const tenantId = tenantUtil.getTenantId();
    if (!tenantId) {
      wx.showToast({ title: '请先选择租户', icon: 'none' });
      return;
    }

    wx.showLoading({ title: '出库中...' })

    // 模拟出库接口
    setTimeout(() => {
      wx.hideLoading();
      wx.showToast({ title: '出库成功', icon: 'success' });
      this.hideClothModal();
    }, 800);

    // 真实后端接口（对接时取消注释）
    /*
    try {
      await request({
        url: 'https://你的域名/api/cloth/out',
        data: { barcode: this.data.barcode }
      });
      wx.hideLoading();
      wx.showToast({ title: '出库成功', icon: 'success' });
      this.hideClothModal();
    } catch (err) {
      wx.hideLoading();
      console.error('出库失败：', err);
    }
    */
  },

  // ===================== 打印相关核心方法 =====================
  /**
   * 选择打印方式（预览/蓝牙）
   * @param {String} type - preview:预览打印，bluetooth:蓝牙打印
   */
  choosePrintType(e) {
    const type = e.currentTarget.dataset.type;
    const { printData } = this.data;
    if (!printData.barcode) {
      wx.showToast({ title: '无打印数据', icon: 'none' });
      return;
    }

    if (type === 'preview') {
      // 预览打印（生成条码标签图片）
      this.previewPrint(printData);
    } else if (type === 'bluetooth') {
      // 蓝牙打印
      this.bluetoothPrint(printData);
    }
  },

  /**
   * 预览打印（生成条码标签图片，可截图/连接打印机打印）
   */
  previewPrint(printData) {
    // 拼接条码生成接口（可替换为自己的条码生成服务）
    const barcodeUrl = `https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=${printData.barcode}`;
    // 构造标签内容
    const labelContent = `
      条码：${printData.barcode}
      型号：${printData.model}
      米数：${printData.meters}m
      颜色：${printData.color}
      门幅：${printData.width}
    `;

    // 预览图片（实际项目可生成带内容的标签图片）
    wx.previewImage({
      urls: [barcodeUrl],
      success: () => {
        wx.showToast({ title: '请截图/连接打印机打印', icon: 'none' });
      }
    });

    // 可选：保存标签图片到本地
    wx.downloadFile({
      url: barcodeUrl,
      success: (res) => {
        wx.saveImageToPhotosAlbum({
          filePath: res.tempFilePath,
          success: () => {
            wx.showToast({ title: '条码图片已保存到相册', icon: 'success' });
          }
        });
      }
    });
  },

  /**
   * 蓝牙打印（对接主流蓝牙条码打印机）
   */
  bluetoothPrint(printData) {
    const { connectedDeviceId } = this.data;
    // 已连接打印机，直接发送打印指令
    if (connectedDeviceId) {
      this.sendPrintCommand(printData);
      return;
    }

    // 未连接打印机，先搜索蓝牙设备
    wx.showLoading({ title: '搜索蓝牙打印机...' });
    wx.startBluetoothDevicesDiscovery({
      services: ['0000FFE0-0000-1000-8000-00805F9B34FB'], // 打印机通用服务ID
      success: (res) => {
        wx.hideLoading();
        // 获取已发现的蓝牙设备
        wx.getBluetoothDevices({
          success: (res) => {
            const devices = res.devices.filter(device => device.name && device.name.includes('Printer')); // 筛选打印机设备
            if (devices.length === 0) {
              wx.showToast({ title: '未找到蓝牙打印机', icon: 'none' });
              return;
            }
            this.setData({ bluetoothDevices: devices });
            // 选择第一个打印机连接
            this.connectBluetoothDevice(devices[0].deviceId);
          }
        });
      },
      fail: () => {
        wx.hideLoading();
        wx.showToast({ title: '蓝牙搜索失败，请开启蓝牙', icon: 'none' });
      }
    });
  },

  /**
   * 初始化蓝牙状态
   */
  initBluetooth() {
    wx.openBluetoothAdapter({
      success: () => {
        this.setData({ isBluetoothOpen: true });
      },
      fail: () => {
        this.setData({ isBluetoothOpen: false });
        wx.showToast({ title: '请开启蓝牙以使用打印功能', icon: 'none' });
      }
    });
  },

  /**
   * 连接蓝牙打印机
   */
  connectBluetoothDevice(deviceId) {
    wx.createBLEConnection({
      deviceId: deviceId,
      success: () => {
        this.setData({ connectedDeviceId: deviceId });
        wx.showToast({ title: '打印机连接成功', icon: 'success' });
        // 连接成功后发送打印指令
        this.sendPrintCommand(this.data.printData);
      },
      fail: () => {
        wx.showToast({ title: '打印机连接失败', icon: 'none' });
      }
    });
  },

  /**
   * 发送打印指令到蓝牙打印机
   */
  sendPrintCommand(printData) {
    const { connectedDeviceId } = this.data;
    // 获取打印机服务和特征值（通用指令，不同打印机可调整）
    wx.getBLEDeviceServices({
      deviceId: connectedDeviceId,
      success: (res) => {
        const serviceId = res.services[0].uuid;
        wx.getBLEDeviceCharacteristics({
          deviceId: connectedDeviceId,
          serviceId: serviceId,
          success: (res) => {
            const charId = res.characteristics[0].uuid;
            // 构造打印内容（适配ESC/POS指令）
            const printContent = `
              ${printData.barcode}\n
              型号：${printData.model}\n
              米数：${printData.meters}m\n
              颜色：${printData.color}\n
              门幅：${printData.width}\n
              \n\n
            `;
            // 转换为ArrayBuffer发送
            const buffer = new ArrayBuffer(printContent.length);
            const dataView = new DataView(buffer);
            for (let i = 0; i < printContent.length; i++) {
              dataView.setUint8(i, printContent.charCodeAt(i));
            }
            // 发送打印指令
            wx.writeBLECharacteristicValue({
              deviceId: connectedDeviceId,
              serviceId: serviceId,
              characteristicId: charId,
              value: buffer,
              success: () => {
                wx.showToast({ title: '打印指令发送成功', icon: 'success' });
                this.setData({ showPrintModal: false });
              },
              fail: () => {
                wx.showToast({ title: '打印指令发送失败', icon: 'none' });
              }
            });
          }
        });
      }
    });
  },

  /**
   * 模拟自动化设备入库（前端测试用）
   */
  simulateAutoIn() {
    const autoData = {
      model: 'C002',
      meters: 32.7,
      color: '黑色',
      width: '1.8m'
    };
    // 调用自动化设备入库接口
    this.autoClothIn(autoData);
  },

  /**
   * 自动化设备入库接口（预留）
   */
  async autoClothIn(autoData) {
    const tenantId = tenantUtil.getTenantId();
    if (!tenantId) {
      wx.showToast({ title: '请先选择租户', icon: 'none' });
      return;
    }

    wx.showLoading({ title: '自动化入库中...' });
    // 真实接口（对接后端时使用）
    /*
    try {
      const res = await request({
        url: 'https://你的域名/api/auto/cloth/in',
        data: autoData
      });
      wx.hideLoading();
      // 自动打印条码
      this.setData({
        printData: res.data.printData,
        showPrintModal: true
      });
      wx.showToast({ title: '自动化入库成功', icon: 'success' });
    } catch (err) {
      wx.hideLoading();
      console.error('自动化入库失败：', err);
    }
    */

    // 模拟返回
    setTimeout(() => {
      wx.hideLoading();
      const printData = {
        barcode: `CL${new Date().getFullYear().toString().slice(2)}${(new Date().getMonth()+1).toString().padStart(2, '0')}${new Date().getDate().toString().padStart(2, '0')}${Math.floor(Math.random()*10000).toString().padStart(4, '0')}`,
        ...autoData,
        tenantId: tenantId
      };
      this.setData({
        printData: printData,
        showPrintModal: true
      });
      wx.showToast({ title: '自动化入库成功，准备打印', icon: 'success' });
    }, 1000);
  },

  // ===================== 辅助方法 =====================
  getInventoryStats() {
    // 库存统计逻辑（略）
  },

  hideClothModal() {
    this.setData({ showClothModal: false, barcode: '', clothInfo: {}, inputMeters: 0 });
  },

  hidePrintModal() {
    this.setData({ showPrintModal: false });
  },

  stopPropagation() {},
  handleBack() { wx.navigateBack() },
  handleStockCheck() { wx.showToast({ title: '盘点功能开发中', icon: 'none' }) },
  handleShowAllRecord() {},
});