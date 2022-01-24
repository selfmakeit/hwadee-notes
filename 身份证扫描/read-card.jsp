<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/html/portlet/common/init.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>读卡</title>
<style type="text/css">
		.explain-title{
			padding: 10px 30px;
			font-size: 22px;
		}
		.explain-content{
			padding: 10px 30px;
		}
		
		.read-but{
			display:flex; 
			justify-content: center;
		}
		
		.layui-layer-content{
		    padding: 0px
		}
</style>
<script type="text/javascript" charset="utf-8"  src="<%=path%>/js/readCard.js"/></script>
</head>
<body>
	<div class="layui-row">
	    <div class="layui-col-md12">
	      	<div class="layui-panel" >
	      		<div class="explain-title">扫描说明</div>
	      		<div class="explain-content">1.读卡器相关资料<a href="<%=simplePath%>/readCard/readCard.rar">(下载)</a></div>
	      		<div class="explain-content">2.安装驱动程序。(根据自己电脑系统位数选择相应版本安装，且以管理员权限安装)</div>
	      		<div class="explain-content">3.安装读卡程序。双击“华视电子读卡服务.exe”进行安装。(以管理员权限安装)</div>
		  		<div class="explain-content">4.将身份证放到读卡设备上面。</div>
		  		<div class="explain-content">5.点击扫描。</div>
			<div class="read-but">
				<button type="button" class="layui-btn layui-btn-normal layui-btn-lg layui-icon layui-layer-btn-c" onclick="readCard()">扫描</button>
			  	<button type="button" class="layui-btn layui-btn-normal layui-btn-lg layui-icon layui-layer-btn-c" onclick="getBack()">取消</button>
			</div>
			</div>
      	</div>   
    </div>

<script type="text/javascript">

//返回
function getBack(cardNumber){
	var index = parent.layer.getFrameIndex(window.name);
	parent.layer.close(index);//关闭弹窗层
	parent.queryUserInfo(cardNumber)
}



//读卡(华视自带读卡程序，网页二次开发)
function readCard(){
	var cardNumber=startReadCard();
	if(validationUtil.isEmpty(cardNumber)){
    	layer.msg("请求失败,请检查设备是否正常连接！");
		return;
	}
	getBack(cardNumber);
}
</script>
</body>
</html>