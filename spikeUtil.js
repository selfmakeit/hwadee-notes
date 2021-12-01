import {postUrl,hasErrorMsg} from "@/pages/api/api.js"
import {errdata} from "@/pages/api/errdata.js"
import {url} from "@/pages/api/request.js"
export let spike ={
	//同步获取数据字典名称
	getDicNameById:async function(id){
		var result;
		if(id){
			result= await doGetDicItemById(id);
		}else{
			return "";
		}
		return result.codeName;
	},
	//同步获取数据字典值
	getDicValueById:async function(id){
		var result;
		if(id){
			result= await doGetDicItemById(id);
		}else{
			return "";
		}
		return result.codeValue;
	},
	//同步获取数据字典值
	getDicValueByTypeCode:async function(type){
		var result;
		if(type){
			result= await doGetDicItemByTypeCode(type);
		}else{
			return "";
		}
		return result.codeValue;
	},
	//同步获取改类型所有数据字典
	queryAllDictByTypeCode:async function(type){
		var result;
		if(type){
			result= await queryDictByTypeCode(type);
		}else{
			return "";
		}
		return result;
	},
	isEmptyObject:function(obj){
		var res =false;
		if(typeof obj === 'undefined'){
			res = true;
		}
		else if(JSON.stringify(obj) === '"[]"' || obj === '"[]"' || JSON.stringify(obj) === "[]"){
			res = true;
		}
		else if(JSON.stringify(obj) === '"{}"' || obj === '"{}"' || JSON.stringify(obj) === "{}"){
			res = true;
		}
		// else if(isJsonString(obj)){
		// 	var sds = JSON.parse(obj);
		// 	if(JSON.parse(obj)==="{}"){
		// 		res = true;
		// 	}
		// }
		else if(obj== null){
			res = true;
		}
		else if (obj.constructor === Object && Object.keys(obj).length  == 0) {
		    res = true;
		}
		return res;
	},
	//下载并保存文件
	downloadPaper(fileUrl) {
		const downloadTask = uni.downloadFile({
			url: url+fileUrl, //仅为示例，并非真实的资源
			success: (res) => {
				if (res.statusCode === 200) {
					console.log('下载成功');
				}
				let that = this;
				uni.saveFile({
					tempFilePath: res.tempFilePath, //临时路径
					success: function(res2) {
						uni.showToast({
							icon: 'none',
							mask: true,
							title: '文件已保存：' + res2.savedFilePath, //保存路径
							duration: 3000,
						});
						setTimeout(() => {
							//打开文档查看
							uni.openDocument({
								filePath: res2.savedFilePath,
								success: function(res2) {
									// console.log('打开文档成功');
								}
							});
						}, 3000)
					}
				});
				}
			});
			downloadTask.onProgressUpdate((res) => {
				console.log('下载进度' + res.progress);
				console.log('已经下载的数据长度' + res.totalBytesWritten);
				console.log('预期需要下载的数据总长度' + res.totalBytesExpectedToWrite);
		});
	},
	//预览保存图片
	previewAndDownImg(imageUrl){
	        var images = [];
	        images.push(imageUrl);
	        console.log(images)  // ["http://192.168.100.251:8970/6_1597822634094.png"]
	        uni.previewImage({ // 预览图片  图片路径必须是一个数组 => ["http://192.168.100.251:8970/6_1597822634094.png"]
	            current:0,
	            urls:images,
	            longPressActions: {  //长按保存图片到相册
	                itemList: ['保存图片'],
	                success: (data)=> {
	                    console.log(data);
	                    uni.saveImageToPhotosAlbum({ //保存图片到相册
	                        filePath: payUrl,
	                        success: function () {
	                            uni.showToast({icon:'success',title:'保存成功'})
	                        },
	                        fail: (err) => {
	                            uni.showToast({icon:'none',title:'保存失败，请重新尝试'})
	                        }
	                    });
	                },
	                fail: (err)=> {
	                    console.log(err.errMsg);
	                }
	        }
	        });
	    },
	/*验证是否为手机号或者座机号码 */
	isTelCode(str) {
			var reg=/(^(?:(?:0\d{2,3})-)?(?:\d{7,8})(-(?:\d{3,}))?$)|(^0{0,1}1[3|4|5|6|7|8|9][0-9]{9}$)/;
			return reg.test(str);
		} ,
		/*校验邮件地址是否合法 */
	isEmail(str) {
			var reg=/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
			return reg.test(str);
		},
		/*校验身份证号是否合法 */
	isEmail(str) {
			var reg=/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/; 
			return reg.test(str);
		},
	
}

//以下方法私有，不导出
function doGetDicItemById(id){
		var requestUrl = url+"/api/jsonws/dictionary-portlet.dictionaryitem/get-dictionary-by-dic-item-id-common";
		var param ={
			    dicItemId: id
			  };
		return new Promise((resolve,reject) =>{
			uni.request({
				url: requestUrl,
				method:'POST',
				data:param,
				header: {
					'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
					'Authorization': 'Bearer ' + uni.getStorageSync("Token")
				},
				success:(res)=>{
					resolve(res.data.datas[0])
				},
				fail:(err)=>{
					reject(err)
				}
			})
		})
	}
	function doGetDicItemByTypeCode(type){
		var requestUrl = url+"/api/jsonws/dictionary-portlet.dictionaryitem/get-dictionarys-by-code-type-common";
		var param ={
			   typeCode: type
			  };
		return new Promise((resolve,reject) =>{
			uni.request({
				url: requestUrl,
				method:'POST',
				data:param,
				header: {
					'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
					'Authorization': 'Bearer ' + uni.getStorageSync("Token")
				},
				success:(res)=>{
					resolve(res.data.datas[0])
				},
				fail:(err)=>{
					reject(err)
				}
			})
		})
	}
function isJsonString(str) {
        try {
            if (typeof JSON.parse(str) == "object") {
                return true;
            }
        } catch(e) {
        }
        return false;
    }

function queryDictByTypeCode(type){
	var requestUrl = url+"/api/jsonws/dictionary-portlet.dictionaryitem/get-dictionarys-by-code-type-common";
	var param ={typeCode: type};
	return new Promise((resolve,reject) =>{
		uni.request({
			url: requestUrl,
			method:'POST',
			data:param,
			header: {
				'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
				'Authorization': 'Bearer ' + uni.getStorageSync("Token")
			},
			success:(res)=>{
				resolve(res.data.datas)
			},
			fail:(err)=>{
				reject(err)
			}
		})
	})
}	