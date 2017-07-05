
$(function () {
    $("input[type=checkbox]").bootstrapSwitch();
})

var addressgrid_selector = "#addressgrid-table";
//查询数据时向后台提交的参数
var postData = {};
var jsonData;
var addresspager_selector = "#addressgrid-pager";
var init = false;


//初始列表数据
$(function ($) {
    initGrid();
    
    initClickEvent();

    //添加表单验证并提交,选择器的内容一定要是form表单，里面一定要有一个submit的按钮
    $("#divAdd form").validate({
        //验证规则
        //http://www.runoob.com/jquery/jquery-plugin-validate.html
        debug: true,//提交表单，true:不提交表单,手动ajax
        rules: {
            name: { required: true },
            //state: { required: true },
          //  city: { required: true },
          //  dist: { required: true },
          //  streetAddress: { required: true },
            phone: { required: true },

        }, submitHandler: function (form) {
            var data = getEelementData("#divAdd");
            
            $.ajax({
    			type : "post",
    			url : "/consignee_sendperson/add",
    			data : data,
    			success : function(response) {
    				var code = response.code;
    				if (code == "200") {
    					 window.tips("保存成功", false);
    					 clearInputData("#divAdd");
    					 refreshDataGrid(postData);
    				} else if ("607" == code) {
    					alert("您尚未登录或登录已失效！");
    					userlogout();
    				} else {
    					// 失败
    					alert("保存失败，原因是:" + response.message);
    				}
    			}
    		});
            
            
            
            
            // top.window.infoDialog("提交", "数据是" + data, true);
            //ajaxSubmit 提交到后台操作，操作完成后调用下面方法
            
            
           
        }
    });

    //编辑表单验证并提交,选择器的内容一定要是form表单，里面一定要有一个submit的按钮
    $("#divEdit form").validate({
        //验证规则
        //http://www.runoob.com/jquery/jquery-plugin-validate.html
        debug: true,//提交表单，true:不提交表单,手动ajax
        rules: {
            name: { required: true },
          //  state: { required: true },
           // city: { required: true },
          //  dist: { required: true },
           // streetAddress: { required: true },
            phone: { required: true },

        }, submitHandler: function (form) {
        	
        	
        	
        	
        	
            var data = getEelementData("#divEdit");
            
            
            $.ajax({
    			type : "post",
    			url : "/consignee_sendperson/modify_info",
    			data : data,
    			success : function(response) {
    				var code = response.code;
    				if (code == "200") {
    					 window.tips("保存成功", false);
    					 clearInputData("#divEdit");
    					//刷新列表数据
    			          refreshDataGrid(postData);
    			          $("#cancelEdit").click();
    				} else if ("607" == code) {
    					alert("您尚未登录或登录已失效！");
    					userlogout();
    				} else {
    					// 失败
    					alert("删除信息失败，原因是:" + response.message);
    				}
    			}
    		});
            
            
            
           // top.window.infoDialog("提交", "数据是" + data, true);
            //ajaxSubmit 提交到后台操作，操作完成后调用下面方法
           
           
            
        }
    });
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


    $("#btnAdd").click(function () {
        $("#divList").hide();
        $("#divEdit").hide();
        $("#divAdd").show();
        refreshDataGrid(postData);
    });

    $("#cancelAdd").click(function () {
        $("#divList").show();
        $("#divEdit").hide();
        $("#divAdd").hide();
    });

    $("#cancelEdit").click(function () {
        $("#divList").show();
        $("#divEdit").hide();
        $("#divAdd").hide();
    });

    $("#divAdd #hc").click(function () {
        $("#divAdd t[name=hc]").show();
        $("#divAdd t[name=dl]").hide();
    });

    $("#divAdd #dl").click(function () {
        $("#divAdd t[name=hc]").hide();
        $("#divAdd t[name=dl]").show();
    });

    $("#divEdit #hc").click(function () {
        $("#divEdit t[name=hc]").show();
        $("#divEdit t[name=dl]").hide();
    });

    $("#divEdit #dl").click(function () {
        $("#divEdit t[name=hc]").hide();
        $("#divEdit t[name=dl]").show();
    });
}

function initGrid() {
    $(window).resize(function () {
        $(addressgrid_selector).setGridWidth($(".page-content").width() - 2, true);
    });

    jQuery(addressgrid_selector).jqGrid({
        url: "/consignee_sendperson/search",
        datatype: "json",
        height: "auto",
        mtype: "get",

       
    colNames: ['寄件人姓名', '电话', '州',  "城市","详细地址","邮编", "EMail", "公司名称", "操作" ],
        colModel: [

            {
                name: 'name', index: 'name', width: 60, formatter: function (cellvalue, options, row) {
                    return '<a href="javascript:void(0)" onclick="showdetail(' + row.id + ')">' + cellvalue + '</a>';
                }
            },
            { name: 'phone', index: 'phone', width: 80, },
            { name: 'province', index: 'province', width: 60, },
            { name: 'city', index: 'city', width: 60, },
            { name: 'streetAddress', index: 'streetAddress', width: 140, },
            { name: 'zipCode', index: 'zipCode', width: 60, },
            
            { name: 'email', index: 'email', width: 100, },
            { name: 'company', index: 'company', width: 80, },
             {
                 name: 'op', index: 'op', width: 120, formatter: function (cellValue, option, row) {
                     //返回修改按钮
                     var edit = "<button  style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"update('" + row.id + "')\"  ><span class='icon-edit'></span>修改</button>";
                     var del = "<button style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"del('" + row.id + "')\"  ><span class='fa fa-times'></span>删除</button>";
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
        pager: addresspager_selector,
        altRows: true,

        multiselect: false,
        //multikey: "ctrlKey",
        multiboxonly: true,

        loadComplete: function (data) {
        	
        	
           // jsonData = data.data.datas;
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
				alert("修改失败，原因："+data.message);
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

//点击编辑
function update(id) {
    var row = jsonData.getData(id);
    setInputData("#divEdit", row);
    $("#divList").hide();
    $("#divEdit").show();
    $("#divAdd").hide();
}

//删除
function del(id) {

    if (id == undefined) { top.window.tips("请选择需要删除的行", false); return; }

    top.window.confirmDialog("确定删除选择的数据？", "删除提示", function () {
    	
		$.ajax({
			type : "post",
			url : "/consignee_sendperson/del",
			data : {
				"id" : id
			},
			success : function(response) {
				var code = response.code;
				if (code == "200") {
					  window.tips("数据已删除", false);
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

function showdetail(id) {

}



