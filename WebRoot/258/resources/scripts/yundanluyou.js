$(function() {
	$("input[type=checkbox]").bootstrapSwitch();
});

var yundangrid_selector = "#yundangrid-table";
// 查询数据时向后台提交的参数
var postData = {};

// 后台返回的datas
var jsonData = {};

var yundanpager_selector = "#yundangrid-pager";
var init = false;

// 初始列
$(function($) {

	$(window).resize(
			function() {
				$(yundangrid_selector).setGridWidth(
						$(".col-xs-12").width() - 20, true);

			});
	loadseachinfo_route();//装载其它信息
	luyouGrid();


	
	
	//点击运单搜索
    $("#btnyundan").click(function () {
        postData = getEelementData("#divyundan");
        $(yundangrid_selector).jqGrid("setGridParam", { postData: postData }).trigger("reloadGrid");
    });
    //绑定运单输入框的enter事件进行搜索
    $("#yundan_oid").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnyundan").click();
        }
    }));
    $("#yundan_sod").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnyundan").click();
        }
    }));
    $("#yundan_god").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnyundan").click();
        }
    }));

    //点击运单搜索
    $("#btnAdvanceSearch").click(function () {
        var data = getEelementData("#divAdvanceSearch");
        refreshDataGrid(data);
    });

});

