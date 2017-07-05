
var addressgrid_selector = "#addressgrid-table";
//查询数据时向后台提交的参数

var addresspager_selector = "#addressgrid-pager";
var init = false;

//初始列表数据
$(function ($) {
	
	var id=request("id");
    if(removenull(id)=="")
    {
    	return false;
    }
    
  
    var orderId="";;
	 $.ajax({
			type : "post",
			url : "/admin/t_order/get_one_by_admin",
			data : {id:id},
			success : function(response) {
				var code = response.code;
				if (code == "200") {
					
					if(response.data!=null)
					{
						$("#header_torderId").html(response.data.torderId);

						setSpanText("#torderdata", response.data);
						if(response.data.position!=null)
						{
							$("#header_position").html(response.data.position.position);
						}
						if(response.data.type=="1"||response.data.type==1)
						{
							$("span[name='i_type']").html("转运中转");
						}
						else if(response.data.type=="0"||response.data.type==0)
						{
							$("span[name='i_type']").html("本地转运");
						}
						else
						{
							$("span[name='i_type']").html("");
						}
						

						$("#torderdata span[name='state']").html(torderstatemap(response.data.state));
						
						
						
					}
					
					 if(removenull(response.data.id)!="")//格式化路由
					{
						 initGrid(response.data.id);
					}
					
				} else if ("607" == code) {
					alert("您尚未登录或登录已失效！");
					adminlogout();
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

  
    
    
    var send_postData={"id":orderId};
    
    
    jQuery(addressgrid_selector).jqGrid({
        url: "/admin/t_order/get_one_route_by_admin",
        datatype: "json",
        height: "auto",
        mtype: "get",
        postData:send_postData,
        //时间	状态	状态说明	备注
        colNames: ['时间', '转运单号', "状态", "备注","操作人"],
        colModel: [
             { name: 'date', index: 'date', width: 80, },
            {
                name: 't_orderId', index: 't_orderId', width: 60,
            },
            { name: 'state', index: 'state', width: 120, },
             { name: 'remark', index: 'remark', width: 120, },
             { name: 'employeeName', index: 'employeeName', width: 60, },
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
				adminlogout();
			}
            else {
                alert("上传失败，原因：" + data.message);
            }

        }
    });


}