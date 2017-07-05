
//premium_rate提交保险的比例
//premium_max_value保险的保额最大值
//lowest_weight_value_flag重量的最小值，即多少磅起
//jw_commodity_rate 商品的进位值，即大于等于此值，将会进位计算
//cur_usa_cn 当前的汇率

var default_print_button_id = "";//用于控制回车时选择触发的按钮

var premium_rate = 0;
var premium_max_value = 0;
var lowest_weight_value_flag = 0;
var jw_commodity_rate = 0;
var price_carry_flag = 0;//计算方法
var cur_usa_cn = 0;
function getglobalargs_commrate() {
    var flags = "premium_rate,premium_max_value,lowest_weight_value_flag,jw_commodity_rate,price_carry_flag,cur_usa_cn";//获取商品进位值
    $.ajax({
        type: "post",
        url: "../../admin/globalargs/getcontents",
        data: {
            "flags": flags
        },
        success: function (response) {
            var code = response.code;

            if ("200" == code) {

                if (response.data != null) {
                    premium_rate = response.data[0];	//提交保险的比例
                    premium_max_value = response.data[1];//保险的保额最大值
                    lowest_weight_value_flag = response.data[2];//重量的最小值，即多少磅起
                    jw_commodity_rate = response.data[3];//商品的进位值
                    price_carry_flag = response.data[4];//当前的计算方法
                    cur_usa_cn = response.data[5];//当前的汇率

                    //alert(jw_commodity_rate);
                } else {

                }
            } else {

            }

        }
    });
}


function removenull(str) {
    if ((str == "") || (str == "null") || (str == "undefined") || (str == null)) {
        return "";
    }
    else {
        return str;
    }
}

$(function () {
    $("input[type=checkbox]").bootstrapSwitch();

});

var receiverRowData;

//初始化收件人列表数据
$(function ($) {

    getOrderById();
    seachreceiverinfo();//查找收件人信息
    seachsenderinfo();//查找发件人信息
    //seachusersinfo();//会员信息


    loadChannelSelectOption();//装载渠道信息
    getglobalargs_commrate();//获取全局变量 


    $("#print_online_order").hide();
    $("#usersinfo").click(function () {
        seachusersinfo();//查找会员信息
    });
    $("#senderinfo").click(function () {
        seachsenderinfo();//查找发件人信息
    });

    //$("#btsearchonlineOrder").click(function () {

    //    var orderid = $("#searchonlineOrder").val();
    //    if (removenull(orderid) == "") {
    //        alert("在线置单号不能为空!");
    //        return false;
    //    }
    //    orderid = orderid.trim();
    //    if (orderid.length < 4) {
    //        alert("输入单号长度太短，请重新输入!");
    //        return false;
    //    }
    //    $("#searchshangmenOrder").val("");//清空上门取件运单号
    //    getonlineMorderbyOrderId(orderid, "0");//查找在线置单
    //});

    //在线置单回车处理
    $("#searchonlineOrder").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btsearchonlineOrder").click();
        }
    }));


    //上门取件
    $("#btsearchshangmenOrder").click(function () {

        var orderid = $("#searchshangmenOrder").val();
        if (removenull(orderid) == "") {
            alert("上门取件单号不能为空!");
            return false;
        }
        orderid = orderid.trim();
        if (orderid.length < 5) {
            alert("输入上门取件单号长度太短，请重新输入!");
            return false;
        }
        //清空在线单号文本框
        $("#searchonlineOrder").val("");
        getonlineMorderbyOrderId(orderid, "1");//查找上门取件的在线置单
    });

    //上门取件处理
    $("#searchshangmenOrder").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btsearchshangmenOrder").click();
        }
    }));

    $(":checkbox[name='payway']").bootstrapSwitch('destroy');
    printbuttoncontrol();//默认打印运单的类型及隐藏
    reg_unbind_keydown_print();//取消回车绑定打印的选项
    reg_bind_keydown_print();//回车绑定打印的选项
    history_orders();
});

//添加商品条目
function addGoodsItem() {
    //商品类型回车实现

    var teplament = $("#goodsItemTeplament").html();
    $("#goodsItemBody").append("<tr>" + teplament + "</tr>");


    reg_unbind_keydown_print();//取消回车绑定打印的选项
    reg_bind_keydown_print();//回车绑定打印的选项



}

//删除商品条目
function delGoodsItem(obj) {
    $(obj).parent().parent().remove();
}

//显示隐藏身份证按钮字样变换
function displayReceiverIdTable(source) {
    var table = $("#receiverIdTable");
    if (table.css("display") == "block") {
        table.hide();
        $(source).html(' <span class="ace-icon fa fa-home icon-on-right bigger-110"></span>显示身份证信息');
        if (receiverRowData != null) {

        }
    } else {
        table.show();
        $(source).html(' <span class="ace-icon fa fa-home icon-on-right bigger-110"></span>隐藏身份证信息');
    }
}

//点击上传身份证显示切换

$(function () {

    $("#upload").click(function () {
        $("#uploadshenfenzheng").show();
    });
    $("#hcradio").click(function () {
        $("#uploadhcTd").show();
        $("#uploadzfTd").hide();
    });

    $("#zfradio").click(function () {
        $("#uploadhcTd").hide();
        $("#uploadzfTd").show();
    });
});


//点击查询发件人信息
function seachsenderinfo() {
    $("#gview_usersgrid-table").hide();
    $("#gview_usersgrid-pager").hide();
    $("#gview_sendergrid-table").show();
    $("#gview_sendergrid-pager").show();

    //$("#usersgrid-table").find('div.ui-jqgrid-hdiv').hide();
    //  $(this).closest('.ui-jqgrid-view').find('div.ui-jqgrid-hdiv').show();

    var sendergrid_selector = "#sendergrid-table";
    var senderpager_selector = "#sendergrid-pager";

    $(window).resize(function () {
        $(sendergrid_selector).setGridWidth($("#senderTableInfo").width(), true);
    });
    var send_postData = { info: "" };
    jQuery(sendergrid_selector).jqGrid({
        url: "../../admin/send_user/search",
        datatype: "json",
        mtype: "post",
        postData: send_postData,
        height: 100,
        colNames: ['姓名', '发件人电话', '州', "城市", "区县", "地址", "邮编", "邮箱", "公司名称"],
        colModel: [

            { name: 'name', index: 'name', width: 40 },
            { name: 'phone', index: 'phone', width: 50 },
             { name: 'state', index: 'state', width: 50, hidedlg: true, hidden: true },
             { name: 'city', index: 'city', width: 50 },
             { name: 'dist', index: 'dist', width: 50, hidedlg: true, hidden: true },
             { name: 'address', index: 'address', width: 100, hidedlg: true, hidden: true },
             { name: 'zipcode', index: 'zipcode', width: 40 },
             { name: 'email', index: 'email', width: 50, hidedlg: true, hidden: true },
             { name: 'company', index: 'company', width: 50, hidedlg: true, hidden: true },
        ],
        /*
         * 	total;//总页数
            page;//当前页
            records;//查出的记录数
         * */
        jsonReader: {
            root: "data.datas",
            page: "data.pageNow",
            total: "data.pageCount",
            records: "data.rowCount"
        },



        viewrecords: true,
        rowNum: 10,
        rowList: [10, 20, 30],
        // pager: senderpager_selector,
        altRows: true,

        multiselect: true,
        //multikey: "ctrlKey",
        multiboxonly: true,

        loadComplete: function (data) {


            if ("200" == data.code)//处理成功
            { }
            else if ("607" == data.code) {
                alert("您尚未登录或登录已失效！");
                top.location.href = "../admin/login.html";
            }
            else {
                alert("失败：失败原因是" + data.message);
            }


            var table = this;
            setTimeout(function () {
                //alert(table);
                updatePagerIcons(table);
            }, 0);

            $(".ui-jqgrid-labels [role=checkbox]").hide();
            $(sendergrid_selector + " [role=checkbox]").attr({ "type": "radio", "name": "receivergrid-radio" });

        },
        onSelectRow: function (rowid, status) {
            var rowData = $(sendergrid_selector).jqGrid("getRowData", rowid);
            setInputData("#senderTableInfo", rowData);
            $("#senderTableInfo").find(":text[name='usermoney']").val("");//清空账户余额
            $("#senderTableInfo").find(":text[name='id']").val(removenull(""));//清空用户id号
            $(":checkbox[name='payway']").removeAttr("checked");
            //$(":checkbox[name='payway']").attr("disabled","true");
        },
        autowidth: true
    });

    //navButtons
    jQuery(sendergrid_selector).jqGrid('navGrid', senderpager_selector,
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
        }
    );


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

    //注册查询事件
    $("#btnSender").click(function () {
        $("#gview_usersgrid-table").hide();
        $("#gview_usersgrid-pager").hide();
        $("#gview_sendergrid-table").show();
        $("#gview_sendergrid-pager").show();
        $(sendergrid_selector).jqGrid("setGridParam", { postData: { info: $.trim($("#senderinfo").val()) } }).trigger("reloadGrid");
    });

    //注册enter搜索事件
    $("#senderinfo").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnSender").click();
        }
    }));


}



