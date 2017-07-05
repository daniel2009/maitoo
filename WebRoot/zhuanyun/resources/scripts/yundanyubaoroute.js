
var addressgrid_selector = "#addressgrid-table";
//查询数据时向后台提交的参数
var postData = {};
var jsonData;
var addresspager_selector = "#addressgrid-pager";
var init = false;

//初始列表数据
$(function ($) {
    loadseachinfo();
    initGrid();
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
        url: "/admin/t_order/get_all_t_order",
        datatype: "json",
        height: "auto",
        mtype: "post",
        // postData:{"state":"0"},
        //运单号	货物品名	预报日期	操作
        colNames: ['用户id', '用户', '转运单号', '商品备注', "转运仓库","入库仓库","仓位", '状态', '创建日期','问题备注', "状态","操作"],
        colModel: [

                 { name: 'user.id', index: 'user.realName', width: 40, },
                 { name: 'user.phone', index: 'user.phone', width: 60,formatter:function(cellValue,option,row){
                	 if(row.user==null)
                	 {
                		 return "";
                	 }
                	 else
                	 {
                		 return removenull(row.user.realName)+"/"+removenull(row.user.phone);
                	 }
                 } },
            { name: 'torderId', index: 'torderId', width: 80, },

             { name: 'remark', index: 'remark', width: 150, },
            { name: 'storeName', index: 'storeName', width: 60, },
            { name: 'i_storeName', index: 'i_storeName', width: 60, },
            { name: 'positionId', index: 'positionId', width: 60,formatter:function(cellValue,option,row){
            	if(row.position==null)
            	{
            		return "";
            	}
            	else
            	{
            		return removenull(row.position.position);
            	}
            } },
            
            
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
                name: 'createDate', index: 'createDate', width: 80
            },
            { name: 'qremark', index: 'qremark', width: 150,formatter:function(cellValue,option,row){
            	return "<input name='qremark' value='"+removenull(row.qremark)+"' />";
            } },
            { name: 'state', index: 'state', width: 80,formatter:function(cellValue,option,row){
            	
            
            	
            	
            	// 若是动态生成，请从后台中返回到前端页面隐藏起来
				var select = '<select name="newstate"><option value="-1">请选择</option><option value="0">用户预报</option><option value="1">录入失败</option><option value="2">包裹异常</option><option value="3">转运入库</option><option value="4">转运出库</option><option value="5">已入仓库</option><option value="6">已完成</option><option value="7">已自提</option></select>';
				var mySelect = select;
				var option = $(select).find(
						"option[value='" + row.state
								+ "']");
				if (option.length > 0) {
					var old = option[0].outerHTML;
					var newOption = option.attr(
							"selected", true)[0].outerHTML;
					mySelect = mySelect.replace(old,
							newOption);
				}
				return mySelect;
            	
            	//return "<input name='qremark' value='"+removenull(row.remark)+"' />";
            } },
            
             {
                 name: 'op', index: 'op', width: 100, formatter: function (cellValue, option, row) {
                     //返回修改按钮

                    // var del = "<button style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"del('" + row.id + "')\"  ><span class='fa fa-times'></span>删除</button>";
                     var route = "<button style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button' value='"+ row.id + "' onclick=\"modifystate(this)\"  ><span class='icon-edit'></span>修改</button>";
                     return route;
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

        multiselect: true,
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
                adminlogout();
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
        	 var ids = $(addressgrid_selector).jqGrid('getGridParam', 'selarrrow');
           	 
        	 $("#rowschoose").html("0");
             $("#rowschoosemoney").html("0");
             $("#rowschoosecost").html("0");

              //$("#morder_num").html(ids.length);
        	 var totalmoeny=0;
        	 var totalcost=0;
              for(var i=0;i<ids.length;i++)
	           {
	            	var id=ids[i];
	            	var row = jsonData.getData(id);
	            	if(!isNaN(parseFloat(row.totalmoney)&&parseFloat(row.totalmoney)>0))
	            	{
	            		totalmoeny=parseFloat(totalmoeny)+parseFloat(row.totalmoney);
	            	}
	            	if(!isNaN(parseFloat(row.totalcost)&&parseFloat(row.totalcost)>0))
	            	{
	            		totalcost=parseFloat(totalcost)+parseFloat(row.totalcost);
	            	}
	            	
	           }
              totalmoeny=Math.round(totalmoeny*100)/100;
              totalcost=Math.round(totalcost*100)/100;
              $("#rowschoose").html(ids.length);
              $("#rowschoosemoney").html(totalmoeny);
              $("#rowschoosecost").html(totalcost);
             // alert(totalmoeny);
        },
        onSelectAll: function (aRowids, status) {
        	var ids = aRowids;
        	 $("#rowschoose").html("0");
             $("#rowschoosemoney").html("0");
             $("#rowschoosecost").html("0");

              //$("#morder_num").html(ids.length);
        	 var totalmoeny=0;
        	 var totalcost=0;
              for(var i=0;i<ids.length;i++)
	           {
	            	var id=ids[i];
	            	var row = jsonData.getData(id);
	            	if(!isNaN(parseFloat(row.totalmoney)&&parseFloat(row.totalmoney)>0))
	            	{
	            		totalmoeny=parseFloat(totalmoeny)+parseFloat(row.totalmoney);
	            	}
	            	if(!isNaN(parseFloat(row.totalcost)&&parseFloat(row.totalcost)>0))
	            	{
	            		totalcost=parseFloat(totalcost)+parseFloat(row.totalcost);
	            	}
	            	
	           }
              totalmoeny=Math.round(totalmoeny*100)/100;
              totalcost=Math.round(totalcost*100)/100;
              $("#rowschoose").html(ids.length);
              $("#rowschoosemoney").html(totalmoeny);
              $("#rowschoosecost").html(totalcost);
             
        },
        caption: "预报列表",
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



//删除
function del(id) {

    if (id == undefined) { top.window.tips("请选择需要删除的预报运单", false); return; }

    top.window.confirmDialog("确定删除选择的数据？", "删除提示", function () {
        $.ajax({
            type: "post",
            data: { "id": id },
            url: "/admin/t_order/delete_one",
            success: function (response) {
                var code = response.code;
                if (code == "200") {
                    alert("删除成功！");
                    refreshDataGrid(postData);
                } else if ("607" == code) {
                    alert("您尚未登录或登录已失效！");
                    adminlogout();
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

function modifystate(obj)
{
	var id=$(obj).val();
	var state=$(obj).parent().parent().find("select[name='newstate']").val();
	var qremark=$(obj).parent().parent().find("input[name='qremark']").val();
	 top.window.confirmDialog("确定修改？", "修改提示", function () {
	        $.ajax({
	            type: "post",
	            data: { "id": id,"state":state,"qremark":qremark },
	            url: "/admin/t_order/modify_state",
	            success: function (response) {
	                var code = response.code;
	                if (code == "200") {
	                    alert("修改成功！");
	                    refreshDataGrid(postData);
	                } else if ("607" == code) {
	                    alert("您尚未登录或登录已失效！");
	                    adminlogout();
	                } else {

	                    alert("修改失败，原因是:" + response.message);
	                }
	            }
	        });
	    });
}
