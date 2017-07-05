$(function($) {
	loadstoreseachinfo();
	// getchannels();
});

var yundangrid_selector = "#yundangrid-table";

function getchannelsbystores() {
	var storeId = $("select[name='select_stores']").val();
	if (removenull(storeId) == "" || (removenull(storeId) == "-1")) {
		$("select[name='select_channels']").html(
				"<option value='-1'>请选择渠道</option>");
		$(yundangrid_selector).clearGridData();
		return false;
	}

	$("select[name='select_channels']").html("");
	$.ajax({
		type : "post",
		data : {
			"wid" : storeId
		},
		url : "/admin/channel/get_by_store",
		success : function(response) {
			var code = response.code;
			if ("200" == code) {
				var emp = response.data;
				if (emp != null) {
					var str = "<option value='-1'>请选择渠道</option>";
					$.each(emp, function() {
						str += "<option value='" + this.id + "'>" + this.name
								+ "</option>";
					});
					$("select[name='select_channels']").html(str);
					loadcommudityinfo();
					$("select[name='select_channels']").change(changechannel);// 渠道改变执行重新搜索

				}
			} else if ("607" == code) {
				alert("您尚未登录或登录已失效！");
				adminlogout();
			} else {
				alert("获取数据失败，原因是：" + response.message);
			}
		}
	});
}