//点击查询会员信息
function seachusersinfo() {

    $("#gview_usersgrid-table").show();
    $("#gview_usersgrid-pager").show();
    $("#gview_sendergrid-table").hide();
    $("#gview_sendergrid-pager").hide();

    var sendergrid_selector = "#usersgrid-table";
    var senderpager_selector = "#usersgrid-pager";

    /* $(window).resize(function () {
         $(sendergrid_selector).setGridWidth($("#senderTableInfo").width(), true);
     });*/
    var send_postData = { usersinfo: "" };
    jQuery(sendergrid_selector).jqGrid({
        url: "/admin/user/m_search",
        datatype: "json",
        mtype: "post",
        postData: send_postData,
        height: 100,
        colNames: ['id号', '姓名', '发件人电话', '地址', "邮箱", "人民币", "美元", "收件代码", "收件标识", "余额", "用户标识", "类型"],
        colModel: [
                   { name: 'id', index: 'id', width: 40 },
            { name: 'realName', index: 'realName', width: 40 },
            { name: 'phone', index: 'phone', width: 50 },
             { name: 'address', index: 'address', width: 50, hidedlg: true, hidden: true },
             { name: 'email', index: 'email', width: 50, hidedlg: true, hidden: true },
             { name: 'rmbBalance', index: 'rmbBalance', width: 50, hidedlg: true, hidden: true },
             { name: 'usdBalance', index: 'usdBalance', width: 100, hidedlg: true, hidden: true },
             { name: 'usercode', index: 'usercode', width: 50, hidedlg: true, hidden: true },
             { name: 'useralias', index: 'useralias', width: 50, hidedlg: true, hidden: true },

             {
                 name: 'money', index: 'money', width: 50,
                 formatter: function (cellvalue, options, row) {

                     return "$" + removenull(row.usdBalance) + "/￥" + removenull(row.rmbBalance);
                 }
             },
             {
                 name: 'userflag', index: 'userflag', width: 50,
                 formatter: function (cellvalue, options, row) {

                     return removenull(row.usercode) + "/" + removenull(row.useralias);
                 }
             },
             {
                 name: 'type', index: 'type', width: 50,
                 formatter: function (cellvalue, options, row) {
                     if (cellvalue == "0") {
                         return "普通会员";
                     }
                     else if (cellvalue == "1") {
                         return "银牌VIP会员";
                     }
                     else if (cellvalue == "2") {
                         return "黄金VIP会员";
                     }
                     else if (cellvalue == "3") {
                         return "白金VIP会员";
                     }
                     else if (cellvalue == "4") {
                         return "钻石VIP会员";
                     }
                     else if (cellvalue == "5") {
                         return "至尊VIP1会员";
                     }
                     else if (cellvalue == "6") {
                         return "至尊VIP2会员";
                     }
                     else if (cellvalue == "7") {
                         return "至尊VIP3会员";
                     }
                     else if (cellvalue == "8") {
                         return "至尊VIP4会员";
                     }
                     else {
                         return "会员未定";
                     }

                 }
             },
        ],
        /*
         * 	total;//总页数
            page;//当前页
            records;//查出的记录数
         * */
        jsonReader: {
            root: "data.datas",
            page: "data.pageNow",
            total: "data.pageCount",
            records: "data.rowCount"
        },



        viewrecords: true,
        rowNum: 10,
        rowList: [10, 10, 30],
        // pager: senderpager_selector,
        altRows: true,

        multiselect: true,
        //multikey: "ctrlKey",
        multiboxonly: true,

        loadComplete: function (data) {

            if ("200" == data.code)//处理成功
            {

            }
            else if ("607" == data.code) {
                alert("您尚未登录或登录已失效！");
                top.location.href = "../admin/login.html";
            }
            else {
                alert("失败：失败原因是" + data.message);
            }


            var table = this;
            setTimeout(function () {
                //alert(table);
                updatePagerIcons(table);
            }, 0);

            $(".ui-jqgrid-labels [role=checkbox]").hide();
            $(sendergrid_selector + " [role=checkbox]").attr({ "type": "radio", "name": "receivergrid-radio" });

        },
        onSelectRow: function (rowid, status) {
            //之前发件人的参数与会员的参数不太一样，所以要独立处理

            var rowData = $(sendergrid_selector).jqGrid("getRowData", rowid);

            $("#senderTableInfo").find(":text[name='name']").val(removenull(rowData.realName));//姓名
            $("#senderTableInfo").find(":text[name='phone']").val(removenull(rowData.phone));//电话
            $("#senderTableInfo").find(":text[name='address']").val(removenull(rowData.address));//地址
            $("#senderTableInfo").find(":text[name='city']").val(removenull(rowData.city));//地址
            $("#senderTableInfo").find(":text[name='state']").val(removenull(rowData.state));//地址
            $("#senderTableInfo").find(":text[name='zipcode']").val(removenull(rowData.zipcode));//邮编
            $("#senderTableInfo").find(":text[name='email']").val(removenull(rowData.email));//邮箱
            $("#senderTableInfo").find(":text[name='company']").val(removenull(rowData.company));//公司名称
            $("#senderTableInfo").find(":text[name='id']").val(removenull(rowData.id));//用户id号
            $("#senderTableInfo").find(":text[name='usermoney']").val("$" + removenull(rowData.usdBalance) + "/￥" + removenull(rowData.rmbBalance));//公司名称

            if (((parseFloat(rowData.usdBalance) > 0) || (parseFloat(rowData.rmbBalance))) && (!isNaN(parseFloat(rowData.usdBalance))) && (!isNaN(parseFloat(rowData.rmbBalance)))) {
                $("#senderTableInfo").find(":checkbox[name='payway']").attr("checked", true);//此方法第二次后不起作用
                $("#senderTableInfo").find(":checkbox[name='payway']").prop("checked", true);
            }
            else {
                $("#senderTableInfo").find(":checkbox[name='payway']").attr("checked", false);
            }
            // users_to_senerdinfo("#senderTableInfo", rowData);
        },
        autowidth: true
    });

    //navButtons
    jQuery(sendergrid_selector).jqGrid('navGrid', senderpager_selector,
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
        }
    );


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

    //注册查询事件
    $("#btnUsers").click(function () {
        $("#gview_usersgrid-table").show();
        $("#gview_usersgrid-pager").show();
        $("#gview_sendergrid-table").hide();
        $("#gview_sendergrid-pager").hide();
        $(sendergrid_selector).jqGrid("setGridParam", { postData: { usersinfo: $.trim($("#usersinfo").val()) } }).trigger("reloadGrid");
    });

    //注册enter搜索事件
    $("#usersinfo").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnUsers").click();
        }
    }));
}


function users_to_senerdinfo(selector, obj) {
    //对某个标签内的input进行赋值

    var propertyNames = getObjectPropNameArr(obj);
    $(selector + " select," + selector + " input").each(function (j) {


        for (var i = 0; i < propertyNames.length; i++) {
            if ($(this).attr("name") == propertyNames[i]) {
                $(this).val(obj[propertyNames[i]]);
                break;
            }
        }
    });

}


function seachreceiverinfo() {
    var rinfo = $("#receiverinfo").val();
    //alert(rinfo);
    var receivergrid_selector = "#receivergrid-table";
    var receiverpager_selector = "#receivergrid-pager";

    $(window).resize(function () {
        $(receivergrid_selector).setGridWidth($("#receiverInfoTable").width(), true);
    });



    var rev_postData = { info: rinfo };
    // alert(rev_postData);
    jQuery(receivergrid_selector).jqGrid({
        url: "../../admin/receive_user/search",
        datatype: "json",
        mtype: "post",
        postData: rev_postData,
        height: 100,
        shrinkToFit: true,
        colNames: ['姓名', '收件人电话', '省', "市", "区县", "地址", "邮编", "身份证号", "身份证正面图", "身份证反面图", "身份证合成图", "第二名称", "详细地址"],
        colModel: [

            { name: 'name', index: 'name', width: 40 },
            { name: 'phone', index: 'phone', width: 50 },
             { name: 'state', index: 'state', width: 50, hidedlg: true, hidden: true },
             { name: 'city', index: 'city', width: 50, hidedlg: true, hidden: true },
             { name: 'dist', index: 'dist', width: 50, hidedlg: true, hidden: true },
             { name: 'address', index: 'address', width: 100, hidedlg: true, hidden: true },
             { name: 'zipcode', index: 'zipcode', width: 40, hidedlg: true, hidden: true },
             { name: 'cardid', index: 'cardid', width: 40, hidedlg: true, hidden: true },
             { name: 'cardurl', index: 'cardurl', width: 40, hidedlg: true, hidden: true },
             { name: 'cardother', index: 'cardother', width: 40, hidedlg: true, hidden: true },
             { name: 'cardtogether', index: 'cardtogether', width: 40, hidedlg: true, hidden: true },
             { name: 'secondName', index: 'secondName', width: 40, hidedlg: true, hidden: true },
             {
                 name: 'alladdress', index: 'alladdress', width: 110,//完整地址信息


                 formatter: function (cellvalue, options, row) {

                     return removenull(row.state) + removenull(row.city) + removenull(row.dist) + removenull(row.address);
                 }
             },
        ],
        /*
         * 	total;//总页数
            page;//当前页
            records;//查出的记录数
         * */
        jsonReader: {
            root: "data.datas",
            page: "data.pageNow",
            total: "data.pageCount",
            records: "data.rowCount"
        },

        viewrecords: true,
        rowNum: 10,
        rowList: [10, 20, 30],
        // pager: receiverpager_selector,
        altRows: true,

        multiselect: true,
        //multikey: "ctrlKey",
        multiboxonly: true,
        loadonce: false,

        loadComplete: function (data) {
            if ("200" == data.code)//处理成功
            { }
            else if ("607" == data.code) {
                alert("您尚未登录或登录已失效！");
                top.location.href = "../admin/login.html";
            }
            else {
                alert("失败：失败原因是" + data.message);
            }

            var table = this;

            //alert(data.code);
            setTimeout(function () {
                //table=this.data;
                updatePagerIcons(table);
            }, 0);

            $(".ui-jqgrid-labels [role=checkbox]").hide();
            $(receivergrid_selector + " [role=checkbox]").attr({ "type": "radio", "name": "receivergrid-radio" });

        },
        onSelectRow: function (rowid, status) {
            receiverRowData = $(receivergrid_selector).jqGrid("getRowData", rowid);
            setInputData("#receiverInfoTable", receiverRowData);


            //	alert(receiverRowData.cardid);
            //调用身份证信息
            //  if(removenull(receiverRowData.secondName)=="")//如果第二名称存在，就会匹配了身份证信息
            {
                var flag = 0;
                if (removenull(receiverRowData.cardid) == "") {
                    $("#receiverIdTable").find(":text[name='cardid']").val("");//身份证号
                }
                else {
                    $("#receiverIdTable").find(":text[name='cardid']").val(receiverRowData.cardid);//身份证号
                    flag = 1;
                }
                if (removenull(receiverRowData.cardtogether) == "") {
                    $("#receiverIdTable").find("img[id='hcimage']").attr("src", "");
                }
                else {
                    $("#receiverIdTable").find("img[id='hcimage']").attr("src", receiverRowData.cardtogether);
                    flag = 1;
                }

                if (removenull(receiverRowData.cardurl) == "") {
                    $("#receiverIdTable").find("img[id='zmimage']").attr("src", "");
                }
                else {
                    $("#receiverIdTable").find("img[id='zmimage']").attr("src", receiverRowData.cardurl);
                    flag = 1;
                }

                if (removenull(receiverRowData.cardother) == "") {
                    $("#receiverIdTable").find("img[id='fmimage']").attr("src", "");
                }
                else {
                    $("#receiverIdTable").find("img[id='fmimage']").attr("src", receiverRowData.cardother);
                    flag = 1;
                }

                if (flag == 1)//包含身份证信息，展开
                {
                    var showblock = $("#receiverIdTable").attr("style");
                    if (showblock == "display: block;" || showblock == "")//
                    {
                        //$("#cardinfoshow").click();
                    }
                    else//原来是隐藏的，要展示出来
                    {
                        $("#cardinfoshow").click();
                    }
                }
            }
        },
        autowidth: true
    });

    //navButtons
    jQuery(receivergrid_selector).jqGrid('navGrid', receiverpager_selector,
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
        }
    );


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


    //注册查询事件
    $("#btnReceiver").click(function () {
        $(receivergrid_selector).jqGrid("setGridParam", { postData: { info: $.trim($("#receiverinfo").val()) } }).trigger("reloadGrid");
    });

    //注册enter搜索事件
    $("#receiverinfo").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnReceiver").click();
        }
    }));
    //var selr = jQuery(receivergrid_selector).jqGrid('getGridParam','selrow');
}
//装载商品信息
function loadCommoditySelectOption() {

    $("select[name='commodityName']").html("");
    var cid = $("select[name='channellist']").val();
    var additivePrice = $("select[name='channellist']").find("option:selected").attr("additivePrice");
    $(":text[name='addtivePrice']").val(additivePrice);
    $.ajax({
        post: "get",
        url: "/admin/commudityPrice/getpricebyself",
        data: {
            "cid": cid
        },
        success: function (response) {
            var code = response.code;
            if ("200" == code) {

                // showCommodityList(response.data);;
                var str = "";
                $.each(response.data, function () {
                    str += "<option value='" + this.id + "' >"
							+ this.commudityAdmin.name + "</option>";
                });
                $("select[name='commodityName']").html(str);
                //msOrderCalcFreight();
            } else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                adminlogout();
            }
        }
    });
}
//装载渠道选择
function loadChannelSelectOption() {

    $.ajax({
        post: "get",
        url: "/admin/channel/get_by_self",
        success: function (response) {
            var code = response.code;
            if ("200" == code) {

                // showCommodityList(response.data);;
                var str = "";
                $.each(response.data, function () {
                    str += "<option value='" + this.id
							+ "' additivePrice='" + this.additivePrice + "'>"
							+ this.name + "</option>";
                });
                $("select[name='channellist']").html(str);



                $("select[name='channellist']").change(loadCommoditySelectOption);


                //要装载历史运单的渠道查找信息
                var strh = "<option value='-1'>请选择渠道</option>" + str;
                $("select[name='history_cid']").html(strh);

                loadCommoditySelectOption();
            } else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                adminlogout();
            }
        }
    });
}

