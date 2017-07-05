
$(function () {
    $("input[type=checkbox]").bootstrapSwitch();
})

var yundangrid_selector = "#yundangrid-table";
//查询数据时向后台提交的参数
var postData = {};
var jsonData = [];
//初始列表数据
$(function ($) {

    var yundanpager_selector = "#yundangrid-pager";
    var init = false;
    $(window).resize(function () {
        $(yundangrid_selector).setGridWidth($(".page-content").width() - 20, true);
    });
	
    jQuery(yundangrid_selector).jqGrid({
        url: "/admin/flyinfo/search",
        datatype: "json",
        height: "auto",
        mtype : "post",
        //航班号	航班状态	运单更改	所属门店	运单数量	创建时间	修改时间		导入表格	操作
        colNames: ['航班号', '航班状态','修改运单状态','状态变更短信','身份证提醒通知', '备注',  "所属门店", "运单数量", "创建时间", "修改时间", "操作", "上传运单"],
        colModel: [

            { name: 'flightno', index: 'flightno', width: 100, },
            {
                name: 'state', index: 'state', width: 100, formatter: function (cellValue, option, row) {
                	
                	//return morderstatemap(cellValue);
                    //返回状态下拉框
                    //若是动态生成，请从后台中返回到前端页面隐藏起来
                    var select = '<select name="state_id" style="width:90%;"><option value="-1">请选择状态</option><option value="3" selected="selected">送往集散中心</option><option value="4">已打件</option><option value="5">送往机场</option><option value="6">空运中</option><option value="7">海关清关</option><option value="8">美淘中国分部已揽收</option><option value="9">已完成</option></select>';

                    var mySelect = select;
                    var option = $(select).find("option[value='" + row.state + "']");
                    if (option.length > 0) {
                        var old = option[0].outerHTML;
                        var newOption = option.attr("selected", true)[0].outerHTML;
                        mySelect = mySelect.replace(old, newOption);
                    }
                    return mySelect;
                }
            },
            { name: 'modifystate', index: 'modifystate', width: 60, 
            	formatter: function (cellValue, option, row) {
            		return '<input type="checkbox" name="modifystate_id" checked="checked"/>';
            	}
            },
            { name: 'sendmessage', index: 'sendmessage', width: 60, 
            	formatter: function (cellValue, option, row) {
            		
            		return '<input type="checkbox" name="sendmessage_name"/>';
            	}
            },
            { name: 'sendmessage_cardId', index: 'sendmessage_cardId', width: 60, 
            	formatter: function (cellValue, option, row) {
            		
            		return '<input type="checkbox" name="sendmessage_cardId"/>';
            	}
            },
             {
                 name: 'remark', index: 'remark', width: 100,formatter: function (cellValue, option, row) {
                	 return "<textarea name='remark_name'>"+cellValue+"</textarea>";
                 }
                
             },
             { name: 'warehouseName', index: 'warehouseName', width: 100 },
             { name: 'ordersno', index: 'ordersno', width: 60, },
            { name: 'createDate', index: 'createDate', width: 100, },
             { name: 'modifyDate', index: 'modifyDate', width: 100 },
             
             {
                 name: 'op', index: 'op', width: 120, formatter: function (cellValue, option, row) {
                     //返回修改按钮
                     var edit = "<button  class='btn btn-purple btn-sm' type='button'  onclick=\"update('" + row.id + "')\"  ><span class='icon-edit'></span>修改</button>";
                     var del = "<button style='margin-left:10px;' class='btn btn-danger btn-sm' type='button'  onclick=\"del('" + row.id + "')\"  ><span class='fa fa-times'></span>删除</button>";
                     return edit + del;
                 }
             },
             {
                 name: 'upload', index: 'upload', width: 60, formatter: function (cellValue, option, row) {
                     //返回修改按钮
                     var edit = "<button  class='btn btn-purple btn-sm' type='button'  onclick=\"uploadfile('" + row.id + "')\"  ><span class='icon-cloud-upload'></span>上传</button>";
                     return edit;
                 }
             }
        ],
       
        jsonReader : {
        	root:"data.datas",
        	page: "data.pageNow",
        	total: "data.pageCount",
        	records: "data.rowCount"
        	},
        viewrecords: true,
        rowNum: 10,
        rowList: [10, 30, 50],
        pager: yundanpager_selector,
        altRows: true,

        multiselect: false,
        //multikey: "ctrlKey",
        multiboxonly: true,

        loadComplete: function (data) {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
            }, 0);
            
          
            if(data.code=="200")
            {
	            if((removenull(data.data)!="")&&removenull(data.data.datas)!="")
	            {
		            jsonData = data.data.datas;
		           
	            }
            }
            else if("607" == data.code) {
				alert("您尚未登录或登录已失效！");
				top.location.href = "../admin/login.html";
			}
            else
            {
            	alert("读取出错，原因："+data.message);
            }
            
            
            
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
        caption: "航班表",
        autowidth: true
    });


    //navButtons
    jQuery(yundangrid_selector).jqGrid('navGrid', yundanpager_selector,
        { 	//navbar options
            edit: false,
            editicon: 'icon-pencil blue',
            add: false,
            addicon: 'icon-plus-sign purple',
            del: false,
            delicon: 'icon-trash red',
            search: false,
            searchicon: 'icon-search orange',
            refresh: true,
            refreshicon: 'icon-refresh green',
            view: false,
            viewicon: 'icon-zoom-in grey',

            //删除
            delfunc: function () {

            }
        }
    )


    //replace icons with FontAwesome icons like above
    function updatePagerIcons(table) {
        var replacement =
        {
            'ui-icon-seek-first': 'icon-double-angle-left bigger-140',
            'ui-icon-seek-prev': 'icon-angle-left bigger-140',
            'ui-icon-seek-next': 'icon-angle-right bigger-140',
            'ui-icon-seek-end': 'icon-double-angle-right bigger-140'
        };
        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
            var icon = $(this);
            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

            if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
        })
    }

    //点击搜索
    $("#btnSearch").click(function () {
        postData = getEelementData("#divHangbanhao");
        $(yundangrid_selector).jqGrid("setGridParam", { postData: postData }).trigger("reloadGrid");
    });
    //绑定运单输入框的enter事件进行搜索
    $("#yundan").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnyundan").click();
        }
    }));

    $("#btnAdd").click(function () {
        $("#divAdd").toggle();
    });

    //点击高级搜索
    $("#btnAdvanceSearch").click(function () {
        var data = getEelementData("#divAdvanceSearch");
        refreshDataGrid(data);
    });

    //添加航班表单验证并提交,选择器的内容一定要是form表单，里面一定要有一个submit的按钮
    $("#divAdd form").validate({
        //验证规则
        //http://www.runoob.com/jquery/jquery-plugin-validate.html
        rules: {
            hangbanhao2: { required: true, minlength: 4 },
            state2: { required: true }
        }, submitHandler: function (form) {
            var data = getEelementData("#divAdd");
            
            
            
        	var flightno = data.hangbanhao2;
        	var state = data.state2;
        	var remark = data.remark;
 
        	if(flightno=="")
        	{
        		alert("航班号不能为空!");
        		return;
        	}
        	if(state==""||state=="-1")
        	{
        		alert("状态不能为空!");
        		return;
        	}
        	$.ajax({
        		type : "post",
        		url : "/admin/flyinfo/add_morder",
        		data : {
        			flightno : flightno,
        			state : state,
        			remark : remark
        		},
        		success : function(response) {
        			 //清空输入框
        			$("#divAdd").toggle();
                    clearInputData("#divAdd");
        			var code = response.code;
        			if (code == "200") {
        				alert("添加成功");
        				 //刷新列表数据
        	            refreshDataGrid(postData);
        			} else if (code == "607") {
        				alert("您尚未登录或登录已失效！");
        				adminlogout();
        			} else {
        				alert("添加失败，失败原因是:" + response.message);
        			}
        		}
        	});
            
            
            
            
            
            
            
            
           // window.infoDialog("提交", "航班号是" + data.hangbanhao2, true);
            //提交到后台操作，操作完成后调用下面方法
            //隐藏新增div
           // $("#divAdd").toggle();
           
           
        }
    });
});


