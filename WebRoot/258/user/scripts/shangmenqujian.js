
var addressgrid_selector = "#addressgrid-table";
//查询数据时向后台提交的参数
var postData = {};
var jsonData;
//初始列表数据
$(function ($) {

    var addresspager_selector = "#addressgrid-pager";
    var init = false;
    $(window).resize(function () {
        $(addressgrid_selector).setGridWidth($(".page-content").width() - 2, true);
    });

    jQuery(addressgrid_selector).jqGrid({
        // url: "testdata/shangmenqujian.html",
        url: "/sm_order/user_search",
        datatype: "json",
        height: "auto",
        mtype: "get",

        colNames: ['门店', '预约时间', '确认时间', "状态", "寄件人", "数量", "备注", "操作"],
        colModel: [

            {
                name: 'storeName', index: 'storeName', width: 100
            },
            { name: 'appointmentDate', index: 'appointmentDate', width: 100 },
            { name: 'confirmDate', index: 'confirmDate', width: 100 },

            {
                name: 'state', index: 'state', width: 60, formatter: function (cellValue, option, row) {
                    var state = cellValue;
                   // alert(state);
                    switch (state) {
                    	case "0":
                    		return "未处理";
                        case "1":
                            return "处理中";
                        case "2":
                            return "处理完成";
                        default:
                            return "<span style='color:red;'>异常</span>";
                    
                    }
                }
            },
             { name: 'name', index: 'name', width: 60 },
              { name: 'quantity', index: 'quantity', width: 30 },
               { name: 'remark', index: 'remark', width: 120 },

             {
                 name: 'op', index: 'op', width: 120, formatter: function (cellValue, option, row) {
                     var d = "<button  style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"showDetail('" + row.id + "')\"  ><span class='icon-edit'></span>详情</button>";
                     var c="";
                     if(row.state=="1")
                     {
                    	 c="";
                     }
                     else
                    {
                      c = "<button style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"cancelCallOrder('" + row.id + "')\"  ><span class='fa fa-times'></span>删除</button>";
                    }
                      return d + c;
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
        pager: addresspager_selector,
        altRows: true,

        multiselect: false,
        //multikey: "ctrlKey",
        multiboxonly: true,

        loadComplete: function (data) {
            var table = this;
            
            if(data.code=="200")
            {
            	if(data.data!=null)
            	{
            		jsonData = data.data.datas;
            	}
            }
           
            	
            setTimeout(function () {
                updatePagerIcons(table);
            }, 0);
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


    //navButtons
    jQuery(addressgrid_selector).jqGrid('navGrid', addresspager_selector,
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
        postData = getEelementData("#divToolbar");
        $(addressgrid_selector).jqGrid("setGridParam", { postData: postData }).trigger("reloadGrid");
    });
    //绑定输入框的enter事件进行搜索
    $("#keyword").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnSearch").click();
        }
    }));

    $("#btnAdd").click(function () {
        $("#divAdd").show();
        $("#divToolbar").hide();
        $("#divGrid").hide();
        initWarehouse();
       
    });

    $("#cancel").click(function () {
        $("#divAdd").hide();
        $("#divToolbar").show();
        $("#divGrid").show();
        clearInputData("#divAdd");
    });

    $("#close").click(function () {
        $("#divDetail").hide();
        $("#divList").show();
    });


});

function initWarehouse() {
    var select = $("#warehouseSelect");
    $.ajax({
        type: "get",
        url: "/user/warehouse/all",
        success: function (response) {
            var code = response.code;
            if (code == "200") {

                var data = response.data;
                var options = "";
                
                for (var i = 0; i < data.length; i++) {
                    var row = data[i];
                    if(row.callOrderAvailable=="1")
                    {
                    	options += ' <option value="' + row.id + '" country="' + row.country + '" province="' + row.province + '" callordercity="' + row.callOrderCity + '">' + row.name + '</option>'
                
                    }
                }
                if(options=="")
                {
                	alert("对不起，暂时开能上门收货业务!");
                	window.location.href="index.html";
                	return false;
                }
                select.html(options);
                changeWarehouseSelect();
                init_to_sender();
                
                getselfsetting();
                
            } else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                userlogout();
            } else {
                alert("获取仓库失败，原因是:" + response.message);
            }
        }
    });
}

