

$(function () {

	
		
	$.ajax({
     	type : "get",
 		url : "/admin/warehouse/all",
 		success : function(response) {
 			var code = response.code;
 			if ("200" == code) {
 				var warehousedata=response.data;
 				$.ajax({
 			     	type : "post",
 			 		url : "/admin/endicial/getargs",
 			 		success : function(response) {
 			 			var code = response.code;
 			 			if ("200" == code) {
 			 					setInputData("#table_endicia_arg", response.data);
 			 					var wids=response.data.authWarehouseId;
 			 					var wid="";
 			 					if(wids!=null)
 			 					{
 			 						wid=wids.split(";");
 			 					}
 			 					var str1="";
 			 					$.each(warehousedata,function(){
 			 						var flag=0;
 			 						if(wid!="")
 			 						{
	 			 						for(var i=0;i<wid.length;i++)
	 			 						{
	 			 							if(this.id==wid[i])
	 			 							{
	 			 								flag=1;
	 			 								break;
	 			 							}
	 			 						}
 			 						}
 			 						
 			 						if(flag==1)//这是可以使用此功能的门店
 			 						{
 			 							str1+='<input name="authwlist" type="checkbox" value="'+this.id+'" checked="checked"/>'+this.name+' &nbsp;&nbsp;';
 			 						}
 			 						else
 			 						{
 			 							str1+='<input name="authwlist" type="checkbox" value="'+this.id+'" />'+this.name+' &nbsp;&nbsp;';
 			 						}
 			 					});
 			 					
 			 					$("#authwarehouselist").html(str1);
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

function submitEndiciaArg()
{
	 var authWarehouseId="";
	 $("#table_endicia_arg").find(":checkbox[name='authwlist']").each(function(){
		
		 var sfruit=$(this).is(':checked');
         if(sfruit==true)
    	 {
        	 if(authWarehouseId=="")
     		{
        		 authWarehouseId=$(this).val();
     		}
        	 else
           {
        		 authWarehouseId+=";"+$(this).val(); 
           }
    	 }
	 });
	
	 $("#table_endicia_arg").find(":hidden[name='authWarehouseId']").val(authWarehouseId);
	 var postData = getEelementData("#table_endicia_arg");
	
	 
	 $.ajax({
	     	type : "post",
	 		url : "/admin/endicial/configarg",
	 		data:postData,
	 		success : function(response) {
	 			var code = response.code;
	 			if ("200" == code) {
	 				alert("修改成功");
	 				window.location.href = "../admin/Endicia_manage.html";
	 				}
	 			 	else if("607" == code) {
	 					alert("您尚未登录或登录已失效！");
	 					top.location.href = "../admin/login.html";
	 				}
	 				else
	 				{
	 					alert("删除失败，原因是："+response.message);
	 				}
	 	}});
}