// 路由列表
function luyouGrid() {
	jQuery(yundangrid_selector)
			.jqGrid(
					{
						url : "/admin/m_order/search",
						datatype : "json",
						height : "auto",
						mtype : "post",
						postData: postData,
						// 运单号 所属用户 收件人姓名 运费($) 当前状态 航班号 第三方快递 创建时间 操作
						colNames : [ '运单号', '发件人', '收件人', "收件人地址", "状态",
								"航班号", "第三方快递", "创建时间", "操作", ],
						colModel : [

								{
									name : 'orderId',
									index : 'orderId',
									width : 110,
									formatter : function(cellvalue, options,
											row) {
										return '<a href="javascript:void(0)" onclick="showdetail('
												+ row.id
												+ ')">'
												+ cellvalue
												+ '</a>';
									}
								},
								{
									name : 'suserId',
									index : 'suserId',
									width : 120,// 发件人信息

									formatter : function(cellvalue, options,
											row) {
										if (removenull(row.suser) != "")// 发件人用户id不为空
										{
											return removenull(row.suser.name)
													+ "/"
													+ removenull(row.suser.phone);
										} else {
											return "";
										}

									}
								},
								{
									name : 'ruserId',
									index : 'ruserId',
									width : 120,// 收件人信息

									formatter : function(cellvalue, options,
											row) {
										// alert(row.ruser);
										if (removenull(row.ruser) != "")// 发件人用户id不为空
										{
											return removenull(row.ruser.name)
													+ "/"
													+ removenull(row.ruser.phone);
										} else {
											return "";
										}

									}
								},
								 { name: 'raddress', index: 'ruserId', width: 150,//收件人地址
					            	
					            	formatter: function (cellvalue, options, row) {
					                  if(removenull(row.ruser)!="")//发件人用户id不为空
					                  {
					                	  return removenull(row.ruser.state)+removenull(row.ruser.city)+removenull(row.ruser.dist)+removenull(row.ruser.address);
					                  }
					                  else
					                 {
					                	  return "";
					                 }
					            		
					                }
					            },
								{
									name : 'state',
									index : 'state',
									width : 100,
									formatter : function(cellvalue, options,
											row) {
										// 若是动态生成，请从后台中返回到前端页面隐藏起来
										var select = '<select name="newstate"><option value="0">运单作废</option><option value="1">包裹异常</option><option value="2">已揽收</option><option value="3">送往集散中心</option><option value="4">已打件</option><option value="5">送往机场</option><option value="6">空运中</option><option value="7">海关清关</option><option value="8">美淘中国分部已揽收</option><option value="9">收件人已接收</option></select>';
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
									}
								},
								{
									name : 'flyno',
									index : 'flyno',
									width : 100,
								},
								{
									name : 'thirdNo',
									index : 'thirdNo',
									width : 130,
									formatter : function(cellvalue, options,
											row) {
										var str="<span>"+"国内快递公司(英文)："+"</span>";
										str+="<input type='text' name='thirdPNS' value='"+removenull(row.thirdPNS)+"'/><br/>";
										str+="<span>"+"国内快递单号："+"</span>";
										str+="<input type='text' name='thirdNo' value='"+removenull(row.thirdNo)+"'/>";
										return str;
									}
								},
								{
									name : 'createDate',
									index : 'createDate',
									width : 100
								},
								{
									name : 'op',
									index : 'op',
									width : 160,
									formatter : function(cellvalue, options,
											row) {
										// 返回修改按钮
										var edit = "";// 修改状态是任何人都可以修改自己运单的状态
										// if (row.state < 2) {
										edit = "<a style='width:60px;margin-left:2px;margin-top:2px' class='btn btn-purple btn-sm'  href='javascript:void(0)' onclick='updateState("
												+ row.id
												+ ")' ><span class='icon-edit'></span>修改</a>";
										//edit +="&nbsp;";
										edit += "<a style='width:90px;margin-left:2px;margin-top:2px' class='btn btn-purple btn-sm'  href='javascript:void(0)' onclick='checkoneroute(\""
											+ row.orderId
											+ "\")' ><span class='ace-icon fa fa-search icon-on-right bigger-110'></span>查看路由</a>";
										// }
										return edit;
									}
								} ],
						jsonReader : {
							/*root : "data.datas",
							page : "data.pageNow",
							total : "data.pageSize",
							records : "data.rowCount"*/
								root: "data.datas",
					            page: "data.pageNow",
					            total: "data.pageCount",
					            records: "data.rowCount"
						},
						viewrecords : true,
						rowNum : 10,
						rowList : [ 10, 30, 50 ],
						pager : yundanpager_selector,
						altRows : true,

						multiselect : true,
						// multikey: "ctrlKey",
						multiboxonly : true,

						loadComplete : function(data) {
							//jsonData = data.data.datas;
							var table = this;
							setTimeout(function() {
								updatePagerIcons(table);
							}, 0);
							// 防止出现水平滚动条

							if (init == false) {
								$(" .ui-jqgrid-bdiv").css({
									'overflow-x' : 'hidden'
								});
								$(" .ui-jqgrid-htable th:last")
										.css(
												{
													"width" : $(
															" .ui-jqgrid-htable th:last")
															.width() - 2
												});
								$(" .jqgfirstrow td:last")
										.css(
												{
													"width" : $(
															" .jqgfirstrow td:last")
															.width() - 2
												});
								init = true;
							}

							// 隐藏不可删除和编辑的复选框
							var hiddenIds = [];
							for (var i = 0; i < jsonData.length; i++) {
								var row = jsonData[i];
								if (row.state >= 2) {
									hiddenIds.push(row.id);
								}
							}
							hideJgridCheckbox(yundangrid_selector, hiddenIds);
							
							 if(data.code=="200")
					            {
						            if((removenull(data.data)!="")&&removenull(data.data.datas)!="")
						            {
							            jsonData = data.data.datas;
							            for (var i = 0; i < jsonData.length; i++) {
							                var row = jsonData[i];
							                if (row.state >= 2) {
							                    hiddenIds.push(row.id);
							                }
							            }
							            hideJgridCheckbox(yundangrid_selector, hiddenIds);
						            }
					            }
					            else if("607" == data.code) {
									alert("您尚未登录或登录已失效！");
									top.location.href = "../admin/login.html";
								}
					            else
					            {
					            	alert("读取出错，原因："+data.message);
					            }
							
							
						},
						gridComplete : function() {

						},
						onSelectRow : function(rowid, status) {
						
							
						},
						onSelectAll : function(aRowids, status) {

						},
						autowidth : true
					});

	// navButtons
	jQuery(yundangrid_selector).jqGrid(
			'navGrid',
			yundanpager_selector,
			{ // navbar options
				edit : false,
				editicon : 'icon-pencil blue',
				add : false,
				addicon : 'icon-plus-sign purple',
				del : true,
				delicon : 'icon-trash red',
				search : false,
				searchicon : 'icon-search orange',
				refresh : true,
				refreshicon : 'icon-refresh green',
				view : false,
				viewicon : 'icon-zoom-in grey',

				// 删除
				delfunc : function() {
					
					var ids = $(yundangrid_selector).jqGrid('getGridParam',
							'selarrrow');
					
					var result = ids;
					for (var i = 0; i < ids.length; i++) {
						var display = $(yundangrid_selector).find(
								"[id=" + ids[i] + "]").find(
								"[role=checkbox]:hidden");
						if (display.length > 0) {
							// $(yundangrid_selector).setSelection(i+1, false);
							result = result.remove(ids[i]);
						}
					}

					//alert(result);
					if (result.length == 0) {
						window.tips("请选择需要删除的行", false);
						return;
					}

					window.confirmDialog("确定删除选择的数据？", "删除提示", function() {
						//window.tips("数据已删除", false);
						// todo 这里补充 和后台交互的代码
						// 删除完成后 在success回调中调用下面方法刷新数据列表
					      $.ajax({
			                	type : "post",
			            		url : "/admin/m_order/delete",
			            		data : {
			            			"ids" : ids
			            		},
			            		success : function(response) {
			            			var code = response.code;
			            			if ("200" == code) {
			            				alert("删除成功!");
			            				 //todo 这里补充 和后台交互的代码
			                            //删除完成后 在success回调中调用下面方法刷新数据列表
			                            refreshDataGrid(postData);
			            				}
			            			 	else if("607" == code) {
			            					alert("您尚未登录或登录已失效！");
			            					top.location.href = "../admin/login.html";
			            				}
			            				else
			            				{
			            					alert("删除失败，原因是："+response.message);
			            				}
			            			}});
						//refreshDataGrid(postData);
					});

				}
			});

	// replace icons with FontAwesome icons like above
	function updatePagerIcons(table) {
		var replacement = {
			'ui-icon-seek-first' : 'icon-double-angle-left bigger-140',
			'ui-icon-seek-prev' : 'icon-angle-left bigger-140',
			'ui-icon-seek-next' : 'icon-angle-right bigger-140',
			'ui-icon-seek-end' : 'icon-double-angle-right bigger-140'
		};
		$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon')
				.each(
						function() {
							var icon = $(this);
							var $class = $.trim(icon.attr('class').replace(
									'ui-icon', ''));

							if ($class in replacement)
								icon.attr('class', 'ui-icon '
										+ replacement[$class]);
						});
	}
}

