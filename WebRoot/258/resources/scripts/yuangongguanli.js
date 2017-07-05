

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
        url: "/admin/emp/search_admin",
        datatype: "json",
        height: "auto",
        mtype: "get",
        //员工ID	门市编号	门市名称	权限	操作
        colNames: ['员工ID','员工用户名', '门市编号', '门市名称','店长', '权限', "操作"],
        colModel: [
               {
                   name: 'id', index: 'id', width: 70, fix:true, formatter: function (cellValue, option, row) {
                       return '<a onclick="showdetail(' + row.id + ')">' + cellValue + '</a>';
                   }
               },
            {
                name: 'account', index: 'account', width: 70, fix:true, formatter: function (cellValue, option, row) {
                    return '<a onclick="showdetail(' + row.id + ')">' + cellValue + '</a>';
                }
            },
             { name: 'storeId', index: 'storeId', width: 60, fix: true },
             { name: 'storeName', index: 'storeName', width: 80 ,fix:true },


             
             {
                 name: 'storeMaster', index: 'storeMaster', width: 50, formatter: function (cellValue, option, row) {
                    if(cellValue=="1")
                    {
                    	return "是";
                    }
                    else
                    {
                    	return "否";
                    }
                     
                 }
             },
             
             {
                 name: 'rmbBalance', index: 'rmbBalance', width: 260, formatter: function (cellValue, option, row) {
                     var auth = "";
                     if (row.authority) {
                         for (var i = 0; i < row.authority.length; i++) {
                             auth += row.authority[i].name + ",";
                         }
                     }
                     return auth;
                 }
             },

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
        caption: "员工列表",
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
        })
    }


    $("#btnAdd").click(function () {
    	
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
            password: { required: true, minlength: 6, maxlength: 20 },
            account: { required: true, minlength: 2,maxlength:20 },
           
        }, submitHandler: function (form) {
            //account:admin
            //password:111111
            //sid:4
            //sname:Los Angeles 总仓
            //phone:
            //auth:
            var data = "";
            data += "account=" + $("#divAdd input[name=account]").val();
            data += "&password=" + $("#divAdd input[name=password]").val();
            data += "&phone=" + $("#divAdd input[name=phone]").val();
            data +="&sid="+ $("#divAdd [name=storeId]").val();
            data += "&sname=" + $("#divAdd [name=storeId]").find("option:selected").text();
            data += "&storeMaster=" + $("#divAdd [name=storeMaster]").val();
            
            $("#divAdd [name=authority_id]:checked").each(function () {
                data += "&auth=" +$(this).attr("val");
            });
            $.ajax({
                type: "post",
                url: "/admin/emp/add_no_pic",
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
            //phone: { required: true, minlength: 10 },
            storeId: { required: true }
        }, submitHandler: function (form) {
            var data = getEelementData("#divEdit");
            //sid:4
            //sname:Los Angeles 总仓
            //auth:100
            var data = "";
            data += "id=" + $("#divEdit input[name=id]").val();
            data += "&password=" + $("#divEdit input[name=password]").val();
            data += "&phone=" + $("#divEdit input[name=phone]").val();
            data += "&sid=" + $("#divEdit [name=storeId]").val();
            data += "&sname=" + $("#divEdit [name=storeId]").find("option:selected").text();
            data += "&storeMaster=" + $("#divEdit [name=storeMaster]").val();

            $("#divEdit [name=authority_id]:checked").each(function () {
                data += "&auth=" + $(this).attr("val");
            });
            $.ajax({
                type: "post",
                url: "/admin/emp/modifybyadmin",
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
    clearInputData("#divEdit");
    setInputData("#divEdit", row);
    setSpanText("#divEdit", row);
    
    $("#divEdit select[name='storeMaster']").val(row.storeMaster);
    $("#divGrid").hide();
    $("#divEdit").show();
    getWarehouse(row.storeId);
    $("#divEdit [name=authority_id]").each(function () {
        var a = row.authority;
        for (var i = 0; i < a.length; i++) {
            if (a[i].authority_id == $(this).attr("val")) {
                $(this).attr("checked", "checked");
            }
        }
    });
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
    
    window.confirmDialog("确定删除选中的员工?", "删除提示", function () {
        $.ajax({
            type: "post",
            url: "/admin/emp/delete",
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





