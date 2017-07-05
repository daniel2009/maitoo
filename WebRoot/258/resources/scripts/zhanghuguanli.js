

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
        url: "/admin/user/search_admin",
        //url: "testdata/zhanghudata.html",
        datatype: "json",
        height: "auto",
        mtype: "get",
        //用户编号	用户名	账户余额	充值金额	货币类型	备注	操作
        colNames: ['用户编号','电话', '姓名','邮箱', '会员等级','余额(rmb/usd)', '充值金额', '货币类型', "备注", "操作"],
        //{"id":"1205","nickName":"233232","realName":"ken","phone":"233232","email":null,"qq":null,"recommender":null,"signDate":"2016-01-15 13:59:28","type":"1","status":"0","country":null,"address":null,"empaccount":null,"rmbBalance":"0.00","usdBalance":"0.00","usercode":"451574","useralias":"BCZYEM","groupId":null,"phoneState":"0","emailState":"0","regType":"0","modifyDate":null,"createDate":null},
        colModel: [
            {
                name: 'id', index: 'id', width: 50, formatter: function (cellvalue, options, row) {
                    return '<a href="javascript:void(0)" onclick="showdetail('
                            + row.id
                            + ')">'
                            + cellvalue
                            + '</a>';
                }
            },
            { name: 'phone', index: 'phone', width: 100 },
            { name: 'realName', index: 'realName', width: 100 },
            { name: 'email', index: 'email', width: 100 },
            {
                name: 'type', index: 'type', width: 80, formatter: function (cellValue, option, row) {
                    if (cellValue == "0") {
                        return "普通会员";
                    } else if (cellValue == "1") {
                        return "白银VIP会员";
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
                name: 'rmbBalance', index: 'rmbBalance', width: 80, formatter: function (cellValue, option, row) {
                    return row.rmbBalance + "/" + row.usdBalance;
                }
            },
            {
                name: 'realAmount', index: 'realAmount', width: 60, formatter: function (cellValue, option, row) {
                    return '<input name="realAmount" type="number" placeholder="" />';
                }
            },
             {
                 name: 'type', index: 'type', width: 80, formatter: function (cellValue, option, row) {
                     var select = '<select style="width:100px;" class="text-input" name="currency"><option selected="selected" value="11">美元</option><option value="12">人民币</option></select>';
                     return select;
                 }
             },



            {
                name: 'remark', index: 'remark', width: "150", formatter: function (cellValue, option, row) {
                    return ' <input name="remark" type="text" style="width:90%" placeholder="" />';
                }
            },

             {
                 name: 'op', index: 'op', width: 70, formatter: function (cellValue, option, row) {
                     var chongzhi = "<button style='margin-left:5px;margin-top:1px;' class='btn btn-purple btn-sm' type='button'  onclick=\"chongzhi('" + row.id + "')\"  ><span class='icon-credit-card'></span>充值</button>";
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



function chongzhi(id) {
    var data = {};
    data.userId = id;
    //userId: 1221
    //realAmount: 12
    //currency: 11
    //remark: 11

    data.realAmount = $(yundangrid_selector).find("[id=" + id + "]").find("input[name='realAmount']").val();
    data.currency = $(yundangrid_selector).find("[id=" + id + "]").find("select[name='currency']").val();
    data.remark = $(yundangrid_selector).find("[id=" + id + "]").find("input[name='remark']").val();

    //if (data.realAmount <= 0) { alert("充值金额不能小于等于0"); return; }
    
    
    $.ajax({
        type: "post",
        url: "/admin/msrecharge",
        data: data,
        success: function (response) {
            var code = response.code;

            if ("200" == code) {
                alert("充值成功");
                refreshDataGrid(postData);
            } else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                top.location.href = "../admin/login.html";
            }
            else {
                alert("充值出错，原因：" + response.message);
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




