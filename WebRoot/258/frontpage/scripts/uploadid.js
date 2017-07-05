

$(function () {
	$("input[name='cardId']").change(verfycardid);
});

function verfycardid()
{
	var cardid=$("input[name='cardId']").val();
	if(cardid!=null||cardid!="undefined")
	{
		cardid=cardid.toUpperCase();
	}
    var str= checkIdcard(cardid);
   
    if(str=="验证通过!")
    {
    	$("#cardidresult").html("");
    }
    else
    {
    	 	$("#cardidresult").html(str);
    		$("#cardidresult").attr("style","color:red;");
    		
    }
}

function clearInputData(selector) {
    if (selector == undefined) { selector = "body"; }
    $(selector + " select," + selector + " input").each(function (j) {
        $(this).val("");
    });
}
function submitcardinfo() {

    var orderId = $("input[name='orderId']").val();
    var username = $("input[name='username']").val();
    var cardId = $("input[name='cardId']").val();
    var cardfront = $(":file[name='cardfront']").val();
    var cardother = $(":file[name='cardother']").val();
    if (orderId == "") {
        alert("运单号不能为空!");
        return false;
    }
    orderId=orderId.trim();
    if (username == "") {
        alert("姓名不能为空!");
        return false;
    }
    username=username.trim();
    if (cardId == "") {
        alert("身份证号码不能为空!");
        return false;
    }
    cardId=cardId.trim();
    if (cardfront == "") {
        alert("必须上传身份证正面图片!");
        return false;
    }
    if (cardother == "") {
        alert("必须上传身份证背面图片!");
        return false;
    }

    cardId=cardId.toUpperCase();
    $("input[name='cardId']").val(cardId);
    
    
    
    
    
    var str= checkIdcard(cardId);
    
    
    
    if(str=="验证通过!")
    {
    	//	$("#cardId_result").attr("style","color:blue;");
    }
    else
    {
    		$("#cardidresult").attr("style","color:red;");
    		alert("身份证号错误："+str);
    		return false;
    }
    
    var fdata=$("input[name='cardfront_data']").val();
    var odata=$("input[name='cardother_data']").val();
    if(removenull(fdata)==""||fdata.length<23)//没有压缩的数据
    {
    }
    else//包含有压缩的数据，清空上传的图片
    {
    	$(":file[name='cardfront']").val("");
    }
    
    if(removenull(odata)==""||odata.length<23)//没有压缩的数据
    {
    }
    else//包含有压缩的数据，清空上传的图片
    {
    	$(":file[name='cardother']").val("");
    }
    $("#cardidresult").html("");
	if($(":file[name='cardfront']").val()==""&&$(":file[name='cardother']").val()=="")
	{
	    $.ajax({
			type : "post",
			url : "/guest/upload_cardid_nopic",
			data : {
				"orderId" : orderId,
				"username" : username,
				"cardId" : cardId,
				"cardfront_data" : fdata,
				"cardother_data" : odata
			},
			success : function(data) {
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
	                clearInputData("#uploadcardid");
	                $("input[name='cardfront_data']").val("");
	                $("input[name='cardother_data']").val("");
	            }
	            else {
	                alert("上传失败，原因：" + data.message);
	            }
	
	        }
		});

	}
	else
	{
	    $("#uploadcardid").ajaxSubmit({
	        type: 'post',
	        //dataType : 'text',
	        url: "/guest/upload_cardid",
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
	                clearInputData("#uploadcardid");
	                $("input[name='cardfront_data']").val("");
	                $("input[name='cardother_data']").val("");
	            }
	            else {
	                alert("上传失败，原因：" + data.message);
	            }
	
	        }
	    });
	}


}


///guest/upload_cardid_nopic