//商品变化时，计算对应的值
function commuditydataChange() {
    var allquantity = 0;
    var allsjweight = 0;
    var allcvalue = 0;
    var allcother = 0;
    var allctariff = 0;
    $("select[name='commodityName']").each(
			function () {




			    var cquantity = $(this).parent().parent().find(":text[name='cquantity']").val();
			    if ((cquantity == "") || (cquantity.trim() == "")) {

			    }
			    else {
			        allquantity = parseFloat(allquantity) + parseFloat(cquantity);
			    }

			    var cweight = $(this).parent().parent().find(":text[name='cweight']").val();
			    if ((cweight == "") || (cweight.trim() == "")) {

			    }
			    else {
			        allsjweight = parseFloat(allsjweight) + parseFloat(cweight);
			    }

			    var cvalue = $(this).parent().parent().find(":text[name='cvalue']").val();
			    if ((cvalue == "") || (cvalue.trim() == "")) {

			    }
			    else {
			        allcvalue = parseFloat(allcvalue) + parseFloat(cvalue);
			    }

			    var ctariff = $(this).parent().parent().find(":text[name='ctariff']").val();
			    if ((ctariff == "") || (ctariff.trim() == "")) {

			    }
			    else {
			        allctariff = parseFloat(allctariff) + parseFloat(ctariff);
			    }

			    var cother = $(this).parent().parent().find(":text[name='cother']").val();
			    if ((cother == "") || (cother.trim() == "")) {

			    }
			    else {
			        allcother = parseFloat(allcother) + parseFloat(cother);
			    }
			});

    $(":text[name='allsjweight']").val(allsjweight);
    $(":text[name='allquantity']").val(allquantity);
    $(":text[name='allcvalue']").val(allcvalue);
    $(":text[name='allcother']").val(allcother);
    $(":text[name='allctariff']").val(allctariff);


    if (parseFloat(lowest_weight_value_flag) >= parseFloat(allsjweight))//小于最低计费重量
    {
        $(":text[name='allweight']").val(lowest_weight_value_flag);
    }
    else {
        var j = parseInt(allsjweight);
        var rate = parseFloat(allsjweight) - parseInt(allsjweight);
        if (parseFloat(jw_commodity_rate) < parseFloat(rate)) {
            $(":text[name='allweight']").val(j + 1);
        }
        else {
            if (price_carry_flag == "1" || price_carry_flag == "3")//退位计算
            {
                $(":text[name='allweight']").val(j);
            }
            else if (price_carry_flag == "2" || price_carry_flag == "4")//实际计算
            {
                $(":text[name='allweight']").val(allsjweight);
            }
            else {
                $(":text[name='allweight']").val(allsjweight);
            }
        }
    }
}

//保险额度计算
function countpremiummoney_user() {
    var rate = premium_rate;
    if ((rate == "0") || (removenull(rate) == null)) {
        return false;
    }
    rate = parseFloat(rate);
    if (isNaN(rate)) {
        return false;
    }
    var premium1 = parseFloat($(":text[name='insurance']").val());
    if (isNaN(premium1)) {
        return false;
    }
    if (premium1 > 0)
    { }
    else if (premium1 < 0) {
        alert("保险不能小于0!");
        return false;
    }
    else {
        return false;
    }
    var premiumt = parseFloat(premium1 / rate);
    if (isNaN(premiumt)) {
        return false;
    }
    if (parseFloat(premium_max_value) != "NaN") {
        if (premiumt > parseFloat(premium_max_value)) {
            alert("最大保险额度不能超过" + premium_max_value + "美元!");
            $(":text[name='insurance']").val(premium_max_value * rate);
            $(":text[name='maxinsurance']").val(premium_max_value);
        }
        else {
            $(":text[name='maxinsurance']").val(premiumt);
        }
    }
    else {
        $(":text[name='maxinsurance']").val(premiumt);
    }

}


function submitmsorderA4() {

    //先判断是网上置单的可能
    var onlineorderid = $("#searchonlineOrder").val();
    if ((removenull(onlineorderid) == "") || (onlineorderid.trim().length == 0)) {
        submitmsorder("0");//表示打印A4运单 
    }
    else {
        alert("当前状态为打印网上置单，请先清空\"网上置单号\"的搜索框，否则无法直接打印A4类型运单！");
    }
}

function submitmsorder46() {
    //先判断是网上置单的可能
    var onlineorderid = $("#searchonlineOrder").val();
    if ((removenull(onlineorderid) == "") || (onlineorderid.trim().length == 0)) {
        submitmsorder("0");//表示打印4x6运单 
    }
    else {
        alert("当前状态为打印网上置单，请先清空\"网上置单号\"的搜索框，否则无法直接打印海关类型运单！");
    }
}
function submitMrder4x6() {
    //先判断是网上置单的可能
    var onlineorderid = $("#searchonlineOrder").val();
    if ((removenull(onlineorderid) == "") || (onlineorderid.trim().length == 0)) {
        submitmsorder("2");//表示普通4x6的热敏单
    }
    else {
        alert("当前状态为打印网上置单，请先清空\"网上置单号\"的搜索框，否则无法直接打印普通热敏类型运单！");
    }
}

function submitwanshanOrder() {
    var type = "2";

    if (default_print_button_id == "0")//A4打印
    {
        type = "0";
    }
    else if (default_print_button_id == "2")//海关打印
    {
        type = "1";
    }

    //根据文本框的内容决定是网上置单还是上门收货
    var sorderid = removenull($("#searchshangmenOrder").val()).trim();
    var worderid = removenull($("#searchonlineOrder").val()).trim();
    if ((sorderid != "") && (worderid != ""))//两个不能同时不为空
    {
        alert("无法识别网上置单号还是上门收货单号，请清空其中一个搜索文本框");
        return false;
    }

    submitmsorder(type, 0);//提交网上置单

}

//提交上门收货运单
function submitshanmenxiaohuo() {
    var type = "2";

    if (default_print_button_id == "0")//A4打印
    {
        type = "0";
    }
    else if (default_print_button_id == "2")//海关打印
    {
        type = "1";
    }

    submitmsorder(type, 0);
}

