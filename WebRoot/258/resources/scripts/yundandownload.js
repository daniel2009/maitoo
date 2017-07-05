
var yundangrid_selector = "#yundangrid-table";
//查询数据时向后台提交的参数
var postData = {};

//初始列表数据
$(function ($) {
	var yundanpager_selector = "#yundangrid-pager";
    var init = false;
    $(window).resize(function () {
        $(yundangrid_selector).setGridWidth($(".col-xs-12").width(), true);
    });

    jQuery(yundangrid_selector).jqGrid({
        url: "/admin/m_order/search",
        datatype: "json",
        height: "400",
        mtype:"post",
     
        colNames: ['运单号', '货物品名', '所属用户', "收件人姓名","状态","创建时间","身份证正面图","身份证反面图","身份证合成图"],
        colModel: [

            { name: 'orderId', index: 'phone', width: 150, },
            { name: 'name', index: 'name', width: 100, },
             { name: 'province', index: 'province', width: 60 },
             { name: 'city', index: 'city', width: 60 },
              { name: 'name', index: 'name', width: 100, },
             { name: 'province', index: 'province', width: 60 },
             { name: 'city', index: 'city', width: 60 },
              { name: 'name', index: 'name', width: 100, },
             { name: 'province', index: 'province', width: 60 }
             
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
            var rows = data.data.datas.length;
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
        },
        onSelectAll: function (aRowids, status) {
            var rows = aRowids.length;
            if (status == false) { rows = 0; }
            $("#orderschecked_no").html(rows);
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
            del: true,
            delicon: 'icon-trash red',
            search: false,
            searchicon: 'icon-search orange',
            refresh: true,
            refreshicon: 'icon-refresh green',
            view: false,
            viewicon: 'icon-zoom-in grey',
            //删除
         /*   delfunc: function () {
                var ids = $(yundangrid_selector).jqGrid('getGridParam', 'selarrrow');
                //alert(ids);
                
                if((removenull(ids)=="")||(ids.length<1))
                {
                	alert("必须选择要删除的运单!");
                	return false;
                }
                
               // var aid = new Array(ids);
                //alert(aid);
                //var aaa=ids.split(",");
                //alert(aaa[0]);
                //alert(aaa[1]);
                $.ajax({
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
            			}});
                
                
                
               
            }*/
        }
    );
    //点击运单搜索
    $("#btnYundanhao").click(function () {
        postData = getEelementData("#divYundaohao");
        $(yundangrid_selector).jqGrid("setGridParam", { postData: postData }).trigger("reloadGrid");
    });
    //绑定运单输入框的enter事件进行搜索
    $("#yundanhao").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnYundanhao").click();
        }
    }));

    //点击航班搜索
    $("#btnHangbanhao").click(function () {
        postData = getEelementData("#divHangbanhao");
        $(yundangrid_selector).jqGrid("setGridParam", { postData: postData }).trigger("reloadGrid");
    });
    //绑定航班输入框的enter事件进行搜索
    $("#hangbanhao").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnHangbanhao").click();
        }
    }));

    //点击高级搜索
    $("#btnAdvanceSearch").click(function () {
        var data = getEelementData(".advSearch");
        refreshDataGrid(data);
    });

});


//提交js对象参数刷新列表数据
function refreshDataGrid(data) {
    $(yundangrid_selector).jqGrid("setGridParam", { postData: data }).trigger("reloadGrid");
}



