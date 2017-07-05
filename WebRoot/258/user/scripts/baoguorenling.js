
var addressgrid_selector = "#addressgrid-table";
//查询数据时向后台提交的参数
var postData = {};
var jsonData;
var addresspager_selector = "#addressgrid-pager";
var init = false;

//初始列表数据
$(function ($) {
    initGrid();
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
}

function initGrid() {
    $(window).resize(function () {
        $(addressgrid_selector).setGridWidth($("#divList").width() - 30, true);
    });

    jQuery(addressgrid_selector).jqGrid({
        url: "testdata/baoguorenlingdata.html",
        datatype: "json",
        height: "auto",
        mtype: "get",
        colNames: ['包裹单号', "认领标题", '认领内容', '图片1', "图片2", "图片3", "收件人", "收件人电话", "创建日期", "操作"],
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
            { name: 'revName', index: 'revName', width: 60 },
             { name: 'baoguoPhone', index: 'baoguoPhone', width: 100, },
            { name: 'createDate', index: 'createDate', width: 100, },
            
            {
                name: 'op', index: 'op', width: 75, formatter: function (cellValue, option, row) {
                    //返回认领按钮
                    var pay = "<button  style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"pay('" + row.id + "')\"  ><span class='icon-edit'></span>认领</button>";

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
        pager: addresspager_selector,
        altRows: true,

        multiselect: false,
        //multikey: "ctrlKey",
        multiboxonly: true,

        loadComplete: function (data) {
            jsonData = data.data.datas;
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



//付款
function pay(id) {

    if (id == undefined) { top.window.tips("请选择需要认领的包裹", false); return; }

    top.window.confirmDialog("确定认领此包裹？", "提示", function () {
        window.tips("包裹已认领", false)
        //todo 这里补充 和后台交互的代码
        //删除完成后 在success回调中调用下面方法刷新数据列表
        refreshDataGrid(postData);
    });

}
