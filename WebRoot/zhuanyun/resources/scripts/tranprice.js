

var yundangrid_selector = "#yundangrid-table";
var yundanpager_selector = "#yundangrid-pager";
//查询数据时向后台提交的参数
var postData = {};
var jsonData = [];
var init = false;
//初始列表数据
$(function ($) {

 
    $(window).resize(function () {
        $(yundangrid_selector).setGridWidth($(".page-content").width() - 20, true);
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
    
    initZhanghuList();
});

function initZhanghuList() {
    jQuery(yundangrid_selector).jqGrid({
        url: "/t_order/get_tranprice",
        //url: "testdata/zhanghudata.html",
        datatype: "json",
        height: "auto",
        mtype: "get",
        //用户编号	用户名	账户余额	充值金额	货币类型	备注	操作
        colNames: ['所属门店','成本', '转运价格','自提价格','修改时间',"操作"],
        //{"id":"1205","nickName":"233232","realName":"ken","phone":"233232","email":null,"qq":null,"recommender":null,"signDate":"2016-01-15 13:59:28","type":"1","status":"0","country":null,"address":null,"empaccount":null,"rmbBalance":"0.00","usdBalance":"0.00","usercode":"451574","useralias":"BCZYEM","groupId":null,"phoneState":"0","emailState":"0","regType":"0","modifyDate":null,"createDate":null},
        colModel: [
            {
                name: 'storeName', index: 'storeName', width: 100
            },
            { name: 'cost', index: 'cost', width: 50,formatter: function (cellValue, option, row){
            	return "<input name='tran_cost_"+row.storeId+"' value='"+row.cost+"'>";
            } },
            { name: 'price', index: 'price', width: 50,formatter: function (cellValue, option, row){
            	return "<input name='tran_price_"+row.storeId+"' value='"+row.price+"'>";
            } },
            { name: 'self_price', index: 'self_price', width: 50,formatter: function (cellValue, option, row){
            	return "<input name='tran_self_price_"+row.storeId+"' value='"+row.self_price+"'>";
            } },
            { name: 'modifyDate', index: 'modifyDate', width: 100 },



             {
                 name: 'op', index: 'op', width: 70, formatter: function (cellValue, option, row) {
                     var chongzhi = "<button style='margin-left:5px;margin-top:1px;' class='btn btn-purple btn-sm' type='button'  onclick=\"update('" + row.storeId + "')\"  ><span class='icon-credit-card'></span>修改</button>";
                     return chongzhi;
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
        rowNum: 1000,
       // rowList: [10, 30, 50],
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
        caption: "账户列表",
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
}


//提交js对象参数刷新列表数据
function refreshDataGrid(data) {
    $(yundangrid_selector).jqGrid("setGridParam", { postData: data }).trigger("reloadGrid");
}



function update(storeId) {
    var data = {};
   
    
    data.cost = $(yundangrid_selector).find("input[name='tran_cost_"+storeId+"']").val();
    data.price = $(yundangrid_selector).find("input[name='tran_price_"+storeId+"']").val();
    data.self_price = $(yundangrid_selector).find("input[name='tran_self_price_"+storeId+"']").val();
    data.storeId = storeId;

    if (data.cost <= 0||isNaN(parseFloat(data.cost))) { alert("成本不能小于等于0"); return; }
    if (data.price <= 0||isNaN(parseFloat(data.price))) { alert("转运价格不能小于等于0"); return; }
    if (data.self_price <= 0||isNaN(parseFloat(data.self_price))) { alert("自提价格不能小于等于0"); return; }
    $.ajax({
        type: "post",
        url: "/t_order/tranprice_modify",
        data: data,
        success: function (response) {
            var code = response.code;

            if ("200" == code) {
                alert("修改成功");
                refreshDataGrid(postData);
            } else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                top.location.href = "../admin/login.html";
            }
            else {
                alert("修改出错，原因：" + response.message);
            }

        }
    });

}

function back() {
    $("#divGrid").show();
    $("#divChongzhiRecordGrid").hide();
}

// 显示详细
function showdetail(id) {
    var row = jsonData.getData(id);
    setSpanText("#senderTableInfo", row.suser);
    $("#divGrid").hide();
    $("#divChongzhiRecordGrid").show();
    initChongzhiRecordGrid(id);
}


var inited = false;
//查看详情中的充值记录
function initChongzhiRecordGrid(userId) {
    jQuery("#chongzhiRecordGrid").jqGrid({
         url: "/admin/account/get_one?userId="+userId,
        //url: "testdata/yuchongzhigetone.html",
        datatype: "json",
        height: "auto",
        mtype: "get",
        //名称	操作人	操作店面	金额	货币类型	操作类型	状态	最近更新时间	备注
        colNames: ['名称', '操作人', '操作店面', '金额', '货币类型', "操作类型", "状态", "最近更新时间", "备注"],
  
        colModel: [
             { name: 'name', index: 'name', width: 70 },
            {
                name: 'empName', index: 'empName', width: 50, formatter: function (cellvalue, options, row) {
                    return cellvalue == null ? "" : cellvalue;
                }
            },
            {
                name: 'empStore', index: 'empStore', width: 60, formatter: function (cellvalue, options, row) {
                    return cellvalue == null ? "" : cellvalue;
                }
            },
            { name: 'amount', index: 'amount', width: 50 },
            { name: 'currency', index: 'currency', width: 50 },
            {
                name: 'type', index: 'type', width: 80, formatter: function (cellvalue, options, row) {
                    var str = "";
                    if (row.type == '13') {
                        str += "后台管理员账户操作";
                    } else if (row.type == '11') {
                        str += "信用卡充值";
                    } else if (row.type == '12') {
                        str += "人民币充值";
                    } else if (row.type == '2') {
                        str += "消费";
                    } else {
                        str += row.type ;
                    }
                    return str;
                }
            },
            {
                name: 'state', index: 'state', width: 80, formatter: function (cellvalue, options, row) {
                    var str = "";
                    if (cellvalue == '0') {
                        str += "未处理";
                    } else if (cellvalue == '1') {
                        str += "处理完成";
                    } else if (cellvalue == '2') {
                        str += "处理失败";
                    } else {
                        str +=cellvalue ;
                    }

                    return str;
                }
            },
            { name: 'modifyDate', index: 'modifyDate', width: 100 },
            
            {
                name: 'remark', index: 'remark', width:120
            }
        ],

        jsonReader: {
            root: "data.details",
            page: "data.pageNow",
            total: "data.pageCount",
            records: "data.rowCount"
        },
        viewrecords: true,
        rowNum: 1000,
        rowList: [10, 30, 50],
        pager: "",
        altRows: true,

        multiselect: false,
        //multikey: "ctrlKey",
        multiboxonly: true,

        loadComplete: function (data) {
            var table = this;
           

            if (data.code == "200") {
                var account = '￥' + data.data.rmb + '&nbsp;&nbsp;/&nbsp;&nbsp;$' + data.data.usd;
                var userinfo = data.data.user.phone + (data.data.user.realName == "" ? "" : "/" + data.data.user.realName);
                $("#account").html(account);
                $("#userinfo").html(userinfo);
                
            }
            else if ("607" == data.code) {
                alert("您尚未登录或登录已失效！");
                top.location.href = "../admin/login.html";
            }
            else {
                alert("读取出错，原因：" + data.message);
            }



            //防止出现水平滚动条
            if (inited == false) {
                $(".ui-jqgrid-bdiv").css({ 'overflow-x': 'hidden' });
                $(".ui-jqgrid-htable th:last").css({ "width": $(".ui-jqgrid-htable th:last").width() - 2 });
                $(".jqgfirstrow td:last").css({ "width": $(".jqgfirstrow td:last").width() - 2 });
                inited = true;
            }
        },
        gridComplete: function () {

        },
        onSelectRow: function (rowid, status) {

        },
        onSelectAll: function (aRowids, status) {

        },
        caption: "账户操作记录",
        autowidth: true
    });

}




