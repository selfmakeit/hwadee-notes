<%@ include file="/html/portlet/common/init.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<style>
<!--

-->
.main{height: 600px;}


</style>
<div id="<portlet:namespace/>chart-panel"></div>
<div id="<portlet:namespace/>main" class="main"></div>


<script type="text/javascript">
var <portlet:namespace/>request="/api/jsonws/bigdata-statistic-display-portlet.somestatistic/platform-organization-distribution";
var <portlet:namespace/>myChart ;
var <portlet:namespace/>parentcode ;
$(function(){
	
	<portlet:namespace/>myChart=echarts.init(document.getElementById('<portlet:namespace/>main'));
    <portlet:namespace/>myChart.on('click',  <portlet:namespace/>echartsMapClick); 

	$('<div class="<portlet:namespace/>back">返 回</div>').appendTo( $('#<portlet:namespace/>chart-panel'));

		$('.<portlet:namespace/>back').css({
		    'left': '25px',
		    'top': '25px',
		    'color': 'rgb(179, 239, 255)',
		    'font-size': '16px',
		    cursor: 'pointer',
		    'z-index': '100'
		})

		$('.<portlet:namespace/>back').click(function() {
			<portlet:namespace/>iniMap();
		})

	

	<portlet:namespace/>myChart.showLoading();

	<portlet:namespace/>iniMap();
})


var <portlet:namespace/>mappoints=[];
/*  
		特殊城市  省市区  会少一级
		
	    //2个特别行政区

*/

function <portlet:namespace/>iniMap(cityCode){
	<portlet:namespace/>parentcode=cityCode;
	if(!cityCode){
		$.post(<portlet:namespace/>request,{type: 'province', superiorId: 0},function(res){
			if(res.stat){
				var <portlet:namespace/>data = $.parseJSON(res.datas);	
				<portlet:namespace/>init("<%=path%>/js/map/china.json",<portlet:namespace/>data);

			}else{
				layer.msg(res.message);
			}
		});
		
	}else if(cityCode%10000==0){//省
		if(cityCode==110000||cityCode==120000||cityCode==310000||cityCode==500000){//四个直辖市  点击后直接进入区
			
			if(cityCode==110000){
				cityCode=110100;
			}else if(cityCode==120000){
				cityCode=120100;
			}else if(cityCode==310000){
				cityCode=310100;			
			}else if(cityCode==500000){
				cityCode=500100;			
			}
			
			$.post(<portlet:namespace/>request,{type: 'area', superiorId: cityCode},function(res){
				if(res.stat){
					var <portlet:namespace/>data= $.parseJSON(res.datas);	
					<portlet:namespace/>init("<%=path%>/js/map/city/"+cityCode+"_full.json",<portlet:namespace/>data,"area");						
				}else{
					
					layer.msg(res.message);
				}
			});
		}else{
			$.post(<portlet:namespace/>request,{type: 'city', superiorId: cityCode},function(res){
				if(res.stat){
					var <portlet:namespace/>data= $.parseJSON(res.datas);	
					<portlet:namespace/>init("<%=path%>/js/map/province/"+cityCode+".json",<portlet:namespace/>data,"city");
				}else{
					
					layer.msg(res.message);
				}
			});
		}
	}else{//点击市
		$.post(<portlet:namespace/>request,{type: 'area', superiorId: cityCode},function(res){
			if(res.stat){
				var <portlet:namespace/>data= $.parseJSON(res.datas);	
				<portlet:namespace/>init("<%=path%>/js/map/city/"+cityCode+"_full.json",<portlet:namespace/>data,"area");
				
				
			}else{
				
				layer.msg(res.message);
			}
		});
	
	}
}


//echarts点击事件
function  <portlet:namespace/>echartsMapClick(params) {
	
    if (!params.data) {
        return
    } else {
        //如果当前是区级（不以0结尾），那就直接return
        if (params.data.cityCode%10!=0) {
            return
        }
        <portlet:namespace/>iniMap(params.data.cityCode)
    }
}
      