//提交运单处理
function submitmsorder(type, online) {
    commuditydataChange();//在提交之前，还是要算一下，要不回车的话，可能会出错

    var validate = true;

    var formData = new FormData();
    var printway = type;



    if (online == 1)//网上置单要提交运单号
    {
        //var orderId = $("#searchonlineOrder").val();
        //if (removenull(orderId) == null) {
        //    alert("网上置单号不能为空！");
        //    return false;
        //}
        //orderId = orderId.trim();
        //if (orderId.length < "5") {
        //    alert("网上置单号号太短，检查是否已经被修改！");
        //    return false;
        //}
        formData.append("orderId", "");

        formData.append("suser.id", onlineorder_postData.suser.id);
        formData.append("ruser.id", onlineorder_postData.ruser.id);
        // formData.append("type", "-10");//表示在线置单
        formData.append("type", "");
        if (default_print_button_id == "0")//A4打印
        {
            printway = 0;
        }
        else if (default_print_button_id == "1")//4x6打印
        {
            printway = 2;
        }
        else if (default_print_button_id == "2")//海关打印
        {
            printway = 1;
        }
        else {
            printway = 2;
        }

    }
    else if (online == 2)//网上置单后上门收货
    {
        var orderId = $("#searchshangmenOrder").val();
        if (removenull(orderId) == null) {
            alert("上门取货单号不能为空！");
            return false;
        }
        orderId = orderId.trim();
        if (orderId.length < "5") {
            alert("上门取件单号太短，检查是否已经被修改！");
            return false;
        }
        formData.append("orderId", orderId);

        formData.append("suser.id", onlineorder_postData.suser.id);
        formData.append("ruser.id", onlineorder_postData.ruser.id);
        formData.append("type", "-9");//表示上门取件

        if (default_print_button_id == "0")//A4打印
        {
            printway = 0;
        }
        else if (default_print_button_id == "1")//4x6打印
        {
            printway = 2;
        }
        else if (default_print_button_id == "2")//海关打印
        {
            printway = 1;
        }
        else {
            printway = 2;
        }

    }
    formData.append("printway", printway);
    //构造发件人数据
    var name = $("#senderTableInfo").find(":text[name='name']").val();
    var phone = $("#senderTableInfo").find(":text[name='phone']").val();
    var address = $("#senderTableInfo").find(":text[name='address']").val();
    var city = $("#senderTableInfo").find(":text[name='city']").val();
    var state = $("#senderTableInfo").find(":text[name='state']").val();
    var zipcode = $("#senderTableInfo").find(":text[name='zipcode']").val();
    var email = $("#senderTableInfo").find(":text[name='email']").val();
    var company = $("#senderTableInfo").find(":text[name='company']").val();


    //选择支付方式0是余额支付，1是现金支付
    var payway1 = $("#senderTableInfo").find(":checkbox[name='payway']").is(":checked");
    var userId = $("#senderTableInfo").find(":text[name='id']").val();
    var payway = 0;
    if (payway1 == true)//余额付款
    {
        payway = 0;//余额支付要确认用户存在
        if (removenull(userId) == "") {
            alert("只有选择会员才能够进行余额支付!");
            return false;
        }
    }
    else {
        payway = 1;
    }

    formData.append("payWay", payway);//0表示余额支付，1表示现金支付
    formData.append("userId", "");//当选择会员时，此用于标识归属的id


    if (name == "" || name == null) {
        $("#senderTableInfo").find(":text[name='name']").css({
            "border-color": "red"
        });
        $("#senderTableInfo").find(":text[name='name']").change(
				function () {
				    if ($("#senderTableInfo").find(":text[name='name']")
							.val() != null)
				        $("#senderTableInfo").find(":text[name='name']")
								.css({
								    "border-color": ""
								});
				});
        validate = false;
    }

    if (phone == "" || phone == null) {
        $("#senderTableInfo").find(":text[name='phone']").css({
            "border-color": "red"
        });
        $("#senderTableInfo").find(":text[name='phone']").change(
				function () {
				    if ($("#senderTableInfo").find(":text[name='phone']")
							.val() != null)
				        $("#senderTableInfo").find(":text[name='phone']")
								.css({
								    "border-color": ""
								});
				});
        validate = false;
    }
    formData.append("suser.name", name);
    formData.append("suser.phone", phone);
    formData.append("suser.address", address);
    formData.append("suser.city", city);
    formData.append("suser.state", state);
    formData.append("suser.zipcode", zipcode);
    formData.append("suser.email", email);
    formData.append("suser.company", company);


    //构造收件人数据
    var name = $("#receiverInfoTable").find(":text[name='name']").val();
    var phone = $("#receiverInfoTable").find(":text[name='phone']").val();
    var address = $("#receiverInfoTable").find(":text[name='address']").val();
    var dist = $("#receiverInfoTable").find(":text[name='dist']").val();
    var city = $("#receiverInfoTable").find(":text[name='city']").val();
    var state = $("#receiverInfoTable").find(":text[name='state']").val();
    var zipcode = $("#receiverInfoTable").find(":text[name='zipcode']").val();
    var cardid = $("#receiverIdTable").find(":text[name='cardid']").val();//身份证号
    var hcimage = $("#receiverIdTable").find("img[id='hcimage']").attr("src");//身份证合成图
    var zmimage = $("#receiverIdTable").find("img[id='zmimage']").attr("src");//身份证正面图
    var fmimage = $("#receiverIdTable").find("img[id='fmimage']").attr("src");//身份证反面图
    var cardurlfile = "";
    var cardurlotherfile = "";
    var cardurltogetherfile = "";

    var filetype = $(":radio[name='shenfenzhenradio']:checked").val();
    //$(":radio[name='userId']:checked").val()

    if (filetype == "hc")//合成图文件
    {

        cardurltogetherfile = $(":file[name='hcpicture']").val();


        $(":file[name='zmpicture']").val("");//正面图
        $(":file[name='fmpicture']").val("");//反面图

        if ((removenull(cardurltogetherfile) != "")) {
            if (removenull(cardid) == "") {


                $("#receiverIdTable").find(":text[name='cardid']").css({
                    "border-color": "red"
                });
                $("#receiverIdTable").find(":text[name='cardid']").change(
						function () {
						    if ($("#receiverIdTable").find(":text[name='cardid']")
									.val() != null)
						        $("#receiverIdTable").find(":text[name='cardid']")
										.css({
										    "border-color": ""
										});
						});
                validate = false;



                alert("上传身份证必须同时上传身份证号!");
                return false;
            }
            var picture = document.getElementById("hcpicture");
            //alert(picture.value);
            //return false;
            if (picture == "" || picture == null)
            { }
            else
            {
                formData.append("cardurltogetherfile", picture.files[0]);
            }
        }



    }
    else if (filetype == "zf")//上传正反两面图
    {
        $(":file[name='hcpicture']").val("");
        cardurlfile = $(":file[name='zmpicture']").val();//正面图
        cardurlotherfile = $(":file[name='fmpicture']").val();//反面图
        if ((removenull(cardurlfile) == "") && (removenull(cardurlotherfile) != "") && (removenull(zmimage) == "")) {
            alert("必须同时上传正反两面身份证图片!");
            return false;
        }
        if ((removenull(cardurlotherfile) == "") && (removenull(cardurlfile) != "") && (removenull(fmimage) == "")) {
            alert("必须同时上传正反两面身份证图片!");
            return false;
        }
        if ((removenull(cardurlotherfile) != "") || (removenull(cardurlfile) != "")) {
            if (removenull(cardid) == "") {


                $("#receiverIdTable").find(":text[name='cardid']").css({
                    "border-color": "red"
                });
                $("#receiverIdTable").find(":text[name='cardid']").change(
						function () {
						    if ($("#receiverIdTable").find(":text[name='cardid']")
									.val() != null)
						        $("#receiverIdTable").find(":text[name='cardid']")
										.css({
										    "border-color": ""
										});
						});
                validate = false;



                alert("上传身份证必须同时上传身份证号!");
                return false;
            }

        }
        var picture = document.getElementById("zmpicture");
        if (picture == "" || picture == null)
        { }
        else
        {
            formData.append("cardurlfile", picture.files[0]);
        }

        picture = document.getElementById("fmpicture");
        if (picture == "" || picture == null) { }
        else {
            formData.append("cardurlotherfile", picture.files[0]);
        }
    }

    if (phone == "" || phone == null) {
        $("#receiverInfoTable").find(":text[name='phone']").css({
            "border-color": "red"
        });
        $("#receiverInfoTable").find(":text[name='phone']").change(
				function () {
				    if ($("#receiverInfoTable").find(":text[name='phone']")
							.val() != null)
				        $("#receiverInfoTable").find(":text[name='phone']")
								.css({
								    "border-color": ""
								});
				});
        validate = false;
    }

    if (name == "" || name == null) {
        $("#receiverInfoTable").find(":text[name='name']").css({
            "border-color": "red"
        });
        $("#receiverInfoTable").find(":text[name='name']").change(
				function () {
				    if ($("#receiverInfoTable").find(":text[name='name']")
							.val() != null)
				        $("#receiverInfoTable").find(":text[name='name']")
								.css({
								    "border-color": ""
								});
				});
        validate = false;
    }

    if (state == "" || state == null) {
        $("#receiverInfoTable").find(":text[name='state']").css({
            "border-color": "red"
        });
        $("#receiverInfoTable").find(":text[name='state']").change(
				function () {
				    if ($("#receiverInfoTable").find(":text[name='state']")
							.val() != null)
				        $("#receiverInfoTable").find(":text[name='state']")
								.css({
								    "border-color": ""
								});
				});
        validate = false;
    }
    if (city == "" || city == null) {
        $("#receiverInfoTable").find(":text[name='city']").css({
            "border-color": "red"
        });
        $("#receiverInfoTable").find(":text[name='city']").change(
				function () {
				    if ($("#receiverInfoTable").find(":text[name='city']")
							.val() != null)
				        $("#receiverInfoTable").find(":text[name='city']")
								.css({
								    "border-color": ""
								});
				});
        validate = false;
    }

    if (dist == "" || dist == null) {
        $("#receiverInfoTable").find(":text[name='dist']").css({
            "border-color": "red"
        });
        $("#receiverInfoTable").find(":text[name='dist']").change(
				function () {
				    if ($("#receiverInfoTable").find(":text[name='dist']")
							.val() != null)
				        $("#receiverInfoTable").find(":text[name='dist']")
								.css({
								    "border-color": ""
								});
				});
        validate = false;
    }

    if (address == "" || address == null) {
        $("#receiverInfoTable").find(":text[name='address']").css({
            "border-color": "red"
        });
        $("#receiverInfoTable").find(":text[name='address']").change(
				function () {
				    if ($("#receiverInfoTable").find(":text[name='address']")
							.val() != null)
				        $("#receiverInfoTable").find(":text[name='address']")
								.css({
								    "border-color": ""
								});
				});
        validate = false;
    }

    formData.append("ruser.name", name);
    formData.append("ruser.phone", phone);
    formData.append("ruser.address", address);
    formData.append("ruser.dist", dist);
    formData.append("ruser.city", city);
    formData.append("ruser.state", state);
    formData.append("ruser.zipcode", zipcode);

    formData.append("ruser.cardid", cardid);
    formData.append("ruser.cardtogether", hcimage);
    formData.append("ruser.cardurl", zmimage);
    formData.append("ruser.cardother", fmimage);



    //开始构造商品类信息
    var flag = -1;
    var ii = 0;
    $("select[name='commodityName']").each(
			function () {

			    if (flag >= 0)//第一行为隐藏行的添加模板，不计入商品信息
			    {


			        var commodityid = $(this).val();//获取商品id
			        var commodityname = $(this).find("option:selected").text();//获取商品名称
			        var cproductname = $(this).parent().parent().find(":text[name='cproductname']").val();//获取品名
			        var cbrandname = $(this).parent().parent().find(":text[name='cbrandname']").val();//获取品牌
			        var cquantity = $(this).parent().parent().find(":text[name='cquantity']").val();//获取数量
			        var cweight = $(this).parent().parent().find(":text[name='cweight']").val();//获取重量
			        var cvalue = $(this).parent().parent().find(":text[name='cvalue']").val();//获取价值
			        var ctariff = $(this).parent().parent().find(":text[name='ctariff']").val();//获取价值
			        var cother = $(this).parent().parent().find(":text[name='cother']").val();//获取其它费用
			        var cremark = $(this).parent().parent().find(":text[name='cremark']").val();//获取备注



			        if ((removenull(cproductname) == "") && (removenull(cbrandname) == "") && (removenull(cquantity) == "") && (removenull(cweight) == "") && (removenull(cvalue) == ""))
			        { }
			        else
			        {


			            if ((removenull(commodityid) == "") || (parseFloat(commodityid)) <= 0) {

			                $(this).css({
			                    "border-color": "red"
			                });
			                $(this).change(
                                    function () {
                                        if ($(this)
                                                .val() != null)
                                            $(this)
                                                    .css({
                                                        "border-color": ""
                                                    });
                                    });
			                validate = false;

			            }

			            if (cproductname == "" || cproductname == null) {
			                $(this).parent().parent().find(":text[name='cproductname']").css({
			                    "border-color": "red"
			                });
			                $(this).parent().parent().find(":text[name='cproductname']").change(
                                    function () {
                                        if ($(this).parent().parent().find(":text[name='cproductname']")
                                                .val() != null)
                                            $(this).parent().parent().find(":text[name='cproductname']")
                                                    .css({
                                                        "border-color": ""
                                                    });
                                    });
			                validate = false;
			            }
			            if (cbrandname == "" || cbrandname == null) {
			                $(this).parent().parent().find(":text[name='cbrandname']").css({
			                    "border-color": "red"
			                });
			                $(this).parent().parent().find(":text[name='cbrandname']").change(
                                    function () {
                                        if ($(this).parent().parent().find(":text[name='cbrandname']")
                                                .val() != null)
                                            $(this).parent().parent().find(":text[name='cbrandname']")
                                                    .css({
                                                        "border-color": ""
                                                    });
                                    });
			                validate = false;
			            }

			            if (cquantity == "" || cquantity == null || isNaN(parseFloat(cquantity)) || (parseFloat(cquantity) <= 0) || (isInteger(cquantity) == false)) {
			                $(this).parent().parent().find(":text[name='cquantity']").css({
			                    "border-color": "red"
			                });
			                $(this).parent().parent().find(":text[name='cquantity']").change(
                                    function () {
                                        if ($(this).parent().parent().find(":text[name='cquantity']")
                                                .val() != null)
                                            $(this).parent().parent().find(":text[name='cquantity']")
                                                    .css({
                                                        "border-color": ""
                                                    });
                                    });
			                validate = false;
			            }

			            if (cweight == "" || cweight == null || isNaN(parseFloat(cweight)) || (parseFloat(cweight) <= 0)) {
			                $(this).parent().parent().find(":text[name='cweight']").css({
			                    "border-color": "red"
			                });
			                $(this).parent().parent().find(":text[name='cweight']").change(
                                    function () {
                                        if ($(this).parent().parent().find(":text[name='cweight']")
                                                .val() != null)
                                            $(this).parent().parent().find(":text[name='cweight']")
                                                    .css({
                                                        "border-color": ""
                                                    });
                                    });
			                validate = false;
			            }

			            if (cvalue == "" || cvalue == null || isNaN(parseFloat(cvalue)) || (parseFloat(cvalue) <= 0)) {
			                $(this).parent().parent().find(":text[name='cvalue']").css({
			                    "border-color": "red"
			                });
			                $(this).parent().parent().find(":text[name='cvalue']").change(
                                    function () {
                                        if ($(this).parent().parent().find(":text[name='cvalue']")
                                                .val() != null)
                                            $(this).parent().parent().find(":text[name='cvalue']")
                                                    .css({
                                                        "border-color": ""
                                                    });
                                    });
			                validate = false;
			            }

			            if (ctariff == "" || ctariff == null || isNaN(parseFloat(ctariff)) || (parseFloat(ctariff) < 0)) {
			                $(this).parent().parent().find(":text[name='ctariff']").css({
			                    "border-color": "red"
			                });
			                $(this).parent().parent().find(":text[name='ctariff']").change(
                                    function () {
                                        if ($(this).parent().parent().find(":text[name='ctariff']")
                                                .val() != null)
                                            $(this).parent().parent().find(":text[name='ctariff']")
                                                    .css({
                                                        "border-color": ""
                                                    });
                                    });
			                validate = false;
			            }
			            if (cother == "" || cother == null || isNaN(parseFloat(cother)) || (parseFloat(cother) < 0)) {
			                $(this).parent().parent().find(":text[name='cother']").css({
			                    "border-color": "red"
			                });
			                $(this).parent().parent().find(":text[name='cother']").change(
                                    function () {
                                        if ($(this).parent().parent().find(":text[name='cother']")
                                                .val() != null)
                                            $(this).parent().parent().find(":text[name='cother']")
                                                    .css({
                                                        "border-color": ""
                                                    });
                                    });
			                validate = false;
			            }
			            formData.append("detail[" + ii + "].commodityId", commodityid);
			            formData.append("detail[" + ii + "].name", commodityname);
			            formData.append("detail[" + ii + "].productName", cproductname);
			            formData.append("detail[" + ii + "].brands", cbrandname);
			            formData.append("detail[" + ii + "].quantity", cquantity);
			            formData.append("detail[" + ii + "].weight", cweight);
			            formData.append("detail[" + ii + "].value", cvalue);
			            formData.append("detail[" + ii + "].tariff", ctariff);
			            formData.append("detail[" + ii + "].other", cother);
			            formData.append("detail[" + ii + "].remark", cremark);
			            ii = ii + 1;
			        }

			    }

			    flag = flag + 1;


			});


    if (ii == 0) {
        alert("至少录入一条商品信息!");
        return false;
    }
    //获取参数
    var weight = $(":text[name='allsjweight']").val();//计费总重量
    formData.append("weight", weight);
    if (parseFloat(weight) <= 0) {
        alert('\"重量\"输入错误！');
        return false;
    }

    if (isNaN(parseFloat(weight))) {
        alert('\"重量\"输入错误！');
        return false;
    }

    var allsjweight = $(":text[name='allsjweight']").val();//实际总重量
    formData.append("sjweight", allsjweight);

    var allquantity = $(":text[name='allquantity']").val();//数量
    formData.append("quantity", allquantity);
    if (parseFloat(allcvalue) < 0) {
        alert('\"数量\"输入错误！');
        return false;
    }
    if (isNaN(parseFloat(allquantity))) {
        alert('\"数量\"输入错误！');
        return false;
    }
    var allcvalue = $(":text[name='allcvalue']").val();//价值
    formData.append("value", allcvalue);
    if (parseFloat(allcvalue) < 0) {
        alert('\"价值\"输入错误！');
        return false;
    }
    if (isNaN(parseFloat(allcvalue))) {
        alert('\"价值\"输入错误！');
        return false;
    }

    var insurance = $(":text[name='insurance']").val();//保险
    formData.append("insurance", insurance);

    if (parseFloat(insurance) < 0) {
        alert('\"保险\"输入错误！');
        return false;
    }
    if (isNaN(parseFloat(insurance))) {
        alert('\"保险\"输入错误！');
        return false;
    }
    var allcother = $(":text[name='allcother']").val();//价值
    formData.append("other", allcother);
    if (parseFloat(allcother) < 0) {
        alert('\"其它费用\"输入错误！');
        return false;
    }
    if (isNaN(parseFloat(allcother))) {
        alert('\"其它费用\"输入错误！');
        return false;
    }

    var allctariff = $(":text[name='allctariff']").val();//关税
    formData.append("tariff", allctariff);
    if (parseFloat(allctariff) < 0) {
        alert('\"关税\"输入错误！');
        return false;
    }
    if (isNaN(parseFloat(allctariff))) {
        alert('\"关税\"输入错误！');
        return false;
    }
    var totalremark = $(":text[name='totalremark']").val();//备注
    formData.append("remark", totalremark);

    var channelId = $("select[name='channellist']").val();//渠道id
    formData.append("channelId", channelId);
    if ((removenull(channelId) == "") || (isNaN(parseFloat(channelId))) || (parseFloat(channelId) <= 0)) {
        alert('\"渠道\"信息错误！');
        return false;
    }


    if (validate == false) {
        alert("信息填写不完整，请填写完整后提交!");
        return false;
    }




    //判断是否发送短信
    var automessage = 0;
    if ($(":checkbox[name='sendmessage']").is(':checked') == true) {
        automessage = 1;
    }

    formData.append("automessage", automessage);
    var httpRequest = new XMLHttpRequest();
    var url = "/admin/m_order/check_price";

    httpRequest.onreadystatechange = function () {
        if (4 == httpRequest.readyState) {
            if (200 == httpRequest.status) {
                //alert(httpRequest.responseText);
                obj = JSON.parse(httpRequest.responseText);

                if (obj.code == "200") {
                    //alert("核实价格:"+obj.data+"美元");
                    var message = removenull(obj.message);


                    if (!((online == "1") || (online == "2")))//不是提交在线置单
                    {
                        if (confirm("核实价格: " + obj.data + "美元,是否继续？          " + message)) {
                            var httpRequest1 = new XMLHttpRequest();
                            var url1 = "../../admin/m_order/ms_add";
                            httpRequest1.onreadystatechange = function () {
                                if (4 == httpRequest1.readyState) {
                                    if (200 == httpRequest1.status) {
                                        var obj1 = JSON.parse(httpRequest1.responseText);

                                        if (obj1.code == "200") {

                                            //window.location.href = "../admin/printpage.html"+"?"+"id="+obj1.data;
                                            //alert(obj1.data);
                                            if (printway == "1")//海关热敏4*6打印
                                            {
                                                //$("#iframePrint").attr("src", "printpageremin.html?id="+obj1.data);


                                                var iframeid = "iframePrint";
                                                document.getElementById(iframeid).contentWindow.document.write("");
                                                $("#" + iframeid).attr("src", "").attr("src", "printpageremin.html?id=" + obj1.data + "&ran=" + Math.random());
                                                beginPrint(iframeid, "Full-Page");
                                                alert("打印海关运单成功！");
                                                clearmenshidadan();

                                            }
                                            else if (printway == "0")//打印A4纸
                                            {
                                                //$("#iframePrint").attr("src", "printpage.html?id="+obj1.data+"&ran="+Math.random());
                                                //$("#iframePrint").attr("src", "").attr("src", "printpage.html?id=" + obj1.data + "&ran=" + Math.random());
                                                var iframeid = "iframePrint";
                                                document.getElementById(iframeid).contentWindow.document.write("");
                                                $("#" + iframeid).attr("src", "").attr("src", "printpage.html?id=" + obj1.data + "&ran=" + Math.random());
                                                beginPrint(iframeid);
                                                alert("打印A4运单成功！");
                                                clearmenshidadan();
                                            }
                                            else//2及其它表示打印普通4*6运单
                                            {
                                                //$("#iframePrint").attr("src", "printpage64.html?id="+obj1.data+"&ran="+Math.random());
                                                var iframeid = "iframePrint";
                                                document.getElementById(iframeid).contentWindow.document.write("");
                                                $("#" + iframeid).attr("src", "").attr("src", "printpage64.html?id=" + obj1.data + "&ran=" + Math.random());
                                                beginPrint(iframeid);
                                                alert("打印普通热敏运单成功！");
                                                clearmenshidadan();
                                            }
                                        } else if ("607" == obj1.code) {
                                            alert("您尚未登录或登录已失效！");
                                            adminlogout();
                                        }
                                        else {
                                            alert("提交出错，出错原因：" + obj1.message);
                                        }

                                        refreshDataGrid_history(postData);//刷新历史数据
                                    }

                                }
                            }
                            httpRequest1.open("post", url1, true);
                            httpRequest1.send(formData);

                        }
                    }
                    else {
                        if (confirm("在线置单运单核实价格: " + obj.data + "美元,是否继续？          " + message)) {
                            var httpRequest1 = new XMLHttpRequest();
                            var url1 = "/admin/m_order/ms_modify_online";
                            httpRequest1.onreadystatechange = function () {
                                if (4 == httpRequest1.readyState) {
                                    if (200 == httpRequest1.status) {
                                        var obj1 = JSON.parse(httpRequest1.responseText);

                                        if (obj1.code == "200") {


                                            if (confirm("是否直接打印?")) {
                                                if (default_print_button_id == "2")//海关热敏4*6打印
                                                {
                                                    //$("#iframePrint").attr("src", "printpageremin.html?id="+obj1.data);


                                                    var iframeid = "iframePrint";
                                                    document.getElementById(iframeid).contentWindow.document.write("");
                                                    $("#" + iframeid).attr("src", "").attr("src", "printpageremin.html?id=" + obj1.data + "&ran=" + Math.random());
                                                    beginPrint(iframeid, "Full-Page");
                                                    alert("打印海关运单成功！", focusprint(online));


                                                    //document.getElementById("searchonlineOrder").focus();//测试光标转到在线置单
                                                    clearmenshidadan();

                                                }
                                                else if (default_print_button_id == "0")//打印A4纸
                                                {
                                                    //$("#iframePrint").attr("src", "printpage.html?id="+obj1.data+"&ran="+Math.random());
                                                    //$("#iframePrint").attr("src", "").attr("src", "printpage.html?id=" + obj1.data + "&ran=" + Math.random());
                                                    var iframeid = "iframePrint";
                                                    document.getElementById(iframeid).contentWindow.document.write("");
                                                    $("#" + iframeid).attr("src", "").attr("src", "printpage.html?id=" + obj1.data + "&ran=" + Math.random());
                                                    beginPrint(iframeid);

                                                    alert("打印A4运单成功！", focusprint(online));
                                                    clearmenshidadan();
                                                }
                                                else//2及其它表示打印普通4*6运单
                                                {
                                                    //$("#iframePrint").attr("src", "printpage64.html?id="+obj1.data+"&ran="+Math.random());
                                                    var iframeid = "iframePrint";
                                                    document.getElementById(iframeid).contentWindow.document.write("");
                                                    $("#" + iframeid).attr("src", "").attr("src", "printpage64.html?id=" + obj1.data + "&ran=" + Math.random());
                                                    beginPrint(iframeid);
                                                    alert("打印普通热敏运单成功！", focusprint(online));
                                                    clearmenshidadan();
                                                }
                                            }
                                            clearmenshidadan();//成功的直接清空




                                        } else if ("607" == obj1.code) {
                                            alert("您尚未登录或登录已失效！");
                                            adminlogout();
                                        }
                                        else {
                                            alert("提交出错，出错原因：" + obj1.message);
                                        }

                                        refreshDataGrid_history(postData);//刷新历史数据
                                        /*if(online=="1")
										{
											document.getElementById("searchonlineOrder").focus();
										}
										else if(online=="2")
										{
											document.getElementById("searchshangmenOrder").focus();
										}*/
                                    }

                                }
                            }
                            httpRequest1.open("post", url1, true);
                            httpRequest1.send(formData);

                        }
                    }
                } else if ("607" == obj.code) {
                    alert("您尚未登录或登录已失效！");
                    adminlogout();
                }
                else {
                    alert("提交出错，出错原因：" + obj.message);
                }

                //nav95Click();
            }
        }
    };
    httpRequest.open("post", url, true);
    httpRequest.send(formData);

    return false;


}

