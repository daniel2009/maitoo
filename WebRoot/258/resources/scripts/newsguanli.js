

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

    jQuery(yundangrid_selector).jqGrid({
        url: "/admin/news/search",
        //url: "testdata/newsdata.html",
        datatype: "json",
        height: "auto",
        mtype: "get",
        //新闻编号	新闻标题	新闻内容	发布人	添加时间	修改时间	备注	操作
        colNames: ['新闻编号', '新闻标题', '新闻内容', '发布人', "添加时间", "修改时间", "备注", "操作"],
        //{"id":"43","title":"test1","content":"afa asdfasf asdf asf asdf asfasdfasfas faasfsds asfasdf asfasd fasf asdfasf","releaseTime":null,"author":"admin","authorid":"104","releasetime":"2016-02-16 11:57:12","modifytime":"2016-02-16 11:57:12","viewcontent":"afa asdfasf asdf asf asdf asfasdfasfas faasfsds asfasdf asfasd fasf asdfasf","tag":null,"img":null,"remark":"admin 创建！","pic1":null},
        colModel: [
            { name: 'id', index: 'id', width: 40, },

             { name: 'title', index: 'title', width: 80 },
              { name: 'content', index: 'content', width: 120 },
              { name: 'author', index: 'author', width: 60 },
             { name: 'releasetime', index: 'releasetime', width: 90 },
             { name: 'modifytime', index: 'modifytime', width: 90 },
             { name: 'remark', index: 'remark', width: 90 },

             {
                 name: 'op', index: 'op', width: 100, formatter: function (cellValue, option, row) {

                     var edit = "<button  style='margin-left:5px;margin-top:1px;' class='btn btn-purple btn-sm' type='button'  onclick=\"update('" + row.id + "')\"  ><span class='icon-edit'></span>修改</button>";
                     var del = "<button style='margin-left:5px;margin-top:1px;' class='btn btn-danger btn-sm' type='button'  onclick=\"del('" + row.id + "')\"  ><span class='fa fa-times'></span>删除</button>";
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
        caption: "新闻列表",
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
                var ids = $(yundangrid_selector).jqGrid('getGridParam', 'selarrrow');
                var idData = "";
                for (var i = 0; i < ids.length; i++) {
                    idData = idData + "ids=" + ids[i] + "&";
                }
                del(idData);
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


    $("#btnAdd").click(function () {
        clearInputData("#divAdd");
        $("#divAdd[name=content]").text("");
        $("#divGrid").hide();
        $("#divAdd").show();
    });

    $(".cancel").click(function () {
        $("#divGrid").show();
        $("#divAdd").hide();
        $("#divEdit").hide();
    });

    //点击高级搜索
    $("#btnAdvanceSearch").click(function () {
        var data = getEelementData("#divAdvanceSearch");
        refreshDataGrid(data);
    });


    $("#divAdvanceSearch #keyword").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnAdvanceSearch").click();
        }
    }));

    //添加表单验证并提交,选择器的内容一定要是form表单，里面一定要有一个submit的按钮
    $("#divAdd form").validate({
        //验证规则
        //http://www.runoob.com/jquery/jquery-plugin-validate.html
        rules: {
            title: { required: true, minlength: 3 },
            content: { required: true }
        }, submitHandler: function (form) {
            $("#divAdd form").ajaxSubmit({
                type: "post",
                beforeSubmit: function () {

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
                        alert("添加成功");
                        refreshDataGrid(postData);
                    } else if (code == "607") {
                        alert("您尚未登录或登录已失效！");
                        adminlogout();
                    } else {
                        alert("添加失败，失败原因是:" + response.message);
                    }
                }
            });
        }
    });

    $("#divEdit form").validate({
        //验证规则
        //http://www.runoob.com/jquery/jquery-plugin-validate.html
        rules: {
            title: { required: true, minlength: 3 },
            content: { required: true }
        }, submitHandler: function (form) {
            $("#divEdit form").ajaxSubmit({
                type: "post",
                beforeSubmit: function () {

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
                        refreshDataGrid(postData);
                        $(".cancel").click();
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

//提交js对象参数刷新列表数据
function refreshDataGrid(data) {
    $(yundangrid_selector).jqGrid("setGridParam", { postData: data }).trigger("reloadGrid");
}

function update(id) {
    var row = jsonData.getData(id);
    clearInputData("#divEdit");
    $("#divEdit[name=content]").html(row.content);
    setInputData("#divEdit", row);

    if (row.pic1 != undefined && row.pic1 != "") {
        $("#picA").attr("href", row.pic1);
        $("#picA img").attr("src", row.pic1);
    }
    else
    {
    	 $("#picA").attr("href", "");
         $("#picA img").attr("src", "");
    }
    $("#divGrid").hide();
    $("#divEdit").show();
}

function del(id) {
    var idData = "";
    if (id * 1 > 0) {// 单个删除
        idData = "ids=" + id;
    } else {
        idData = id;//批量删除 ，拼接为 id=1&id=2的字符
    }
    window.confirmDialog("确定删除选中的新闻?", "删除提示", function () {
        $.ajax({
            type: "post",
            url: "/admin/news/del",
            data: idData,
            success: function (response) {
                var code = response.code;
                if (code == "200") {
                    alert("删除成功");
                    //todo 这里补充 和后台交互的代码
                    //删除完成后 在success回调中调用下面方法刷新数据列表
                    refreshDataGrid(postData);
                } else if (code == "607") {
                    alert("您尚未登录或登录已失效！");
                    adminlogout();
                } else {
                    alert("删除失败，失败原因是:" + response.message);
                }
            }
        });
    });
}

