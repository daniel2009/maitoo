
var receiverGrid, receiverJsonData;
var yundangrid_selector = "#yundangrid-table";
//查询数据时向后台提交的参数
var postData = {};
var jsonData = [];
//初始列表数据
$(function ($) {
    initDataGrid();
    loadseachinfo();
    initReceiverGrid();


    $(document).bind('mousedown', function (event) {
        var $target = $(event.target);
        if ((!($target.parents().andSelf().is('#divKehu')))) {
            $("#divKehu").hide();
        }
    })

    //搜索收件人
    $("#btnSearchReceiver").click(function () {
        receiverGrid.setGridParam({ postData: { keyword: $("#divKehu [name=keyword]").val() } }).trigger("reloadGrid");
    });
    //收件人搜索
    $("#divKehu [name=keyword]").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnSearchReceiver").click();
        }
    }));


    $(".cancel").click(function () {
        $("#divGrid").show();
        $("#divEdit").hide();
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


    $("#divEdit form").validate({
        //验证规则
        //http://www.runoob.com/jquery/jquery-plugin-validate.html
        rules: {
            userId: { required: true, minlength: 10 },
            state: { required: true },
            torderId: { required: true },
            remark: { required: true },
            qremark: { required: true },
        }, submitHandler: function (form) {
            var data = getEelementData("#divEdit");

            $.ajax({
                type: "post",
                url: "/admin/t_order/modify_fail_torder",
                data: data,
                success: function (response) {
                    var code = response.code;
                    if (code == "200") {
                        alert("修改成功");
                        $(".cancel").click();
                        refreshDataGrid(postData);
                    } else if (code == "607") {
                        alert("您尚未登录或登录已失效！");
                        adminlogout();
                    } else {
                        alert("修改失败，失败原因是:" + response.message);
                    }
                }
            });
        }
    });
});