var LODOP;
$(function () {
    setTimeout(function () {
        LODOP = getLodop();
    }, 2000);
});

function getMyLodop() {
    if (LODOP == undefined) {
        LODOP = getLodop();
    }
}

// printMode参数
//“Full-Width” –宽度按纸张的整宽缩放；
//“Full-Height”–高度按纸张的整高缩放：
//“Full-Page” –按整页缩放，也就是既按整宽又按整高缩放；
//此外还可以按具体百分比例，格式为“Width:XX%;Height:XX%”或“XX%”

function beginPrint(iframeid, printMode) {
    getMyLodop();
    var win = {};
    win = document.getElementById(iframeid).contentWindow;
    var isNotPrint = true;

    //if (printMode == undefined) { printMode = "100%" }
    if (printMode == undefined) { printMode = "Full-Page"; }
    var number = 0;
    var t = setInterval(function () {
        if (isNotPrint && win.isLoad) {
            try {
                if (LODOP == undefined) {
                    isNotPrint = false;
                    win.print();
                } else {
                    isNotPrint = false;
                    LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT", printMode);
                    ////ADD_PRINT_HTM(Top,Left,Width,Height,strHtmlContent)
                    LODOP.ADD_PRINT_HTM("0.5cm", "0.5cm", "RightMargin:0.5cm", "BottomMargin:0.5cm", win.document.documentElement.innerHTML);
                    // LODOP.PREVIEW();//预览打印
                    LODOP.PRINT();//直接打印

                }
                clearInterval(t);
            } catch (e) {
                isNotPrint = true;
                console.log(e.message);
            }
        }
        number++;
        if (number > 20) {
            clearInterval(t);
            alert("时间过长，打印失败！");
            return false;
        }
    }, 300);
}

/*function a4Print(id) {
    var iframeid = "iframePrint1";
    $("#" + iframeid).attr("src", "").attr("src", "printpage.html?id=" + id + "&ran=" + Math.random());
    beginPrint(iframeid);
}


function hot4x6(id) {
    var iframeid = "iframePrint3";
    $("#" + iframeid).attr("src", "").attr("src", "printpage64.html?id=" + id + "&ran=" + Math.random());
    beginPrint(iframeid);
}

function haiguanPrint(id) {
    var iframeid = "iframePrint2";
    $("#" + iframeid).attr("src", "").attr("src", "printpageremin.html?id=" + id + "&ran=" + Math.random());
    beginPrint(iframeid, "Full-Page");
}
*/

//省市区选择
$(function () {

    //选择省
    $("#receiverInfoTable input[name=state]").bigAutocomplete({
        data: $.areaData.p,
        //url: "areadata.html",//可以请求到后台，返回的格式{"data":[{"title":"广西"}]}
        callback: function (p) {
        }
    });


    //选择市
    $("#receiverInfoTable input[name=city]").focusin(function () {
        $(this).bigAutocomplete({
            data: $.areaData.c[$("#receiverInfoTable input[name=state]").val()]
        });
    });

    //县区
    $("#receiverInfoTable input[name=dist]").focusin(function () {
        $(this).bigAutocomplete({
            data: $.areaData.a[$("#receiverInfoTable input[name=state]").val() + "-" + $("#receiverInfoTable input[name=city]").val()]
        });
    });
});



