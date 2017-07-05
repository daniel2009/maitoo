
var addressgrid_selector2 = "#addressgrid-table2";
//查询数据时向后台提交的参数
var postData2 = {};
var addresspager_selector2 = "#addressgrid-pager2";
var init2 = false;

//初始列表数据
$(function ($) {
    initGrid2();
    initClickEvent2();
});

function initClickEvent2() {
    //点击搜索
    $("#btnSearch2").click(function () {
        postData2 = getEelementData("#divaddress2");
        $(addressgrid_selector2).jqGrid("setGridParam", { postData: postData2 }).trigger("reloadGrid");
    });
    //绑定输入框的enter事件进行搜索
    $("#keyword2").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnSearch2").click();
        }
    }));

}

function initGrid2() {
    $(window).resize(function () {
        $(addressgrid_selector2).setGridWidth($("#divList").width() - 30, true);
    });

    jQuery(addressgrid_selector2).jqGrid({
        url: "testdata/baoguorenlingdata.html",
        datatype: "json",
        height: "auto",
        mtype: "get",
        colNames: ['包裹单号', "认领标题", '认领内容', '图片1', "图片2", "图片3", "审核信息", "回复", "状态", "操作"],
        colModel: [
            { name: 'id', index: 'id', width: 60 },
             { name: 'title', index: 'title', width: 100 },
            { name: 'content', index: 'content', width: 180 },

            {
                name: 'pic1', index: 'pic1', width: 50, formatter: function (cellValue, option, row) {
                    if (cellValue != "") {
                        var img = "<img src='" + cellValue + "' width='40' heigth='30'>";
                        return img;
                    } else {
                        return "";
                    }
                }
            },
            {
                name: 'pic2', index: 'pic2', width: 50, formatter: function (cellValue, option, row) {
                    if (cellValue != "") {
                        var img = "<img src='" + cellValue + "' width='40' heigth='30'>";
                        return img;
                    } else {
                        return "";
                    }
                }
            }, {
                name: 'pic3', index: 'pic3', width: 60, formatter: function (cellValue, option, row) {
                    if (cellValue != "") {
                        var img = "<img src='" + cellValue + "' width='60' heigth='40'>";
                        return img;
                    } else {
                        return "";
                    }
                }
            },
            { name: 'reninfo', index: 'reninfo', width: 100 },
             { name: 'totalAudit', index: 'totalAudit', width: 100, },
            { name: 'state', index: 'state', width: 60, },

            {
                name: 'op', index: 'op', width: 75, formatter: function (cellValue, option, row) {
                    //返回认领按钮
                    var pay = "<button  style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"pay('" + row.id + "')\"  ><span class='icon-edit'></span>操作</button>";

                    return pay;
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
        pager: addresspager_selector2,
        altRows: true,

        multiselect: false,
        //multikey: "ctrlKey",
        multiboxonly: true,

        loadComplete: function (data) {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
            }, 0);
            //防止出现水平滚动条
            if (init2 == false) {
                $(".ui-jqgrid-bdiv").css({ 'overflow-x': 'hidden' });
                $(".ui-jqgrid-htable th:last").css({ "width": $(".ui-jqgrid-htable th:last").width() - 2 });
                $(".jqgfirstrow td:last").css({ "width": $(".jqgfirstrow td:last").width() - 2 });
                init2 = true;
            }
        },
        gridComplete: function () {

        },
        onSelectRow: function (rowid, status) {

        },
        onSelectAll: function (aRowids, status) {

        },
        caption: "",
        width: $("#home").width(),
        autowidth: false
    });


    //navButtons
    jQuery(addressgrid_selector2).jqGrid('navGrid', addresspager_selector2,
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
function refreshDataGrid2(data) {
    $(addressgrid_selector2).jqGrid("setGridParam", { postData: data }).trigger("reloadGrid");
}
