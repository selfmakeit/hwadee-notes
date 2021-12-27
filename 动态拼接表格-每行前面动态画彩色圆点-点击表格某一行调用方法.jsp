<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/html/portlet/common/comint.jsp" %>

<style type="text/css">
	.tag-org-oc{
		display: flex;
        justify-content: center; /* 水平居中 */
        align-items: center;     /* 垂直居中 */
	}
   	.pr-container{
   		height: 380px;width: 50%;
   	}
   	.push-wraper{
		width:50%;
		height:auto;
		display: flex;
        justify-content: center; /* 水平居中 */
        align-items: center;     /* 垂直居中 */
		background-color: rgb(99, 141, 209,0.8);
	}
	.push-table{
		text-align: center;
	}
	.push-table-head{
		font-weight: bold;
	}
	.push-table-body{
		color: rgb(137, 233, 13);
	}
	.push-table-body td{
		height: 30px;
	}
	.count{
		font-weight: bold;
	}
	.circle {
		  height: 15px;
		  width: 15px;
		  border-radius: 50%;
		  display: inline-block;
	}
</style>
<div class="tag-org-oc">
	 <div id="container" class="pr-container">
      </div>
	<div class="push-wraper">
      <table class="push-table">
      		<thead  class="push-table-head">	
   				<tr>
   					<td colspan="3">成果机构学习计划标签数量分析</td>
   				</tr>
   				<tr>
   					<td >成果名称</td>
   					<td style="width: 30%">标签名称</td>
   					<td>数量</td>
   				</tr>
      		</thead>
      		<tbody id="databody" class="push-table-body">
      			
      		</tbody>
      </table>
    </div>
</div>
<script type="text/javascript">
var urlList = {
		getOutcomeList:"/api/jsonws/bigdata-statistic-display-portlet.somestatistic/outcome-plan-tag-number",
};
var preRow=null;
$(function(){
	$.post(urlList.getOutcomeList,{topN:"<%=topN%>"}
  		,function(data){
  			try {
  				var res =JSON.parse(data);
  				$.each(res, function (i, o) {
  	  				var item =res[i];
  	  				var hml="<tr id='"+item.outcomeId+"' class='outcomtItemRow'>"
  	  							+"<td>"
	  	  							+"<div style='text-align:left;'>"
		  	  							+"<span id ='dot"+i+"' class='circle'></span>"
		  	  							+item.outcomeName
	  	  							+"</div>"
	  	  							+"</td>"
	  		  						+"<td>"+(item.hasOwnProperty('tagName')?item.tagName:"")
	  		  						+"</td>";
	  		  						+"<td class='count'>"+(item.hasOwnProperty('number')?item.number:"")
	  		  						+"</td>"
  		  						+"</tr>";
  	  				$('#databody').append(hml);
  	  				var c=getRandomColor();
  	  				$('#dot'+i).css("background-color",c);
  					if(i===0){
  						$('#'+item.outcomeId).trigger("click");
  					}
  	  		 	})
			} catch (e) {
				// TODO: handle exception
			}
  	})
	
$('table').on('click', '.outcomtItemRow', function(){ 
	    var id = $(this).attr("id");
	    console.log(id);
	    if(preRow){
		    preRow.css("background-color",'');
	    }
	    $(this).css("background-color",'green');
	    preRow=$(this);
	    drawStatistic(id);
	});
})
function drawStatistic(id){
	//do something
}

function getRandomColor(){
	var r = Math.floor(Math.random()*255);
    var g = Math.floor(Math.random()*255);
    var b = Math.floor(Math.random()*255);
    var color = 'rgba('+ r +','+ g +','+ b +',0.8)';
    return color
}

</script>
</body>
</html>