//清空门市运单列表
function clearmenshidadan() {
    //清空发件人搜索框
    $("#senderinfo").val("");
    $("#btnSender").click();

    //清空收件人搜索框
    $("#receiverinfo").val("");
    $("#btnReceiver").click();

    //清空寄件人和收件人
    clearInputData("#senderTableInfo");
    clearInputData("#receiverInfoTable");
    //恢复选择第一个option
    loadChannelSelectOption();
    //$("#removecommudityitem").click();//删除新添加

    $("select[name='commodityName']").each(
			function () {

			    $(this).parent().parent().find(":text[name='cproductname']").val("");//清空品名
			    $(this).parent().parent().find(":text[name='cbrandname']").val("");//清空品牌
			    $(this).parent().parent().find(":text[name='cquantity']").val("");//清空数量
			    $(this).parent().parent().find(":text[name='cweight']").val("");//清空重量
			    $(this).parent().parent().find(":text[name='cvalue']").val("");//清空价值
			    $(this).parent().parent().find(":text[name='ctariff']").val("0");//清空价值
			    $(this).parent().parent().find(":text[name='cother']").val("0");//清空其它费用
			    $(this).parent().parent().find(":text[name='cremark']").val("");//清空备注

			});
    $("input[name='insurance']").val("0");//清空保险
    $("input[name='maxinsurance']").val("0");//清空保险额度
    $("input[name='totalremark']").val("");//清空总备注
    $("input[name='cardid']").val("");//清空身份证号
    $("#hcimage").attr("src", "");//图片
    $("#zmimage").attr("src", "");//图片
    $("#fmimage").attr("src", "");//图片
    $(":file[name='hcpicture']").val("");//图片
    $(":file[name='zmpicture']").val("");//图片
    $(":file[name='fmpicture']").val("");//图片


    var showblock = $("#receiverIdTable").attr("style");
    if (showblock == "display: block;" || showblock == "")//隐藏身份证信息
    {
        $("#cardinfoshow").click();
    }

    $("#searchonlineOrder").val("");
    $("#searchshangmenOrder").val("");
    $("#searchonlineOrder").val("");
    $("#onlineorderuserinfo").html("");
    $("#print_online_order").hide();
    commuditydataChange();


    if (printA4_id == 2) {
        $("#printA4_id").show();
    }
    else {
        $("#printA4_id").hide();
    }
    if (print4x6_id == 2) {
        $("#print4x6_id").show();
    }
    else {
        $("#print4x6_id").hide();
    }
    if (printsorder_id == 2) {
        $("#printsorder_id").show();
    }
    else {
        $("#printsorder_id").hide();
    }

}
function clearreturnbegin() {
    window.location.href = "menshidadan.html";
}




//历史运单操作
var yundangrid_history_selector = "#yundangrid_history-table";
var postData = {};
var jsonData = [];
function history_orders() {


    var yundanpager_history_selector = "#yundangrid_history-pager";
    var init = false;
    $(window).resize(function () {
        $(yundangrid_history_selector).setGridWidth($(".col-xs-12").width() - 20, true);
    });
    //loadseachinfo();//装载其它信息
    postData = getEelementData("#divAdvanceSearch");
    jQuery(yundangrid_history_selector).jqGrid({
        url: "/admin/m_order/search_emp",
        // url: "WebForm1.aspx",
        datatype: "json",
        height: "450",
        mtype: "post",
        postData: postData,
        colNames: ['运单号', '发件人', '收件人', '重量', '价格', '状态', '修改状态', "支付状态", "问题备注", "操作"],
        colModel: [
            //{ name: 'state', index: 'state', width: 0, hidden: "true" },
            {
                name: 'orderId', index: 'orderId', width: 100,
                formatter: function (cellvalue, options, row) {
                    return '<a href="javascript:void(0)" onclick="showdetail(' + row.id + ')">' + cellvalue + '</a>';
                }
            },
            {
                name: 'suserId', index: 'suserId', width: 110,//发件人信息

                formatter: function (cellvalue, options, row) {
                    if (removenull(row.suser) != "")//发件人用户id不为空
                    {
                        return removenull(row.suser.name) + "/" + removenull(row.suser.phone);
                    }
                    else {
                        return "";
                    }

                }
            },
            {
                name: 'ruserId', index: 'ruserId', width: 110,//收件人信息

                formatter: function (cellvalue, options, row) {
                    //alert(row.ruser);
                    if (removenull(row.ruser) != "")//发件人用户id不为空
                    {
                        return removenull(row.ruser.name) + "/" + removenull(row.ruser.phone);
                    }
                    else {
                        return "";
                    }

                }
            },



            {
                name: 'weight', index: 'weight', width: 80
            },

            {
                name: 'totalmoney', index: 'totalmoney', width: 80
            },
            {
                name: 'state', index: 'state', width: 80,
                formatter: function (cellvalue, options, row) {

                    return morderstatemap(cellvalue);
                }
            },
            {
                name: 'newstate',
                index: 'newstate',
                width: 100,
                formatter: function (cellvalue, options,
						row) {
                    // 若是动态生成，请从后台中返回到前端页面隐藏起来
                    //var select = '<select name="newstate_'+row.id+'" onchange="modifynewstate('+row.id+')"><option value="-1">选择新状态</option><option value="0">运单作废</option><option value="1">包裹异常</option><option value="2">已揽收</option></select>';
                    var select = '<select name="newstate"><option value="-1">选择新状态</option><option value="0">运单作废</option><option value="1">包裹异常</option><option value="2">已揽收</option></select>';
                    var mySelect = select;
                    var option = $(select).find(
							"option[value='" + row.state
									+ "']");
                    if (option.length > 0) {
                        var old = option[0].outerHTML;
                        var newOption = option.attr(
								"selected", true)[0].outerHTML;
                        mySelect = mySelect.replace(old,
								newOption);
                    }
                    return mySelect;
                }
            },
		    {
		        name: 'payornot',
		        index: 'payornot',
		        width: 100,
		        formatter: function (cellvalue, options,
						row) {
		            // 若是动态生成，请从后台中返回到前端页面隐藏起来
		            //var select = '<select name="payornot_'+row.id+'" onchange="modifynewstate('+row.id+')"><option value="-1">状态异常</option><option value="0">未付款</option><option value="1">已付款</option></select>';
		            var select = '<select name="newpayornot"><option value="-1">状态异常</option><option value="0">未付款</option><option value="1">已付款</option></select>';
		            var mySelect = select;
		            var option = $(select).find(
							"option[value='" + row.payornot
									+ "']");
		            if (option.length > 0) {
		                var old = option[0].outerHTML;
		                var newOption = option.attr(
								"selected", true)[0].outerHTML;
		                mySelect = mySelect.replace(old,
								newOption);
		            }
		            return mySelect;
		        }
		    },
			  {
			      name: 'qremark',
			      index: 'qremark',
			      width: 100,
			      formatter: function (cellvalue, options,
                          row) {
			          var str = "";
			          str = "<input name='newqremark' value='" + removenull(row.qremark) + "'/>";
			          return str;

			      }
			  },
			{
			    name: 'dowith',
			    index: 'dowith',
			    width: 100,
			    formatter: function (cellvalue, options,
						row) {
			        var edit = "";// 修改状态是任何人都可以修改自己运单的状态
			        // if (row.state < 2) {

			        if (row.state == 0 || row.state == "-10")//已作废的运单不能够修改
			        { }
			        else if (row.payWay == 0)//余额支付的不能修改
			        { }
			        else
			        {
			            edit = "<a style='width:60px;margin-left:2px;' class='btn btn-purple btn-sm'  href='javascript:void(0)' onclick='updateState("
                                + row.id
                                + ")' ><span class='icon-edit'></span>修改</a>";
			        }
			        return edit;

			    }
			},


        ],
        jsonReader: {
            root: "data.datas",
            page: "data.pageNow",
            total: "data.pageCount",
            records: "data.rowCount"
        },
        viewrecords: true,
        rowNum: 15,
        rowList: [15, 50, 100, 500, 1000],
        pager: yundanpager_history_selector,
        altRows: true,
        multiselect: true,
        //multikey: "ctrlKey",
        multiboxonly: true,

        loadComplete: function (json) {






            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
            }, 0);
            //防止出现水平滚动条
            if (init == false) {
                $("#jqgh_yundangrid_history-table_cb [role=checkbox]").show();
                //  $(yundangrid_history_selector + " [role=checkbox]").attr({ "type": "radio", "name": "receivergrid-radio" });



                $(".ui-jqgrid-bdiv").css({ 'overflow-x': 'hidden' });
                $(".ui-jqgrid-htable th:last").css({ "width": $(".ui-jqgrid-htable th:last").width() - 2 });
                $(".jqgfirstrow td:last").css({ "width": $(".jqgfirstrow td:last").width() - 2 });
                init = true;
            }
            $("#jqgh_yundangrid_history-table_cb [role=checkbox]").show();
            //隐藏不可删除和编辑的复选框
            var hiddenIds = [];
            if (json.code == "200") {
                if ((removenull(json.data) != "") && removenull(json.data.datas) != "") {
                    jsonData = json.data.datas;

                    var allrows = $(yundangrid_history_selector).jqGrid('getGridParam', 'records');//获取当前jqGrid的总记录数；
                    //alert(allrows);

                    //  var rows = jsonData.length;
                    $("#orders_no").html(allrows);
                    $("#orderschecked_no").html("0");
                    $("#orderschecked_weight").html("0");
                    $("#orderschecked_totalmoney").html("0");
                    //防止出现水平滚动条

                    /*for (var i = 0; i < jsonData.length; i++) {
		                var row = jsonData[i];
		                if (row.state >= 2) {
		                    hiddenIds.push(row.id);
		                }
		            }
		            hideJgridCheckbox(yundangrid_selector, hiddenIds);*/
                }
            }
            else if ("607" == json.code) {
                alert("您尚未登录或登录已失效！");
                top.location.href = "../admin/login.html";
            }
            else {
                alert("读取出错，原因：" + json.message);
            }



        },
        gridComplete: function () {
            //隐藏 city=已揽收的 复选框

        },
        onSelectRow: function (rowid, status) {

            var ids = $(yundangrid_history_selector).jqGrid('getGridParam', 'selarrrow');
            $(":hidden[name='checked_ids']").val(ids);
            $("#orderschecked_no").html(ids.length);
            var weight = 0;
            var totalmoney = 0;

            for (var i = 0; i < ids.length; i++) {
                var id = ids[i];
                var row = jsonData.getData(id);
                if (isNaN(row.totalmoney)) {
                    alert("运单号" + row.orderId + "价格无法识别,价格值为：" + row.totalmoney);
                }
                else {
                    totalmoney = parseFloat(totalmoney) + parseFloat(row.totalmoney);
                }


                if (isNaN(row.weight)) {
                    alert("运单号" + row.orderId + "重量无法识别,重量值为：" + row.weight);
                }
                else {
                    weight = parseFloat(weight) + parseFloat(row.weight);
                }
            }


            $("#orderschecked_weight").html(weight);
            $("#orderschecked_totalmoney").html(totalmoney);

        },


        onSelectAll: function (aRowids, status) {
            var rows = aRowids.length;
            if (status == false) { rows = 0; }
            $("#orderschecked_no").html(rows);
            if (status != false) {
                $(":hidden[name='checked_ids']").val(aRowids);

                var ids = aRowids;
                var weight = 0;
                var totalmoney = 0;

                for (var i = 0; i < ids.length; i++) {
                    var id = ids[i];
                    var row = jsonData.getData(id);
                    if (isNaN(row.totalmoney)) {
                        alert("运单号" + row.orderId + "价格无法识别,价格值为：" + row.totalmoney);
                    }
                    else {
                        totalmoney = parseFloat(totalmoney) + parseFloat(row.totalmoney);
                    }


                    if (isNaN(row.weight)) {
                        alert("运单号" + row.orderId + "重量无法识别,重量值为：" + row.weight);
                    }
                    else {
                        weight = parseFloat(weight) + parseFloat(row.weight);
                    }
                }


                $("#orderschecked_weight").html(weight);
                $("#orderschecked_totalmoney").html(totalmoney);
            }
            else {
                $(":hidden[name='checked_ids']").val("");
                $("#orderschecked_weight").html("0");
                $("#orderschecked_totalmoney").html("0");
            }


        },


        autowidth: true
    });


    //navButtons
    jQuery(yundangrid_history_selector).jqGrid('navGrid', yundanpager_history_selector,
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
                //var ids = $(yundangrid_history_selector).jqGrid('getGridParam', 'selarrrow');
                //alert(ids);

                // if((removenull(ids)=="")||(ids.length<1))
                //{
                //	alert("必须选择要删除的运单!");
                //	return false;
                // }

                // var aid = new Array(ids);
                //alert(aid);
                //var aaa=ids.split(",");
                //alert(aaa[0]);
                //alert(aaa[1]);
                /*  $.ajax({
                      type : "post",
                      url : "/admin/m_order/delete",
                      data : {
                          "ids" : ids
                      },
                      success : function(response) {
                          var code = response.code;
                          if ("200" == code) {
                              alert("删除成功!");
                               //todo 这里补充 和后台交互的代码
                              //删除完成后 在success回调中调用下面方法刷新数据列表
                              refreshDataGrid(postData);
                              }
                              else if("607" == code) {
                                  alert("您尚未登录或登录已失效！");
                                  top.location.href = "../admin/login.html";
                              }
                              else
                              {
                                  alert("删除失败，原因是："+response.message);
                              }
                          }});*/




            }
        }
    );


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

    //点击运单搜索
    $("#history_btnAdvanceSearch").click(function () {
        postData = getEelementData("#divAdvanceSearch");
        $(yundangrid_history_selector).jqGrid("setGridParam", { postData: postData }).trigger("reloadGrid");
    });
    //绑定运单输入框的enter事件进行搜索
    $("#history_userinfo").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#history_btnAdvanceSearch").click();
        }
    }));




}



