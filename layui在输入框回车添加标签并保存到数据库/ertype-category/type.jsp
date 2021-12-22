<%@page import="com.hwadee.liferay.basic.common.util.UserCurrentOrganization"%>
<%@page import="com.hwadee.cb.organization.service.OrganizationInfoManagementLocalServiceUtil"%>
<%@page import="com.hwadee.cb.organization.model.OrganizationInfoManagement"%>
<%@page import="javax.transaction.SystemException"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="/html/portlet/ertype-category/error.jsp" %>
<%@ page isErrorPage="true" %>
<!-- 判断当前用户所属机构是否是学校  不是就抛异常  -->
   	<%   
    	String organizationId = UserCurrentOrganization.getCurrentOrganization();
    	int isSchool = OrganizationInfoManagementLocalServiceUtil.fetchOrganizationInfoManagement(Long.parseLong(organizationId)).getIsSchool();
   	 	if(isSchool==0){
    	 		throw new SystemException("不是学校");
    	 	}
   	 %> 
<div class="tags" id="tag">
     <input type="text" name="" id="inputTags" placeholder="回车添加类别" autocomplete="off">
</div>
<script type="text/javascript">
var urlList = {
		getTypeList: "/api/jsonws/learn-plan-portlet.schoolcourseertype/get-school-own-er-type",
		delType: "/api/jsonws/learn-plan-portlet.schoolcourseertype/del-er-type",
		addType: "/api/jsonws/learn-plan-portlet.schoolcourseertype/add-er-type",
}
$.ajaxSettings.async = false;
$(function () {
   
	loadType();
});
var globalTypeMap={};
var globalTypeArr=[];
layui.config({
    base: '/js/', //inputTags.js存放的文件位置
}).use(['inputTags'], function() {
    var inputTags = layui.inputTags;
    inputTags.render({
        elem: '#inputTags', //定义输入框input对象
        content: globalTypeArr, //默认标签
        aldaBtn: false, //是否开启获取所有数据的按钮
        done: function(value) { //回车后的回调
           $.post(urlList.addType,{typeName :value},function(res){
        	   var data =JSON.parse(res);
        	   if(!data.stat){
        		   globalTypeArr.splice($.inArray(value,globalTypeArr),1);
        	   }else{
        		   loadType();
        	   }
        	   layer.msg(JSON.parse(res).message);
           });
        },
        del:function(value){
        	$.post(urlList.delType,{typeId :globalTypeMap[value]},function(res){
         	   var data =JSON.parse(res);
         	   if(!data.stat){
         		   globalTypeArr.push(value);
         	   }else{
        		   loadType();
        	   }
         	   layer.msg(JSON.parse(res).message);
            });
        }
    })
});

function loadType(){
	$.post(urlList.getTypeList,{SchoolId :'<%=UserCurrentOrganization.getCurrentOrganization()%>'},function(res){
		var list = res.datas;
		$.each(list, function(i,v) {
			globalTypeMap[v['typeName']]=v['typeId'];
			globalTypeArr.push(v['typeName']);
		});
	})
}
function delType(){
	$.post(urlList.getTypeList,{SchoolId :'<%=UserCurrentOrganization.getCurrentOrganization()%>'},function(res){
		var list = res.datas;
		$.each(list, function(i,v) {
			globalTypeMap[v['typeName']]=v['typeId'];
			globalTypeArr.push(v['typeName']);
		});
	})
}
</script>
