

var yundangrid_selector = "#yundangrid-table";
var yundanpager_selector = "#yundangrid-pager";
//查询数据时向后台提交的参数
var postData = {};
var jsonData = [];
var init = false;
//初始列表数据
$(function ($) {

 
    $(window).resize(function () {
        $(yundangrid_selector).setGridWidth($(".page-content").width() - 20, true);
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
    
    initList();
    loadstoreseachinfo();
});

function initList() {
    jQuery(yundangrid_selector).jqGrid({
        url: "/admin/sm_order/admin_search",
        datatype: "json",
        height: "auto",
        mtype: "post",
        //编号	用户	姓名	所属州	门店	预约时间	手机	确认时间	操作状态
        colNames: ['编号', '用户', '姓名','手机',   '门店', '预约时间', '确认时间','地址','状态' ,'备注', "操作"],
        
    colModel: [
        {
            name: 'id', index: 'id', width: 50,hidedlg:true,hidden:true
        },
        { name: 'userId', index: 'userId', width: 40 },
        { name: 'name', index: 'name', width: 80 },
        { name: 'phone', index: 'phone', width: 80 },
        { name: 'storeName', index: 'storeName', width: 80 },
        { name: 'appointmentDate', index: 'appointmentDate', width: 80 },
       
        { name: 'confirmDate', index: 'confirmDate', width: 80 },
         
        {
            name: 'alladdress', index: 'alladdress', width: 150, formatter: function (cellValue, option, row) {
              
            	var alladdress=row.address;
            	if(removenull(row.city)!="")
            	{
            		if(removenull(alladdress)!="")
            		{
            			alladdress=alladdress+", "+row.city;
            		}
            		else
            		{
            			alladdress=row.city;
            		}
            	}
            	
            	if(removenull(row.province)!="")
            	{
            		if(removenull(alladdress)!="")
            		{
            			alladdress=alladdress+", "+row.province;
            		}
            		else
            		{
            			alladdress=row.province;
            		}
            	}
            	if(removenull(row.zipCode)!="")
            	{
            		if(removenull(alladdress)!="")
            		{
            			alladdress=alladdress+", "+row.zipCode;
            		}
            		else
            		{
            			alladdress=row.zipCode;
            		}
            	}
            	
            	return alladdress;
            }
         },
        
         {
             name: 'state', index: 'state', width: 60, formatter: function (cellValue, option, row) {
               
             	var state=row.state;
             	var str="";
             	if(state=="0")
             	{
             		str="未处理";
             		//str="<option value='0'>未处理</option><option value='1'>处理中</option><option value='2'>处理完成</option><option value='-10'>异常</option>";
             	}
             	else if(state=="1")
             	{
             		str="处理中";
             		//str="<option value='0'>未处理</option><option value='1' selected='selected'>处理中</option><option value='2'>处理完成</option><option value='-10'>异常</option>";
             	}
             	else if(state=="2")
             	{
             		str="处理完成";
             		//str="<option value='0'>未处理</option><option value='1' >处理中</option><option value='2' selected='selected'>处理完成</option><option value='-10'>异常</option>";
             	}
             	else
             	{
             		str="异常";
             		//str="<option value='0'>未处理</option><option value='1' >处理中</option><option value='2' >处理完成</option><option value='-10' selected='selected'>异常</option>";
             	}
             	
             	//str="<select name='change_state_"+row.id+"' >"+str+"</select>";
             	
             	return str;
             }
          },
         /* {
              name: 'remark', index: 'remark', width: 100, formatter: function (cellValue, option, row) {
                
              	
              	var str="";
           
              	str="<textarea name='change_remark_"+row.id+"'  >"+row.remark+"</textarea>";
              	
              	return str;
              }
           },*/
           {
               name: 'remark', index: 'remark', width: 100, 
            },
         
         {
             name: 'op', index: 'op', width: 70, formatter: function (cellValue, option, row) {
                 var chuli = "<button style='margin-left:5px;margin-top:1px;' class='btn btn-purple btn-sm' type='button'  onclick=\"chuli('" + row.id + "')\"  ><span class='icon-credit-card'></span>修改</button>";
                // var view = "<button style='margin-left:5px;margin-top:1px;' class='btn btn-purple btn-sm' type='button'  onclick=\"view('" + row.id + "')\"  ><span class='icon-credit-card'></span>查看</button>";
                 //if(row.state=="1"){
                    // return view;
                 //}else{
                     return chuli;
               //  }
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
        caption: "预约上门取件列表",
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
}


//提交js对象参数刷新列表数据
function refreshDataGrid(data) {
    $(yundangrid_selector).jqGrid("setGridParam", { postData: data }).trigger("reloadGrid");
}



function chuli(id) {
	var row = jsonData.getData(id);
	
    setInputData("#divEdit", row);
    if(removenull(row.confirmDate)=="")//如果原来为空，把用户提交的时间作为确认时间
    {
    	$("#divEdit input[name='confirmDate']").val(row.appointmentDate);
    	
    }
    
    editDialog = dialog({
        zIndex: 99999,
        fixed: false,
        padding: "5px",
        title: "修改运单",
        //url: "yundanedit.html?id=" + id,
        content: document.getElementById("divEdit"),
        width: $(window).width() - 5,
        height: $("body").height() - 55,//默认为auto

    });
    editDialog.show();
	
	
	return false;
   
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
							str +=  ' <option value="' + this.id + '" country="' + this.country + '" province="' + this.province + '" callordercity="' + this.callOrderCity + '">' + this.name + '</option>';
						});
					}
					$("select[name='storeId']").html(str);
					
					
					//getchannelsbystores();
					//$("select[name='select_stores']").change(
						//	getchannelsbystores);
					changeWarehouseSelect();
					changeCitySelect();
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

function changeWarehouseSelect() {
    var option = $("select[name='storeId'] option:selected");
    var value=option.attr("value");
    if(value=="-1"||removenull(value)=="")
    {
    	var addOption = "<option value='-1'>请选择城市</option>";
    	$("select[name=city]").html(addOption);
    	return false;
    }
    


    var addOption = "<option value='-1'>请选择城市</option><option>";
    if (option.attr("callordercity").replace(/\s+/g, "") != "") {
        addOption += option.attr("callOrderCity").replace(/[\n\r]+/g, "</option><option>");
    }
    addOption += "</option>";
    var regExp = new RegExp("(<option></option>)+");
    addOption = addOption.replace(regExp, "");
    $("select[name=city]").html(addOption);
}
function changeCitySelect() {
    var option = $("select[name='city'] option:selected");
    var value=option.attr("value");
   // alert(option.text());
    if(value=="-1"||removenull(option.text())==""||removenull(option.text())=="请选择城市")
    {
    	$(":hidden[name='city_text']").val("");
    	return false;
    }
    $(":hidden[name='city_text']").val(option.text());
}


//修改运单
$(function () {
    //添加表单验证并提交,选择器的内容一定要是form表单，里面一定要有一个submit的按钮
    $("form").validate({
        //验证规则
        //http://www.runoob.com/jquery/jquery-plugin-validate.html
        debug: true,//不提交表单
    
        submitHandler: function (form) {
            var data = getEelementData("form");

            $.ajax({
                type: "post",
                url: "/admin/sm_order/admin_modify",
                data: data,
                success: function (response) {
                    var code = response.code;
                    if (code == "200") {
                        alert("修改成功");
                        //隐藏新增div
                       /* $("#divAdd").hide();
                        $("#divToolbar").show();
                        $("#divGrid").show();*/
                        //清空输入框
                        clearInputData("#divAdd");
                        //$("#divAdd textarea[name='remark']").val("");
                        //刷新列表数据
                        refreshDataGrid(postData);
                    } else if ("607" == code) {
                        alert("您尚未登录或登录已失效！");
                        adminlogout();
                    } else {
                        // 失败
                        alert("修改失败，原因是:" + response.message);
                    }
                }
            });
        }
    });
})


function printsmorders()
{
	 var ids = $(yundangrid_selector).jqGrid('getGridParam', 'selarrrow');
     //alert(ids);
     
     if((removenull(ids)=="")||(ids.length<1))
     {
     	alert("必须选择要删除的运单!");
     	return false;
     }
     
     $("#iframePrint").attr("src", "printSmorderPagelist.html?id="+ids+"&ran="+Math.random());
     return false;
     $.ajax({
     	type : "post",
 		url : "/admin/sm_order/print_list",
 		data : {
 			"ids" : ids
 		},
 		success : function(response) {
 			var code = response.code;
 			if ("200" == code) {
 				alert("打印成功!");
 				 //todo 这里补充 和后台交互的代码
                 //删除完成后 在success回调中调用下面方法刷新数据列表
                // refreshDataGrid(postData);
 				}
 			 	else if("607" == code) {
 					alert("您尚未登录或登录已失效！");
 					top.location.href = "../admin/login.html";
 				}
 				else
 				{
 					alert("打印失败，原因是："+response.message);
 				}
 			}});
}