//显示详细
function showdetail(id) {
    var row = jsonData.getData(id);
    setSpanText("#senderTableInfo1", row.suser);
    setSpanText("#receiverInfoTable1", row.ruser);
    setImgSrc("#receiverInfoTable1", row.ruser);//添加身份证图片
    // setSpanText("#receiverInfoTable", row.ruser);

    $("#goodsItemBody1").html("");
    for (var i = 0; i < row.detail.length; i++) {
        var teplament = $("#goodsItemTeplament1").html();
        $("#goodsItemBody1").append("<tr id=itemTr" + i + ">" + teplament + "</tr>");
        setSpanText("#itemTr" + i, row.detail[i]);
    }

    $("#divDetail span").each(function () {
        if ($(this).attr("name")) {
            $(this).css({ "display": "inline-block", "min-width": "60px" });
        }
    });




    $("#typeName").html(mordertypemap(row.type));
    $("#stateName").html(morderstatemap(row.state));
    $("#paywayName").html(morderpaywaymap(row.payWay));

    // $("#huizhonginfo span[name='storeName']").val(row.storeName);
    // $("#huizhonginfo span[name='employeeName']").val(row.employeeName);

    if (row.payornot == "0") {
        $("span[name='payornotName']").html("未付款");
    }
    else if (row.payornot == "1") {
        $("span[name='payornotName']").html("已付款");
    }
    else {
        $("span[name='payornotName']").html("状态异常");
    }


    dialog({
        title: "运单详细信息",
        content: $("#divDetail").html(),
        width: $(window).width() - 60,
        height: $("#divDetail").height() + 160,
        //cancelVal: '关闭',
        //cancel: true //为true等价于function(){}
    }).show();

    //运单信息，金额，总重量等从后台获取 然后调用下面方法把值设置到
    //yundanTableInfo 中，后台返回的name 和span的name一直即可
    setSpanText("#yundanTableInfo", row);
    setSpanText("#huizhonginfo", row);
    if (removenull(row.user) != "") {
        setSpanText("#belonguserid", row.user);
    }



}

function modifynewstate(id) {
    var state = $("select[name='newstate_" + id + "']").val();
    if (removenull(state) != "") {
        var str = "";
        if (state == "0")//运单作废
        {
            str = "运单作废";
        }
        else if (state == "1")//包裹异常
        {
            str = "包裹异常";
        }
        else if (state == "2")//已揽收
        {
            str = "已揽收";
        }

        if (str != "") {
            if (confirm("确定修改状态为\"" + str + "\"?")) {
                ///admin/m_order/modify_ms_state
                $.ajax({
                    type: "post",
                    url: "/admin/m_order/modify_ms_state",
                    data: {
                        "id": id,
                        "state": state
                    },
                    success: function (response) {
                        var code = response.code;
                        if ("200" == code) {
                            alert("修改成功!");
                            //todo 这里补充 和后台交互的代码
                            //删除完成后 在success回调中调用下面方法刷新数据列表
                            //refreshDataGrid(postData);
                        }
                        else if ("607" == code) {
                            alert("您尚未登录或登录已失效！");
                            top.location.href = "../admin/login.html";
                        }
                        else {
                            alert("修改失败，原因是：" + response.message);
                        }
                    }
                });
            }
        }
    }

}



//提交js对象参数刷新列表数据
function refreshDataGrid_history(data) {
    $(yundangrid_history_selector).jqGrid("setGridParam", { postData: data }).trigger("reloadGrid");
}


//修改历史信息
function updateState(id) {
    var fieldName = "newstate";
    var state = $(yundangrid_history_selector).find("[id=" + id + "]").find(
			"[aria-describedby$=" + fieldName + "] select").val();
    fieldName = "payornot";
    var newpayornot = $(yundangrid_history_selector).find("[id=" + id + "]").find(
			"[aria-describedby$=" + fieldName + "] select").val();

    var newqremark = $(yundangrid_history_selector).find("[id=" + id + "]").find("input[name='newqremark']").val();
    //var thirdno= $(yundangrid_selector).find("[id=" + id + "]").find("input[name='thirdNo']").val();

    if (confirm("确定修改？")) { } else { return false; }

    $.ajax({
        type: "post",
        url: "/admin/m_order/modify_ms_info",
        data: {
            "id": id,
            "state": state,
            "payornot": newpayornot,
            "qremark": newqremark
        },
        success: function (response) {
            var code = response.code;

            if ("200" == code) {

                alert("修改成功");
                refreshDataGrid_history(postData);
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


function printpagelist() {
    var ids = $(":hidden[name='checked_ids']").val();
    if (removenull(ids) == "" || ids == "[]") {
        alert("请选择要打印的运单后操作!");
        return false;
    }
    $("#iframePrint").attr("src", "printpagelist.html?id=" + ids + "&ran=" + Math.random());
}
var printA4_id = 2;
var print4x6_id = 2;
var printsorder_id = 2;
//获取显示或隐藏的打印按钮及默认打印
function printbuttoncontrol() {
    $.ajax({
        type: "post",
        url: "/admin/ms_control/getself",
        success: function (response) {
            var code = response.code;
            if ("200" == code) {
                var self = response.data;

                if (self != null) {
                    if (removenull(self.printItems) != "") {
                        var aaa = self.printItems.split(";");
                        for (var i = 0; i < aaa.length; i++) {
                            if (aaa[i] == "0") {
                                printA4_id = 1;
                            }
                            else if (aaa[i] == "1") {
                                print4x6_id = 1;
                            }
                            else if (aaa[i] == "2") {
                                printsorder_id = 1;
                            }
                        }
                    }
                    else {
                        printA4_id = 0;
                        print4x6_id = 0;
                        printsorder_id = 0;
                    }
                    default_print_button_id = self.keydownItem;//设置默认打印的运单类型
                    if (printA4_id == 1) {
                        printA4_id = 2;
                        $("#printA4_id").show();
                    }
                    else {
                        printA4_id = 0;
                        $("#printA4_id").hide();
                    }
                    if (print4x6_id == 1) {
                        print4x6_id = 2;
                        $("#print4x6_id").show();
                    }
                    else {
                        print4x6_id = 0;
                        $("#print4x6_id").hide();
                    }
                    if (printsorder_id == 1) {
                        printsorder_id = 2;
                        $("#printsorder_id").show();
                    }
                    else {
                        printsorder_id = 0;
                        $("#printsorder_id").hide();
                    }

                }
            }
            else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                top.location.href = "../admin/login.html";
            }
            else {
                alert("获取数据失败，原因是：" + response.message);
            }
        }
    });

}



//注册回车直接打印
function reg_bind_keydown_print() {

    //价格市计算
    $(":text[name='cquantity']").change(commuditydataChange);
    $(":text[name='cweight']").change(commuditydataChange);
    $(":text[name='cvalue']").change(commuditydataChange);
    $(":text[name='ctariff']").change(commuditydataChange);
    $(":text[name='cother']").change(commuditydataChange);
    $(":text[name='insurance']").change(countpremiummoney_user);

    $("#senderTableInfo").find(":text[name='name']").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            keydown_print();
        }
    }));//发件人回车
    $("#senderTableInfo").find(":text[name='phone']").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            keydown_print();
        }
    }));//电话
    $("#senderTableInfo").find(":text[name='address']").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            keydown_print();
        }
    }));//地址
    $("#senderTableInfo").find(":text[name='city']").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            keydown_print();
        }
    }));//地址
    $("#senderTableInfo").find(":text[name='state']").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            keydown_print();
        }
    }));//地址
    $("#senderTableInfo").find(":text[name='zipcode']").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            keydown_print();
        }
    }));//邮编
    $("#senderTableInfo").find(":text[name='email']").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            keydown_print();
        }
    }));//邮箱
    $("#senderTableInfo").find(":text[name='company']").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            keydown_print();
        }
    }));//公司名称




    //收件人回车事件 
    $("#receiverInfoTable").find(":text[name='name']").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            keydown_print();
        }
    }));
    $("#receiverInfoTable").find(":text[name='phone']").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            keydown_print();
        }
    }));
    $("#receiverInfoTable").find(":text[name='address']").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            keydown_print();
        }
    }));
    $("#receiverInfoTable").find(":text[name='dist']").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            keydown_print();
        }
    }));
    $("#receiverInfoTable").find(":text[name='city']").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            keydown_print();
        }
    }));
    $("#receiverInfoTable").find(":text[name='state']").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            keydown_print();
        }
    }));
    $("#receiverInfoTable").find(":text[name='zipcode']").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            keydown_print();
        }
    }));
    $("#receiverIdTable").find(":text[name='cardid']").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            keydown_print();
        }
    }));


    //商品类型回车实现
    $("input[name='cproductname']").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            keydown_print();
        }
    }));

    $("input[name='cbrandname']").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            keydown_print();
        }
    }));
    $("input[name='cquantity']").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            keydown_print();
        }
    }));

    $("input[name='cweight']").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            keydown_print();
        }
    }));
    $("input[name='cvalue']").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            keydown_print();
        }
    }));

    $("input[name='ctariff']").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            keydown_print();
        }
    }));

    $("input[name='cother']").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            keydown_print();
        }
    }));

    $("input[name='cremark']").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            keydown_print();
        }
    }));

    //保险绑定
    $("input[name='insurance']").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            keydown_print();
        }
    }));

    //备注绑定
    $("input[name='totalremark']").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            keydown_print();
        }
    }));

}

