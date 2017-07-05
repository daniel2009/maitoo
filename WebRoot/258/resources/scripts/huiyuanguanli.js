

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
        url: "/admin/user/search_admin",
       // url: "testdata/huiyuandata.html",
        datatype: "json",
        height: "auto",
        mtype: "get",
        //编号	妮称	真实姓名	联系电话		QQ	注册时间	预存款	用户类型	操作
        colNames: ['编号',  '真实姓名', '联系电话','妮称',  "注册时间", "余额(rmb/usd)", "会员等级", "操作"],
        colModel: [
            { name: 'id', index: 'id', width: 50, },
            
             { name: 'realName', index: 'realName', width: 80 },
              { name: 'phone', index: 'phone', width: 100 },
              { name: 'nickName', index: 'nickName', width: 80 },
             { name: 'signDate', index: 'signDate', width: 100 },
             {
                 name: 'rmbBalance', index: 'rmbBalance', width: 80, formatter: function (cellValue, option, row) {
                     return "￥"+row.rmbBalance + "/$" + row.usdBalance;
                 }
             },
            {
                name: 'type', index: 'type', width: 80, formatter: function (cellValue, option, row) {
                    if (cellValue == "0") {
                        return "普通会员";
                    } else if (cellValue == "1") {
                        return "银牌VIP会员";
                    }
                    else if (cellValue == "2") {
                        return "黄金VIP会员";//VIP会员
                    }
                    else if (cellValue == "3") {
                        return "白金VIP会员";//VIP会员
                    }
                    else if (cellValue == "4") {
                        return "钻石VIP会员";//VIP会员
                    }
                    else if(cellValue=="5")
                	{
                		return "至尊VIP1会员";
                	}
                	else if(cellValue=="6")
                	{
                		return "至尊VIP2会员";
                	}
                	else if(cellValue=="7")
                	{
                		return "至尊VIP3会员";
                	}
                	else if(cellValue=="8")
                	{
                		return "至尊VIP4会员";
                	}
                    else {
                        return "未知";
                    }
                }
            },

             {
                 name: 'op', index: 'op', width: 150, formatter: function (cellValue, option, row) {
                     var login = "<button style='margin-left:5px;margin-top:1px;' class='btn btn-purple btn-sm' type='button'  onclick=\"login('" + row.id + "')\"  ><span class='icon-unlock'></span>登录</button>";
                     var edit = "<button  style='margin-left:5px;margin-top:1px;' class='btn btn-purple btn-sm' type='button'  onclick=\"update('" + row.id + "')\"  ><span class='icon-edit'></span>修改</button>";
                     var del = "<button style='margin-left:5px;margin-top:1px;' class='btn btn-danger btn-sm' type='button'  onclick=\"del('" + row.id + "')\"  ><span class='fa fa-times'></span>删除</button>";
                     return edit + del + login;
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

        multiselect: true,
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
        caption: "会员列表",
        autowidth: true
    });


    //navButtons
    jQuery(yundangrid_selector).jqGrid('navGrid', yundanpager_selector,
        { 	//navbar options
            edit: false,
            editicon: 'icon-pencil blue',
            add: false,
            addicon: 'icon-plus-sign purple',
            del: true,
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
                    idData = idData + "id=" + ids[i]+"&";
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
        $("#divGrid").hide();
        $("#divAdd").show();
    });
    
    $(".cancel").click(function () {
        $("#divGrid").show();
        $("#divAdd").hide();
        $("#divEdit").hide();
    });

    //点击高级搜索
    $("#btnAdvanceSearch").click(function () {
        var data = getEelementData("#divAdvanceSearch");
        refreshDataGrid(data);
    });
    
    $("#divAdvanceSearch #userId").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnAdvanceSearch").click();
        }
    }));
    $("#divAdvanceSearch #keyword").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnAdvanceSearch").click();
        }
    }));

    //添加航班表单验证并提交,选择器的内容一定要是form表单，里面一定要有一个submit的按钮
    $("#divAdd form").validate({
        //验证规则
        //http://www.runoob.com/jquery/jquery-plugin-validate.html
        rules: {
            phone: { required: true, minlength: 10 },
            password: { required: true, minlength: 6 },
            type: { required: true }
        }, submitHandler: function (form) {
            var data = getEelementData("#divAdd");
            data.name = data.phone;
            $.ajax({
                type: "post",
                url: "/admin/user/add",
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
            phone: { required: true, minlength: 10 },
            type: { required: true }
        }, submitHandler: function (form) {
            var data = getEelementData("#divEdit");

            $.ajax({
                type: "post",
                url: "/admin/user/add",
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
    //"phoneState":"0","emailState":"0","regType":"0"
   // if (row.phoneState == 0) { row.phoneState = "未验证" } else { row.phoneState = "已验证"; }
    //if (row.emailState == 0) { row.emailState = "未验证" } else { row.emailState = "已验证"; }
    if (row.regType == 0) {
        row.regType = "电话";
    } else {
        row.regType = "email";
    }
    setSpanText("#divEdit",row);
    $("#divEdit select[name='type']").val(row.type);
    $("#divGrid").hide();
    $("#divEdit").show();
}

function del(id) {
    var idData = "";
    if (id * 1 > 0) {// 单个删除
        idData = "id=" + id;
    } else {
        idData = id;//批量删除 ，拼接为 id=1&id=2的字符
    }
    window.confirmDialog("确定删除选中的会员?", "删除提示", function () {
        $.ajax({
            type: "post",
            url: "/admin/user/del",
            data: idData,
            success: function (response) {
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
    });
}

function login(id) {
	
	$.ajax({
		type : "post",
		url : "/admin/user/loginAsUser",
		data : {
			"id" : id
		},
		success : function(response){
			var code = response.code;
			if ("200" == code) {
				alert("登陆成功");
				//top.location.href = "/258/user/index.html";
				 window.open("/258/user/index.html");
			} else if ("607" == code) {
				alert("您尚未登录或登录已失效！");
				logout();
			} else {
				alert("以会员方式登陆失败，失败原因:" + response.message);
			}
			return false;
		}
	});
}



