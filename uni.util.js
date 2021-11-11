/* loading */
//显示loading窗口
let showLoading = function(title){
	uni.showLoading({
	    title: title,
		mask: true
	});
}
//隐藏loading窗口
let hideLoading = function(){
	uni.hideLoading();
}

/* 提示信息 */
/* 	icon说明
	success	显示成功图标，此时 title 文本最多显示 7 个汉字长度。默认值	
	error	显示错误图标，此时 title 文本最多显示 7 个汉字长度。	
	loading	显示加载图标，此时 title 文本最多显示 7 个汉字长度。	支付宝小程序不支持
	none	不显示图标，此时 title 文本在小程序最多可显示两行，App仅支持单行显示。 */
let showToast = function(title, timeOut, icon){
	uni.showToast({
		title: title,
		duration: timeOut,
		mask: true,
		icon: icon
	});
}

/* 扫一扫*/
let scanOneScan = function(){
	// 允许从相机和相册扫码
	uni.scanCode({
	    success: function (res) {
	        console.log('条码类型：' + res.scanType);
	        console.log('条码内容：' + res.result);
	    }
	});
}

/* 前往某页面,可以返回 */
let navigateToPage = function navigateToPage(e){
	uni.navigateTo({
		url:e
	})
}
/* 切换tab */
let switchTabPage = function switchTabPage(e){
	uni.switchTab({
		url:e
	})
}
/* 页面重定向 */
let reLaunchPage = function reLaunchPage(e){
	uni.reLaunch({
		url: e
	});
}
/* 关闭所有页面,跳转到指定页面 */
let redirectPage = function redirectPage(e){
	uni.redirectTo({
		url: e
	});
}
/* 返回上一级页面 */
let backToPage = function backToPage(e){
	uni.navigateBack({
		delta: 1
	});
}
/* 页面传参 */
let setPageParam = function setPageParam(key, value){
	uni.setStorageSync(key, value)
}
/* 页面取参 */
let getPageParam = function getPageParam(key){
	return uni.getStorageSync(key)
}
/* 清除参数 */
let removePageParam = function removePageParam(key){
	return uni.removeStorageSync(key)
}
/* 返回上一级页面并执行上级页面的mounted方法 */
let backToPageAndRefresh = function backToPageAndRefresh(key){
	let pages = getCurrentPages(); // 当前页面
	let beforePage = pages[pages.length - 2];// 上一页
	uni.navigateBack({
	    success: function() {
			console.log(beforePage)
	        beforePage.$vm.selectNew(); // 执行上一页的mounted方法
	    }
	})
}

/* 返回上一级页面并执行上级页面的onLoad方法 */
let backToPageAndRefreshOnLoad = function backToPageAndRefreshOnLoad(){
	let pages = getCurrentPages(); // 当前页面
	let beforePage = pages[pages.length - 2]; // 上一页
	uni.navigateBack({
	    success: function() {
	        beforePage.onLoad(); // 执行上一页的onLoad方法
	    }
	})
}
export{ showLoading,hideLoading,navigateToPage,switchTabPage,redirectPage,showToast,backToPage,setPageParam,getPageParam,removePageParam,reLaunchPage,backToPageAndRefresh,backToPageAndRefreshOnLoad,scanOneScan }