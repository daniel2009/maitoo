
var addressgrid_selector = "#addressgrid-table";
//查询数据时向后台提交的参数
var postData = {};
var jsonData;
var addresspager_selector = "#addressgrid-pager";
var init = false;

//初始列表数据
$(function ($) {
    if ($(window).width() < 640) {
        initPhoneGrid();
    } else {
        initGrid();
    }
    initClickEvent();
});

function initClickEvent() {
    //点击搜索
    $("#btnSearch").click(function () {
        postData = getEelementData("#divaddress");
        $(addressgrid_selector).jqGrid("setGridParam", { postData: postData }).trigger("reloadGrid");
    });
    //绑定输入框的enter事件进行搜索
    $("#keyword").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnSearch").click();
        }
    }));


    $("#btnAdd").click(function () {
        window.location.href = "zaixianzhidan.html?id=0";
    });


}

function initGrid() {
    $(window).resize(function () {
        $(addressgrid_selector).setGridWidth($(".page-content").width() - 2, true);
    });

    jQuery(addressgrid_selector).jqGrid({
        url: "/m_order/search_online_byUser",
        datatype: "json",
        height: "auto",
        mtype: "get",

        colNames: ['订单号', '发件人', '收件人', '收件地址', "货物品名", "创建日期", "操作"],
        colModel: [

            {
                name: 'orderId', index: 'orderId', width: 110, formatter: function (cellvalue, options, row) {
                    return '<a href="javascript:void(0)" onclick="showdetail(' + row.id + ')">' + cellvalue + '</a>';
                }
            },
            {
                name: 'sendperson', index: 'sendperson', width: 100, formatter: function (cellvalue, options, row) {
                    var text = "";
                    text = removenull(row.suser.name) + "/" + removenull(row.suser.phone);
                    return text;

                }
            },
            {
                name: 'revperson', index: 'revperson', width: 100, formatter: function (cellvalue, options, row) {
                    var text = "";
                    text = removenull(row.ruser.name) + "/" + removenull(row.ruser.phone);
                    return text;

                }
            },
            {
                name: 'address', index: 'address', width: 180, formatter: function (cellvalue, options, row) {
                    var text = "";
                    text = removenull(row.ruser.state) + removenull(row.ruser.city) + removenull(row.ruser.dist) + removenull(row.ruser.address);
                    return text;

                }
            },
            {
                name: 'detail', index: 'detail', width: 150, formatter: function (cellvalue, options, row) {
                    var text = "";
                    if (row.detail != null) {
                        for (var i = 0; i < row.detail.length; i++) {
                            if (text == "") {
                                text = row.detail[i].productName + "*" + row.detail[i].quantity;
                            }
                            else {
                                text = text + ";" + row.detail[i].productName + "*" + row.detail[i].quantity;
                            }
                        }
                    }
                    return text;
                }
            },

            {
                name: 'createDate', index: 'createDate', width: 100
            },

             {
                 name: 'op', index: 'op', width: 320, formatter: function (cellValue, option, row) {
                     //返回修改按钮
                     var edit = "<button  style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"update('" + row.id + "')\"  ><span class='icon-edit'></span>修改</button>";
                     var del = "<button style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"del('" + row.id + "')\"  ><span class='fa fa-times'></span>删除</button>";
                     var print = "<button style='margin:1px 0px 0px 5px;padding:0px 3px;width:80px' class='btn btn-purple btn-sm' type='button'  onclick=\"a4Print_user_online('" + row.id + "')\"  ><span class='fa fa-print'></span>A4打印</button>";
                     var print46 = "<button style='margin:1px 0px 0px 5px;padding:0px 3px;width:120px' class='btn btn-purple btn-sm' type='button'  onclick=\"hot4x6Print_user_online('" + row.id + "')\"  ><span class='fa fa-print'></span>热敏4x6打印</button>";
                     return edit + del + print + print46;
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





            if (data.code == "200")//返回成功
            {
                if (data.data != null) {
                    jsonData = data.data.datas;
                }

            }
            else if ("607" == data.code) {
                alert("您尚未登录或登录已失效！");
                userlogout();
            } else {
                alert("修改失败，原因：" + data.message);
            }


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
}

function initPhoneGrid() {
    $(window).resize(function () {
        $(addressgrid_selector).setGridWidth($(".page-content").width() - 2, true);
    });

    jQuery(addressgrid_selector).jqGrid({
        url: "/m_order/search_online_byUser",
        datatype: "json",
        height: "auto",
        mtype: "get",

        colNames: ['订单号', '收件人',"货物品名", "操作"],
        colModel: [

            {
                name: 'orderId', index: 'orderId', width: 150
            },
            {
                name: 'revperson', index: 'revperson', width: 100, formatter: function (cellvalue, options, row) {
                    var text = "";
                    text = removenull(row.ruser.name) + "/" + removenull(row.ruser.phone);
                    return text;

                }
            },
            
            {
                name: 'detail', index: 'detail', width: 100, formatter: function (cellvalue, options, row) {
                    var text = "";
                    if (row.detail != null) {
                        for (var i = 0; i < row.detail.length; i++) {
                            if (text == "") {
                                text = row.detail[i].productName + "*" + row.detail[i].quantity;
                            }
                            else {
                                text = text + ";" + row.detail[i].productName + "*" + row.detail[i].quantity;
                            }
                        }
                    }
                    return text;
                }
            },

             {
                 name: 'op', index: 'op', width: 100, formatter: function (cellValue, option, row) {
                     //返回修改按钮
                     var edit = "<button  style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"update('" + row.id + "')\"  ><span class='icon-edit'></span>修改</button>";
                     var del = "<button style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"del('" + row.id + "')\"  ><span class='fa fa-times'></span>删除</button>";
                     var detail = "<button style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"showdetail(" + row.id + ")\"  ><span class='fa fa-times'></span>详情</button>";

                     return edit+del;
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





            if (data.code == "200")//返回成功
            {
                if (data.data != null) {
                    jsonData = data.data.datas;
                }

            }
            else if ("607" == data.code) {
                alert("您尚未登录或登录已失效！");
                userlogout();
            } else {
                alert("修改失败，原因：" + data.message);
            }


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
}


//提交js对象参数刷新列表数据
function refreshDataGrid(data) {
    $(addressgrid_selector).jqGrid("setGridParam", { postData: data }).trigger("reloadGrid");
}

//点击编辑
function update(id) {
    //zaixianzhidan 通过id来区分是修改还是新增. id=0为新增，id>0为修改
    window.location.href = "zaixianzhidanModify.html?id=" + id;
}

//删除
function del(id) {

    if (id == undefined) { top.window.tips("请选择需要删除的行", false); return; }

    top.window.confirmDialog("确定删除选择的数据？", "删除提示", function () {


        $.ajax({
            type: "post",
            url: "/m_order/user_delete",
            data: { id: id },
            success: function (response) {
                var code = response.code;
                if (code == "200") {
                    window.tips("数据已删除", false);
                    //todo 这里补充 和后台交互的代码
                    //删除完成后 在success回调中调用下面方法刷新数据列表
                    refreshDataGrid(postData);
                } else if ("607" == code) {
                    alert("您尚未登录或登录已失效！");
                    userlogout();
                } else {
                    // 失败
                    alert("删除失败，原因是:" + response.message);
                }

            }
        });




    });

}

//显示运单详情
function showdetail(id) {
    window.location.href = "yundandetail.html?id=" + id;
}

function a4Print_user_online(id) {
    var iframeid = "iframePrint_online_userA4";
    $("#" + iframeid).focus();
    window.open("printpageA4.html?id=" + id + "&ran=" + Math.random());
    // $("#" + iframeid).attr("src", "").attr("src", "printpageA4.html?id=" + id + "&ran=" + Math.random());
    $("#" + iframeid).focus();
    // beginPrintA4(iframeid);
}

//热敏4X6打印
function hot4x6Print_user_online(id) {
    var iframeid = "iframePrint_online_user64";
    $("#" + iframeid).focus();
    window.open("printpage64.html?id=" + id + "&ran=" + Math.random());
    // $("#" + iframeid).attr("src", "").attr("src", "printpageA4.html?id=" + id + "&ran=" + Math.random());
    $("#" + iframeid).focus();
    // beginPrintA4(iframeid);
}