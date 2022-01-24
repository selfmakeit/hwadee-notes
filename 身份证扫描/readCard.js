/**========================================读卡器读取证件信息==========================================*/
/**========================================二次开发华视读卡器==========================================*/
function startReadCard(){
	var cardNumber="";
	connect();
	cardNumber=readCert();
	disconnect();
	return cardNumber;
} 
 
function ajax(options) {
	if(options.type==null)
	{
		options.type="POST";
	}
	
	if(options.url==null)
	{
		options.url="";
	}
	
	if(options.timeout==null)
	{
		options.timeout=5000;
	}
	
	if(options.onComplate==null)
	{
		options.onComplate=function() 
		{
		}
	}
	
	if(options.onError==null)
	{
		options.onError=function() 
		{
		}
	}
	
	if(options.onSuccess==null)
	{
		options.onSuccess=function() 
		{
		}
	}
	
	if(options.data)
	{
		options.data="";
	}

	var xml ;	
	if (typeof ActiveXObject != 'undefined') {
	var aVersions = ["Microsoft.XMLHTTP", "Msxml2.XMLHttp.6.0", "Msxml2.XMLHttp.5.0", "Msxml2.XMLHttp.4.0", "Msxml2.XMLHttp.3.0"];
	for (var i = 0; i < aVersions.length; i++) {
		try {
			xml = new ActiveXObject(aVersions[i]);
			}catch (e) {}
		}
	} else if (typeof XMLHttpRequest != 'undefined'){
		xml = new XMLHttpRequest();
	}

	xml.open(options.type, options.url, false);
	
	var timeoutLength = options.timeout;
	
	var requestDone = false;
	
	setTimeout(function() {
				requestDone = true;
			}, timeoutLength);
			
	xml.onreadystatechange = function() {
	
		if (xml.readyState == 4 && !requestDone) {
		
			if (httpSuccess(xml)) {
			
				options.onSuccess(httpData(xml));
			}
			
			else {
				options.onError();
			}
			
			options.onComplate();
			
			xml = null;
		}
	};

	xml.send();

	function httpSuccess(r) {
		try {
			return !r.status && location.protocol == "file:"
					||
					(r.status >= 200 && r.status <= 300)
					||
					r.status == 304
					||

					navigator.userAgent.indexOf("Safari") >= 0
					&& typeof r.status == "undefined";
		} catch (e) {
		}
		return false;
	}
	
	function httpData(r) {
		/*if(r!='' && r.getResponseHeader("responseType")!='')
		var ct = r.getResponseHeader("responseType");
		if(ct)
		{
			if(ct=="script")
			{
				eval.call(window, data);
			}
			if(ct=="xml")
			{
				return r.responseXML;
			}
			if(ct=="json")
			{
				return JSON.parse(r.responseText);
			}
		}*/
		return r.responseText;
	}
}



//打开连接
function connect(){
	function onSuccess(data){
/*		document.getElementById("result").value = "提示:" + data.match("\"errorMsg\" : (.*)")[1] + "\n返回值：" + data.match("\"resultFlag\" : (.*)")[1];*/	
	}

	var options=new Object();
	options.type="GET";
	//options.url = "/OpenDevice";
	options.url = "http://127.0.0.1:19196/OpenDevice" + "&" + "t=" + Math.random();		//URL后面加一个随机参数的目的是为了防止IE内核浏览器的数据缓存
	options.timeout=5000;
	options.onSuccess=onSuccess;
	ajax(options);
}
//关闭连接
function disconnect(){
	function onSuccess(data){
		/*document.getElementById("result").value = "提示:" + data.match("\"errorMsg\" : (.*)")[1] + "\n返回值：" + data.match("\"resultFlag\" : (.*)")[1];*/
	}
	var options=new Object();
	options.type="GET";
	//options.url="CloseDevice";
	options.url = "http://127.0.0.1:19196/CloseDevice" + "&" + "t=" + Math.random();	//URL后面加一个随机参数的目的是为了防止IE内核浏览器的数据缓存
	options.timeout=5000;
	options.onSuccess=onSuccess;
	ajax(options);
}

//读卡
function readCert() {
	var cardNumber="";
	function onSuccess(data){
		//console.info(data);
		cardNumber=data.match("\"certNumber\" : \"(.*?)\"")[1];
		//console.info("证件号码："+cardNumber);
	}

	var startDt = new Date();
	var options=new Object();
	options.type="GET";
	//options.url="readcard";
	options.url = "http://127.0.0.1:19196/readcard" + "&" + "t=" + Math.random();	//URL后面加一个随机参数的目的是为了防止IE内核浏览器的数据缓存
	options.timeout=5000;
	options.onSuccess=onSuccess;
	ajax(options);
	return cardNumber;
}
/**========================================二次开发华视读卡器==========================================*/