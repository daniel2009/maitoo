

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
        url: "/admin/warehouse/search_admin",
        //url: "testdata/cangkudata.html",
        datatype: "json",
        height: "auto",
        mtype: "get",
        //编号	排序	名称	详细地址	联系人	联系电话	国家	省/州	城市	邮编	是否转运	操作
        colNames: ['门店id', '名称','国家',  '州', '城市', '详细地址', '邮编','联系人', '联系电话',  '上门取货', "操作"],
        colModel: [
            //{"id":"4","name":"Los Angeles 总仓","company":"weiye express(伟业快递)","country":"USA","province":"CA","city":"Temple","address":"4969 sereno dr","zipCode":"91780","telephone":"(626)8617959","contactName":"xiaochi cai","serialNo":"1","remark":"dfgdsfgd","createDate":"2015-05-16 02:08:23","groupId":"1","printPhoneCN":"13800000000","printPhoneUSA":"62600000","printLogo":"/resources/pics/global/printpic_KjjUS_87539.png","print2Code":"/resources/pics/global/printpic_XLUNk_98328.png","type":"1","toChinaPrice":null,"changeWarehousePrice":null,"callOrderAvailable":"1","callOrderCity":"水电费\r\n傻大个\r\n收到","tranprice":null,"printUrl":"www.US258.com","printWidCode":"56"}
            { name: 'id', index: 'id', width: 30 },
            { name: 'name', index: 'name', width: 80 },
            { name: 'country', index: 'country', width: 40 },
            { name: 'province', index: 'province', width: 50 },
            { name: 'city', index: 'city', width: 60 },
            { name: 'address', index: 'address', width: 80 },
            { name: 'zipCode', index: 'zipCode', width: 60 },
             { name: 'contactName', index: 'contactName', width: 60 },
             { name: 'telephone', index: 'telephone', width: 80, fix: true },

             
             
             {
                 name: 'callOrderAvailable', index: 'callOrderAvailable', width: 40, formatter: function (cellValue, option, row) {
                     if (cellValue == 1) {
                         return "<span style='color:blue'>是</span>";
                     } else {
                         return "否";
                     }
                 }
             },

             {
                 name: 'op', index: 'op', width: 100, fix: true, formatter: function (cellValue, option, row) {
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
        caption: "门店列表",
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
                var ids = $(yundangrid_selector).jqGrid('getGridParam', 'selarrrow');
                var idData = "";
                for (var i = 0; i < ids.length; i++) {
                    idData = idData + "id=" + ids[i] + "&";
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
        $("#divGrid").hide();
        $("#divAdd").show();
        getWarehouse(0);
    });

    $(".cancel").click(function () {
        $("#divGrid").show();
        $("#divAdd").hide();
        $("#divEdit").hide();
        $("#divDetail").hide();
        $('#divAdd form').resetForm();
        $('#divEdit form').resetForm();
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

    $("#divAdd input[name=ckcallOrderAvailable]").change(function () {
        if ($(this).is(':checked') == true) {
            $("#divAdd input[name=callOrderAvailable]").val("1");
        } else {
            $("#divAdd input[name=callOrderAvailable]").val("0");
        }
    });
    
    $("#divAdd input[name=cshowstore]").change(function () {
        if ($(this).is(':checked') == true) {
            $("#divAdd input[name=showstore]").val("1");
        } else {
            $("#divAdd input[name=showstore]").val("0");
        }
    });
    $("#divAdd input[name=csendmessage]").change(function () {
        if ($(this).is(':checked') == true) {
            $("#divAdd input[name=sendmessage]").val("1");
        } else {
            $("#divAdd input[name=sendmessage]").val("0");
        }
    });

    $("#divAdd input[name=cktype]").change(function () {
        if ($(this).is(':checked') == true) {
            $("#divAdd input[name=type]").val("1");
        } else {
            $("#divAdd input[name=type]").val("0");
        }
    });

    $("#divEdit input[name=ckcallOrderAvailable]").change(function () {
        if ($(this).is(':checked') == true) {
            $("#divEdit input[name=callOrderAvailable]").val("1");
        } else {
            $("#divEdit input[name=callOrderAvailable]").val("0");
        }
    });
    
    
    $("#divEdit input[name=cshowstore]").change(function () {
        if ($(this).is(':checked') == true) {
            $("#divEdit input[name=showstore]").val("1");
        } else {
            $("#divEdit input[name=showstore]").val("0");
        }
    });
    $("#divEdit input[name=csendmessage]").change(function () {
        if ($(this).is(':checked') == true) {
            $("#divEdit input[name=sendmessage]").val("1");
        } else {
            $("#divEdit input[name=sendmessage]").val("0");
        }
    });

    $("#divEdit input[name=cktype]").change(function () {
        if ($(this).is(':checked') == true) {
            $("#divEdit input[name=type]").val("1");
        } else {
            $("#divEdit input[name=type]").val("0");
        }
    });

    //添加验证并提交,选择器的内容一定要是form表单，里面一定要有一个submit的按钮
    $("#divAdd form").validate({
        //name":"Los Angeles 总仓","company":"weiye express(伟业快递)","country":"USA","province":"CA","city":"Temple","address":"4969 sereno dr","zipCode,wid_code_char2
        rules: {
            name: { required: true, minlength: 2, maxlength: 56 },
            country: { required: true, minlength: 2, maxlength: 56 },
            province: { required: true, minlength: 2, maxlength: 56 },
            city: { required: true, minlength: 2, maxlength: 56 },
            address: { required: true, minlength: 2, maxlength: 128 },
            zipCode: { required: true, minlength: 5, maxlength: 6 },
            printWidCode: { required: true, minlength: 2, maxlength: 2 },

        }, submitHandler: function (form) {

        	
        	  if ($("#divAdd input[name=cshowstore]").is(':checked') == true) {
                  $("#divAdd input[name=showstore]").val("1");
              } else {
                  $("#divAdd input[name=showstore]").val("0");
              }
         
         
              if ($("#divAdd input[name=csendmessage]").is(':checked') == true) {
                  $("#divAdd input[name=sendmessage]").val("1");
              } else {
                  $("#divAdd input[name=sendmessage]").val("0");
              }
            $("#divAdd form").ajaxSubmit({
                type: "post",
                url: "/admin/warehouse/add",
                beforeSubmit: function () {
                    //alert("tijiao");
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
                        $('#divAdd form').resetForm();
                        //$(".cancel").click();
                        //刷新列表数据
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
        	  name: { required: true, minlength: 2, maxlength: 56 },
              country: { required: true, minlength: 2, maxlength: 56 },
              province: { required: true, minlength: 2, maxlength: 56 },
              city: { required: true, minlength: 2, maxlength: 56 },
              address: { required: true, minlength: 2, maxlength: 128 },
              zipCode: { required: true, minlength: 5, maxlength: 6 },
              printWidCode: { required: true, minlength: 2, maxlength: 2 },

        }, submitHandler: function (form) {
        	
        	
        	
        	
            
            if ($("#divEdit input[name=cshowstore]").is(':checked') == true) {
                $("#divEdit input[name=showstore]").val("1");
            } else {
                $("#divEdit input[name=showstore]").val("0");
            }
       
       
            if ($("#divEdit input[name=csendmessage]").is(':checked') == true) {
                $("#divEdit input[name=sendmessage]").val("1");
            } else {
                $("#divEdit input[name=sendmessage]").val("0");
            }
           

            $("#divEdit form").ajaxSubmit({
                type: "post",
                url: "/admin/warehouse/modify",
                beforeSubmit: function () {
                    //alert("tijiao");
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
                        //$(".cancel").click();
                        //刷新列表数据
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


//提交js对象参数刷新列表数据
function refreshDataGrid(data) {
    $(yundangrid_selector).jqGrid("setGridParam", { postData: data }).trigger("reloadGrid");
}

function update(id) {
    var row = jsonData.getData(id);
    clearInputData("#divEdit");
    setInputData("#divEdit", row);
    setSpanText("#divEdit", row);
    $("#divGrid").hide();
    $("#divEdit").show();
    $("#divEdit textarea[name=remark]").val(row.remark);
    $("#divEdit textarea[name=callOrderCity]").val(row.callOrderCity);
    if (row.callOrderAvailable == 1) {
        $("#divEdit input[name=ckcallOrderAvailable]").attr("checked", true);
        $("#divEdit input[name=callOrderAvailable]").val("1");
    } else {
        $("#divEdit input[name=ckcallOrderAvailable]").attr("checked", false);
        $("#divEdit input[name=callOrderAvailable]").val("0");
    }

    
    if (row.sendmessage == 1) {
        $("#divEdit input[name=csendmessage]").attr("checked", true);
        $("#divEdit input[name=sendmessage]").val("1");
    } else {
        $("#divEdit input[name=csendmessage]").attr("checked", false);
        $("#divEdit input[name=sendmessage]").val("0");
    }
    
    if (row.showstore == 1) {
        $("#divEdit input[name=cshowstore]").attr("checked", true);
        $("#divEdit input[name=showstore]").val("1");
    } else {
        $("#divEdit input[name=cshowstore]").attr("checked", false);
        $("#divEdit input[name=showstore]").val("0");
    }
    
    if (row.type == 1) {
        $("#divEdit input[name=cktype]").attr("checked", true);
        $("#divEdit input[name=type]").val("1");
    } else {
        $("#divEdit input[name=cktype]").attr("checked", false);
        $("#divEdit input[name=type]").val("0");
    }
}

function showdetail(id) {
    $("#divGrid").hide();

    setSpanText("#divDetail", row);
    $("#divDetail").show();
}

function del(id) {

    window.confirmDialog("确定删除选中的门店?", "删除提示", function () {
        $.ajax({
            type: "post",
            url: "/admin/warehouse/del",
            data: { id: id },
            success: function (response) {
                var code = response.code;
                if (code == "200") {
                    alert("删除成功");

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

function getWarehouse(selectId) {
    $.ajax({
        type: "get",
        url: "/admin/warehouse/all",
        //data: { id: id },
        success: function (response) {
            var code = response.code;
            if (code == "200") {
                var data = response.data;
                var options = "";
                for (var i = 0; i < data.length; i++) {
                    if (data[i].id == selectId) {
                        options += '<option value="' + data[i].id + '" selected="selected">' + data[i].name + '</option>';
                    } else {
                        options += '<option value="' + data[i].id + '">' + data[i].name + '</option>';
                    }
                }
                $("select[name=storeId]").html(options);
            } else if (code == "607") {
                alert("您尚未登录或登录已失效！");
                adminlogout();
            } else {
                alert("获取仓库信息失败，失败原因是:" + response.message);
            }
        }
    });
}





