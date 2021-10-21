

# Uni-App笔记

## js从html标签中提取内容（包含嵌套标签）

```javascript
var content =str.replace(/<\/?.+?>/g,"");
```

## uni-app同步请求数据

* 利用**async**和**await**配合使用，这两个关键字必须成对出现，在调用返回Promise的方法前使用await，在调用的方法外面使用async申明。

```js
import {postUrl,hasErrorMsg} from "@/pages/api/api.js"
import {errdata} from "@/pages/api/errdata.js"
import {url} from "@/pages/api/request.js"
export let spike ={
	//方法外面使用async申明
	getDicNameById:async function(id){
		var result;
		if(id){
            //调用时使用await
			result= await this.doGetDicItemById(id);
		}else{
			return "";
		}
		return result.codeName;
	},
    //返回Promise的方法
	doGetDicItemById: function(id){
		var requestUrl = url;
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
	},
}
```

**如果有多层的话要逐层使用，除了最里面的那个返回promise的方法**

```js
	methods: {
			getArchiveDetailById:async function() {
				let that = this;
				let data = this.archiveData;
				var photo = await postUrl(userArchivesUrl.getUserPhotoByUserId, {"userId":data.userId});
				data['profilePhoto'] = url+photo;
				data.gender = await spike.getDicNameById(data.gender);
				data.birthProvince =await spike.getDicNameById(data.birthProvince);
				data.birthCity =await spike.getDicNameById(data.birthCity);
				//成果列表
				await postUrl(userArchivesUrl.getAcquiredOutcome, {"submitId":data.userId}).then((res)=>{
					var datas = res.datas;
					var rows = JSON.parse(datas).rows;
						if(JSON.stringify(rows)=='[]')that.list = null;
						else that.list = rows;	
				});
				
				that.archiveData=data;
				
			}
		}
```