function cancelCallOrder(id) {
    top.window.confirmDialog("确定删除预约？", "取消提示", function () {
        $.ajax({
            type: "post",
            url: "/sm_order/user_delete",
            data: {
                id: id
            },
            success: function (response) {
                var code = response.code;
                if (code == "200") {
                    alert("取消成功");
                    refreshDataGrid(postData);
                } else if ("607" == code) {
                    alert("您尚未登录或登录已失效！");
                    userlogout();
                } else {
                    // 失败
                    alert("删除信息失败，原因是:" + response.message);
                }
            }
        });
    });
}
function changeWarehouseSelect() {
    var option = $("#warehouseSelect option:selected");
    $("#warehouseCountry").html(option.attr("country"));
    $("#warehouseProvince").val(option.attr("province"));

    $("#swarehouseCountry").val(option.attr("country"));
    $("#swarehouseProvince").val(option.attr("province"));

    var addOption = "<option>";
    if (option.attr("callOrderCity").replace(/\s+/g, "") != "") {
        addOption += option.attr("callOrderCity").replace(/[\n\r]+/g, "</option><option>");
    }
    addOption += "</option>";
    var regExp = new RegExp("(<option></option>)+");
    addOption = addOption.replace(regExp, "");
    $("#warehouseCallOrderCity").html(addOption);
}


//添加

$(function () {
    //添加表单验证并提交,选择器的内容一定要是form表单，里面一定要有一个submit的按钮
    $("form").validate({
        //验证规则
        //http://www.runoob.com/jquery/jquery-plugin-validate.html
        debug: true,//不提交表单
        rules: {
            name: { required: true },
            appointmentDate: { required: true },
            storeId: { required: true },

            city: { required: true },
            address: { required: true },
            zipCode: { required: true },
            phone: { required: true },
            quantity: { required: true, number: true, range: [1, 1000] },
            weight: { required: true, number: true }
        },
        submitHandler: function (form) {
            var data = getEelementData("form");

            $.ajax({
                type: "post",
                url: "/sm_order/user_add",
                data: data,
                success: function (response) {
                    var code = response.code;
                    if (code == "200") {
                        alert("预约成功");
                        //隐藏新增div
                        $("#divAdd").hide();
                        $("#divToolbar").show();
                        $("#divGrid").show();
                        //清空输入框
                        clearInputData("#divAdd");
                        //刷新列表数据
                        refreshDataGrid(postData);
                    } else if ("607" == code) {
                        alert("您尚未登录或登录已失效！");
                        userlogout();
                    } else {
                        // 失败
                        alert("预约失败，原因是:" + response.message);
                    }
                }
            });
        }
    });
})


//提交js对象参数刷新列表数据
function refreshDataGrid(data) {
    $(addressgrid_selector).jqGrid("setGridParam", { postData: data }).trigger("reloadGrid");
}

//显示详情
function showDetail(id) {
    $("#divList").hide();
    $("#divDetail").show();
    var row = jsonData.getData(id);
    setSpanText("#divDetail", row);
}



function init_to_sender()
{
	//获取用户信息
    $.ajaxEx({
        type: "get",
        url: "/user/get_self",
        success: function (response) {
            var code = response.code;
            if (code == "200") {
            	if((removenull(response.data.realName)=="")||(removenull(response.data.phone)==""))
            	{
            		alert("完善资料后才能够在线置单!");
            		window.location.href="userifnoedit.html";
            		return false;
            	}
            	//setInputData("#divAdd", response.data);
                $("#divAdd").find("input[name='name']").val(removenull(response.data.realName));
                $("#divAdd").find("input[name='phone']").val(removenull(response.data.phone));
            	
            	
            
            } else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                userlogout();
            } else {

                alert("获取个人信息失败，原因是:" + response.message);
            }
        }
    });
}

//获取数据
function getselfsetting()
{
	 $.ajax({
	        type: "post",
	        url: "/user/user_setting/getself",
	        success: function (response) {
	            var code = response.code;
	            
	            if ("200" == code) {
	            	//获取成功
	            	if(response.data!=null)
	            	{
	            		$("#warehouseSelect").val(response.data.s_store);
	            	}
	            } else if ("607" == code) {
	                alert("您尚未登录或登录已失效！");
	                userlogout();
	            }else
	            {
	            	
	            	
	            }
	        }
	    });
}