//取消注册回车直接打印，调用绑定之前先调用此函数
function reg_unbind_keydown_print() {

    //v
    $("#senderTableInfo").find(":text[name='name']").unbind();//发件人回车
    $("#senderTableInfo").find(":text[name='phone']").unbind();//电话
    $("#senderTableInfo").find(":text[name='address']").unbind();//地址
    $("#senderTableInfo").find(":text[name='city']").unbind();//地址
    $("#senderTableInfo").find(":text[name='state']").unbind();//地址
    $("#senderTableInfo").find(":text[name='zipcode']").unbind();//邮编
    $("#senderTableInfo").find(":text[name='email']").unbind();//邮箱
    $("#senderTableInfo").find(":text[name='company']").unbind();//公司名称




    //收件人回车事件 
    $("#receiverInfoTable").find(":text[name='name']").unbind();
    $("#receiverInfoTable").find(":text[name='phone']").unbind();
    $("#receiverInfoTable").find(":text[name='address']").unbind();
    $("#receiverInfoTable").find(":text[name='dist']").unbind();
    $("#receiverInfoTable").find(":text[name='city']").unbind();
    $("#receiverInfoTable").find(":text[name='state']").unbind();
    $("#receiverInfoTable").find(":text[name='zipcode']").unbind();
    $("#receiverIdTable").find(":text[name='cardid']").unbind();


    //商品类型回车实现
    $("input[name='cproductname']").unbind();

    $("input[name='cbrandname']").unbind();
    $("input[name='cquantity']").unbind();

    $("input[name='cweight']").unbind();
    $("input[name='cvalue']").unbind();

    $("input[name='ctariff']").unbind();

    $("input[name='cother']").unbind();

    $("input[name='cremark']").unbind();

    //保险绑定
    $("input[name='insurance']").unbind();

    //备注绑定
    $("input[name='totalremark']").unbind();

}

//执行回车直接打印操作
function keydown_print() {
    var orderid = $("#searchonlineOrder").val();
    var sorderid = $("#searchshangmenOrder").val();
    if ((removenull(orderid) && (orderid.trim().length > 4)) || (removenull(sorderid) && (sorderid.trim().length > 4)))//判断是不是网上置单,运单号不可能小于4，所以加宽判定标准
    {
        submitwanshanOrder();
    }
    else {
        $("#searchonlineOrder").val("");
        $("#searchshangmenOrder").val("");
        if (default_print_button_id == "0")//A4打印
        {
            submitmsorderA4();
        }
        else if (default_print_button_id == "1")//4x6打印
        {
            submitMrder4x6();
        }
        else if (default_print_button_id == "2")//海关打印
        {
            submitmsorder46();
        }
    }
}





var onlineorder_postData = {};
function getOrderById() {
    $.ajax({
        type: "get",
        url: "/admin/m_order/get_one_by_id",
        data: {
            "id": request("id")
        },
        success: function (
						response) {
            var code = response.code;
            if (code == "200") {
                if (response.data != null) {

                    $("#print_online_order").show();

                    $("#print_online_order strong").html("提交复制置单");

                    $("#printA4_id").hide();
                    $("#print4x6_id").hide();
                    $("#printsorder_id").hide();

                    onlineorder_postData = response.data;

                    //装载发件人
                    setInputData("#senderTableInfo", response.data.suser);

                    //装载接收人
                    setInputData("#receiverInfoTable", response.data.ruser);
                    setInputData("#receiverIdTable", response.data.ruser);

                    //身份信息
                    var flag = 0;
                    if (removenull(response.data.ruser.cardid) == "") {
                        $("#receiverIdTable").find(":text[name='cardid']").val("");//身份证号
                    }
                    else {
                        $("#receiverIdTable").find(":text[name='cardid']").val(response.data.ruser.cardid);//身份证号
                        flag = 1;
                    }
                    if (removenull(response.data.ruser.cardtogether) == "") {
                        $("#receiverIdTable").find("img[id='hcimage']").attr("src", "");
                    }
                    else {
                        $("#receiverIdTable").find("img[id='hcimage']").attr("src", response.data.ruser.cardtogether);
                        flag = 1;
                    }

                    if (removenull(response.data.ruser.cardurl) == "") {
                        $("#receiverIdTable").find("img[id='zmimage']").attr("src", "");
                    }
                    else {
                        $("#receiverIdTable").find("img[id='zmimage']").attr("src", response.data.ruser.cardurl);
                        flag = 1;
                    }

                    if (removenull(response.data.ruser.cardother) == "") {
                        $("#receiverIdTable").find("img[id='fmimage']").attr("src", "");
                    }
                    else {
                        $("#receiverIdTable").find("img[id='fmimage']").attr("src", response.data.ruser.cardother);
                        flag = 1;
                    }

                    if (flag == 1)//包含身份证信息，展开
                    {
                        var showblock = $("#receiverIdTable").attr("style");
                        if (showblock == "display: block;" || showblock == "")//
                        {
                            //$("#cardinfoshow").click();
                        }
                        else//原来是隐藏的，要展示出来
                        {
                            $("#cardinfoshow").click();
                        }
                    }

                    //渠道的选择
                    $("select[name='channellist']").val(response.data.channelId);
                    //loadCommoditySelectOption_online();//重新选择渠道





                    //开始插入商品选择
                    var commoditylist = document.getElementsByName("commodityName");
                    if ((commoditylist.length - 1) < onlineorder_postData.detail.length)//商品的条数大于现存的，要添加商品按钮
                    {
                        for (var i = 0; i < onlineorder_postData.detail.length - commoditylist.length + 3; i++) {
                            addGoodsItem();
                        }
                    }
                    for (var i = 0; i < onlineorder_postData.detail.length; i++) {
                        $(commoditylist[i + 1]).val(onlineorder_postData.detail[i].commodityId);

                        $(commoditylist[i + 1]).parent().parent().find(":text[name='cproductname']").val(onlineorder_postData.detail[i].productName);//获取品名
                        $(commoditylist[i + 1]).parent().parent().find(":text[name='cbrandname']").val(onlineorder_postData.detail[i].brands);//获取品牌

                        if (parseFloat(onlineorder_postData.detail[i].quantity) == 0)
                        { }
                        else
                        {
                            $(commoditylist[i + 1]).parent().parent().find(":text[name='cquantity']").val(onlineorder_postData.detail[i].quantity);//获取数量	
                        }
                        if (parseFloat(onlineorder_postData.detail[i].weight) == 0)
                        { }
                        else
                        {
                            //onlineorder_postData.detail[i].weight
                            $(commoditylist[i + 1]).parent().parent().find(":text[name='cweight']").val("");//获取重量	
                        }
                        if (parseFloat(onlineorder_postData.detail[i].value) == 0)
                        { }
                        else
                        {
                            $(commoditylist[i + 1]).parent().parent().find(":text[name='cvalue']").val(onlineorder_postData.detail[i].value);//获取价值
                        }


                        if (parseFloat(onlineorder_postData.detail[i].tariff) == 0) {
                            $(commoditylist[i + 1]).parent().parent().find(":text[name='ctariff']").val("0");//获取关税
                        }
                        else {
                            $(commoditylist[i + 1]).parent().parent().find(":text[name='ctariff']").val(onlineorder_postData.detail[i].tariff);//获取关税
                        }
                        if (parseFloat(onlineorder_postData.detail[i].other) == 0) {
                            $(commoditylist[i + 1]).parent().parent().find(":text[name='cother']").val(onlineorder_postData.detail[i].other);//获取其它费用
                        }
                        else {
                            $(commoditylist[i + 1]).parent().parent().find(":text[name='cother']").val(onlineorder_postData.detail[i].other);//获取其它费用
                        }
                        //$(commoditylist[i+1]).parent().parent().find(":text[name='ctariff']").val(onlineorder_postData.detail[i].tariff);//获取关税
                        //$(commoditylist[i+1]).parent().parent().find(":text[name='cother']").val(onlineorder_postData.detail[i].other);//获取其它费用
                        $(commoditylist[i + 1]).parent().parent().find(":text[name='cremark']").val(onlineorder_postData.detail[i].remark);//获取备注
                    }
                    commuditydataChange();


                    $("input[name='insurance']").val(response.data.insurance);
                    $("input[name='totalremark']").val(response.data.remark);
                    countpremiummoney_user();
                    document.getElementById("cweightfocus").value = "";
                    document.getElementById("cweightfocus").focus();


                }
            }
            else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                adminlogout();
            }
            else {
                alert("获取信息失败，出错原因：" + response.message);
                $("#searchshangmenOrder").val("");
                $("#searchonlineOrder").val("");
                clearmenshidadan();
                //window.location.href = "zaixianzhidanlist.html";
            }


        }
    });
}

//由在线置单引发的添加商品
function loadCommoditySelectOption_online() {

    $("select[name='commodityName']").html("");
    var cid = $("select[name='channellist']").val();
    var additivePrice = $("select[name='channellist']").find("option:selected").attr("additivePrice");
    $(":text[name='addtivePrice']").val(additivePrice);
    $.ajax({
        post: "get",
        url: "/admin/commudityPrice/getpricebyself",
        data: {
            "cid": cid
        },
        success: function (response) {
            var code = response.code;
            if ("200" == code) {

                // showCommodityList(response.data);;
                var str = "";
                $.each(response.data, function () {
                    str += "<option value='" + this.id + "'>"
							+ this.commudityAdmin.name + "</option>";
                });
                $("select[name='commodityName']").html(str);



                //onlineorder_postData 装载完商品后，添加在线的商品

                //var commoditylist=document.getElementsByName("commodityName");
                var commoditylist = document.getElementsByName("commodityName");
                if ((commoditylist.length - 1) < onlineorder_postData.detail.length)//商品的条数大于现存的，要添加商品按钮
                {
                    for (var i = 0; i < onlineorder_postData.detail.length - commoditylist.length + 3; i++) {
                        addGoodsItem();
                    }
                }
                for (var i = 0; i < onlineorder_postData.detail.length; i++) {
                    $(commoditylist[i + 1]).val(onlineorder_postData.detail[i].commodityId);

                    $(commoditylist[i + 1]).parent().parent().find(":text[name='cproductname']").val(onlineorder_postData.detail[i].productName);//获取品名
                    $(commoditylist[i + 1]).parent().parent().find(":text[name='cbrandname']").val(onlineorder_postData.detail[i].brands);//获取品牌

                    if (parseFloat(onlineorder_postData.detail[i].quantity) == 0)
                    { }
                    else
                    {
                        $(commoditylist[i + 1]).parent().parent().find(":text[name='cquantity']").val(onlineorder_postData.detail[i].quantity);//获取数量	
                    }
                    if (parseFloat(onlineorder_postData.detail[i].weight) == 0)
                    { }
                    else
                    {
                        $(commoditylist[i + 1]).parent().parent().find(":text[name='cweight']").val(onlineorder_postData.detail[i].weight);//获取重量	
                    }
                    if (parseFloat(onlineorder_postData.detail[i].value) == 0)
                    { }
                    else
                    {
                        $(commoditylist[i + 1]).parent().parent().find(":text[name='cvalue']").val(onlineorder_postData.detail[i].value);//获取价值
                    }



                    if (parseFloat(onlineorder_postData.detail[i].tariff) == 0) {
                        $(commoditylist[i + 1]).parent().parent().find(":text[name='ctariff']").val("0");//获取关税
                    }
                    else {
                        $(commoditylist[i + 1]).parent().parent().find(":text[name='ctariff']").val(onlineorder_postData.detail[i].tariff);//获取关税
                    }

                    if (parseFloat(onlineorder_postData.detail[i].other) == 0) {
                        $(commoditylist[i + 1]).parent().parent().find(":text[name='cother']").val("0");//获取其它费用
                    }
                    else {
                        $(commoditylist[i + 1]).parent().parent().find(":text[name='cother']").val(onlineorder_postData.detail[i].other);//获取其它费用
                    }

                    //$(commoditylist[i+1]).parent().parent().find(":text[name='ctariff']").val(onlineorder_postData.detail[i].tariff);//获取关税
                    //$(commoditylist[i+1]).parent().parent().find(":text[name='cother']").val(onlineorder_postData.detail[i].other);//获取其它费用
                    $(commoditylist[i + 1]).parent().parent().find(":text[name='cremark']").val(onlineorder_postData.detail[i].remark);//获取备注
                }
                commuditydataChange();
                document.getElementById("cweightfocus").focus();
            } else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                adminlogout();
            }
        }
    });
}


function focusprint(online) {
    if (online == "1") {
        document.getElementById("searchonlineOrder").focus();//测试光标转到在线置单
    }
    else if (online == "2") {
        document.getElementById("searchshangmenOrder").focus();//测试光标转到上门收货
    }
}




