$(function () {
    //多选框开关
    //$("input[type=checkbox]").bootstrapSwitch();

    //点击提交
    $("from [type=submit]").click(function () {
        var valid = $("from").validate().valid();
        return valid;
    });

    //验证规则设置
    $("form").validate({
        rules: {
            kehumingcheng: { required: true },
            yundanmingcheng: { required: true, minlength: 4 },
            email: { required: true, email: true },
            xuhao: { required: true, number: true, range: "[0,99]" },
            qudao: { required: true },
            kaishiriqi: { required: true, date: true }
        }
    });

    $("form [name=kehumingcheng]").focusin(function () {
        var t = $(this);
        var x = t.offset().left;
        var y = t.offset().top;
        $("#divKehu").css({ "left": x, "top": y -15 }).show();
    });

  

    //选择省
    $("input[name=province]").bigAutocomplete({
        data: $.areaData.p,
        //url: "testdata/areadata.html",
        callback: function (p) {
        }
    });
   

    //选择市
    $("input[name=city]").focusin(function () {
        $(this).bigAutocomplete({
            data: $.areaData.c[$("input[name = province]").val()]
        });
    });

    //县区
    $("input[name=area]").focusin(function () {
        $(this).bigAutocomplete({
            data: $.areaData.a[$("input[name = province]").val() + "-" + $("input[name = city]").val()]
        });
    })

    receiverGrid();
});




//列表数据查询
function receiverGrid() {
    var receivergrid_selector = "#receivergrid-table";
    receiverGrid = jQuery(receivergrid_selector).jqGrid({
        url: "testdata/senderdata.html",
        datatype: "json",
        height: 120,
        shrinkToFit: true,
        colNames: ['姓名', '州', "城市"],
        colModel: [

            { name: 'name', index: 'name', width: 160 },
             { name: 'province', index: 'province', width: 100 },
             { name: 'city', index: 'city', width: 100 },
        ],

        viewrecords: true,
        rowNum: 10,
        rowList: [5, 10, 30],
        // pager: receiverpager_selector,
        altRows: true,

        multiselect: true,
        //multikey: "ctrlKey",
        multiboxonly: true,

        loadComplete: function (data) {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
            }, 0);

            $(".ui-jqgrid-labels [role=checkbox]").hide();
            $(receivergrid_selector + " [role=checkbox]").attr({ "type": "radio", "name": "receivergrid-radio" });

        },
        onSelectRow: function (rowid, status) {
            var selData = $(receivergrid_selector).jqGrid("getRowData", rowid);
            $("form [name=kehumingcheng]").val(selData.name);
            $("#divKehu").hide();
        },
        autowidth: true
    });
}


$(function () {
    $("#btnTanChuang1").click(function () {
        window.confirmDialog("确定删除此行数据？", "删除提示", function () {
            // window.tips(msg, modal, time, jumpUrl)
            window.tips("数据已删除", false)
        });
    });

    $("#btnTanChuang2").click(function () {
        //window.infoDialog = window.errorDialog = function (title, msg, modal, jumpUrl) 
        window.infoDialog("提交操作结果", "数据提交成功", true, "add.html");
    });

 
})
