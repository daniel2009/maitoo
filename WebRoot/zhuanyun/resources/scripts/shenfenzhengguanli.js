

var yundangrid_selector = "#yundangrid-table";
//查询数据时向后台提交的参数
var postData = {};
var jsonData = [];
//初始列表数据
$(function ($) {

    var yundanpager_selector = "#yundangrid-pager";
    var init = false;

    $(window).resize(function () {
        $(yundangrid_selector).setGridWidth($(".page-content").width() - 20, true);
    });

    var grid = jQuery(yundangrid_selector).jqGrid({
        url: "/admin/cardid_lib/searchbykey",
        //url: "testdata/shenfenzhengdata.html",
        postData: { searchInfo: "" },
        datatype: "json",
        height: "auto",
        mtype: "get",
        //	姓名	省	市	县/区	地址	身份证号	照片	时间	操作
        colNames: ['姓名', '省', '市', "县/区", "地址", "身份证号", "照片","有无图片", "电话","修改时间", "操作"],
        //{"id":"58","cname":"贾磊","province":null,"city":null,"dist":null,"address":null,"cardid":"11010119770615401X","createDate":"2016-03-27 01:22:47","modifyDate":"2016-03-27 01:22:47","picurl":"有","result":null}
        colModel: [
            {
                name: 'cname', index: 'id', width: 60
            },
            {
                name: 'province', index: 'province', width: 50
            },
             {
                 name: 'city', index: 'city', width: 50
             },
              {
                  name: 'dist', index: 'dist', width: 50
              },
               {
                   name: 'address', index: 'address', width: 100
               },
               {
                   name: 'cardid', index: 'cardid', width: 100
               },
               {
                   name: 'picurl', index: 'picurl', width: 60, formatter: function (cellValue, option, row) {
                	   
                	   if(removenull(cellValue)!=null)
                	   {
                		   return "<a href='"+cellValue+"'>"+"<img width='50px' height='50px' src='"+cellValue+"'/></a>";
                	   }
                	   else
                	   {
                		   return "";
                	   }
                   }
               },
               {
                   name: 'picflag', index: 'picflag', width: 30
               },
               
               {
                   name: 'phone', index: 'phone', width: 60
               },
            {
                name: 'modifyDate', index: 'modifyDate', width: 80
            },
            {
                name: 'op', index: 'op', width: 50, formatter: function (cellValue, option, row) {
                    var del = "<button style='margin-left:5px;margin-top:1px;' class='btn btn-danger btn-sm' type='button'  onclick=\"del('" + row.id + "')\"  ><span class='fa fa-times'></span>删除</button>";
                    return del;
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

        multiselect: false,
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
        caption: "身份证信息列表",
        autowidth: true
    });


    //navButtons
    jQuery(yundangrid_selector).jqGrid('navGrid', yundanpager_selector,
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

    //点击高级搜索
    $("#btnAdvanceSearch").click(function () {
        var data = getEelementData("#divAdvanceSearch");
        postData = data;
        refreshDataGrid(data);
    });

    $("#divAdvanceSearch #orderId").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnAdvanceSearch").click();
        }
    }));

});


//提交js对象参数刷新列表数据
function refreshDataGrid(data) {
    $(yundangrid_selector).jqGrid("setGridParam", { postData: data }).trigger("reloadGrid");
}

function del(id) {
    window.confirmDialog("确定删除选中的身份证信息?", "删除提示", function () {
        $.ajax({
            type: "post",
            url: "/admin/cardid_lib/delone",
            data: { id: id },
            success: function (response) {
                var code = response.code;
                if ("200" == code) {
                    alert("删除成功");
                    refreshDataGrid(postData);
                } else if ("607" == code) {
                    alert("您尚未登录或登录已失效！");
                    top.location.href = "../admin/login.html";
                }
                else {
                    alert("删除出错，原因：" + response.message);
                }
            }
        });
    });
}

function check() {
    $.ajax({
        type: "post",
        url: " /admin/cardid_lib/pre_check",
        success: function (response) {
            var code = response.code;
            if ("200" == code) {
                alert("检测到身份证图片共：" + response.data + "个");

            } else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                top.location.href = "../admin/login.html";
            }
            else {
                alert("检测出错，原因：" + response.message);
            }
        }
    });
}

function import_cardid_zip() {
    var formId = "#import_cardid_zip_form";
    importFile(formId, function () {
        if ($(formId + " [type=file]").val() == "") {
            alert("请先选择需要导入的文件");
            return false;
        }
        return true;
    });
}

function import_cardid_pre() {
    var formId = "#import_cardid_pre_form";
    importFile(formId);
}

function import_cardid_orders() {
    var formId = "#import_cardid_orders_form";
    $(formId).ajaxSubmit({
        type: "post",
        beforeSubmit: function () {
            if ($(formId + " [type=file]").val() == "") {
                alert("请先选择需要导入的文件");
                return false;
            }
        },
        success: function (response) {
            if (typeof (response) != "object") {
            	if(response.indexOf("<PRE>")!=-1)//返回包含前后结束符，在ie的情况下，有的会自动加上,所以要判断是否包含<PRE></PRE>，返回的，ie5,ie7,ie8是大小的，ie9是小写的，ie10之后不会添加此前后缀，所以要统一处理
            	{
            		response=response.substr(response.indexOf("<PRE>")+5, response.indexOf("</PRE>")-5); 
            	}
            	else if(response.indexOf("<pre>")!=-1)
            	{
            		response=response.substr(response.indexOf("<pre>")+5, response.indexOf("</pre>")-5); 
            	}
                response = $.parseJSON(response);
            }
            var code = response.code;
            if (code == "200") {
                alert("导出成功");
            } else if (code == "607") {
                alert("您尚未登录或登录已失效！");
                adminlogout();
            } else {
                alert("导出失败，失败原因是:" + response.message);
            }
        }
    });
}

function importFile(formId,checkfileFunc) {
    $(formId).ajaxSubmit({
        type: "post",
        beforeSubmit: function () {
            if (checkfileFunc != undefined) {
                return checkfileFunc();
            }
        },
        success: function (response) {
            if (typeof (response) != "object") {
            	if(response.indexOf("<PRE>")!=-1)//返回包含前后结束符，在ie的情况下，有的会自动加上,所以要判断是否包含<PRE></PRE>，返回的，ie5,ie7,ie8是大小的，ie9是小写的，ie10之后不会添加此前后缀，所以要统一处理
            	{
            		response=response.substr(response.indexOf("<PRE>")+5, response.indexOf("</PRE>")-5); 
            	}
            	else if(response.indexOf("<pre>")!=-1)
            	{
            		response=response.substr(response.indexOf("<pre>")+5, response.indexOf("</pre>")-5); 
            	}
                response = $.parseJSON(response);
            }
            var code = response.code;
            if (code == "200") {
                alert("导入成功");
                refreshDataGrid(postData);
            } else if (code == "607") {
                alert("您尚未登录或登录已失效！");
                adminlogout();
            } else {
                alert("导入失败，失败原因是:" + response.message);
            }
        }
    });
}