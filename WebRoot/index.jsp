<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'index.jsp' starting page</title>
  	<script type="text/javascript">
  	var netType={"network_type:wifi":"wifi网络",
  	"network_type:edge":"非wifi,包含3G/2G",
  	"network_type:fail":"网络断开连接",
  	"network_type:wwan":"2g或者3g"}
  	
  	function onBridgeReady(){
 		WeixinJSBridge.call('hideOptionMenu');
 		WeixinJSBridge.call('hideToolbar');
 		WeixinJSBridge.invoke('getNetworkType',{},
 		function(e){
 			alert(netType[e.err_msg])
 	    	WeixinJSBridge.log(e.err_msg);
 	    });
	}

	if (typeof WeixinJSBridge == "undefined"){
    	if( document.addEventListener ){
        	document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
   		}else if (document.attachEvent){
        	document.attachEvent('WeixinJSBridgeReady', onBridgeReady); 
        	document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
    	}
	}else{
    	onBridgeReady();
	}
  	</script>
  </head>
  	
  <body>
  	welcome !!!
  </body>
</html>
