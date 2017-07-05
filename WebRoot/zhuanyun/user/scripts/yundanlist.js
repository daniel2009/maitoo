
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
    $("#info").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnSearch").click();
        }
    }));


    $("#btnAdd").click(function () {
        window.location.href = "zaixianzhidan.html?id=0";
    });

}

function changetonopay() {
    $("select[name='payornot']").val("0");
    $("#btnSearch").click();
}
function changetoabandonQ() {
    $("select[id='stateSelect']").val("0");
    $("#btnSearch").click();
}

function changetoquestionQ() {
    $("select[id='stateSelect']").val("1");
    $("#btnSearch").click();
}


function initGrid() {
    $(window).resize(function () {
        $(addressgrid_selector).setGridWidth($(".page-content").width() - 2, true);
    });

    jQuery(addressgrid_selector).jqGrid({
        url: "/m_order/searchbyUser",
        datatype: "json",
        height: "auto",
        mtype: "get",
        colNames: ['订单号','转运单号', '货品明细', '收件人', '收件地址', "创建时间", "费用($)", "重量(lb)", "状态", "操作"],
        colModel: [
            {
                name: 'orderId', index: 'orderId', width: 80, formatter: function (cellvalue, options, row) {
                    return '<a href="javascript:void(0)" onclick="showdetail(' + row.id + ')">' + cellvalue + '</a>';
                }
            },
            {
                name: 'torderids', index: 'torderids', width: 100, formatter: function (cellvalue, options, row) {
                	var text = "";
                	if(row.torders!=null&&row.torders.length>0)
                	{
                		$.each(row.torders,function(){
                			if(text=="")
                			{
                				text=this.torderId;
                			}
                			else
                			{
                				text=text+"<br/>"+this.torderId;
                			}
                		});
                	}
                	
                   
                    return text;

                }
            },

            {
                name: 'detail', index: 'detail', width: 150, formatter: function (cellvalue, options, row) {
                    var text = "";
                    if (row.detail != null) {
                        for (var i = 0; i < row.detail.length; i++) {
                            if (text == "") {
                                text = row.detail[i].productName + "*"+ row.detail[i].brands + "*"+ row.detail[i].quantity;
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
                name: 'revperson', index: 'revperson', width: 100, formatter: function (cellvalue, options, row) {
                    var text = "";
                    text = removenull(row.ruser.name) + "/" + removenull(row.ruser.phone);
                    return text;

                }
            },
            {
                name: 'address', index: 'address', width: 150, formatter: function (cellvalue, options, row) {
                    var text = "";
                    text = removenull(row.ruser.state) + removenull(row.ruser.city) + removenull(row.ruser.dist) + removenull(row.ruser.address);
                    return text;

                }
            },
            { name: 'createDate', index: 'createDate', width: 60 },
            { name: 'totalmoney', index: 'totalmoney', width: 30 },

            { name: 'sjweight', index: 'sjweight', width: 30, formatter: function (cellvalue, options, row) {
            	if(removenull(cellvalue)==""||isNaN(parseFloat(cellvalue))||parseFloat(cellvalue)<=0)
            	{
            		return removenull(row.weight);
            	}
            	else
            	{
            		return row.sjweight;
            	}
            } },
            {
                name: 'stateStr', index: 'stateStr', width: 80, formatter: function (cellvalue, options, row) {
                    var text = "";
                    text = morderstatemap(row.state);
                    return text;

                }
            },

            {
                name: 'op', index: 'op', width: 190, formatter: function (cellValue, option, row) {
                    //返回修改按钮
                    var pay = "";
                    if (row.payornot == "0")//未付款的运单，
                    {
                        if (row.state > 1)//状态至少已揽收的
                        {
                            pay = "<button  style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"pay('" + row.id + "')\"  ><span class='icon-edit'></span>支付</button>";
                        }

                    }
                    //返回修改按钮
                    //var edit = "<button  style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"update('" + row.id + "')\"  ><span class='icon-edit'></span>修改</button>";
                    //var del = "<button style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"del('" + row.id + "')\"  ><span class='fa fa-times'></span>删除</button>";

                    var detail = "<button style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"showdetail(" + row.id + ")\"  ><span class='fa fa-times'></span>详情</button>";

                  //  return edit+del+ pay + detail;
                    return  pay + detail;
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
            //jsonData = data.data.datas;
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
                alert("加载失败，原因：" + data.message);
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
        url: "/m_order/searchbyUser",
        datatype: "json",
        height: "auto",
        mtype: "get",
        colNames: ['订单号', '转运单号', "状态", "操作"],
        colModel: [
            {
                name: 'orderId', index: 'orderId', width: 100, formatter: function (cellvalue, options, row) {
                    return '<a href="javascript:void(0)" onclick="showdetail(' + row.id + ')">' + cellvalue + '</a>';
                }
            },
            {
                name: 'torderids', index: 'torderids', width: 90, formatter: function (cellvalue, options, row) {
                    var text = "";
                    if (row.torders != null && row.torders.length > 0) {
                        $.each(row.torders, function () {
                            if (text == "") {
                                text = this.torderId;
                            }
                            else {
                                text = text + "<br/>" + this.torderId;
                            }
                        });
                    }


                    return text;

                }
            },

           
            {
                name: 'stateStr', index: 'stateStr', width: 60, formatter: function (cellvalue, options, row) {
                    var text = "";
                    text = morderstatemap(row.state);
                    return text;

                }
            },

            {
                name: 'op', index: 'op', width: 80, formatter: function (cellValue, option, row) {
                    //返回修改按钮
                    var pay = "";
                    if (row.payornot == "0")//未付款的运单，
                    {
                        if (row.state > 1)//状态至少已揽收的
                        {
                            pay = "<button  style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"pay('" + row.id + "')\"  ><span class='icon-edit'></span>支付</button>";
                        }

                    }
                    //返回修改按钮
                    //var edit = "<button  style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"update('" + row.id + "')\"  ><span class='icon-edit'></span>修改</button>";
                    //var del = "<button style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"del('" + row.id + "')\"  ><span class='fa fa-times'></span>删除</button>";

                    var detail = "<button style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"showdetail(" + row.id + ")\"  ><span class='fa fa-times'></span>详情</button>";

                    //  return edit+del+ pay + detail;
                    return pay + detail;
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
            //jsonData = data.data.datas;
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
                alert("加载失败，原因：" + data.message);
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

//显示运单详情
function showdetail(id) {
    window.location.href = "yundandetail.html?id=" + id;
}

//付款
function pay(id) {

    if (id == undefined) { top.window.tips("请选择需要付款的运单", false); return; }

    top.window.confirmDialog("确定付款此订单？", "提示", function () {


        $.ajax({
            type: "post",
            url: "/m_order/user_pay",
            data: { id: id },
            success: function (response) {
                var code = response.code;
                if (code == "200") {
                    window.tips("订单已付款", false);
                    getcounts();
                    refreshDataGrid(postData);
                } else if ("607" == code) {
                    alert("您尚未登录或登录已失效！");
                    userlogout();
                } else {
                    // 失败
                    alert("支付失败，原因是:" + response.message);
                }


            }
        });



        //todo 这里补充 和后台交互的代码
        //删除完成后 在success回调中调用下面方法刷新数据列表

    });

}

//点击编辑
function update(id) {
    window.location.href = "zhidanedit.html?id=" + id;
}

//删除
function del(id) {
    if (id == undefined) { top.window.tips("请选择需要删除的运单", false); return; }
    top.window.confirmDialog("确定删除选择的数据？", "删除提示", function () {

        $.ajax({
            type: "post",
            url: "/consignee_sendperson/del",
            data: {
                "id": id
            },
            success: function (response) {
                var code = response.code;
                if (code == "200") {
                    window.tips("数据已删除", false);
                    getcounts();
                    //todo 这里补充 和后台交互的代码
                    //删除完成后 在success回调中调用下面方法刷新数据列表
                    refreshDataGrid(postData);
                } else if ("607" == code) {
                    alert("您尚未登录或登录已失效！");
                    userlogout();
                } else {
                    // 失败
                    alert("删除信息失败，原因是:" + response.message);
                }
            }
        });
    });
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
