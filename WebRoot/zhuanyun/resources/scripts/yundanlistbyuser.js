
var yundangrid_selector = "#yundangrid-table";
//查询数据时向后台提交的参数
var postData = {};
//后台返回的结果
var jsonData = [];

//初始列表数据
$(function ($) {

    var yundanpager_selector = "#yundangrid-pager";
    var init = false;
    $(window).resize(function () {
        $(yundangrid_selector).setGridWidth($(".col-xs-12").width() - 20, true);
    });
   
    jQuery(yundangrid_selector).jqGrid({
        url: "/admin/m_order/search_paybyuser",
        //url: "testdata/yundanlistdata.html",
        datatype: "json",
        height: "auto",
        mtype: "get",
        postData: postData,
        colNames: ['订单号', '运单仓位', "转运单号", '会员', '余额','收件人', '总价钱', "重量", "创建时间", "操作"],
        colModel: [

            {
                name: 'orderId', index: 'orderId', width: 80,
                formatter: function (cellvalue, options, row) {
                    return '<a href="javascript:void(0)" onclick="showdetail(' + row.id + ')">' + cellvalue + '</a>';
                }
            },
            { name: 'sposition.position', index: 'sposition.position', width: 40},//运单操作仓位
              {
                  name: 'torderids', index: 'torderids', width: 80, formatter: function (cellvalue, options, row) {
                      var text = "";
                      if (row.torders != null && row.torders.length > 0) {
                          $.each(row.torders, function () {
                              if (text == "") {
                                  text = this.torderId;
                              }
                              else {
                                  text = text + "<br/>" + this.torderId;
                              }
                          });
                      }
                      return text;
                  }
              },
            {
                name: 'userId', index: 'userId', width: 100,//发件人信息

                formatter: function (cellvalue, options, row) {
                    if (removenull(row.user) != "")//发件人用户id不为空
                    {
                        return removenull(row.user.id) + "/" + removenull(row.user.realName)+"/" +removenull(row.user.phone);
                    }
                    else {
                        return "";
                    }

                }
            },
            
        
            {
                name: 'money', index: 'money', width: 60,//发件人信息

                formatter: function (cellvalue, options, row) {
                    if (removenull(row.user) != "")//发件人用户id不为空
                    {
                        return "$"+removenull(row.user.usdBalance) + "/￥" + removenull(row.user.rmbBalance);
                    }
                    else {
                        return "";
                    }

                }
            },
            {
                name: 'ruserId', index: 'ruserId', width: 100,//收件人信息

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
            { name: 'totalmoney', index: 'totalmoney', width: 40 },
             { name: 'weight', index: 'weight', width: 40 },

             { name: 'createDate', index: 'createDate', width: 80 },//创建时间
             {
                 name: 'operation', index: 'operation', width: 60, //align: "center",
                 formatter: function (cellvalue, options, row) {
                     //返回修改按钮
                	  var text = "<select><option value='0'>未处理</option><option value='1'>已处理</option>";
                      
                      
                      if ((row.confirm_user_pay == "1"||row.confirm_user_pay == 1)) {
  
                     	 text = "<select onchange='changeprocess(this)' oid='"+row.id+"'><option value='0'>未处理</option><option value='1' selected='selected'>已处理</option>";
                      }
                      else
                      {
                     	 text = "<select onchange='changeprocess(this)' oid='"+row.id+"'><option value='0' selected='selected'>未处理</option><option value='1' >已处理</option>";
                      }
                      return text;
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
        rowNum: 10,
        rowList: [10, 30, 50],
        pager: yundanpager_selector,
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
                $(".ui-jqgrid-bdiv").css({ 'overflow-x': 'hidden' });
                $(".ui-jqgrid-htable th:last").css({ "width": $(".ui-jqgrid-htable th:last").width() - 2 });
                $(".jqgfirstrow td:last").css({ "width": $(".jqgfirstrow td:last").width() - 2 });
                init = true;
            }
            //隐藏不可删除和编辑的复选框
            var hiddenIds = [];
            if (json.code == "200") {
                if ((removenull(json.data) != "") && removenull(json.data.datas) != "") {
                    jsonData = json.data.datas;
                    for (var i = 0; i < jsonData.length; i++) {
                        var row = jsonData[i];
                        if (row.state >= 2) {
                            hiddenIds.push(row.id);
                        }
                    }
                    hideJgridCheckbox(yundangrid_selector, hiddenIds);
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
            //设置没复选框的行不给选中
            if (status == true) {
                var display = $(yundangrid_selector).find("[id=" + rowid + "]").find("[role=checkbox]:hidden");
                if (display.length > 0) { $(yundangrid_selector).setSelection(rowid, false); }
            }

        },
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
                //alert(ids);

                if ((removenull(ids) == "") || (ids.length < 1)) {
                    alert("必须选择要删除的运单!");
                    return false;
                }

                // var aid = new Array(ids);
                //alert(aid);
                //var aaa=ids.split(",");
                //alert(aaa[0]);
                //alert(aaa[1]);
                $.ajax({
                    type: "post",
                    url: "/admin/m_order/delete",
                    data: {
                        "ids": ids
                    },
                    success: function (response) {
                        var code = response.code;
                        if ("200" == code) {
                            alert("删除成功!");
                            //todo 这里补充 和后台交互的代码
                            //删除完成后 在success回调中调用下面方法刷新数据列表
                            refreshDataGrid(postData);
                        }
                        else if ("607" == code) {
                            alert("您尚未登录或登录已失效！");
                            top.location.href = "../admin/login.html";
                        }
                        else {
                            alert("删除失败，原因是：" + response.message);
                        }
                    }
                });




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

    //点击运单搜索
    $("#btnyundan").click(function () {
        postData = getEelementData("#divyundan");
        $(yundangrid_selector).jqGrid("setGridParam", { postData: postData }).trigger("reloadGrid");
    });
    //绑定运单输入框的enter事件进行搜索
    $("#yundan_oid").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnyundan").click();
        }
    }));
    $("#yundan_sod").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnyundan").click();
        }
    }));
    $("#yundan_god").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnyundan").click();
        }
    }));

    //点击运单搜索
    $("#btnAdvanceSearch").click(function () {
        var data = getEelementData("#divAdvanceSearch");
        refreshDataGrid(data);
    });

    loadShelves();
});

