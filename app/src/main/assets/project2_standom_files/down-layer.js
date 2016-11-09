/**
 * 页面底端显示 "下载安装" 的banner
 */
function GetQueryString(name){
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}

var q_nodown = GetQueryString('nodown');
var ua = navigator.userAgent.toLowerCase(); 
if ( ua.match(/DrivingTest/i) ) {
	// in app , no down
} else if ( !q_nodown || '0'==q_nodown ){
	var link = document.createElement("link");
    link.rel = 'stylesheet';
    link.type = 'text/css';
    link.href = 'http://picture.eclicks.cn/kaojiazhao/400/down-layer.css';
    document.getElementsByTagName('head')[0].appendChild(link);

	var body=document.getElementsByTagName("body")[0];
	body.innerHTML = appendHtml + body.innerHTML;
}