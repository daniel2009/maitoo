
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


    $("#btnAdd").click(function () {
        window.location.href = "zaixianzhidan.html?id=0";
    });


}

function initGrid() {
    $(window).resize(function () {
        $(addressgrid_selector).setGridWidth($(".page-content").width() - 2, true);
    });

    jQuery(addressgrid_selector).jqGrid({
        url: "testdata/zaixianzhidandata.html",
        datatype: "json",
        height: "auto",
        mtype: "get",
     
        colNames: ['预报单号', '包裹运单号', '货物说明', "备注", "入库时间", "重量", "到货仓库", "货运渠道", "是否入库", "选择", "操作"],
        colModel: [

            {
                name: 'orderId', index: 'orderId', width: 100, formatter: function (cellvalue, options, row) {
                    return '<a href="javascript:void(0)" onclick="showdetail(' + row.id + ')">' + cellvalue + '</a>';
                }
            },
            { name: 'cName', index: 'cName', width: 60, },
            { name: 'cPhone', index: 'cPhone', width: 80, },
            { name: 'cName', index: 'cName', width: 60, },
            { name: 'cPhone', index: 'cPhone', width: 80, },
            { name: 'cName', index: 'cName', width: 60, },
            { name: 'cPhone', index: 'cPhone', width: 80, },
            { name: 'cName', index: 'cName', width: 60, },
            { name: 'cPhone', index: 'cPhone', width: 80, },
            {
                name: 'createDate', index: 'createDate', width: 100
            },

             {
                 name: 'op', index: 'op', width: 180, formatter: function (cellValue, option, row) {
                     //返回修改按钮
                     var edit = "<button  style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"update('" + row.id + "')\"  ><span class='icon-edit'></span>修改</button>";
                     var del = "<button style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"del('" + row.id + "')\"  ><span class='fa fa-times'></span>删除</button>";
                     var print = "<button style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"del('" + row.id + "')\"  ><span class='fa fa-print'></span>打印</button>";
                     return edit + del + print;
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

//点击编辑
function update(id) {
    //zaixianzhidan 通过id来区分是修改还是新增. id=0为新增，id>0为修改
    window.location.href = "zaixianzhidan.html?id=" + id;
}

//删除
function del(id) {

    if (id == undefined) { top.window.tips("请选择需要删除的行", false); return; }

    top.window.confirmDialog("确定删除选择的数据？", "删除提示", function () {
        window.tips("数据已删除", false)
        //todo 这里补充 和后台交互的代码
        //删除完成后 在success回调中调用下面方法刷新数据列表
        refreshDataGrid(postData);
    });

}

//显示运单详情
function showdetail(id) {
    window.location.href = "yundandetail.html?id=" + id;
}