// 提交js对象参数刷新列表数据
function refreshDataGrid(data) {
	$(yundangrid_selector).jqGrid("setGridParam", {
		postData : data
	}).trigger("reloadGrid");
}

function updateState(id) {
	var fieldName = "state";
	var state = $(yundangrid_selector).find("[id=" + id + "]").find(
			"[aria-describedby$=" + fieldName + "] select").val();
	var thirdpns= $(yundangrid_selector).find("[id=" + id + "]").find("input[name='thirdPNS']").val();
	var thirdno= $(yundangrid_selector).find("[id=" + id + "]").find("input[name='thirdNo']").val();

	
	
	$.ajax({
		type : "post",
		url : "/admin/m_order/modify_route_state",
		data : {
			"id" : id,
			"state" : state,
			"thirdPNS" : thirdpns,
			"thirdNo" : thirdno
		},
		success : function(response) {
			var code = response.code;
			
			if ("200" == code) {

				alert("修改成功");
				refreshDataGrid(postData);
			}  else if("607" == code) {
				alert("您尚未登录或登录已失效！");
				top.location.href = "../admin/login.html";
			}
			else {
				alert("修改出错，原因："+response.message);
			}
			
		}
	});
	
	
	//var data = ("id:" + id + "  state:" + state);
	//window.infoDialog("提交", "数据是" + data, true);
}

// 显示详细
function showdetail(id) {
    var row = jsonData.getData(id);
    setSpanText("#senderTableInfo", row.suser);
    setSpanText("#receiverInfoTable", row.ruser);
    setImgSrc("#receiverInfoTable", row.ruser);//添加身份证图片
    // setSpanText("#receiverInfoTable", row.ruser);
   
    $("#goodsItemBody").html("");
    for (var i = 0; i < row.detail.length; i++) {
        var teplament = $("#goodsItemTeplament").html();
        $("#goodsItemBody").append("<tr id=itemTr"+i+">" + teplament + "</tr>");
        setSpanText("#itemTr" + i, row.detail[i]);
    }

    $("#divDetail span").each(function () {
        if ($(this).attr("name")) {
            $(this).css({"display":"inline-block","min-width":"60px"});
        }
    });
    
    
    
   
    $("#typeName").html(mordertypemap(row.type));
    $("#stateName").html(morderstatemap(row.state));
    $("#paywayName").html(morderpaywaymap(row.payWay));
    
    if(row.payornot=="0")
    {
    	$("span[name='payornotName']").html("未付款");
    }
    else if(row.payornot=="1")
    {
    	$("span[name='payornotName']").html("已付款");
    }
    else
    {
    	$("span[name='payornotName']").html("状态异常");
    }
    
    dialog({
        title: "运单详细信息",
        content: $("#divDetail").html(),
        width: $(window).width() - 60,
        height: $("#divDetail").height() + 160,
        //cancelVal: '关闭',
        //cancel: true //为true等价于function(){}
    }).show();

    //运单信息，金额，总重量等从后台获取 然后调用下面方法把值设置到
    //yundanTableInfo 中，后台返回的name 和span的name一直即可
    setSpanText("#yundanTableInfo", row);
    setSpanText("#huizhonginfo", row);
    if(removenull(row.user)!="")
    {
    	setSpanText("#belonguserid", row.user);
    }
    
    
    
}



