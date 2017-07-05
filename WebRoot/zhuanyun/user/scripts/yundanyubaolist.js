
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

}

function initGrid() {
    $(window).resize(function () {
        $(addressgrid_selector).setGridWidth($(".page-content").width() - 2, true);
    });

    jQuery(addressgrid_selector).jqGrid({
        url: "/user/t_order/get_t_order",
        datatype: "json",
        height: "auto",
        mtype: "post",
       // postData:{"state":"0"},
        //运单号	货物品名	预报日期	操作
        colNames: ['转运编号','转运单号', '商品备注','状态', '创建日期', "操作"],
        colModel: [

                 { name: 'id', index: 'id', width: 50, },
            { name: 'torderId', index: 'torderId', width: 100, },
          
            { name: 'remark', index: 'remark', width: 150, },
            { name: 'state', index: 'state', width: 60, formatter: function (cellValue, option, row) {
            	if(cellValue=="1"||cellValue=="2")
            	{
            		return "<span style='color:red;'>"+ torderstatemap(cellValue)+"</span>";
            	}
            	else if(cellValue=="5")
            	{
            		return "<span style='color:blue;'>" +torderstatemap(cellValue)+"</span>";
            	}
            	else if(cellValue=="6")
            	{
            		return "<span style='color:gray;'>" +torderstatemap(cellValue)+"</span>";
            	}
            	else  if(cellValue=="7")
            	{
            		return "<span style='color:goldenrod;'>" +torderstatemap(cellValue)+"</span>";
            	}
            	else
            	{
            		return torderstatemap(cellValue);
            	}
            }},
            {
                name: 'createDate', index: 'createDate', width: 100
            },

             {
                 name: 'op', index: 'op', width: 100, formatter: function (cellValue, option, row) {
                     //返回修改按钮
                     
                     var del = "<button style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"del('" + row.id + "')\"  ><span class='fa fa-times'></span>删除</button>";
                     var route = "<button style='margin:1px 0px 0px 5px;padding:0px 3px;width:100px' class='btn btn-primary btn-sm' type='button'  onclick=\"route('" + row.id + "')\"  ><span class='icon-truck'></span>查看路由</button>";
                     if(row.state=="0")
                     {
                    	 return del+route;
                     }
                     else
                     {
                    	 return route;
                     }
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
        	if(data.code=="200")
        	{
        		if(data.data!=null)
        		{
        			jsonData = data.data.datas;
        		}
        	}
        	else if(data.code=="607")
            {
            	alert("请登陆后重新操作！");
            	userlogout();
            }
            else {
                alert("获取失败，原因：" + data.message);
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
        url: "/user/t_order/get_t_order",
        datatype: "json",
        height: "auto",
        mtype: "post",
        // postData:{"state":"0"},
        //运单号	货物品名	预报日期	操作
        colNames: [ '转运单号', '状态',  "操作"],
        colModel: [

                
            { name: 'torderId', index: 'torderId', width: 100, },

           
            {
                name: 'state', index: 'state', width: 60, formatter: function (cellValue, option, row) {
                    if (cellValue == "1" || cellValue == "2") {
                        return "<span style='color:red;'>" + torderstatemap(cellValue) + "</span>";
                    }
                    else if (cellValue == "5") {
                        return "<span style='color:blue;'>" + torderstatemap(cellValue) + "</span>";
                    }
                    else if (cellValue == "6") {
                        return "<span style='color:gray;'>" + torderstatemap(cellValue) + "</span>";
                    }
                    else if (cellValue == "7") {
                        return "<span style='color:goldenrod;'>" + torderstatemap(cellValue) + "</span>";
                    }
                    else {
                        return torderstatemap(cellValue);
                    }
                }
            },

             {
                 name: 'op', index: 'op', width: 100, formatter: function (cellValue, option, row) {
                     //返回修改按钮

                     var del = "<button style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"del('" + row.id + "')\"  ><span class='fa fa-times'></span>删除</button>";
                     var route = "<button style='margin:1px 0px 0px 5px;padding:0px 3px;width:100px' class='btn btn-primary btn-sm' type='button'  onclick=\"route('" + row.id + "')\"  ><span class='icon-truck'></span>查看路由</button>";
                     if (row.state == "0") {
                         return del + route;
                     }
                     else {
                         return route;
                     }
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
            if (data.code == "200") {
                if (data.data != null) {
                    jsonData = data.data.datas;
                }
            }
            else if (data.code == "607") {
                alert("请登陆后重新操作！");
                userlogout();
            }
            else {
                alert("获取失败，原因：" + data.message);
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



//删除
function del(id) {

    if (id == undefined) { top.window.tips("请选择需要删除的预报运单", false); return; }

    top.window.confirmDialog("确定删除选择的数据？", "删除提示", function () {
        $.ajax({
            type: "post",
            data:{"id":id},
            url: "/user/t_order/delete_one",
            success: function (response) {
                var code = response.code;
                if (code == "200") {
                    alert("删除成功！");
                    getcounts();
                    refreshDataGrid(postData);
                } else if ("607" == code) {
                    alert("您尚未登录或登录已失效！");
                    userlogout();
                } else {

                    alert("删除失败，原因是:" + response.message);
                }
            }
        });
    });
}

//显示运单详情
function route(id) {
    window.location.href = "yundanyubaodetail.html?id=" + id;
}


function getcounts() { 
	$.ajax({
	type : "post",
	url : "/t_order/get_counts",
	success: function (response) {
	    var code = response.code;

	    if ("200" == code) {

	        if (response.data != null) {
	            setSpanText("#orderQuantity", response.data);
	            //已入库的数量
	            top.window.document.getElementById("t_haveinputQ").innerText = response.data.t_haveinputQ;
	            top.window.document.getElementById("t_totalQ").innerText = response.data.t_totalQ;
	            
	            
	        }
	    }
	}
});

$.ajax({
	type : "post",
	url : "/m_order/get_counts",
	success : function(response) {
		var code = response.code;

		if ("200" == code) {

			if (response.data != null) {
				setSpanText("#orderQuantity", response.data);
				top.window.document.getElementById("tranwaitpQ").innerText = response.data.tranwaitpQ;
				top.window.document.getElementById("totalQ").innerText = response.data.totalQ;
				top.window.document.getElementById("nopayQ").innerText = response.data.nopayQ;

			} else {

			}
		} else {

		}
		
	}
});
}
