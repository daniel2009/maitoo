$(function () {
	$.ajax({
     	type : "get",
 		url : "/admin/emp/get_self",
 		success : function(response) {
 			var code = response.code;
 			if ("200" == code) {
 				var emp=response.data;
	 				if(emp!=null)
	 				{
	 					$("#warehouseName").html(emp.storeName);
	 					$("#employeeName").html(emp.account);
	 				}
 				}
 			 	else if("607" == code) {
 					alert("您尚未登录或登录已失效！");
 					top.location.href = "../admin/login.html";
 				}
 				else
 				{
 					alert("获取数据失败，原因是："+response.message);
 				}
 			}});
	
	
	
	
	$.ajax({
     	type : "post",
 		url : "/admin/ms_control/getself",
 		success : function(response) {
 			var code = response.code;
 			if ("200" == code) {
 				var self=response.data;
	 				if(self!=null)
	 				{
	 					
	 					
	 					if(removenull(self.printItems)!="")
	 					{
	 						var aaa=self.printItems.split(";");
	 						for(var i=0;i<aaa.length;i++)
	 						{
	 							if(aaa[i]=="0")
	 							{
	 								$("#printItems_0").attr("checked","checked");
	 							}
	 							else if(aaa[i]=="1")
	 							{
	 								$("#printItems_1").attr("checked","checked");
	 							}
	 							else if(aaa[i]=="2")
	 							{
	 								$("#printItems_2").attr("checked","checked");
	 							}
	 						}
	 					}
	 					
	 						if(self.keydownItem=="0")
							{
								$("#printdefault_0").attr("checked","checked");
							}
							else if(self.keydownItem=="1")
							{
								$("#printdefault_1").attr("checked","checked");
							}
							else if(self.keydownItem=="2")
							{
								$("#printdefault_2").attr("checked","checked");
							}
	 					
	 				}
 				}
 			 	else if("607" == code) {
 					alert("您尚未登录或登录已失效！");
 					top.location.href = "../admin/login.html";
 				}
 				else
 				{
 					alert("获取数据失败，原因是："+response.message);
 				}
 			}});
});


function submitcontrol()
{
	
	var checkbox1 =document.getElementsByName("printItems");	
	var choositems="";
	var radion=$(":radio[name='printdefault']:checked").val();
	if(removenull(radion)=="")
	{
		alert("必须选择默认打印选项!");
		return false;
	}
	var flag=0;

	for(var i=0;i<checkbox1.length;i++)
	{
		if($(checkbox1[i]).is(':checked')==true)
		{
			if($(checkbox1[i]).val()==radion)
			{
				flag=1;
			}
			
			
			if(choositems=="")
			{
				choositems=$(checkbox1[i]).val();
			}
			else
			{
				choositems+=","+$(checkbox1[i]).val();
			}
		}
	}
	
	if(flag==0)
	{
		alert("默认打印必须包含在显示打印按钮中!");
		return false;
	}
	if(choositems=="")
	{
		alert("必须选择一个显示打印按钮!");
		return false;
	}
	
	$.ajax({
     	type : "post",
     	data:{
     		items:choositems,
     		pitem:radion
     	},
 		url : "/admin/ms_control/update",
 		success : function(response) {
 			var code = response.code;
 			if ("200" == code) {
 				alert("修改成功!");
 				window.location.href = "../admin/menshidadan_manage.html";	
 			}
 			 	else if("607" == code) {
 					alert("您尚未登录或登录已失效！");
 					top.location.href = "../admin/login.html";
 				}
 				else
 				{
 					alert("获取数据失败，原因是："+response.message);
 				}
 			}});
	
	
	
	
}