function initDataGrid() {
    var yundanpager_selector = "#yundangrid-pager";
    var init = false;
    $(window).resize(function () {
        $(yundangrid_selector).setGridWidth($(".page-content").width() - 20, true);
    });

    jQuery(yundangrid_selector).jqGrid({
        url: "/admin/t_order/get_fail_t_order",
        // url: "testdata/huiyuandata.html",
        datatype: "json",
        height: "auto",
        mtype: "get",
        //{"code":"200","data":{"rowCount":1,"pageCount":1,"pageSize":10,"pageNow":1,"datas":[{"id":"11636","torderId":"ASDFASDFASDFASD","userId":"0","totalmoney":"0","totalcost":"0","weight":"0","sjweight":"0","other":"0","quantity":"0","tariff":"0","value":"0","insurance":"0","remark":"asfasdfasd","channelId":"0","channelName":null,"storeId":"0","storeName":null,"employeeId":"0","employeeName":null,"createDate":"2016-06-19 21:44:15","modifyDate":"2016-06-19 21:44:15","ruserId":"0","ruser":null,"user":null,"state":"1","qremark":"asdfasasdfasf","result":null,"order_ids":null,"type":"0","i_state":"1","i_employeeId":"1","i_storeId":"29","i_employeeName":"admin","i_storeName":"美淘转运总公司（蒙市总店）","i_date":null,"i_jwweight":"2.00","i_weight":"0.05","positionId":"3402","position":{"id":"3402","indexId":"110","position":"W004","modifyDate":"2016-06-19 21:44:15","createDate":"2016-06-15 11:44:26","state":"1","remark":"asdfasasdfasf,转运单号：ASDFASDFASDFASD"},"route":null}]}}
        colNames: ['转运单号', "物品", "转运重量", "入库重量","转运仓库","入库仓库", "仓位", "入库时间",  "失败说明", "操作"],

        colModel: [
            { name: 'torderId', index: 'torderId', width: 100 },
             { name: 'remark', index: 'remark', width: 150 },
             { name: 'weight', index: 'weight', width: 55,formatter:function(cellValue,option,row){
            	 return removenull(row.weight)+"/"+removenull(row.sjweight);
             } },
             { name: 'i_weight', index: 'i_weight', width: 55,formatter:function(cellValue,option,row){
            	 return removenull(row.i_jwweight)+"/"+removenull(row.i_weight);
             } },
             { name: 'storeName', index: 'storeName', width: 50 },
             { name: 'i_storeName', index: 'i_storeName', width: 50 },
             { name: 'position.position', index: 'position.position', width: 50 },
             { name: 'position.createDate', index: 'position.createDate', width: 100 },
             //{
             //    name: 'state', index: 'state', width: 65, formatter: function (cellValue, option, row) {
             //        if (cellValue == "1" || cellValue == "2") {
             //            return "<span style='color:red;'>" + torderstatemap(cellValue) + "</span>";
             //        }
             //        else if (cellValue == "5") {
             //            return "<span style='color:blue;'>" + torderstatemap(cellValue) + "</span>";
             //        }
             //        else if (cellValue == "6") {
             //            return "<span style='color:gray;'>" + torderstatemap(cellValue) + "</span>";
             //        }
             //        else if (cellValue == "7") {
             //            return "<span style='color:goldenrod;'>" + torderstatemap(cellValue) + "</span>";
             //        }
             //        else {
             //            return torderstatemap(cellValue);
             //        }
             //    }
             //},
             { name: 'qremark', index: 'qremark', width: 150 },

             {
                 name: 'op', index: 'op', width: 60, formatter: function (cellValue, option, row) {

                     var edit = "<button  style='margin-left:5px;margin-top:1px;' class='btn btn-purple btn-sm' type='button'  onclick=\"update('" + row.id + "')\"  ><span class='icon-edit'></span>修改</button>";

                     return edit;
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

        multiselect: true,
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
        caption: "入库失败列表",
        autowidth: true
    });


    //navButtons
    jQuery(yundangrid_selector).jqGrid('navGrid', yundanpager_selector,
        { 	//navbar options
            edit: false,
            editicon: 'icon-pencil blue',
            add: false,
            addicon: 'icon-plus-sign purple',
            del: true,
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

function showReceiverSelect(obj) {
    showReceiverSelectObj = obj;
    var t = $(obj);
    var x = t.offset().left;
    var y = t.offset().top;
    $("#divKehu").css({ "left": x, "top": y + 30 }).toggle();
    if ($("#divKehu:visible")) {
        $("#btnSearchReceiver").click();
    }
}

//提交js对象参数刷新列表数据
function refreshDataGrid(data) {
    $(yundangrid_selector).jqGrid("setGridParam", { postData: data }).trigger("reloadGrid");
}

function update(id) {
    var row = jsonData.getData(id);
    clearInputData("#divEdit");
    setInputData("#divEdit", row);
    $("#divEdit input[name='userId']").val(row.user != null ? row.user.id : "");
    $("#divEdit input[name='userName']").val(row.user != null ? row.user.realName : "");
   
    $("#divGrid").hide();
    $("#divEdit").show();
    if(row.type=="1")//转运州包裹，只能是转运入库状态
    {
    	$("#divEdit select[name='state']").html("<option value=\"3\">转运入库</option>");
    }
    else if(row.type=="0")//转运州包裹，只能是转运入库状态
    {
    	$("#divEdit select[name='state']").html("<option value=\"5\">已入仓库</option>");
    }
    else
    {
    	$("#divEdit select[name='state']").html("<option value=\"5\">已入仓库</option><option value=\"3\">转运入库</option>");
    }
}


//收件人列表数据查询
function initReceiverGrid() {
    receiverJsonData = "";
    var receivergrid_selector = "#receivergrid-table";
    var receiverpager_selector = "#receivergrid-pager";
    receiverGrid = jQuery(receivergrid_selector).jqGrid({
        url: "/admin/user/search_admin",
        datatype: "json",
        height: 150,
        //shrinkToFit: true,
        mtype: 'get',
        colNames: ['姓名', '电话', "昵称"],
        colModel: [

            { name: 'realName', index: 'realName', width: 80 },
             { name: 'phone', index: 'phone', width: 120 },
             {
                 name: 'nickName', index: 'nickName', width: 100
             }
        ],

        viewrecords: true,
        rowNum: 10,
        rowList: [5, 10, 30],
        pager: receiverpager_selector,
        altRows: true,

        multiselect: true,
        //multikey: "ctrlKey",
        multiboxonly: true,
        // postData: jsonData,
        jsonReader: {//rowCount":32,"pageCount":4,"pageSize":10,"pageNow":
            root: "data.datas",
            page: "data.pageNow",
            total: "data.pageCount",
            records: "data.rowCount"
        },
        loadComplete: function (data) {
            var table = this;

            setTimeout(function () {
                updatePagerIcons(table);
            }, 0);

            if (data.code == "200") {
                if (data.data != null) {
                    receiverJsonData = data.data.datas;
                }
            } else if ("607" == data.code) {
                alert("您尚未登录或登录已失效！");
                userlogout();
            } else {
                alert("获取收件人信息失败:" + data.message);
            }


            $(".ui-jqgrid-labels [role=checkbox]").hide();
            $(receivergrid_selector + " [role=checkbox]").attr({ "type": "radio", "name": "receivergrid-radio" });




        },
        onSelectRow: function (rowid, status) {
            $("#divKehu").hide();
            var row = receiverJsonData.getData(rowid);
            $("form [name=userName]").val(row.realName + "/" + row.phone);
            $("form [name=userId]").val(row.id);
        },
        autowidth: true
    });

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
}

function loadseachinfo() {

    $.ajax({
        type: "post",
        url: "/admin/warehouse/getself",
        success: function (response) {
            var code = response.code;

            if ("200" == code) {

                var str = "";
                if (response.data != null) {
                    storelist_jason = response.data;
                    if (response.data.length == 1) {
                        //alert(response.data.id);
                        str = "<option value='" + response.data[0].id + "'>" + response.data[0].name + "</option>";
                        /*$.each(response.data,
								function() {
							str+="<option value='"+this.id+"'>"+this.name+"</option>";
						});*/
                    }
                    else {
                        str = "<option value='" + "-1" + "'>" + "请选择门店" + "</option>";
                        $.each(response.data,
								function () {
								    str += "<option value='" + this.id + "'>" + this.name + "</option>";
								});
                    }
                    $("select[name='storeId']").html(str);
                    $("select[name='i_storeId']").html(str);

                } else {

                }
            } else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                top.location.href = "../admin/login.html";
            }
            else {
                alert("读取门店信息出错，原因：" + response.message);
            }

        }
    });
}