
var addressgrid_selector = "#addressgrid-table";
//查询数据时向后台提交的参数

var addresspager_selector = "#addressgrid-pager";
var init = false;

//初始列表数据
$(function ($) {
	
	$("#divAdd input[name='cardId']").change(verfycardid);
	
	  var id=request("id");
    if(removenull(id)=="")
    {
    	return false;
    }
    
    
    $("[name=radioupload]").click(function () {
        if ($(this).val() == "hc") {
            $("#divZM").hide();
            $("#divFM").hide();
            $("#divHC").show();
        } else {
            $("#divZM").show();
            $("#divFM").show();
            $("#divHC").hide();
        }
    });
    
    
    var orderId="";;
	 $.ajax({
			type : "post",
			url : "/m_order/get_one_by_user",
			data : {id:id},
			success : function(response) {
				var code = response.code;
				if (code == "200") {
					
					if(response.data!=null)
					{
						$("#orderId").html(response.data.orderId);
						$("input[name='orderId']").val(response.data.orderId);
						
						orderId=response.data.orderId;
						if(response.data.suser!=null)
						{
							setSpanText("#sendpersondiv", response.data.suser);
						
							
							var alladdress=removenull(response.data.suser.address);
							
							if(removenull(response.data.suser.dist)!="")
							{
								if(alladdress=="")
								{
									alladdress=removenull(response.data.suser.dist);
								}
								else
								{
									alladdress=alladdress+",&nbsp;"+removenull(response.data.suser.dist);
								}
							}
							if(removenull(response.data.suser.city)!="")
							{
								if(alladdress=="")
								{
									alladdress=removenull(response.data.suser.city);
								}
								else
								{
									alladdress=alladdress+",&nbsp;"+removenull(response.data.suser.city);
								}
							}
							if(removenull(response.data.suser.state)!="")
							{
								if(alladdress=="")
								{
									alladdress=removenull(response.data.suser.state);
								}
								else
								{
									alladdress=alladdress+",&nbsp;"+removenull(response.data.suser.state);
								}
							}
							if(removenull(response.data.suser.zipcode)!="")
							{
								if(alladdress=="")
								{
									alladdress=removenull(response.data.suser.zipcode);
								}
								else
								{
									alladdress=alladdress+",&nbsp;"+removenull(response.data.suser.zipcode);
								}
							}
							
							$("#sendpersondiv").find("span[name='alladdress']").html(alladdress);
							
							$("input[name='username']").val(response.data.ruser.name);
							
							//显示上传身份证号
							$("input[name='cardId']").val(removenull(response.data.ruser.cardid));
							
						}
						
						
						if(response.data.ruser!=null)
						{
							setSpanText("#revpersondiv", response.data.ruser);
						
							
							var alladdress=removenull(response.data.ruser.address);
							
							if(removenull(response.data.ruser.dist)!="")
							{
								if(alladdress=="")
								{
									alladdress=removenull(response.data.ruser.dist);
								}
								else
								{
									alladdress=removenull(response.data.ruser.dist)+alladdress;
								}
							}
							if(removenull(response.data.ruser.city)!="")
							{
								if(alladdress=="")
								{
									alladdress=removenull(response.data.ruser.city);
								}
								else
								{
									alladdress=removenull(response.data.ruser.city)+alladdress;
								}
							}
							if(removenull(response.data.ruser.state)!="")
							{
								if(alladdress=="")
								{
									alladdress=removenull(response.data.ruser.state);
								}
								else
								{
									alladdress=removenull(response.data.ruser.state)+alladdress;
								}
							}
							if(removenull(response.data.ruser.zipcode)!="")
							{
								if(alladdress=="")
								{
									alladdress=removenull(response.data.ruser.zipcode);
								}
								else
								{
									alladdress=alladdress+"&nbsp;"+removenull(response.data.ruser.zipcode);
								}
							}
							
							$("#revpersondiv").find("span[name='alladdress']").html(alladdress);
							
						
							
							$("#revpersondiv").find("a[name='cardurl_a']").attr("href",removenull(response.data.ruser.cardurl));
							$("#revpersondiv").find("img[name='cardurl']").attr("src",removenull(response.data.ruser.cardurl));
							$("#revpersondiv").find("a[name='cardother_a']").attr("href",removenull(response.data.ruser.cardother));
							$("#revpersondiv").find("img[name='cardother']").attr("src",removenull(response.data.ruser.cardother));
							$("#revpersondiv").find("a[name='cardtogether_a']").attr("href",removenull(response.data.ruser.cardtogether));
							$("#revpersondiv").find("img[name='cardtogether']").attr("src",removenull(response.data.ruser.cardtogether));
							
							
							
							
						}
						
						if(response.data.detail!="")
						{
							var commudityinfo="";
							$.each(response.data.detail,function(){
								if(commudityinfo=="")
								{
									commudityinfo=this.productName+ "*"+ this.brands+"*"+this.quantity;
								}
								else
								{
									commudityinfo=commudityinfo+";"+this.productName+"*"+ this.brands+"*"+this.quantity;
								}
							});
							$("#packagediv1").find("span[name='commudityinfo']").html(commudityinfo);
						}
						
						
						setSpanText("#packagediv1", response.data);
						setSpanText("#packagediv2", response.data);
						$("#packagediv2").find("span[name='state']").html(morderstatemap(response.data.state));
						var payornotremark="";
						if(response.data.payornot=="0")
						{
							payornotremark="未付款";
						}
						else if(response.data.payornot=="1")
						{
							payornotremark="已付款";
						}
						else
						{
							payornotremark="状态未定";
						}
						$("#packagediv2").find("span[name='payornot']").html(payornotremark);
						/*if(removenull(orderId)!="")//获取路由
						{
							
							 $.ajax({
									type : "post",
									url : "/m_route/guest",
									data : {oid:orderId,kuaidi_type:"0"},
									success : function(response) {
										var code = response.code;
										if (code == "200") {
											alert("获取路由成功");
										}}});
						}*/
						
						
						
						
						
					}
					
				    //setSpanText("#sendpersondiv", response.data.suser);
					// window.tips("获取成功", false);
					// clearInputData("#divAdd");
					// refreshDataGrid(postData);
					
					
					 if(removenull(orderId)!="")//格式化路由
					{
						 initGrid(orderId);
					}
					
				} else if ("607" == code) {
					alert("您尚未登录或登录已失效！");
					userlogout();
				} else {
					// 失败
					alert("获取信息失败，原因是:" + response.message);
				}
			}
		});
	
});



