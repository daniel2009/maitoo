
//初始列表数据
$(function ($) {
   
	$("#divAdd input[name='cardId']").change(verfycardid);
    initAutocomplete();
    initClickEvent();

    //添加表单验证并提交,选择器的内容一定要是form表单，里面一定要有一个submit的按钮
    $("#divAdd form").validate({
        //验证规则
        //http://www.runoob.com/jquery/jquery-plugin-validate.html
        debug: true,//提交表单，true:不提交表单,手动ajax
        rules: {
            name: { required: true },
           // state: { required: true },
           // city: { required: true },
           // dist: { required: true },
            streetAddress: { required: true },
            phone: { required: true },

        }, submitHandler: function (form) {
        	
        	
        	
            //var data = getEelementData("#divAdd");
           // top.window.infoDialog("提交", "数据是1" + data, true);
            //ajaxSubmit 提交到后台操作，操作完成后调用下面方法
        	
        	var cardUrl=$("#divAdd").find(":file[name='cardUrl']").val();//正反面必须同时上传
        	var cardurlother=$("#divAdd").find(":file[name='cardurlother']").val();//正反面必须同时上传
        	if(cardUrl!=""&&cardUrl!=null)
        	{
        		if((cardurlother==null)||(cardurlother==""))
        		{
        			alert("正反面必须同时上传！");
        			return false;
        		}
        	}
        	
        	if(cardurlother!=""&&cardurlother!=null)
        	{
        		if((cardUrl==null)||(cardUrl==""))
        		{
        			alert("正反面必须同时上传！");
        			return false;
        		}
        	}
        	
        	var cardId=$("#divAdd input[name='cardId']").val();
        	 cardId=cardId.toUpperCase();
        	 $("#divAdd input[name='cardId']").val(cardId);
        	    
        	    
        	    
        	    
        	    
        	    var str= checkIdcard(cardId);
        	    
        	    
        	    
        	    if(str=="验证通过!"||cardId=="")
        	    {
        	    	//	$("#cardId_result").attr("style","color:blue;");
        	    }
        	    else
        	    {
        	    		$("#cardidresult").attr("style","color:red;");
        	    		alert("身份证号错误："+str);
        	    		return false;
        	    }
        	
        	
        	$("#addrevaddress").ajaxSubmit({
        		type : 'post',
        		//dataType : 'text',
        		url : "/user/rev_user/add",
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
        			if(data.code=="200")
        			{
        				alert("保存成功!");
        				clearInputData("#divAdd");
        				addAddressDialog.close();
        			}else if ("607" == data.code) {
                        alert("您尚未登录或登录已失效！");
                        userlogout();
                    } else
        			{
        				alert("保存失败，原因："+data.message);
        			}
        		}
        		});
            
            
           
            //刷新列表数据
            //refreshDataGrid(postData);
        }
    });
});

function initClickEvent() {
    
    $("#cancelAdd").click(function () {
        addAddressDialog.close();
    });

   
    $("#divAdd #hc").click(function () {
        $("#divAdd span[name=hc]").show();
        $("#divAdd span[name=dl]").hide();
    });

    $("#divAdd #dl").click(function () {
        $("#divAdd span[name=hc]").hide();
        $("#divAdd span[name=dl]").show();
    });

}


//新增修改省市区选择
function initAutocomplete() {

    //选择省
    $("#divAdd input[name=province]").bigAutocomplete({
        data: $.areaData.p,
        //url: "areadata.html",//可以请求到后台，返回的格式{"data":[{"title":"广西"}]}
        callback: function (p) {
        }
    });


    //选择市
    $("#divAdd input[name=city]").focusin(function () {
        $(this).bigAutocomplete({
            data: $.areaData.c[$("#divAdd input[name=province]").val()]
        });
    });

    //县区
    $("#divAdd input[name=district]").focusin(function () {
        $(this).bigAutocomplete({
            data: $.areaData.a[$("#divAdd input[name=province]").val() + "-" + $("#divAdd input[name=city]").val()]
        });
    });




    //编辑时选择省
    $("#divEdit input[name=province]").bigAutocomplete({
        data: $.areaData.p,
        //url: "areadata.html",//可以请求到后台，返回的格式{"data":[{"title":"广西"}]}
        callback: function (p) {
        }
    });


    //选择市
    $("#divEdit input[name=city]").focusin(function () {
        $(this).bigAutocomplete({
            data: $.areaData.c[$("#divEdit input[name=province]").val()]
        });
    });

    //县区
    $("#divEdit input[name=district]").focusin(function () {
        $(this).bigAutocomplete({
            data: $.areaData.a[$("#divEdit input[name=province]").val() + "-" + $("#divEdit input[name=city]").val()]
        });
    });
}

//选择正反面清空选择
function click_choose_tpe()
{
	
	var a=$(":radio[name='sfz']:checked").val();
	if(a=="0")//正反合成，清空之前的图片
	{
		$(":file[name='cardUrl']").val("");
		$(":file[name='cardurlother']").val("");
		
	}
	else
	{
		$(":file[name='cardurltogether']").val("");
	
	}
}


function clearrecimg(obj)
{
	if (confirm("确认清空所有图片?"))
	{
		$(obj).parent().find('img').attr('src',"");
		$(":hidden[name='precardUrl']").val("");
		$(":hidden[name='precardurlother']").val("");
		$(":hidden[name='precardurltogether']").val("");
	}
	
}

function verfycardid()
{
	var cardid=$("#divAdd input[name='cardId']").val();
	if(cardid!=null||cardid!="undefined")
	{
		cardid=cardid.toUpperCase();
	}
	
	if(cardid=="")
	{
		$("#cardidresult").html("");
		return false;
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