//查看运单路由
function checkoneroute(orderid) {
	$.ajax({
		type : "post",
		url : "/admin/m_route/getone",
		data : {
			"kuaidi_type" : "0",
			"oid" : orderid
		},
		success : function(response) {
			var code = response.code;
			
			if ("200" == code) {

				 $("#goodsRouteItemBody").html("");
				    for (var i = 0; i < response.data.length; i++) {
				        var teplament = $("#goodsItemTeplamentForRoute").html();
				        $("#goodsRouteItemBody").append("<tr id=routeitemTr"+i+">" + teplament + "</tr>");
				        setSpanText("#routeitemTr" + i, response.data[i]);
				        $("#returnurl").attr("style","display:none;");
				        if(response.data[i].state=="")//显示返回的连接内容
						{
						
							if((removenull(response.data[i].returnurl)==""))
							{}
							else
							{
								$("#returnurl").attr("style","width: 800px;height: 500px;margin-left: 50px;word-break:break-all; word-wrap:break-all;");
								$("#returnurl").attr("src",response.data[i].returnurl);
								
							}
								
						}
				    }
				    
				    
				    dialog({
				        title: "路由信息",
				        content: $("#divRouteDetail").html(),
				        width: $(window).width() - 60,
				        height: $("#divRouteDetail").height() + 60,
				       
				        //cancelVal: '关闭',
				        //cancel: true //为true等价于function(){}
				    }).show();
			}  else if("607" == code) {
				alert("您尚未登录或登录已失效！");
				top.location.href = "../admin/login.html";
			}
			else {
				alert("查询出错，原因："+response.message);
			}
			
		}
	});
	return false;
   
    
    
}


function loadseachinfo_route()
{
	
	$.ajax({
		type : "post",
		url : "/admin/warehouse/getself",
		success : function(response) {
			var code = response.code;
			
			if ("200" == code) {

				var str="";
				if (response.data != null) {
					if(response.data.length==1)
					{
						//alert(response.data.id);
						str="<option value='"+response.data[0].id+"'>"+response.data[0].name+"</option>";
						/*$.each(response.data,
								function() {
							str+="<option value='"+this.id+"'>"+this.name+"</option>";
						});*/
					}
					else
					{
						str="<option value='"+"-1"+"'>"+"请选择门店"+"</option>";
						$.each(response.data,
								function() {
							str+="<option value='"+this.id+"'>"+this.name+"</option>";
						});
					}
					
					$("select[name='wid']").html(str);
					
					showChannelTablesearchwid_route();
					
					
					
					$("select[name='wid']").change(showChannelTablesearchwid_route);
						
				} else {

				}
			}  else if("607" == code) {
				alert("您尚未登录或登录已失效！");
				top.location.href = "../admin/login.html";
			}
			else {
				alert("读取门店信息出错，原因："+response.message);
			}
			
		}
	});
}

//搜索的渠道选择
function showChannelTablesearchwid_route() {
	var wid = $("select[name='wid']").val();
	if(wid=="-1")
	{
		$("select[name='cid']").html("<option value='-1'>请选择渠道</option>");
		return "";
	}
	
	//alert(wid);
	
	
	$.ajax({
		type : "post",
		url : "/admin/channel/getall_unlimit",
		data : {
			"wid" : wid
		},
		success : function(response) {
			var code = response.code;
			if ("200" == code) {
				if (response.data == null) {
					//alert("门店中没有该运单的渠道，请联系系统管理员！");
				} else {
					var str = "<option value='-1'>请选择渠道</option>";
					$.each(response.data, function() {
						str += '<option value="' + this.id + '" additiveprice="'+this.additivePrice+'">' + this.name
								+ '</option>';
					});
					$("select[name='cid']").html(str);
					

				}

			} else if ("607" == code) {
				//alert("您尚未登录或登录已失效！");
				adminlogout();
			}
		}
	});
}
