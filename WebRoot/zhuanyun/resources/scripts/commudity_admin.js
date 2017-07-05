$(function ($) {
	getchannels();
});



function getchannels()
{
	$("select[name='select_channels']").html("");
	
	
	
	$.ajax({
     	type : "post",
 		url : "/admin/channel/get_all",
 		success : function(response) {
 			var code = response.code;
 			if ("200" == code) {
 				var emp=response.data;
	 				if(emp!=null)
	 				{
	 					var str="<option value='-1'>请选择渠道</option>";
	 					$.each(emp,function(){
	 						str+="<option value='"+this.id+"'>"+this.name+"</option>";
	 					});
	 					$("select[name='select_channels']").html(str);
	 					
	 					$("select[name='select_channels']").change(changechannel);//渠道改变执行重新搜索
	 					
	 					
	 				}
 				}
 			 	else if("607" == code) {
 					alert("您尚未登录或登录已失效！");
 					adminlogout();
 				}
 				else
 				{
 					alert("获取数据失败，原因是："+response.message);
 				}
 			}});
}


var yundangrid_selector = "#yundangrid-table";
//查询数据时向后台提交的参数
var postData = {};
var jsonData = [];
//初始列表数据
$(function ($) {
	getchannels();
  var yundanpager_selector = "#yundangrid-pager";
  var init = false;
  $(window).resize(function () {
      $(yundangrid_selector).setGridWidth($(".page-content").width() - 20, true);
  });

  jQuery(yundangrid_selector).jqGrid({
      url: "/admin/commudityAdmin/search",
      datatype: "json",
      height: "auto",
      mtype: "get",
      //员工ID	门市编号	门市名称	权限	操作
      colNames: ['商品id','所属渠道','商品名称', '备注', '启用','创建时间',"修改时间","操作"],
      colModel: [
             {
                 name: 'id', index: 'id', width: 30, fix:true,
             },
             {
                 name: 'channelName', index: 'channelName', width: 50, fix:true,formatter:function (cellValue, option, row) {
                	var text="";
                	 if(row.channel!=null)
                	{
                		 text=row.channel.name;
                	}
                	 
                	 return text;
                 }
             },
            
          {
              name: 'name', index: 'name', width: 50, fix:true,formatter:function (cellValue, option, row) {
             	 return "<input name='new_name_"+row.id+"' value='"+cellValue+"'/>";
              }
          },
           { name: 'remark', index: 'remark', width: 80, fix: true ,formatter:function (cellValue, option, row) {
           	 return "<input name='new_remark_"+row.id+"' value='"+cellValue+"'/>";
           }
          },
           { name: 'state', index: 'state', width: 30 ,fix:true,formatter:function (cellValue, option, row) {
        	   var text="";
        	   if(cellValue=="1")//启用
        	   {
        		   text="<select name='new_state_"+row.id+"'><option value='1' selected = 'selected'>是</option><option value='0'>否</option></select>";
        	   }
        	   else
        	   {
        		   text="<select name='new_state_"+row.id+"'><option value='0'>否</option><option value='1'>是</option></select>";
        	   }
        	   
           	 return text;
           }
          },

           { name: 'createDate', index: 'createDate', width: 60 ,fix:true },
           { name: 'modifyDate', index: 'modifyDate', width: 60 ,fix:true },
           {
               name: 'op', index: 'op', width: 100, fix: true, formatter: function (cellValue, option, row) {
                   var edit = "<button  style='margin-left:5px;margin-top:1px;' class='btn btn-purple btn-sm' type='button'  onclick=\"update('" + row.id + "')\"  ><span class='icon-edit'></span>修改</button>";
                   var del = "<button style='margin-left:5px;margin-top:1px;' class='btn btn-danger  btn-sm' type='button'  onclick=\"del('" + row.id + "')\"  ><span class='fa fa-times'></span>删除</button>";
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
      caption: "渠道列表",
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


  $("#btnAdd").click(function () {
  	
	  var channelId=$("select[name='select_channels']").val();
	  if(removenull(channelId)==""||(channelId=="-1"))
	  {
		  alert("请先选择渠道再添加商品!");
		  return false;
	  }
	  $("#add_select_channel_name").html($("select[name='select_channels']").find("option:selected").text());
  //	getWarehouselist("");
      clearInputData("#divAdd");
      $("#divAdd select[name='state']").val("0");
     // $("#divGrid").hide();
   //   $("#divAdd").show();
     // getWarehouse(0);
  	editDialog = dialog({
        zIndex: 99999,
        fixed: false,
        padding: "5px",
        title: "修改运单",
        //url: "yundanedit.html?id=" + id,
        content: document.getElementById("divAdd"),
        width: $(window).width() - 5,
        height: "auto",//默认为auto

    });
    editDialog.show();	
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
  

  //添加航班表单验证并提交,选择器的内容一定要是form表单，里面一定要有一个submit的按钮
  $("#divAdd form").validate({
      //验证规则
      //http://www.runoob.com/jquery/jquery-plugin-validate.html
      rules: {
      	//name: { required: true, minlength: 6, maxlength: 20 },
      	name: { required: true, minlength: 2,maxlength:40 },
      	state: { required: true },
      	
         
      }, submitHandler: function (form) {
   
    	  var channelId=$("select[name='select_channels']").val();
    	  if(removenull(channelId)==""||(channelId=="-1"))
    	  {
    		  alert("请先选择渠道再添加商品!");
    		  return false;
    	  }
    	  
          var data = "";
          data += "name=" + $("#divAdd input[name=name]").val();
          data += "&remark=" + $("#divAdd input[name=remark]").val();
          data += "&state=" + $("#divAdd select[name=state]").val();
          data += "&channelId=" + channelId;
          
          $.ajax({
              type: "post",
              url: "/admin/commudityAdmin/insert",
              data: data,
              success: function (response) {

                  var code = response.code;
                  if (code == "200") {
                      alert("添加成功");
                      clearInputData("#divAdd");
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
          //phone: { required: true, minlength: 10 },
          storeId: { required: true }
      }, submitHandler: function (form) {
          var data = getEelementData("#divEdit");
          //sid:4
          //sname:Los Angeles 总仓
          //auth:100
          var data = "";
          data += "id=" + $("#divEdit input[name=id]").val();
          data += "&name=" + $("#divEdit input[name=name]").val();
          data += "&alias=" + $("#divEdit input[name=alias]").val();
          data += "&additivePrice=" + $("#divEdit input[name=additivePrice]").val();

          $("#divEdit [name=authority_id]:checked").each(function () {
              data += "&auth=" + $(this).attr("val");
          });
          $.ajax({
              type: "post",
              url: "/admin/channel/modify_admin",
              data: data,
              success: function (response) {
                  var code = response.code;
                  if (code == "200") {
                      alert("修改成功");
                      $(".cancel").click();
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
//渠道改变执行
function changechannel()
{
	$("#btnAdvanceSearch").click();
}

function update(id) {
	
    var row = jsonData.getData(id);

    var data = {};
    data.id = id;
    data.channelId = row.channelId;
    //userId: 1221
    //realAmount: 12
    //currency: 11
    //remark: 11
   
    data.name = $(yundangrid_selector).find("input[name='new_name_"+id+"']").val();
    data.remark = $(yundangrid_selector).find("input[name='new_remark_"+id+"']").val();
    data.state = $(yundangrid_selector).find("select[name='new_state_"+id+"']").val();
    
    if(removenull(data.name)=="")
    {
    	alert("商品名称不能为空");
    	return false;
    }

    $.ajax({
        type: "post",
        url: "/admin/commudityAdmin/modity",
        data: data,
        success: function (response) {
            var code = response.code;

            if ("200" == code) {
                alert("修改成功");
                refreshDataGrid(postData);
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


function del(id) {
    
    window.confirmDialog("确定删除选中的商品?如果删除，所有此商品的运单将无法找到其商品信息！", "删除提示", function () {
    	var row = jsonData.getData(id);
    	var channelId=row.channelId;
        $.ajax({
            type: "post",
            url: "/admin/commudityAdmin/delete",
            data: {"id":id,"channelId":channelId},
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