//提交js对象参数刷新列表数据
function refreshDataGrid(data) {
    $(yundangrid_selector).jqGrid("setGridParam", { postData: data }).trigger("reloadGrid");
}

function uploadfile(id)
{
	var row = jsonData.getData(id);
	setSpanText("#flynoTableInfo", row);
	//alert(row.state);
	//$("#stateName").html(morderstatemap(row.state));
	$(":hidden[name='s_id']").val(id);
	//$("selete[name='s_state']").val(row.state);
	//alert(row.remark);
	//$("textarea[name='s_remark']").val(id);
	

	 dialog({
	        title: "修改航班信息",
	        content: $("#divDetail").html(),
	        width: $(window).width() - 60,
	        height: $("#divDetail").height() + 160,
	        //cancelVal: '关闭',
	        //cancel: true //为true等价于function(){}
	    }).show();
	 
	 
	 
	 return false;
}



function update(id) {
	
    var fieldName = "state";
    var state = $(yundangrid_selector).find("[id=" + id + "]").find("[aria-describedby$=" + fieldName + "] select").val();
    
    //var change = $(yundangrid_selector).find("[id=" + id + "]").find("[aria-describedby$='modifystate'] input").attr("checked");
    
    
    var change=$(yundangrid_selector).find("[id=" + id + "]").find(":checkbox[name='modifystate_id']").is(":checked");
    
    
    var modifystate=0;
    if(change==true)
    {
    	modifystate=1;
    }
  
   change=$(yundangrid_selector).find("[id=" + id + "]").find(":checkbox[name='sendmessage_name']").is(":checked");
   var sendmessage=0;
   if(change==true)
   {
	   sendmessage=1;
   }
   
   var sendmessage_cardidchange=$(yundangrid_selector).find("[id=" + id + "]").find(":checkbox[name='sendmessage_cardId']").is(":checked");
   var sendmessage_cardId=0;
   if(sendmessage_cardidchange==true)
   {
	   sendmessage_cardId=1;
   }
   
   if(sendmessage_cardId==1&&sendmessage==1)
   {
	   alert("对不起，不能同时发送状态变更和身份证提醒通知！");
	   return false;
   }
   
   
  var remark= $(yundangrid_selector).find("[id=" + id + "]").find("textarea[name='remark_name']").val();
//  var file=$(yundangrid_selector).find("[id=" + id + "]").find(":file[name='file']").val();
  var formData = new FormData();
  	formData.append("id", id);
	formData.append("state", state);
	formData.append("modifystate", modifystate);
	formData.append("remark", remark);
	formData.append("sendmessage", sendmessage);
	formData.append("sendmessage_cardId", sendmessage_cardId);
	
	
	
	var httpRequest = new XMLHttpRequest();
	httpRequest.onreadystatechange = function(){
		if(4 == httpRequest.readyState){
			if(200 == httpRequest.status){
				obj = JSON.parse(httpRequest.responseText);
				if (obj.code == "200") {
    				alert("修改成功");
    				 //刷新列表数据
    	            refreshDataGrid(postData);
    			} else if (obj.code == "607") {
    				alert("您尚未登录或登录已失效！");
    				adminlogout();
    			} else {
    				alert("添加失败，失败原因是:" + obj.message);
    			}
			}
		}
	};
	httpRequest.open("post", "/admin/flyinfo/modify_morder", true);
	httpRequest.send(formData);
	return false;
	/*
	$.ajax({  
        url: '/admin/flyinfo/modify_morder' ,  
        type: 'POST',  
        data: formData,
        timeout : 600000,  
        cache: false,  
        contentType: false,  
        processData: false,  
        success: function (returndata) {  
            alert(returndata);  
        },  
        error: function (returndata) {  
            alert(returndata);  
        }  
   });  */
}

function del(id) {
	
	
	
	

	//alert(id);
	if (confirm("你是否确定删除此航班信息？ 如果删除，相关联的运单航班信息也会被删除。"))
	{
		$.ajax({
			type : "post",
			url : "/admin/flyinfo/del",
			data : {
				id : id
			},
			success : function(response) {
				var code = response.code;
				if (code == "200") {
				 alert("删除成功");
			        //todo 这里补充 和后台交互的代码
			        //删除完成后 在success回调中调用下面方法刷新数据列表
			      refreshDataGrid(postData);
				} else if (code == "607") {
					alert("您尚未登录或登录已失效！");
					adminlogout();
				} else {
					alert("修改失败，失败原因是:" + response.message);
				}
			}
		});
	}
	return false;

/*
    if (id == undefined) { window.tips("请选择需要删除的行", false); return; }

    window.confirmDialog("确定删除选择的数据？", "删除提示", function () {
        window.tips("数据已删除", false)
        //todo 这里补充 和后台交互的代码
        //删除完成后 在success回调中调用下面方法刷新数据列表
        refreshDataGrid(postData);
    });*/

}