/**
 * url  地图json数据  
 * <portlet:namespace/>data地图人数数据
 * type  主要area使用
 */
function <portlet:namespace/>init(url,<portlet:namespace/>data,type){
	 
	$.getJSON(url,function(geoJson){
		var regions = [];
		if(type){
			regions=[
       	         {
       	        	 name:"南海诸岛",
       	        	 itemStyle:{
       	        		 //
       	        		 normal:{opacity:0}//隐藏地图
       	        	 },
       	        	 label:{show:false},//隐藏文字
       	        	 
       	         }
        	]
		};
	    echarts.registerMap('china', geoJson);
	    <portlet:namespace/>myChart.hideLoading();
	    var <portlet:namespace/>mappoints=geoJson.features;
	    //设置省市区大小
	    var areaZoom=1.8;
	    var areaX=geoJson.features[0].properties.center[0];
	    if(areaX==undefined){
	    	areaX=geoJson.features[0].properties.center['lng'];//直辖市
	    }
	    var areaY=geoJson.features[0].properties.center[1];
	    if(areaY==undefined){
	    	areaY=geoJson.features[0].properties.center['lat'];//直辖市
	    }
	    if(type=="area"){
	    	areaZoom=9;
	    }else if(type=="city"){

	    }
	    var convertData = function (data) {
	        var res = [];
	        for (var i = 0; i < data.length; i++) {
	        	if(data[i].value=="暂无数据"){//无数据 不显示中心
	        		continue;
	        	}
	        	var cityCode =data[i].cityCode;
	        	
        		for(var j=0;j<<portlet:namespace/>mappoints.length;j++){
        			if(<portlet:namespace/>mappoints[j].properties.adcode==cityCode){
	        			var area =<portlet:namespace/>mappoints[j].properties;
	        			if(Object.prototype.toString.call(area.center)=="[object Array]"){
	        				  res.push({
	        					  	name: area.name,
		        	                value: area.center.concat(data[i].value),
		        	                cityCode:cityCode,
		        	                detail:detail
		        	            });
	        				
	        			}else{
	        				 res.push({
	        					 	name: area.name,
		        	                value:[area.center.lng,area.center.lat,data[i].value],
		        	                cityCode:cityCode,
		        	                detail:detail
		        	            });
	        			}
	        			
	        		}
        		} 
	        }
	        return res;
	    };
	    var data=[];
	    for(var x=0;x<<portlet:namespace/>data.length;x++){
	    	var cityCode=<portlet:namespace/>data[x].dicItemId;
	    	var num = <portlet:namespace/>data[x].num;
	    	var detail = <portlet:namespace/>data[x].detail;
	    	var areaName;
	    	if( isNaN(num)||num==0){
	    		num="暂无数据";
	    	}
    		for(var j=0;j<<portlet:namespace/>mappoints.length;j++){
        		if(<portlet:namespace/>mappoints[j].properties.adcode==cityCode){
        			areaName=<portlet:namespace/>mappoints[j].properties.name
        			break;
        		}
        	}
	    	data.push({
	    		name:areaName,
	    		value:num,
	    		cityCode:cityCode,
	    		detail :detail
	    	});
	    }
	    option = {
	            backgroundColor: {
	            type: 'linear',
	            x: 0,
	            y: 0,
	            x2: 1,
	            y2: 1,
	            colorStops: [{
	                offset: 0, color: '#f3f3f3' // 0% 处的颜色
	            }, {
	                offset: 1, color: '#f9f9f9' // 100% 处的颜色
	            }],
	            globalCoord: false // 缺省为 false
	        },
// 	            title: {
// 	                top:20,
// 	                text: '用户地区分布',
// 	                subtext: '',
// 	                x: 'center',
// 	                textStyle: {
// 	                    color: '#ccc'
// 	                }
// 	            },    

	           tooltip: {
	                trigger: 'item',
	                formatter: function (params) {
	                	
	                	if(params.data){
	                		if(params.componentSubType=="map"){//点击地图
		                		if(params.data.value=="暂无数据"){
			                		return params.name + ' : ' + params.data.value;
			                		
			                	}else{
			                		var msg=params.name + "<br>"+' 总共: ' + params.data.value+"(个)";
			                		if(params.data.detail.hasOwnProperty('enterprise')){
			                			msg+="<br>企    业 ："+params.data.detail.enterprise+"(个)";
			                		}
			                		if(params.data.detail.hasOwnProperty('government')){
			                			msg+="<br>事业单位  ："+params.data.detail.government+"(个)";
			                		}
			                		return msg
			                	}
		                	}else{//点击气泡
		                		return params.name + ' : ' + params.data.value[2]+"(个)";
		                	}
	                	}else{
	                		return params.name + ' : 暂无数据';
	                		
	                	}
	                }
	            },

	        legend: {
	            orient: 'vertical',
	            y: 'bottom',
	            x:'right',
	            data:['pm2.5'],
	            textStyle: {
	                color: '#fff'
	            }
	        }, 
	            visualMap: {
	                show: false,
	                min: 0,
	                max: 500,
	                left: 'left',
	                top: 'bottom',
	                text: ['高', '低'], // 文本，默认为数值文本
	                calculable: true,
	                seriesIndex: [1],
	                inRange: {

	                }
	            },
	            geo: {
	                map: 'china',
	                show: true,
	                roam: true,
	                zoom: areaZoom,//设置省市区大小
	                center: [areaX, areaY], // 中心位置坐标
	                label: {
	    				normal: {
	    					show: true
	    				},
	    				emphasis: {
	    					show: false,
	    				}
	    			},
	    			regions :regions,
	                itemStyle: {
	                    normal: {
	                    	areaColor: '#ddd14c',
	                        borderColor: '#ce9a22',//线
	                        shadowColor: '#ce9a22',//外发光
	                        shadowBlur: 20
	                    },
	    				 emphasis: {
	                        areaColor: '#ae7d42',//悬浮区背景
	                    }
	                }
	            },
	            series : [
	          {
	             
	                symbolSize: 5,
	                label: {
	                    normal: {
	                        formatter: '{b}',
	                        position: 'right',
	                        show: false
	                    },
	                    emphasis: {
	                        show: true
	                    }
	                },
	                itemStyle: {
	                    normal: {
	                        color: '#fff'
	                    }
	                },
	                name: 'light',
	                type: 'scatter',
	                coordinateSystem: 'geo',
	                data: convertData(data),
	                
	            },
	             {
	                type: 'map',
	                map: 'china',
	                geoIndex: 0,
	                aspectScale: 0.75, //长宽比
	                showLegendSymbol: false, // 存在legend时显示
	                label: {
	                    normal: {
	                        show: false
	                    },
	                    emphasis: {
	                        show: false,
	                        textStyle: {
	                            color: '#fff'
	                        }
	                    }
	                },
	                roam: true,
	                itemStyle: {
	                    normal: {
	                        areaColor: '#031525',
	                        borderColor: '#FFFFFF',
	                    },
	                    emphasis: {
	                        areaColor: '#2B91B7'
	                    }
	                },
	                animation: false,
	                data: data
	            },
	            {
	                name: 'Top 5',
	                type: 'scatter',
	                coordinateSystem: 'geo',
	                symbol: 'pin',
	                symbolSize: [50,50],
	                label: {
	                    normal: {
	                        show: true,
	                        textStyle: {
	                            color: '#fff',
	                            fontSize: 9,
	                        },
	                        formatter:function (value){
	                        	
	                            return value.data.value[2]
	                        }
	                    }
	                },
	                itemStyle: {
	                    normal: {
	                        color: '#21d827', //标志颜色
	                    }
	                },
	                data: convertData(data),
	                showEffectOn: 'render',
	                rippleEffect: {
	                    brushType: 'stroke'
	                },
	                hoverAnimation: true,
	                zlevel: 1
	            },
	             
	        ]
	        };
	    <portlet:namespace/>myChart.setOption(option);
	    //点击前解绑，防止点击事件触发多次
	     //<portlet:namespace/>myChart.off('click');
	})
}

</script>