//添加商品条目
function addGoodsItem() {
    var teplament = $("#goodsItemTeplament").html();
    $("#goodsItemBody").append(teplament);
}

//删除商品条目
function delGoodsItem(obj) {
    $(obj).parent().parent().remove();
}



//提交预报信息
function yb_submit_form() {
	//先判断是否有填写运单号
	var flag=0;
	var same=false;
	var arr = new Array();
	$("#submitformId input[name='danhao']").each(
			function () {
				
				 var danhao = $(this).val();//转运单号
				 var templa=$(this);
				 var goodname= $(this).parent().parent().find("input[name='goodname']").val();//获取品名
				 if(removenull(danhao)!="")
				 {
					 danhao=danhao.trim();
					 $.each(arr, function(){ 
						 if(this.toUpperCase()==danhao.toUpperCase())
						 {

						     //存在相同的运单号
								
							 templa.css({
						            "border-color": "red"
						        });
								 $(this).change(
										function () {
										    if (templa.val() != null)
										    	templa.css({
														    "border-color": ""
														});
										});
								      
								  
								 alert("本提交中存在相同的转运单号，请检查!");
								 same=true;
								 return false;
						 
						 
						 }
					 }); 
					 arr[flag]=danhao;
					 flag++;
					 
				 }
				 else
				 {}
			     
			});
	
	if(same==true)
	{
		return false;
	}
	
	if(flag==0||flag=="0")
	{
		alert("至少填写一条预报信息");
		return false;
	}
	flag=0;
	var good=true;
	$("#submitformId input[name='danhao']").each(
			function () {
				
				 var danhao = $(this).val();//转运单号
				 var goodname= $(this).parent().parent().find("input[name='goodname']").val();//获取品名
				 if(removenull(danhao)!="")
				 {
					 danhao=danhao.trim();				
					 $(this).val(danhao);
					 
					 
					 re = /^[a-zA-Z0-9]{1,}$/;  /*匹配0-30个字符，只能是数字或字母，不包括下划线的正则表达式*/
					 if (re.test(danhao)) {
					     //匹配
					 }
					 else {
					     //不匹配

							
						 $(this).css({
				            "border-color": "red"
				        });
						 $(this).change(
								function () {
								    if ($(this).val() != null)
								    	$(this).css({
												    "border-color": ""
												});
								});
						      
						  
						 alert("转运单号只能是字母和数字,并且长度在30个字符以内!");
						 good=false;
						 return false;
					 
					 }
					 
					 
					 
					 
					 if(danhao.length<8)
					 {
						
						 $(this).css({
				            "border-color": "red"
				        });
						 $(this).change(
								function () {
								    if ($(this).val() != null)
								    	$(this).css({
												    "border-color": ""
												});
								});
						      
						  
						 alert("转运单号不能小于8位，请检查!");
						 good=false;
						 return false;
					 }
					 else if(danhao.length>30)
					 {
						 $(this).css({
					            "border-color": "red"
					        });
							 $(this).change(
									function () {
									    if ($(this).val() != null)
									    	$(this).css({
													    "border-color": ""
													});
									});
							      
							  
							 alert("转运单号不能大于30个字符，请检查!");
							 good=false;
							 return false;
					 }
					 else
					 {
						 //如果提交的运单号存在，提交的商品备注信息必须填写
						 if(removenull(goodname)=="")//
						 {
							 $(this).parent().parent().find("input[name='goodname']").css({
						            "border-color": "red"
						        });
							 $(this).parent().parent().find("input[name='goodname']").change(
										function () {
										    if ($(this).val() != null)
										    	$(this).css({
														    "border-color": ""
														});
										});
								      
								  
								 alert("必须填写商品备注!");
								 good=false;
								 return false;
						 }
						 else
						{
							 goodname=goodname.trim();
							 $(this).parent().parent().find("input[name='goodname']").val(goodname);
							 if(goodname.length<4)
							 {
								 $(this).parent().parent().find("input[name='goodname']").css({
							            "border-color": "red"
							        });
								 $(this).parent().parent().find("input[name='goodname']").change(
											function () {
											    if ($(this).val() != null)
											    	$(this).css({
															    "border-color": ""
															});
											});
									      
									  
									 alert("商品备注信息过短，请填写详细的信息!");
									 good=false;
									 return false;
							 }
							 else
							 {
								 flag++;
							 }
							 
						}
						 
					 }
				 }
				 else
				 {}
			     
			});
	
		
		if(good==false)
		{
			return false;
		}

		if(flag==0||flag=="0")
		{
			alert("至少填写一条预报信息");
			return false;
		}
	    $("#submitformId").ajaxSubmit({
	        type: 'post',
	        //dataType : 'text',
	        url: "/user/t_order/yb_add",
	        success: function (data) {
	            if (typeof (data) != "object") {
	            	if(data.indexOf("<PRE>")!=-1)//返回包含前后结束符，在ie的情况下，有的会自动加上,所以要判断是否包含<PRE></PRE>，返回的，ie5,ie7,ie8是大小的，ie9是小写的，ie10之后不会添加此前后缀，所以要统一处理
	            	{
	            		data=data.substr(data.indexOf("<PRE>")+5, data.indexOf("</PRE>")-5); 
	            	}
	            	else if(data.indexOf("<pre>")!=-1)
	            	{
	            		data=data.substr(data.indexOf("<pre>")+5, data.indexOf("</pre>")-5); 
	            	}
	            	
	                data = $.parseJSON(data);
	            }
	            if (data.code == "200") {
	                alert("上传成功!");
	                getcounts();
	                clearInputData("#submitformId");
	               
	            }
	            else if(data.code=="607")
	            {
	            	alert("请登陆后重新操作！");
	            	userlogout();
	            }
	            else {
	                alert("上传失败，原因：" + data.message);
	            }
	
	        }
	    });
	


}


function getcounts() { 
	$.ajax({
	type : "post",
	url : "/t_order/get_counts",
	success: function (response) {
	    var code = response.code;

	    if ("200" == code) {

	        if (response.data != null) {
	            setSpanText("#orderQuantity", response.data);
	            //已入库的数量
	            top.window.document.getElementById("t_haveinputQ").innerText = response.data.t_haveinputQ;
	            top.window.document.getElementById("t_totalQ").innerText = response.data.t_totalQ;
	            
	            
	        }
	    }
	}
});

$.ajax({
	type : "post",
	url : "/m_order/get_counts",
	success : function(response) {
		var code = response.code;

		if ("200" == code) {

			if (response.data != null) {
				setSpanText("#orderQuantity", response.data);
				top.window.document.getElementById("tranwaitpQ").innerText = response.data.tranwaitpQ;
				top.window.document.getElementById("totalQ").innerText = response.data.totalQ;
				top.window.document.getElementById("nopayQ").innerText = response.data.nopayQ;

			} else {

			}
		} else {

		}
		
	}
});
}