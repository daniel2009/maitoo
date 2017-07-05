
$(function () {
    $("input[type=checkbox]").bootstrapSwitch();
})

var addressgrid_selector = "#addressgrid-table";
//查询数据时向后台提交的参数
var postData = {};

//初始列表数据
$(function ($) {

    var addresspager_selector = "#addressgrid-pager";
    var init = false;
    $(window).resize(function () {
        $(addressgrid_selector).setGridWidth($(".page-content").width()-1, true);
    });

    jQuery(addressgrid_selector).jqGrid({
        url: "testdata/address.html",
        datatype: "json",
        height: "auto",
        mtype: "get",

        colNames: ['收货人', '电话', '国家/地区', "详细地址", "身份证号", "身份证正面图", "身份证反面图", "身份证合成图", "操作", ],
        colModel: [

            { name: 'name', index: 'name', width: 100, },
            { name: 'phone', index: 'phone', width: 100, },
            { name: 'province', index: 'province', width: 100, },
            { name: 'streetAddress', index: 'streetAddress', width: 100, },
            { name: 'cardId', index: 'cardId', width: 100, },
            { name: 'cardUrl', index: 'cardUrl', width: 100, },
            { name: 'cardurlother', index: 'cardurlother', width: 100, },
             { name: 'cardurltogether', index: 'cardurltogether', width: 100, },


             {
                 name: 'op', index: 'op', width: 120, formatter: function (cellValue, option, row) {
                     //返回修改按钮
                     var edit = "<button  style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"update('" + row.id + "')\"  ><span class='icon-edit'></span>修改</button>";
                     var del = "<button style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"del('" + row.id + "')\"  ><span class='fa fa-times'></span>删除</button>";
                     return edit + del;
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
        postData = getEelementData("#divaddress");
        $(addressgrid_selector).jqGrid("setGridParam", { postData: postData }).trigger("reloadGrid");
    });
    //绑定运单输入框的enter事件进行搜索
    $("#address").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnaddress").click();
        }
    }));

    $("#btnAdd").click(function () {
        $("#divAdd").toggle();
    });

    //点击高级搜索
    $("#btnAdvanceSearch").click(function () {
        var data = getEelementData("#divAdvanceSearch");
        refreshDataGrid(data);
    });

    //添加航班表单验证并提交,选择器的内容一定要是form表单，里面一定要有一个submit的按钮
    $("#divAdd form").validate({
        //验证规则
        //http://www.runoob.com/jquery/jquery-plugin-validate.html
        rules: {
            hangbanhao2: { required: true, minlength: 4 },
            state2: { required: true }
        }, submitHandler: function (form) {
            var data = getEelementData("#divAdd");
            window.infoDialog("提交", "航班号是" + data.hangbanhao2, true);
            //提交到后台操作，操作完成后调用下面方法
            //隐藏新增div
            $("#divAdd").toggle();
            //清空输入框
            clearInputData("#divAdd");
            //刷新列表数据
            refreshDataGrid(postData);
        }
    });
});


//提交js对象参数刷新列表数据
function refreshDataGrid(data) {
    $(addressgrid_selector).jqGrid("setGridParam", { postData: data }).trigger("reloadGrid");
}

function update(id) {

    var fieldName = "state"
    var state = $(addressgrid_selector).find("[id=" + id + "]").find("[aria-describedby$=" + fieldName + "] select").val();
    var change = $(addressgrid_selector).find("[id=" + id + "]").find("[aria-describedby$='province'] input").attr("checked");
    var data = ("id:" + id + "  state:" + state + " change:" + change);

    top.window.infoDialog("提交", "数据是:" + data, true);
}

function del(id) {

    if (id == undefined) { top.window.tips("请选择需要删除的行", false); return; }

    top.window.confirmDialog("确定删除选择的数据？", "删除提示", function () {
        window.tips("数据已删除", false)
        //todo 这里补充 和后台交互的代码
        //删除完成后 在success回调中调用下面方法刷新数据列表
        refreshDataGrid(postData);
    });

}


