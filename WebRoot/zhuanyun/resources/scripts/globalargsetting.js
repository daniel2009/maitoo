
$(function () {
    $("select[name=index]").change(function () {
        $("#tableTitle").html($(this).find("[value=" + $(this).val() + "]").html());
        refreshDataGrid()
    });

    initGrid();

    $(window).resize(function () {
        $(yundangrid_selector).setGridWidth($(".page-content").width() - 20, true);
    });
})

function refreshDataGrid() {
    $(yundangrid_selector).jqGrid("setGridParam", { postData: { index: $("select[name=index]").val() } }).trigger("reloadGrid");
}

var yundangrid_selector = "#yundangrid-table";
var init = false;

//初始列表数据
function initGrid() {

    jQuery(yundangrid_selector).jqGrid({
        url: "/admin/globalArgs/getallbyindex",
       // url: "testdata/argumentdata.html",
        datatype: "json",
        postData: { index: $("select[name=index]").val() },
        height: "auto",
        mtype: "get",
        //'类型',
        colNames: ['参数名称', '修改时间', '参数描述', '参数内容', "操作"],
        colModel: [

             { name: 'name', index: 'name', width: 80, fix: true },
             { name: 'modifytime', index: 'modifytime', width: 60, fix: true },
             {
                 name: 'argremark', index: 'argremark', width: 240, fix: true, formatter: function (cellValue, option, row) {
                     //return '<span name="argremark">' + cellValue + '</span><textarea  style="width:98%;heigth:80px;display:none" name="">' + cellValue + '</textarea>';
                     return cellValue;
                 }
             },
              
             {
                 name: 'argcontent', index: 'argcontent', width: 100, fix: true, formatter: function (cellValue, option, row) {
                     if (row.argtype == 1) {
                         return '<textarea name="argcontent" style="width:98%;height:40px;" >' + cellValue + '</textarea>';
                     } else {
                         return '<a href="' + cellValue + '" target="_blank"><img  style="width:98%;height:40px;" src="' + cellValue + '"/></a>' + '<form action="" id="modify_g_pic_form' + row.id + '" method="post" enctype="multipart/form-data" onsubmit="return false"><input type="hidden" name="id" value="' + row.id + '"/><input type="hidden" name="flag" value="' + row.argflag + '"/><input type="file" name="file"   style="width:98%;margin-top:5px;" ></form>';

                     }
                 }
             },
            
             {
                 name: 'op', index: 'op', width: 50, fix: true, formatter: function (cellValue, option, row) {
                     return '<button type="button" class="btn btn-success btn-sm"  onclick="saveEdit(' + row.id + ',' + row.argtype + ',\'' + row.argflag + '\')"><i class="glyphicon glyphicon-ok"></i><strong>保存</strong></button>';
                 }
             }

        ],

        jsonReader: {
            root: "data"

        },
        viewrecords: true,
        rowNum: 100,
        rowList: [10, 30, 50],
        pager: "",
        altRows: true,

        multiselect: false,
        //multikey: "ctrlKey",
        multiboxonly: true,

        loadComplete: function (data) {
            var table = this;

            if (data.code == "200") {
                if ((removenull(data.data) != "")) {
                    jsonData = data.data;
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
        caption: "",
        autowidth: true
    });
}


function saveEdit(id, argType, argflag) {

    if (argType == 1) {
        var newValue = $.trim($("#" + id + " textarea[name=argcontent]").val());
        if (newValue != undefined) {
            // /admin/globalargs/modifytext id content
            $.ajax({
                type: "post",
                url: "/admin/globalargs/modifytext_admin",
                data: {
                    "id": id,
                    "flag":argflag,
                    "content": newValue
                },
                success: function (response) {
                    var code = response.code;
                    if ("200" == code) {
                        alert("修改成功");
                        refreshDataGrid();
                    } else if ("607" == code) {
                        alert("您尚未登录或登录已失效！");
                        top.location.href = "../admin/login.html";
                    }
                    else {
                        alert("修改出错，原因：" + response.message);
                    }
                }
            });
        }
    } else {
        var formId = "#modify_g_pic_form" + id;
        $(formId).ajaxSubmit({
            type: "post",
            url: "/admin/globalargs/modifypic_admin",
            beforeSubmit: function () {
                if ($(formId + " [type=file]").val() == "") {
                    alert("请先选择文件");
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
                    alert("修改成功");
                    refreshDataGrid();
                } else if (code == "607") {
                    alert("您尚未登录或登录已失效！");
                    adminlogout();
                } else {
                    alert("修改失败，失败原因是:" + response.message);
                }
            }
        });
    }
}






