

var yundangrid_selector = "#yundangrid-table";
//查询数据时向后台提交的参数
var postData = {};
var jsonData = [];

//初始列表数据
$(function ($) {
	var id=request("id");
	$("#divGrid :hidden[name='id']").val(id);
	
	$("#showstoreName").html(request("storeName"));
	$("#showshelveName").html(request("nickName"));
	$("#showshelveNo").html(request("shelvesNo"));
	$("#showremark").html(request("remark"));
	
	
	
	
	
	//alert(id);
	//return false;
	//postData={"id":id};
    var yundanpager_selector = "#yundangrid-pager";
    var init = false;
    $(window).resize(function () {
        $(yundangrid_selector).setGridWidth($(".page-content").width() - 20, true);
    });

    jQuery(yundangrid_selector).jqGrid({
        url: "/admin/Shelves/search_position",
        datatype: "json",
        height: "auto",
        mtype: "post",
        postData:{"id":id},
        //员工ID	门市编号	门市名称	权限	操作
        colNames: ['仓位','状态', '备注','修改日间',"创建时间","操作"],
        colModel: [
               {
                   name: 'position', index: 'position', width: 30, fix:true
               },
            {
                name: 'state', index: 'state', width: 30, fix:true, formatter: function (cellValue, option, row) {
                	if(removenull(row.state)!=""&&row.state=="1")
                	{
                		return "<span style='color:blue'>已使用</span>";
                	}
                	else
                	{
                		return "<span style='color:red'>未使用</span>";
                	}
                }
            },
             { name: 'remark', index: 'remark', width: 80, fix: true },
             { name: 'modifyDate', index: 'wNo', width: 50 ,fix:true },
             { name: 'createDate', index: 'createDate', width: 50 ,fix:true },
             {
                 name: 'op', index: 'op', width: 30, fix: true, formatter: function (cellValue, option, row) {
    
                     var del = "<button style='margin-left:5px;margin-top:1px;' class='btn btn-danger btn-sm' type='button'  onclick=\"del('" + row.id + "')\"  ><span class='fa fa-times'></span>清空</button>";
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
        rowList: [10, 50, 100,500,1000],
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
        caption: "仓位列表",
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
        });
    }

    loadstoreseachinfo();
    $("#btnAdd").click(function () {
    	
    	
    	
    	//loadstoreseachinfo();
        clearInputData("#divAdd");
        $("#divAdd select[name='add_type']").val("0");
       $("#divGrid").hide();
        $("#divAdd").show();
        getWarehouse(0);
    });

    $(".cancel").click(function () {
        $("#divGrid").show();
        $("#divAdd").hide();
        $("#divEdit").hide();
        $("#divDetail").hide();
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
    
    $("#divAdvanceSearch #empId").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnAdvanceSearch").click();
        }
    }));
    



  
});


//提交js对象参数刷新列表数据
function refreshDataGrid(data) {
    $(yundangrid_selector).jqGrid("setGridParam", { postData: data }).trigger("reloadGrid");
}

function update(id) {
	
    var row = jsonData.getData(id);
    loadstoreseachinfo(row);
    clearInputData("#divEdit");
    setInputData("#divEdit", row);
    setSpanText("#divEdit", row);
    
    $("#divEdit select[name='add_type']").val(row.type);
    $("#divGrid").hide();
    $("#divEdit").show();
  /*  getWarehouse(row.storeId);
    $("#divEdit [name=authority_id]").each(function () {
        var a = row.authority;
        for (var i = 0; i < a.length; i++) {
            if (a[i].authority_id == $(this).attr("val")) {
                $(this).attr("checked", "checked");
            }
        }
    });*/
}

function showdetail(id) {
    $("#divGrid").hide();
    var row = jsonData.getData(id);
    row.auth = "";
    if (row.authority) {
        for (var i = 0; i < row.authority.length; i++) {
            row.auth += row.authority[i].name + "，";
        }
    }
    setSpanText("#divDetail", row);
    $("#divDetail").show();
}

function del(id) {
    
    window.confirmDialog("确定清空?如果清空，运单对应的仓位也会被清空！", "清空提示", function () {
        $.ajax({
            type: "post",
            url: "/admin/Shelves_position/clear",
            data: {"id":id},
            success: function (response) {
                var code = response.code;
                if (code == "200") {
                    alert("清空成功");
                    
                    refreshDataGrid(postData);
                } else if (code == "607") {
                    alert("您尚未登录或登录已失效！");
                    adminlogout();
                } else {
                    alert("清空失败，失败原因是:" + response.message);
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
                        options += '<option value="'+data[i].id+'" selected="selected">'+data[i].name+'</option>';
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

//装载上门门店
function loadstoreseachinfo() {

	$.ajax({
		type : "post",
		url : "/admin/warehouse/getself",
		success : function(response) {
			var code = response.code;

			if ("200" == code) {

				var str = "";
				if (response.data != null) {
					storelist_jason = response.data;
					if (response.data.length == 1) {
						// alert(response.data.id);
						//str = "<option value='" + response.data[0].id + "'>"
						//		+ response.data[0].name + "</option>";
						
						str += ' <option value="' + response.data[0].id + '" country="' + response.data[0].country + '" province="' + response.data[0].province + '" callordercity="' + response.data[0].callOrderCity + '">' + response.data[0].name + '</option>';
						/*
						 * $.each(response.data, function() { str+="<option
						 * value='"+this.id+"'>"+this.name+"</option>"; });
						 */
					} else {
						str = "<option value='" + "-1" + "'>" + "请选择门店"
								+ "</option>";
						$.each(response.data, function() {
							str +=  ' <option value="' + this.id + '">' + this.name + '</option>';
						});
					}
					$("select[name='storedId']").html(str);
					$("select[name='s_storeId']").html(str);
					
					
					
					//getchannelsbystores();
					//$("select[name='select_stores']").change(
						//	getchannelsbystores);
					//changeWarehouseSelect();
					//changeCitySelect();
				} else {

				}
			} else if ("607" == code) {
				alert("您尚未登录或登录已失效！");
				top.location.href = "../admin/login.html";
			} else {
				alert("读取门店信息出错，原因：" + response.message);
			}

		}
	});
}