//点击状态搜索
function searchState(state) {
    refreshDataGrid({ state: state });
}

//提交js对象参数刷新列表数据
function refreshDataGrid(data) {
    $(yundangrid_selector).jqGrid("setGridParam", { postData: data }).trigger("reloadGrid");
}

//显示详细
function showdetail(id) {
    var row = jsonData.getData(id);
    setSpanText("#senderTableInfo", row.suser);
    setSpanText("#receiverInfoTable", row.ruser);
    setImgSrc("#receiverInfoTable", row.ruser);//添加身份证图片
    // setSpanText("#receiverInfoTable", row.ruser);

    $("#goodsItemBody").html("");
    for (var i = 0; i < row.detail.length; i++) {
        var teplament = $("#goodsItemTeplament").html();
        $("#goodsItemBody").append("<tr id=itemTr" + i + ">" + teplament + "</tr>");
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
        padding: "20px",
        fixed: false,
        zIndex: 9999,
        // height: $("#divDetail").height() + 160,
        //cancelValue: '关闭',
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


//付款
function paybyadmin(id) {

    if (id == undefined) { top.window.tips("请选择需要付款的运单", false); return; }

    top.window.confirmDialog("确定付款此订单？", "提示", function () {


        $.ajax({
            type: "post",
            url: "/admin/m_order/user_pay",
            data: { id: id },
            success: function (response) {
                var code = response.code;
                if (code == "200") {
                   alert("支付成功，"+response.message);
                   
                    refreshDataGrid(postData);
                } else if ("607" == code) {
                    alert("您尚未登录或登录已失效！");
                    userlogout();
                } else {
                    // 失败
                    alert("支付失败，原因是:" + response.message);
                }


            }
        });



        //todo 这里补充 和后台交互的代码
        //删除完成后 在success回调中调用下面方法刷新数据列表

    });

}
 
function changeprocess(obj)
{
	var confirm_user_pay=$(obj).val();
	var id=$(obj).attr("oid");
    $.ajax({
        type: "post",
        url: "/admin/m_order/confirm_user_pay",
        data: { "confirm_user_pay": confirm_user_pay,"id":id},
        success: function (response) {
            var code = response.code;
            if (code == "200") {
               alert("修改成功，"+response.message);
               
                refreshDataGrid(postData);
            } else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                userlogout();
            } else {
                // 失败
                alert("支付失败，原因是:" + response.message);
            }


        }
    });
}