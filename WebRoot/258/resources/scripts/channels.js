

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
        url: "/admin/channel/search",
        datatype: "json",
        height: "auto",
        mtype: "get",
        //员工ID	门市编号	门市名称	权限	操作
        colNames: ['渠道ID','渠道名称', '备注别名', '授权门市列表','创建时间',"附加费（$）","操作"],
        colModel: [
               {
                   name: 'id', index: 'id', width: 30, fix:true
               },
            {
                name: 'name', index: 'name', width: 50, fix:true
            },
             { name: 'alias', index: 'alias', width: 80, fix: true },
             { name: 'storeListName', index: 'storeListName', width: 150 ,fix:true },

             { name: 'createDate', index: 'createDate', width: 60 ,fix:true },
             { name: 'additivePrice', index: 'additivePrice', width: 30 ,fix:true },
             {
                 name: 'op', index: 'op', width: 100, fix: true, formatter: function (cellValue, option, row) {
                     var edit = "<button  style='margin-left:5px;margin-top:1px;' class='btn btn-purple btn-sm' type='button'  onclick=\"update('" + row.id + "')\"  ><span class='icon-edit'></span>修改</button>";
                     var del = "<button style='margin-left:5px;margin-top:1px;' class='btn btn-danger btn-sm' type='button'  onclick=\"del('" + row.id + "')\"  ><span class='fa fa-times'></span>删除</button>";
                     return edit + del;
                 }
             }
        ],

        jsonReader: {
            root: "data.datas",
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


            if (data.code == "200") {
                if ((removenull(data.data) != "") && removenull(data.data.datas) != "") {
                    jsonData = data.data.datas;

                }
            }
            else if ("607" == data.code) {
                alert("您尚未登录或登录已失效！");
                top.location.href = "../admin/login.html";
            }
            else {
                alert("读取出错，原因：" + data.message);
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
        caption: "渠道列表",
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
                var ids = $(yundangrid_selector).jqGrid('getGridParam', 'selarrrow');
                var idData = "";
                for (var i = 0; i < ids.length; i++) {
                    idData = idData + "id=" + ids[i] + "&";
                }
                del(idData);
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
        });
    }


    $("#btnAdd").click(function () {
    	
    	
    	
    	getWarehouselist("");
        clearInputData("#divAdd");
        $("#divAdd select[name='storeMaster']").val("0");
       $("#divGrid").hide();
        $("#divAdd").show();
        getWarehouse(0);
    });

    $(".cancel").click(function () {
        $("#divGrid").show();
        $("#divAdd").hide();
        $("#divEdit").hide();
        $("#divDetail").hide();
    });

    //点击高级搜索
    $("#btnAdvanceSearch").click(function () {
        var data = getEelementData("#divAdvanceSearch");
        refreshDataGrid(data);
    });

   
    $("#divAdvanceSearch #keyword").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnAdvanceSearch").click();
        }
    }));
    
    $("#divAdvanceSearch #empId").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnAdvanceSearch").click();
        }
    }));
    

    //添加航班表单验证并提交,选择器的内容一定要是form表单，里面一定要有一个submit的按钮
    $("#divAdd form").validate({
        //验证规则
        //http://www.runoob.com/jquery/jquery-plugin-validate.html
        rules: {
        	//name: { required: true, minlength: 6, maxlength: 20 },
        	name: { required: true, minlength: 2,maxlength:40 },
        	additivePrice: { required: true },
        	
           
        }, submitHandler: function (form) {
            //account:admin
            //password:111111
            //sid:4
            //sname:Los Angeles 总仓
            //phone:
            //auth:
        	var additivePrice=$("#divAdd input[name=additivePrice]").val();
        	additivePrice = parseFloat(additivePrice);
			if (isNaN(additivePrice)||(additivePrice<0))
			{
				alert("附加费用只能填写大于或等于0的数值");
				return false;
			}
			
			//是否支付转运
			var checkbox=$("#divAdd :checkbox[name=tran_type]").is(":checked");
			var tran_type=0;
			if(checkbox)
			{
				tran_type=1;
			}
			
        	
            var data = "";
            data += "name=" + $("#divAdd input[name=name]").val();
            data += "&alias=" + $("#divAdd input[name=alias]").val();
            data += "&additivePrice=" + $("#divAdd input[name=additivePrice]").val();
            data += "&show_remark=" + $("#divAdd input[name=show_remark]").val();
            data += "&show_type=" + $("#divAdd input[name=show_type]").val();
            data += "&tran_type=" + tran_type;
           // data +="&sid="+ $("#divAdd [name=storeId]").val();
           // data += "&sname=" + $("#divAdd [name=storeId]").find("option:selected").text();
           // data += "&storeMaster=" + $("#divAdd [name=storeMaster]").val();
            
            $("#divAdd [name=authority_id]:checked").each(function () {
                data += "&auth=" +$(this).attr("val");
            });
            
            $.ajax({
                type: "post",
                url: "/admin/channel/add_admin",
                data: data,
                success: function (response) {

                    var code = response.code;
                    if (code == "200") {
                        alert("添加成功");
                        clearInputData("#divAdd");
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
        }
    });

    $("#divEdit form").validate({
        //验证规则
        //http://www.runoob.com/jquery/jquery-plugin-validate.html
        rules: {
        	name: { required: true, minlength: 2,maxlength:40 },
        	additivePrice: { required: true }
        }, submitHandler: function (form) {
            var data = getEelementData("#divEdit");
            //sid:4
            //sname:Los Angeles 总仓
            //auth:100
            
            var checkbox=$("#divEdit :checkbox[name=tran_type]").is(":checked");
			var tran_type=0;
			if(checkbox)
			{
				tran_type=1;
			}
            
            var data = "";
            data += "id=" + $("#divEdit input[name=id]").val();
            data += "&name=" + $("#divEdit input[name=name]").val();
            data += "&alias=" + $("#divEdit input[name=alias]").val();
            data += "&additivePrice=" + $("#divEdit input[name=additivePrice]").val();
            data += "&show_remark=" + $("#divEdit input[name=show_remark]").val();
            data += "&show_type=" + $("#divEdit input[name=show_type]").val();
            data += "&tran_type=" + tran_type;
            
            $("#divEdit [name=authority_id]:checked").each(function () {
                data += "&auth=" + $(this).attr("val");
            });
            $.ajax({
                type: "post",
                url: "/admin/channel/modify_admin",
                data: data,
                success: function (response) {
                    var code = response.code;
                    if (code == "200") {
                        alert("修改成功");
                        $(".cancel").click();
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
    });
});


//提交js对象参数刷新列表数据
function refreshDataGrid(data) {
    $(yundangrid_selector).jqGrid("setGridParam", { postData: data }).trigger("reloadGrid");
}

function update(id) {
	
    var row = jsonData.getData(id);
    getWarehouselist(row);
    clearInputData("#divEdit");
    setInputData("#divEdit", row);
    setSpanText("#divEdit", row);
    
    $("#divEdit select[name='storeMaster']").val(row.storeMaster);
    if(row.tran_type=="1"||row.tran_type==1)
    {
    	$("#divEdit :checkbox[name=tran_type]").attr("checked","checked");
    }
    else
    {
    	$("#divEdit :checkbox[name=tran_type]").removeAttr("checked");
    	
    }
    
    $("#divGrid").hide();
    $("#divEdit").show();
  /*  getWarehouse(row.storeId);
    $("#divEdit [name=authority_id]").each(function () {
        var a = row.authority;
        for (var i = 0; i < a.length; i++) {
            if (a[i].authority_id == $(this).attr("val")) {
                $(this).attr("checked", "checked");
            }
        }
    });*/
}

function showdetail(id) {
    $("#divGrid").hide();
    var row = jsonData.getData(id);
    row.auth = "";
    if (row.authority) {
        for (var i = 0; i < row.authority.length; i++) {
            row.auth += row.authority[i].name + "，";
        }
    }
    setSpanText("#divDetail", row);
    if(row.tran_type=="1"||row.tran_type==1)
    {
    	
    }
    $("#divDetail").show();
}

function del(id) {
    
    window.confirmDialog("确定删除选中的渠道?如果删除，所有此渠道的运单将无法找到渠道相关数据及其商品信息！", "删除提示", function () {
        $.ajax({
            type: "post",
            url: "/admin/channel/delete_admin",
            data: {id:id},
            success: function (response) {
                var code = response.code;
                if (code == "200") {
                    alert("删除成功");
                    
                    refreshDataGrid(postData);
                } else if (code == "607") {
                    alert("您尚未登录或登录已失效！");
                    adminlogout();
                } else {
                    alert("删除失败，失败原因是:" + response.message);
                }
            }
        });
    });
}

function getWarehouse(selectId) {
    $.ajax({
        type: "get",
        url: "/admin/warehouse/all",
        //data: { id: id },
        success: function (response) {
            var code = response.code;
            if (code == "200") {
                var data = response.data;
                var options = "";
                for (var i = 0; i < data.length; i++) {
                    if (data[i].id == selectId) {
                        options += '<option value="'+data[i].id+'" selected="selected">'+data[i].name+'</option>';
                    } else {
                        options += '<option value="' + data[i].id + '">' + data[i].name + '</option>';
                    }
                }
                $("select[name=storeId]").html(options);
            } else if (code == "607") {
                alert("您尚未登录或登录已失效！");
                adminlogout();
            } else {
                alert("获取仓库信息失败，失败原因是:" + response.message);
            }
        }
    });
}

function getWarehouselist(row) {
    $.ajax({
        type: "get",
        url: "/admin/warehouse/all",
        //data: { id: id },
        success: function (response) {
            var code = response.code;
            if (code == "200") {
                var data = response.data;
                //var options = "";
               
            var str="";
                for (var i = 0; i < data.length; i++) {
                	//显示门店列表
                	str+='<div style="float:left;margin-right:20px;">';
                	str+='<input id="store_'+data[i].id+'" name="authority_id" type="checkbox" val="'+data[i].id+'">';
                	str+='<label for="store_'+data[i].id+'">'+data[i].name+'</label>';
                	str+='</div>';
                	
                	
                   
                }
                if(removenull(row)=="")//添加操作
                {
                	
                	$("#divAdd #storeList_add").html(str);
                }
                else//修改操作
                {
                	$("#divEdit #storeList_edit").html(str);
                	if(removenull(row.storeList)!="")
                	{
                	
                		var list=row.storeList.split(";");
                		$.each(list,function(){
                			$("#divEdit #storeList_edit #store_"+this).attr("checked","checked");
                		});
                		
                	}
                	
                }
            } else if (code == "607") {
                alert("您尚未登录或登录已失效！");
                adminlogout();
            } else {
                alert("获取仓库信息失败，失败原因是:" + response.message);
            }
        }
    });
}



