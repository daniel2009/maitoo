﻿
//====页面引用CLodop云打印必须的JS文件：====

//让其它电脑的浏览器通过本机打印（适用例子）：
//oscript = document.createElement("script");
//oscript.src ="scripts/CLodopfuncs.js";
//var head = document.head || document.getElementsByTagName("head")[0] || document.documentElement;
//head.insertBefore( oscript,head.firstChild );
//让本机浏览器打印(更优先)：
var oscript = document.createElement("script");
oscript.src ="http://localhost:8000/CLodopfuncs.js?priority=1";
var head = document.head || document.getElementsByTagName("head")[0] || document.documentElement;
head.insertBefore( oscript,head.firstChild );

//====获取LODOP对象的主过程：====
function getLodop(oOBJECT,oEMBED){
    var strCLodopInstall = "<br><font color='red'>CLodop云打印服务(localhost本地)未安装启动!点击这里<a href='../resources/scripts/CLodopPrint_Setup_for_Win32NT.exe' target='_self'>执行安装</a>,安装后请刷新页面。</font>";
    var strCLodopUpdate = "<br><font color='red'>CLodop云打印服务需升级!点击这里<a href='../resources/scripts/CLodopPrint_Setup_for_Win32NT.exe' target='_self'>执行升级</a>,升级后请刷新页面。</font>";
    var LODOP;
    try{
        var isIE = (navigator.userAgent.indexOf('MSIE')>=0) || (navigator.userAgent.indexOf('Trident')>=0);
        try{ LODOP=getCLodop();} catch(err) {};
        if (!LODOP && document.readyState!=="complete") {alert("C-Lodop没准备好，请稍后再试！"); return;};
       
        if (!LODOP) {
		 if (isIE) document.write(strCLodopInstall); else
		 document.documentElement.innerHTML=strCLodopInstall+document.documentElement.innerHTML;
                 return;
        } else {

	         if (CLODOP.CVERSION<"2.0.4.2") { 
			if (isIE) document.write(strCLodopUpdate); else
			document.documentElement.innerHTML=strCLodopUpdate+document.documentElement.innerHTML;
		 };
		 if (oEMBED && oEMBED.parentNode) oEMBED.parentNode.removeChild(oEMBED);
		 if (oOBJECT && oOBJECT.parentNode) oOBJECT.parentNode.removeChild(oOBJECT);	
	};
        //===如下空白位置适合调用统一功能(如注册语句、语言选择等):===
        LODOP.SET_LICENSES("", "13BC0D6D22FC0A43A01732748F415AE9", "C94CEE276DB2187AE6B65D56B3FC2848", "");
        //===========================================================
        return LODOP;
    } catch(err) {alert("getLodop出错:"+err);};
};