function initGrid(orderId) {
    $(window).resize(function () {
        $(addressgrid_selector).setGridWidth($(".page-content").width() - 2, true);
    });

  
    
    
    var send_postData={oid:orderId,kuaidi_type:"0"}
    
    
    jQuery(addressgrid_selector).jqGrid({
        url: "/m_route/guest",
        datatype: "json",
        height: "auto",
        mtype: "get",
        postData:send_postData,
        //时间	状态	状态说明	备注
        colNames: ['时间', '状态', "状态说明", "备注"],
        colModel: [
             { name: 'date', index: 'date', width: 80, },
            {
                name: 'state', index: 'state', width: 60,
            },
            { name: 'stateRemark', index: 'stateRemark', width: 120, },
             { name: 'remark', index: 'remark', width: 120, },

        ],

      //  jsonReader: {
       //     root: "data.SE160030349EEA"
      //  },
        jsonReader: {
            root: "data",
           // page: "data.pageNow",
           // total: "data.pageCount",
           // records: "data.rowCount"
        },
        viewrecords: true,
        rowNum: 100,
        rowList: [100, 300, 500],
        pager: "",
        altRows: true,

        multiselect: false,
        //multikey: "ctrlKey",
        multiboxonly: true,

        loadComplete: function (data) {
          
            var table = this;

            //防止出现水平滚动条
            if (init == false) {
                $(".ui-jqgrid-bdiv").css({ 'overflow-x': 'hidden' });
                $(".ui-jqgrid-htable th:last").css({ "width": $(".ui-jqgrid-htable th:last").width() - 2 });
                $(".jqgfirstrow td:last").css({ "width": $(".jqgfirstrow td:last").width() - 2 });
                init = true;
            }
        },
        gridComplete: function () {

        },
        onSelectRow: function (rowid, status) {

        },
        onSelectAll: function (aRowids, status) {

        },
        caption: "",
        autowidth: true
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

function verfycardid()
{
	var cardid=$("input[name='cardId']").val();
	if(cardid!=null||cardid!="undefined")
	{
		cardid=cardid.toUpperCase();
	}
	cardid=cardid.trim();
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


function submitcardinfo() {

    var orderId = $("input[name='orderId']").val();
    var username = $("input[name='username']").val();
    var cardId = $("input[name='cardId']").val();
    var cardfront = $(":file[name='zmpicture']").val();
    var cardother = $(":file[name='fmpicture']").val();
    var hcpicture = $(":file[name='hcpicture']").val();
    if (orderId == "") {
        alert("运单号不能为空!");
        return false;
    }
    if (username == "") {
        alert("姓名不能为空!");
        return false;
    }
    if (cardId == "") {
        alert("身份证号码不能为空!");
        return false;
    }
    var filetype = $(":radio[name='radioupload']:checked").val();
    //$(":radio[name='userId']:checked").val()

    if (filetype == "hc")//合成图文件
    {
    	if (hcpicture == "") {
	        alert("身份证图片不能为空!");
	        return false;
	    }
    }
    else
    {
	    if (cardfront == "") {
	        alert("必须上传身份证正面图片!");
	        return false;
	    }
	    if (cardother == "") {
	        alert("必须上传身份证背面图片!");
	        return false;
	    }
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
    $("#cardidresult").html("");
    
    
    
    if (filetype == "hc")//合成图文件
    {

        $(":file[name='zmpicture']").val("");//正面图
        $(":file[name='fmpicture']").val("");//反面图
    }
    else
    {
    	$(":file[name='hcpicture']").val("");//清空正合成图片
    }
    
    $("#uploadcardid").ajaxSubmit({
        type: 'post',
        //dataType : 'text',
        url: "/user/upload_cardid",
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
                //clearInputData("#uploadcardid");
            }
            else if ("607" == code) {
				alert("您尚未登录或登录已失效！");
				userlogout();
			}
            else {
                alert("上传失败，原因：" + data.message);
            }

        }
    });


}