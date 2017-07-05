

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
        url: "/admin/Shelves/search",
        datatype: "json",
        height: "auto",
        mtype: "get",
        //员工ID	门市编号	门市名称	权限	操作
        colNames: ['所属门店','货架名称','货架编号', '备注','总仓位数', '空闲仓位数','已用仓位数',"创建时间","操作"],
        colModel: [
               {
                   name: 'storeName', index: 'storeName', width: 60, fix:true
               },
               {
                   name: 'nickName', index: 'nickName', width: 60, fix:true
               },
            {
                name: 'shelvesNo', index: 'shelvesNo', width: 30, fix:true
            },
           
             { name: 'remark', index: 'remark', width: 80, fix: true },
             { name: 'wNo', index: 'wNo', width: 30 ,fix:true },
             { name: 'unused_wNo', index: 'unused_wNo', width: 30 ,fix:true },

             { name: 'used_wNo', index: 'used_wNo', width: 30 ,fix:true },
             { name: 'createDate', index: 'createDate', width: 60 ,fix:true },
             {
                 name: 'op', index: 'op', width: 100, fix: true, formatter: function (cellValue, option, row) {
                     var edit = "<button  style='margin-left:5px;margin-top:1px;' class='btn btn-purple btn-sm' type='button'  onclick=\"update('" + row.id + "')\"  ><span class='icon-edit'></span>修改</button>";
                     var check = "<button  style='margin-left:5px;margin-top:1px;' class='btn btn-purple btn-sm' type='button'  onclick=\"check('" + row.id + "')\"  ><span class='icon-edit'></span>仓位</button>";
                     var del = "<button style='margin-left:5px;margin-top:1px;' class='btn btn-danger btn-sm' type='button'  onclick=\"del('" + row.id + "')\"  ><span class='fa fa-times'></span>删除</button>";
                     return edit +check+ del;
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
        caption: "货架列表",
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

    loadstoreseachinfo();
    $("#btnAdd").click(function () {
    	
    	
    	
    	//loadstoreseachinfo();
        clearInputData("#divAdd");
        $("#divAdd select[name='add_type']").val("0");
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
        	shelvesNo: { required: true, minlength: 1,maxlength:1 },
        	wNo: { required: true, minlength: 1,maxlength:4 },
        	remark: { required: true, minlength: 10},
           
        }, submitHandler: function (form) {
            //account:admin
            //password:111111
            //sid:4
            //sname:Los Angeles 总仓
            //phone:
            //auth:
        	var shelvesNo=$("#divAdd input[name=shelvesNo]").val();
        	if(removenull(shelvesNo)==""||shelvesNo.trim().length<1)
        	{
        		alert("货架编号不能为空!");
        		return false;
        	}
        	
        	var wNo=$("#divAdd input[name=wNo]").val();
        	if(removenull(wNo)==""||wNo.trim().length<1)
        	{
        		alert("货架编号不能为空!");
        		return false;
        	}
        	else
        	{
        		if (isNaN(parseFloat(wNo))||(parseFloat(wNo)<=0))
        		{
        			alert("仓位数量必须大于0!");
            		return false;
        		}
        		else if(parseFloat(wNo)>1000)
        		{
        			alert("仓位数量必须小于等于1000!");
            		return false;
        		}
        	}
        	
        	var storeId=$("#divAdd select[name=storedId]").val();
        	if(removenull(storeId)==""||storeId=="-1")
        	{
        		alert("必须选择所属门店!");
        		return false;
        	}
        	
        	var remark=$("#divAdd textarea[name=remark]").val();
        	if(removenull(remark)==""||remark.length<10)
        	{
        		alert("备注信息必须填写并且至少10个字!");
        		return false;
        	}
        	
        	var nickName=$("#divAdd input[name='nickName']").val();
        	if(removenull(nickName)=="")
        	{
        		alert("必须填写货架名称");
        		return false;
        	}
        	
            var data = "";
            data += "shelvesNo=" + shelvesNo;
            data += "&wNo=" + wNo;
            data += "&storeId=" + storeId;
            data += "&remark=" + remark;
            data += "&nickName=" + nickName;
            
            $("#addresult").html("初始化中。。。");
            
            $.ajax({
                type: "post",
                url: "/admin/Shelves/add",
                data: data,
                success: function (response) {
                	$("#addresult").html("");
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
        	remark: { required: true, minlength: 10},
        	nickName: { required: true, minlength: 1}
    
        }, submitHandler: function (form) {
          
            //sid:4
            //sname:Los Angeles 总仓
            //auth:100
        	if(removenull($("#divEdit textarea[name=remark]").val())==""||$("#divEdit textarea[name=remark]").val().length<10)
        	{
        		alert("备注必须大于10个字符!");
        		return false;
        	}
        	var nickName=$("#divEdit input[name='nickName']").val();
        	if(removenull(nickName)=="")
        	{
        		alert("必须填写货架名称");
        		return false;
        	}
            var data = "";
            data += "id=" + $("#divEdit input[name=id]").val();
            data += "&remark=" + $("#divEdit textarea[name=remark]").val();
            data += "&nickName=" + nickName;
           
            $.ajax({
                type: "post",
                url: "/admin/Shelves/modify_remark",
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
    loadstoreseachinfo(row);
    clearInputData("#divEdit");
    setInputData("#divEdit", row);
    setSpanText("#divEdit", row);
    
    $("#divEdit select[name='add_type']").val(row.type);
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
    $("#divDetail").show();
}

function del(id) {
    
    window.confirmDialog("确定删除选中的货架?仅当所有的仓位为空时才能够删除！", "删除提示", function () {
        $.ajax({
            type: "post",
            url: "/admin/Shelves/delete",
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

//装载上门门店
function loadstoreseachinfo() {

	$.ajax({
		type : "post",
		url : "/admin/warehouse/getself",
		success : function(response) {
			var code = response.code;

			if ("200" == code) {

				var str = "";
				if (response.data != null) {
					storelist_jason = response.data;
					if (response.data.length == 1) {
						// alert(response.data.id);
						//str = "<option value='" + response.data[0].id + "'>"
						//		+ response.data[0].name + "</option>";
						
						str += ' <option value="' + response.data[0].id + '" country="' + response.data[0].country + '" province="' + response.data[0].province + '" callordercity="' + response.data[0].callOrderCity + '">' + response.data[0].name + '</option>';
						/*
						 * $.each(response.data, function() { str+="<option
						 * value='"+this.id+"'>"+this.name+"</option>"; });
						 */
					} else {
						str = "<option value='" + "-1" + "'>" + "请选择门店"
								+ "</option>";
						$.each(response.data, function() {
							str +=  ' <option value="' + this.id + '">' + this.name + '</option>';
						});
					}
					$("select[name='storedId']").html(str);
					$("select[name='s_storeId']").html(str);
					
					
					
					//getchannelsbystores();
					//$("select[name='select_stores']").change(
						//	getchannelsbystores);
					//changeWarehouseSelect();
					//changeCitySelect();
				} else {

				}
			} else if ("607" == code) {
				alert("您尚未登录或登录已失效！");
				top.location.href = "../admin/login.html";
			} else {
				alert("读取门店信息出错，原因：" + response.message);
			}

		}
	});
}

function check(id)
{
	var row = jsonData.getData(id);
	document.location.href="shelves_wno.html?id="+id+"&storeName="+row.storeName+"&nickName="+row.nickName+"&shelvesNo="+row.shelvesNo+"&remark="+row.remark;
	
	
	
	
}


