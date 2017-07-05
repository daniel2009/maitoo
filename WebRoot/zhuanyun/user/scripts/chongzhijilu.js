


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
        url: "/account/user/search",
        datatype: "json",
        height: "auto",
        mtype: "get",

        //名称	操作	币种	金额	操作日期	备注
        colNames: ['名称', '操作', '币种', "金额", "操作日期", "备注"],
        colModel: [

            {
                name: 'name', index: 'name', width: 60
            },
            {
                name: 'state', index: 'state', width: 50, formatter: function (cellValue, options, row) {
                    if (cellValue == "0") {
                        return "待处理";
                    }if (cellValue == "1") {
                        return "完成";
                    } if (cellValue == "2") {
                        return "操作失败";
                    } if (cellValue == "3") {
                        return "修改支付状态";
                    }  else {
                        return "未知";
                    }
  
                    
                }
            },
            { name: 'currency', index: 'currency', width: 50, },
            { name: 'amount', index: 'amount', width: 50, },
            { name: 'createDate', index: 'createDate', width: 80, },
             { name: 'remark', index: 'remark', width: 180, }

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
        	
        	if(data.code=="200")//返回成功
        	{
        		if(data.data!=null)
        		{
        			jsonData = data.data.datas;
        		}
        		
        	}
        	else if ("607" == data.code) {
                alert("您尚未登录或登录已失效！");
                userlogout();
            } else
			{
				alert("查找失败，原因："+data.message);
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
        url: "/account/user/search",
        datatype: "json",
        height: "auto",
        mtype: "get",

        //名称	操作	币种	金额	操作日期	备注
        colNames: ['名称', '操作',  "金额", "操作日期"],
        colModel: [

            {
                name: 'name', index: 'name', width: 60
            },
            {
                name: 'state', index: 'state', width: 40, formatter: function (cellValue, options, row) {
                    if (cellValue == "0") {
                        return "待处理";
                    } if (cellValue == "1") {
                        return "完成";
                    } if (cellValue == "2") {
                        return "操作失败";
                    } if (cellValue == "3") {
                        return "修改支付状态";
                    } else {
                        return "未知";
                    }


                }
            },
           
            { name: 'amount', index: 'amount', width: 40, },
            { name: 'createDate', index: 'createDate', width: 80, }
            

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
                alert("查找失败，原因：" + data.message);
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