// 查询数据时向后台提交的参数
var postData = {};
var jsonData = [];
// 初始列表数据
function loadcommudityinfo() {

	var yundanpager_selector = "#yundangrid-pager";
	var init = false;
	$(window).resize(
			function() {
				$(yundangrid_selector).setGridWidth(
						$(".page-content").width() - 20, true);
			});

	jQuery(yundangrid_selector)
			.jqGrid(
					{
						url : "/admin/commudityPrice/searchbyAdmin",
						datatype : "json",
						height : "auto",
						mtype : "get",
						// 普通会员、白银VIP会员、黄金VIP会员、白金VIP会员、钻石VIP会员
						colNames : [ '价格id','商品id', '所属渠道', '商品名称', '成本', '普通会员',
								'白银VIP会员', '黄金VIP会员', '白金VIP会员', '钻石VIP会员',
								'至尊VIP会员', '至尊VIP2会员', '至尊VIP3会员', '至尊VIP4会员','网上置单','上门收货',
								'门店启用', '商品启用', "修改时间", "操作" ],
						colModel : [
								{
									name : 'id',
									index : 'id',
									width : 30,
									fix : true,hidedlg:true,hidden:true
								},
								{
									name : 'commudityId',
									index : 'commudityId',
									width : 30,
									fix : true,
								},
								
								{
									name : 'channelName',
									index : 'channelName',
									width : 50,
									fix : true,
									formatter : function(cellValue, option, row) {
										var text = "";
										if (row.channel != null) {
											text = row.channel.name;
										}

										return text;
									}
								},

								{
									name : 'name',
									index : 'name',
									width : 50,
									fix : true,
									formatter : function(cellValue, option, row) {
										var text = "";
										if (row.commudityAdmin != null) {
											text = row.commudityAdmin.name;
										}

										return text;
									}
								},
								{
									name : 'cost',
									index : 'cost',
									width : 40,
									fix : true,
									formatter : function(cellValue, option, row) {
										var text = "";
										
										text=row.cost;
										return "<span style='color:blue;'>"+text+"</span>";
									}
								},
								// 普通会员
								{
									name : 'price',
									index : 'price',
									width : 50,
									fix : true,
									formatter : function(cellValue, option, row) {
										
                                        var cstate="";
										if(row.commudityAdmin!=null)
										{
											cstate=row.commudityAdmin.state;
										}
										if (cstate == "1")// 启动
										{
											
											
											
											
											if ((row.price != null)&&parseFloat(row.price)!=0) {
												return "<input type='number' name='new_price_"
														+ row.id
														+ "' value='"
														+ row.price
														+ "'/>";
											} else {
												return "<input type='number' name='new_price_"
														+ row.id
														+ "' value='"
														+ "" + "'/>";
											}
										} else {
											if ((row.price != null)&&parseFloat(row.price)!=0) {
												return "<input type='number' name='new_price_"
														+ row.id
														+ "' value='"
														+ row.price
														+ "'  readonly='readonly'/>";
											} else {
												return "<input type='number' name='new_price_"
														+ row.id
														+ "' value='"
														+ ""
														+ "' readonly='readonly'/>";
											}
										}
									}
								},
								// 白银VIP会员
								{
									name : 'vip_0_Price',
									index : 'vip_0_Price',
									width : 50,
									fix : true,
									formatter : function(cellValue, option, row) {
										var cstate="";
										if(row.commudityAdmin!=null)
										{
											cstate=row.commudityAdmin.state;
										}
										if (cstate == "1")// 启动
										{
											if ((row.vip_0_Price != null)&&parseFloat(row.vip_0_Price)!=0) {
												return "<input type='number' name='new_vip_0_Price_"
														+ row.id
														+ "' value='"
														+ row.vip_0_Price
														+ "'/>";
											} else {
												return "<input type='number' name='new_vip_0_Price_"
														+ row.id
														+ "' value='"
														+ "" + "'/>";
											}
										} else {
											if ((row.vip_0_Price != null)&&parseFloat(row.vip_0_Price)!=0) {
												return "<input type='number' name='new_vip_0_Price_"
														+ row.id
														+ "' value='"
														+ row.vip_0_Price
														+ "' readonly='readonly'/>";
											} else {
												return "<input type='number' name='new_vip_0_Price_"
														+ row.id
														+ "' value='"
														+ ""
														+ "' readonly='readonly'/>";
											}
										}
									}
								},
								// 黄金VIP会员
								{
									name : 'vip_1_Price',
									index : 'vip_1_Price',
									width : 50,
									fix : true,
									formatter : function(cellValue, option, row) {
										var cstate="";
										if(row.commudityAdmin!=null)
										{
											cstate=row.commudityAdmin.state;
										}
										if (cstate == "1")// 启动
										{
											if ((row.vip_1_Price != null)&&parseFloat(row.vip_1_Price)!=0) {
												return "<input type='number' name='new_vip_1_Price_"
														+ row.id
														+ "' value='"
														+ row.vip_1_Price
														+ "'/>";
											} else {
												return "<input type='number' name='new_vip_1_Price_"
														+ row.id
														+ "' value='"
														+ "" + "'/>";
											}
										} else {
											if ((row.vip_1_Price != null)&&parseFloat(row.vip_1_Price)!=0) {
												return "<input type='number' name='new_vip_1_Price_"
														+ row.id
														+ "' value='"
														+ row.vip_1_Price
														+ "' readonly='readonly'/>";
											} else {
												return "<input type='number' name='new_vip_1_Price_"
														+ row.id
														+ "' value='"
														+ ""
														+ "' readonly='readonly'/>";
											}
										}
									}
								},
								// 白金VIP会员
								{
									name : 'vip_2_Price',
									index : 'vip_2_Price',
									width : 50,
									fix : true,
									formatter : function(cellValue, option, row) {
										var cstate="";
										if(row.commudityAdmin!=null)
										{
											cstate=row.commudityAdmin.state;
										}
										if (cstate == "1")// 启动
										{
											if ((row.vip_2_Price != null)&&parseFloat(row.vip_2_Price)!=0) {
												return "<input type='number' name='new_vip_2_Price_"
														+ row.id
														+ "' value='"
														+ row.vip_2_Price
														+ "'/>";
											} else {
												return "<input type='number' name='new_vip_2_Price_"
														+ row.id
														+ "' value='"
														+ "" + "'/>";
											}
										} else {
											if ((row.vip_2_Price != null)&&parseFloat(row.vip_2_Price)!=0) {
												return "<input type='number' name='new_vip_2_Price_"
														+ row.id
														+ "' value='"
														+ row.vip_2_Price
														+ "' readonly='readonly'/>";
											} else {
												return "<input type='number' name='new_vip_2_Price_"
														+ row.id
														+ "' value='"
														+ "" + "' readonly='readonly'/>";
											}
										}
									}
								},
								// 钻石VIP会员
								{
									name : 'vip_3_Price',
									index : 'vip_3_Price',
									width : 50,
									fix : true,
									formatter : function(cellValue, option, row) {
										var cstate="";
										if(row.commudityAdmin!=null)
										{
											cstate=row.commudityAdmin.state;
										}
										if (cstate == "1")// 启动
										{
											if ((row.vip_3_Price != null)&&parseFloat(row.vip_3_Price)!=0) {
												return "<input type='number' name='new_vip_3_Price_"
														+ row.id
														+ "' value='"
														+ row.vip_3_Price
														+ "'/>";
											} else {
												return "<input type='number' name='new_vip_3_Price_"
														+ row.id
														+ "' value='"
														+ ""
														+ "'/>";
											}
										}else
										{
											if ((row.vip_3_Price != null)&&parseFloat(row.vip_3_Price)!=0) {
												return "<input type='number' name='new_vip_3_Price_"
														+ row.id
														+ "' value='"
														+ row.vip_3_Price
														+ "' readonly='readonly'/>";
											} else {
												return "<input type='number' name='new_vip_3_Price_"
														+ row.id
														+ "' value='"
														+ ""
														+ "' readonly='readonly'/>";
											}
										}
									}
								},
								// 至尊VIP会员
								{
									name : 'vip_4_Price',
									index : 'vip_4_Price',
									width : 50,
									fix : true,
									formatter : function(cellValue, option, row) {
										var cstate="";
										if(row.commudityAdmin!=null)
										{
											cstate=row.commudityAdmin.state;
										}
										if (cstate == "1")// 启动
										{
											if ((row.vip_4_Price != null)&&parseFloat(row.vip_4_Price)!=0) {
												return "<input type='number' name='new_vip_4_Price_"
														+ row.id
														+ "' value='"
														+ row.vip_4_Price
														+ "'/>";
											} else {
												return "<input type='number' name='new_vip_4_Price_"
														+ row.id
														+ "' value='"
														+ ""
														+ "'/>";
											}
										}
										else
										{
											if ((row.vip_4_Price != null)&&parseFloat(row.vip_4_Price)!=0) {
												return "<input type='number' name='new_vip_4_Price_"
														+ row.id
														+ "' value='"
														+ row.vip_4_Price
														+ "' readonly='readonly'/>";
											} else {
												return "<input type='number' name='new_vip_4_Price_"
														+ row.id
														+ "' value='"
														+ ""
														+ "' readonly/>";
											}
										}
									}
								},
								// // 至尊VIP2会员
								{
									name : 'vip_5_Price',
									index : 'vip_5_Price',
									width : 50,
									fix : true,
									formatter : function(cellValue, option, row) {
										var cstate="";
										if(row.commudityAdmin!=null)
										{
											cstate=row.commudityAdmin.state;
										}
										if (cstate == "1")// 启动
										{
											if ((row.vip_5_Price != null)&&parseFloat(row.vip_5_Price)!=0) {
												return "<input type='number' name='new_vip_5_Price_"
														+ row.id
														+ "' value='"
														+ row.vip_5_Price
														+ "'/>";
											} else {
												return "<input type='number' name='new_vip_5_Price_"
														+ row.id
														+ "' value='"
														+ ""
														+ "'/>";
											}
										}
										else
										{
											if ((row.vip_5_Price != null)&&parseFloat(row.vip_5_Price)!=0) {
												return "<input type='number' name='new_vip_5_Price_"
														+ row.id
														+ "' value='"
														+ row.vip_5_Price
														+ "' readonly='readonly'/>";
											} else {
												return "<input type='number' name='new_vip_5_Price_"
														+ row.id
														+ "' value='"
														+ ""
														+ "' readonly='readonly'/>";
											}
										}
									}
								},
								// // 至尊VIP3会员
								{
									name : 'vip_6_Price',
									index : 'vip_6_Price',
									width : 50,
									fix : true,
									formatter : function(cellValue, option, row) {
										var cstate="";
										if(row.commudityAdmin!=null)
										{
											cstate=row.commudityAdmin.state;
										}
										if (cstate == "1")// 启动
										{
											if ((row.vip_6_Price != null)&&parseFloat(row.vip_6_Price)!=0) {
												return "<input type='number' name='new_vip_6_Price_"
														+ row.id
														+ "' value='"
														+ row.vip_6_Price
														+ "'/>";
											} else {
												return "<input type='number' name='new_vip_6_Price_"
														+ row.id
														+ "' value='"
														+ ""
														+ "'/>";
											}
										}
										else
										{

											if ((row.vip_6_Price != null)&&parseFloat(row.vip_6_Price)!=0) {
												return "<input type='number' name='new_vip_6_Price_"
														+ row.id
														+ "' value='"
														+ row.vip_6_Price
														+ "' readonly='readonly'/>";
											} else {
												return "<input type='number' name='new_vip_6_Price_"
														+ row.id
														+ "' value='"
														+ ""
														+ "' readonly='readonly'/>";
											}
										
										}
									}
								},
								// // 至尊VIP4会员
								{
									name : 'vip_7_Price',
									index : 'vip_7_Price',
									width : 50,
									fix : true,
									formatter : function(cellValue, option, row) {
										var cstate="";
										if(row.commudityAdmin!=null)
										{
											cstate=row.commudityAdmin.state;
										}
										if (cstate == "1")// 启动
										{
											if ((row.vip_7_Price != null)&&parseFloat(row.vip_7_Price)!=0) {
												return "<input type='number' name='new_vip_7_Price_"
														+ row.id
														+ "' value='"
														+ row.vip_7_Price
														+ "'/>";
											} else {
												return "<input type='number' name='new_vip_7_Price_"
														+ row.id
														+ "' value='"
														+ ""
														+ "'/>";
											}
										}
										else
										{
											if ((row.vip_7_Price != null)&&parseFloat(row.vip_7_Price)!=0) {
												return "<input type='number' name='new_vip_7_Price_"
														+ row.id
														+ "' value='"
														+ row.vip_7_Price
														+ "' readonly='readonly'/>";
											} else {
												return "<input type='number' name='new_vip_7_Price_"
														+ row.id
														+ "' value='"
														+ ""
														+ "' readonly='readonly'/>";
											}
										}
									}
								},

								
								// // 网上置单的价格
								{
									name : 'm_price',
									index : 'm_price',
									width : 50,
									fix : true,
									formatter : function(cellValue, option, row) {
										var cstate="";
										if(row.commudityAdmin!=null)
										{
											cstate=row.commudityAdmin.state;
										}
										if (cstate == "1")// 启动
										{
											if ((row.m_price != null)&&parseFloat(row.m_price)!=0) {
												return "<input type='number' name='new_m_price_"
														+ row.id
														+ "' value='"
														+ row.m_price
														+ "'/>";
											} else {
												return "<input type='number' name='new_m_price_"
														+ row.id
														+ "' value='"
														+ ""
														+ "'/>";
											}
										}
										else
										{
											if ((row.m_price != null)&&parseFloat(row.m_price)!=0) {
												return "<input type='number' name='new_m_price_"
														+ row.id
														+ "' value='"
														+ row.m_price
														+ "' readonly='readonly'/>";
											} else {
												return "<input type='number' name='new_m_price_"
														+ row.id
														+ "' value='"
														+ ""
														+ "' readonly='readonly'/>";
											}
										}
									}
								},
								// // 上门收货
								{
									name : 's_price',
									index : 's_price',
									width : 50,
									fix : true,
									formatter : function(cellValue, option, row) {
										var cstate="";
										if(row.commudityAdmin!=null)
										{
											cstate=row.commudityAdmin.state;
										}
										if (cstate == "1")// 启动
										{
											if ((row.s_price != null)&&parseFloat(row.s_price)!=0) {
												return "<input type='number' name='new_s_price_"
														+ row.id
														+ "' value='"
														+ row.s_price
														+ "'/>";
											} else {
												return "<input type='number' name='new_s_price_"
														+ row.id
														+ "' value='"
														+ ""
														+ "'/>";
											}
										}
										else
										{
											if ((row.s_price != null)&&parseFloat(row.s_price)!=0) {
												return "<input type='number' name='new_s_price_"
														+ row.id
														+ "' value='"
														+ row.s_price
														+ "' readonly='readonly'/>";
											} else {
												return "<input type='number' name='new_s_price_"
														+ row.id
														+ "' value='"
														+ ""
														+ "' readonly='readonly'/>";
											}
										}
									}
								},
								
								{
									name : 'storestate',
									index : 'storestate',
									width : 30,
									fix : true,
									formatter : function(cellValue, option, row) {
										var text = "";
										
										if (row.state != null) {
											
											
											
											
											
											
											if (row.state == "1")// 启用
											{
												text = "<select name='new_price_state_"
														+ row.id
														+ "'><option value='1' selected = 'selected'>是</option><option value='0'>否</option></select>";
											} else {
												text = "<select name='new_price_state_"
														+ row.id
														+ "'><option value='0'>否</option><option value='1'>是</option></select>";
											}
										} else {
											text = "<select name='new_price_state_"
													+ row.id
													+ "'><option value='0'>否</option><option value='1'>是</option></select>";
										}
										return text;
									}
								},

								{
									name : 'state',
									index : 'state',
									width : 30,
									fix : true,
									formatter : function(cellValue, option, row) {
										var text = "";
										
										if(row.commudityAdmin!=null)
										{
											if (row.commudityAdmin.state == "1")// 启用
											{
												text = "是";
											} else {
												text = "<span style='color:red'>否</span>";
											}
										}
										else
										{
											text = "<span style='color:red'>否</span>";
										}

										return text;
									}
								},
								{
									name : 'modifyDate',
									index : 'modifyDate',
									width : 60,
									fix : true
								},
								{
									name : 'op',
									index : 'op',
									width : 100,
									fix : true,
									formatter : function(cellValue, option, row) {
										var edit = "<button  style='margin-left:5px;margin-top:1px;' class='btn btn-purple btn-sm' type='button'  onclick=\"update('"
												+ row.id
												+ "')\"  ><span class='icon-edit'></span>修改</button>";
										//var del = "<button style='margin-left:5px;margin-top:1px;' class='btn btn-danger  btn-sm' type='button'  onclick=\"del('"
										//		+ row.id
										//		+ "')\"  ><span class='fa fa-times'></span>删除</button>";
										return edit ;//+ del;
									}
								} ],

						jsonReader : {
							root : "data.datas",
							page : "data.pageNow",
							total : "data.pageCount",
							records : "data.rowCount"
						},
						viewrecords : true,
						rowNum : 10,
						rowList : [ 10, 30, 50 ],
						pager : yundanpager_selector,
						altRows : true,

						multiselect : false,
						// multikey: "ctrlKey",
						multiboxonly : true,

						loadComplete : function(data) {
							var table = this;
							setTimeout(function() {
								updatePagerIcons(table);
							}, 0);

							if (data.code == "200") {
								if ((removenull(data.data) != "")
										&& removenull(data.data.datas) != "") {
									jsonData = data.data.datas;

								}
							} else if ("607" == data.code) {
								alert("您尚未登录或登录已失效！");
								top.location.href = "../admin/login.html";
							} else {
								alert("读取出错，原因：" + data.message);
							}

							// 防止出现水平滚动条
							if (init == false) {
								$(".ui-jqgrid-bdiv").css({
									'overflow-x' : 'hidden'
								});
								$(".ui-jqgrid-htable th:last")
										.css(
												{
													"width" : $(
															".ui-jqgrid-htable th:last")
															.width() - 2
												});
								$(".jqgfirstrow td:last").css(
										{
											"width" : $(".jqgfirstrow td:last")
													.width() - 2
										});
								init = true;
							}
						},
						gridComplete : function() {

						},
						onSelectRow : function(rowid, status) {

						},
						onSelectAll : function(aRowids, status) {

						},
						caption : "渠道列表",
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
				del : false,
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
					var idData = "";
					for (var i = 0; i < ids.length; i++) {
						idData = idData + "id=" + ids[i] + "&";
					}
					del(idData);
				}
			})

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

	$("#btnAdd").click(
			function() {

				var channelId = $("select[name='select_channels']").val();
				if (removenull(channelId) == "" || (channelId == "-1")) {
					alert("请先选择渠道再添加商品!");
					return false;
				}
				$("#add_select_channel_name").html(
						$("select[name='select_channels']").find(
								"option:selected").text());
				// getWarehouselist("");
				clearInputData("#divAdd");
				$("#divAdd select[name='state']").val("0");
				// $("#divGrid").hide();
				// $("#divAdd").show();
				// getWarehouse(0);
				editDialog = dialog({
					zIndex : 99999,
					fixed : false,
					padding : "5px",
					title : "修改运单",
					// url: "yundanedit.html?id=" + id,
					content : document.getElementById("divAdd"),
					width : $(window).width() - 5,
					height : "auto",// 默认为auto

				});
				editDialog.show();
			});

	$(".cancel").click(function() {
		$("#divGrid").show();
		$("#divAdd").hide();
		$("#divEdit").hide();
		$("#divDetail").hide();
	});

	// 点击高级搜索
	$("#btnAdvanceSearch").click(function() {
		var data = getEelementData("#divAdvanceSearch");
		refreshDataGrid(data);
	});

	$("#divAdvanceSearch #keyword").bind("keypress", (function(e) {
		if (e.keyCode == 13) {
			$("#btnAdvanceSearch").click();
		}
	}));

	$("#divAdvanceSearch #empId").bind("keypress", (function(e) {
		if (e.keyCode == 13) {
			$("#btnAdvanceSearch").click();
		}
	}));

	// 添加航班表单验证并提交,选择器的内容一定要是form表单，里面一定要有一个submit的按钮
	$("#divAdd form").validate({
		// 验证规则
		// http://www.runoob.com/jquery/jquery-plugin-validate.html
		rules : {
			// name: { required: true, minlength: 6, maxlength: 20 },
			name : {
				required : true,
				minlength : 2,
				maxlength : 40
			},
			state : {
				required : true
			},

		},
		submitHandler : function(form) {

			var channelId = $("select[name='select_channels']").val();
			if (removenull(channelId) == "" || (channelId == "-1")) {
				alert("请先选择渠道再添加商品!");
				return false;
			}

			var data = "";
			data += "name=" + $("#divAdd input[name=name]").val();
			data += "&remark=" + $("#divAdd input[name=remark]").val();
			data += "&state=" + $("#divAdd select[name=state]").val();
			data += "&channelId=" + channelId;

			$.ajax({
				type : "post",
				url : "/admin/commudityAdmin/insert",
				data : data,
				success : function(response) {

					var code = response.code;
					if (code == "200") {
						alert("添加成功");
						clearInputData("#divAdd");
						// 刷新列表数据
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

	$("#divEdit form").validate(
			{
				// 验证规则
				// http://www.runoob.com/jquery/jquery-plugin-validate.html
				rules : {
					// phone: { required: true, minlength: 10 },
					storeId : {
						required : true
					}
				},
				submitHandler : function(form) {
					var data = getEelementData("#divEdit");
					// sid:4
					// sname:Los Angeles 总仓
					// auth:100
					var data = "";
					data += "id=" + $("#divEdit input[name=id]").val();
					data += "&name=" + $("#divEdit input[name=name]").val();
					data += "&alias=" + $("#divEdit input[name=alias]").val();
					data += "&additivePrice="
							+ $("#divEdit input[name=additivePrice]").val();

					$("#divEdit [name=authority_id]:checked").each(function() {
						data += "&auth=" + $(this).attr("val");
					});
					$.ajax({
						type : "post",
						url : "/admin/channel/modify_admin",
						data : data,
						success : function(response) {
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

}

// 提交js对象参数刷新列表数据
function refreshDataGrid(data) {
	$(yundangrid_selector).jqGrid("setGridParam", {
		postData : data
	}).trigger("reloadGrid");
}
// 渠道改变执行
function changechannel() {
	var storeId = $("select[name='select_stores']").val();
	var cid = $("select[name='select_channels']").val();
	if ((storeId == "-1") || (cid == "-1")) {
		$(yundangrid_selector).clearGridData();
		return false;
	}
	$("#btnAdvanceSearch").click();
}

function update(id) {

	var row = jsonData.getData(id);

	var data = {};
	data.id = id;
	data.storeId=row.storeId;;
	// userId: 1221
	// realAmount: 12
	// currency: 11
	// remark: 11

	data.price = $(yundangrid_selector)
			.find("input[name='new_price_" + id + "']").val();//普通会员价格
	data.vip_0_Price = $(yundangrid_selector).find(
			"input[name='new_vip_0_Price_" + id + "']").val();
	data.vip_1_Price = $(yundangrid_selector).find(
			"input[name='new_vip_1_Price_" + id + "']").val();
	data.vip_2_Price = $(yundangrid_selector).find(
			"input[name='new_vip_2_Price_" + id + "']").val();
	data.vip_3_Price = $(yundangrid_selector).find(
			"input[name='new_vip_3_Price_" + id + "']").val();
	data.vip_4_Price = $(yundangrid_selector).find(
			"input[name='new_vip_4_Price_" + id + "']").val();
	data.vip_5_Price = $(yundangrid_selector).find(
			"input[name='new_vip_5_Price_" + id + "']").val();
	data.vip_6_Price = $(yundangrid_selector).find(
			"input[name='new_vip_6_Price_" + id + "']").val();
	data.vip_7_Price = $(yundangrid_selector).find(
			"input[name='new_vip_7_Price_" + id + "']").val();
	data.state = $(yundangrid_selector).find(
			"select[name='new_price_state_" + id + "']").val();
	
    data.m_price=$(yundangrid_selector).find(
			"input[name='new_m_price_" + id + "']").val();
    
    data.s_price=$(yundangrid_selector).find(
			"input[name='new_s_price_" + id + "']").val();
	
    

	if (isNaN(parseFloat(data.price))||parseFloat(data.price)<=0) {
		alert("普通会员只能大于0的数值");
		return false;
	}
	if (isNaN(parseFloat(data.vip_0_Price))||parseFloat(data.vip_0_Price)<=0) {
		alert("白银会员只能大于0的数值");
		return false;
	}
	if (isNaN(parseFloat(data.vip_1_Price))||parseFloat(data.vip_1_Price)<=0) {
		alert("黄金会员只能大于0的数值");
		return false;
	}
	if (isNaN(parseFloat(data.vip_2_Price))||parseFloat(data.vip_2_Price)<=0) {
		alert("白金会员只能大于0的数值");
		return false;
	}
	if (isNaN(parseFloat(data.vip_3_Price))||parseFloat(data.vip_2_Price)<=0) {
		alert("钻石会员只能大于0的数值");
		return false;
	}
	if (isNaN(parseFloat(data.vip_4_Price))||parseFloat(data.vip_4_Price)<=0) {
		alert("至尊会员只能大于0的数值");
		return false;
	}
	if (isNaN(parseFloat(data.vip_5_Price))||parseFloat(data.vip_5_Price)<=0) {
		alert("至尊2会员只能大于0的数值");
		return false;
	}
	if (isNaN(parseFloat(data.vip_6_Price))||parseFloat(data.vip_6_Price)<=0) {
		alert("至尊3会员只能大于0的数值");
		return false;
	}
	if (isNaN(parseFloat(data.vip_7_Price))||parseFloat(data.vip_7_Price)<=0) {
		alert("至尊4会员只能大于0的数值");
		return false;
	}
	
	if (isNaN(parseFloat(data.vip_7_Price))||parseFloat(data.vip_7_Price)<=0) {
		alert("至尊4会员只能大于0的数值");
		return false;
	}
	if (isNaN(parseFloat(data.m_price))||parseFloat(data.m_price)<=0) {
		alert("网上置单只能大于0的数值");
		return false;
	}
	if (isNaN(parseFloat(data.s_price))||parseFloat(data.s_price)<=0) {
		alert("上门收货只能大于0的数值");
		return false;
	}
	
	
	$.ajax({
		type : "post",
		url : "/admin/commudityAdmin/modity_store_price_admin",
		data : data,
		success : function(response) {
			var code = response.code;

			if ("200" == code) {
				alert("修改成功");
				refreshDataGrid(postData);
			} else if ("607" == code) {
				alert("您尚未登录或登录已失效！");
				top.location.href = "../admin/login.html";
			} else {
				alert("修改出错，原因：" + response.message);
			}

		}
	});

}

// 装载搜索栏信息
var storelist_jason = {};
function loadstoreseachinfo() {

	$.ajax({
		type : "post",
		url : "/admin/warehouse/getself_store",
		success : function(response) {
			var code = response.code;

			if ("200" == code) {

				var str = "";
				if (response.data != null) {
					storelist_jason = response.data;
					if (response.data.length == 1) {
						// alert(response.data.id);
						str = "<option value='" + response.data[0].id + "'>"
								+ response.data[0].name + "</option>";
						/*
						 * $.each(response.data, function() { str+="<option
						 * value='"+this.id+"'>"+this.name+"</option>"; });
						 */
					} else {
						str = "<option value='" + "-1" + "'>" + "请选择门店"
								+ "</option>";
						$.each(response.data, function() {
							str += "<option value='" + this.id + "'>"
									+ this.name + "</option>";
						});
					}
					$("select[name='select_stores']").html(str);
					getchannelsbystores();
					$("select[name='select_stores']").change(
							getchannelsbystores);

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