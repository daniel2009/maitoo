
var yundangrid_selector = "#yundangrid-table";
//查询数据时向后台提交的参数
var postData = {};
var json = [];

//初始列表数据
$(function ($) {

    var init = false;

    $(window).resize(function () {
        $(yundangrid_selector).setGridWidth($(".col-xs-12").width(), true);
    });

    jQuery(yundangrid_selector).jqGrid({
        url: "testdata/yundanlistdata.html",
        datatype: "json",
        height: "320",

        colNames: ['运单号', '货物品名', '所属用户', "收件人姓名", "状态", "创建时间", "重量（lb）", "重量（kg）", "价格"],
        colModel: [

            { name: 'orderId', index: 'phone', width: 150, },
            { name: 'name', index: 'name', width: 100, },
             { name: 'province', index: 'province', width: 60 },
             { name: 'city', index: 'city', width: 60 },
              { name: 'name', index: 'name', width: 100, },
             { name: 'province', index: 'province', width: 60 },
             { name: 'weight', index: 'weight', width: 60 },
              { name: 'sjweight', index: 'sjweight', width: 100, },
             { name: 'value', index: 'value', width: 60 }

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
        pager: "",
        altRows: true,

        multiselect: true,
        //multikey: "ctrlKey",
        multiboxonly: true,

        loadComplete: function (data) {
            var table = this;
            json = data.data.datas;
            var rows = json.length;
            $("#orders_no").html(rows);
            $("#orderschecked_no").html("0");
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
            var ids = $(yundangrid_selector).jqGrid('getGridParam', 'selarrrow');
            $("#orderschecked_no").html(ids.length);
            setTotalVaule(ids);

        },
        onSelectAll: function (aRowids, status) {
            var rows = aRowids.length;
            if (status == false) {
                rows = 0;
                setTotalVaule([]);
            } else {
                var ids = [];
                for (var i = 0; i < json.length; i++) {
                    ids.push(json[i].id);
                }
                setTotalVaule(ids);
            }
            $("#orderschecked_no").html(rows);

        },
        autowidth: true
    });

    //点击高级搜索
    $("#btnAdvanceSearch").click(function () {
        var data = getEelementData(".advSearch");
        refreshDataGrid(data);
    });

    $("button[type=submit]").click(function () {
        var ids = $(yundangrid_selector).jqGrid('getGridParam', 'selarrrow');
        var isSameUser = true;
        var user = "";
        for (var i = 0; i < ids.length; i++) {
            var row = json.getData(ids[i]);
            //row.user进行判断是否存在不同用户的并进行提示
            //if (i > 0 && user != row.user) {
            //    isSameUser = false;
            //    break;
            //}
        }
        if (isSameUser) {
            var data = getEelementData("#trTotal");
            //提交数据
            alert(data.totalMoneys);
        }
    });

});


//提交js对象参数刷新列表数据
function refreshDataGrid(data) {
    $(yundangrid_selector).jqGrid("setGridParam", { postData: data }).trigger("reloadGrid");
}

//设置总数值
function setTotalVaule(ids) {
    var lbw = 0, kgw = 0, tm = 0;
    for (var i = 0; i < ids.length; i++) {
        var r = json.getData(ids[i]);
        lbw += r.weight * 1.0;
        kgw += r.sjweight * 1.0;
        tm += r.value * 1.0;
    }

    $("#totalweightlb").val(lbw);
    $("#totalweightkg").val(kgw);
    $("#totalMoneys").val(tm);
}



