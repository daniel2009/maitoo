

var yundangrid_selector = "#yundangrid-table";
//查询数据时向后台提交的参数
var postData = {};
var jsonData = [];
//初始列表数据
$(function ($) {
	getallnumbers();//获取统计数据
    var yundanpager_selector = "#yundangrid-pager";
    var init = false;
    var state = "-1";
    $(window).resize(function () {
        $(yundangrid_selector).setGridWidth($(".page-content").width() - 20, true);
    });
    loadseachinfo1();
    var grid=  jQuery(yundangrid_selector).jqGrid({
        url: "/admin/account/detail_all_search",
        //url: "testdata/yunchongzhidata.html",
        postData:{state:""},
        datatype: "json",
        height: "auto",
        mtype: "get",
        //	用户编号	用户名	账户余额	预充值金额	充值名称	类型	状态	创建时间		操作
        colNames: ['编号','用户编号',  '操作员工','门店', '金额', "交易名称", '类型', "状态", "修改时间", "备注","确认状态","确认备注","操作"],
        //{"id":"646","userId":"1221","creditId":null,"amount":"12.00","currency":"美元","name":"后台管理员充值","type":"13","state":"1","empId":"104","empName":null,"empStore":null,"groupId":null,"groutId":null,"createDate":"2016-04-17 09:26:56","modifyDate":"2016-04-17 09:26:56","remark":"11","user":{"id":"1221","nickName":"13255556666","realName":"","phone":"13255556666","email":null,"qq":"","recommender":"-1","signDate":"2016-04-16 23:01:06","type":"","status":"0","country":"","address":"","empaccount":"admin","rmbBalance":"0.00","usdBalance":"23.00","usercode":"835796","useralias":"OISAQD","groupId":"1","phoneState":"0","emailState":"0","regType":"0","modifyDate":null,"createDate":null},"credit":null,"realAmount":null}
        colModel: [
            {
                name: 'id', index: 'id', width: 50
            },
            {
                name: 'userId', index: 'userId', width: 50
            },
            
           /* {
                name: 'phone', index: 'phone', width: 100, formatter: function (cellValue, option, row) {
                    if (row.user) {
                        return row.user.phone + (row.user.realName == "" ? "" : "/" + row.user.realName);
                    } else { return "" }
                }
            },*/
            {
                name: 'empId', index: 'empId', width: 80, formatter: function (cellValue, option, row) {
                	if((removenull(row.empId)!="")&&(removenull(row.empName)!=""))
                	{
                		return removenull(row.empId)+"/"+removenull(row.empName);
                	}
                	else
                	{
                		return removenull(row.empId)+removenull(row.empName);
                	}
                	
                  
                }
            },
            {
                name: 'empStore', empStore: 'empId', width: 80, 
            },
            
            
            
            {
                name: 'amount', index: 'amount', width: 60, formatter: function (cellValue, option, row) {
                    return row.amount + row.currency;
                }
            },
            {
                name: 'name', index: 'name', width: 80
            }
            ,
            {
                name: 'type', index: 'type', width: 80, formatter: function (cellValue, option, row) {
                    var type = "";
                    if (row.type == "2") {
                        type = "消费";
                    } else if (row.type == "3") {
                        type = "状态变更";
                    }else if (row.type == "4") {
                        type = "冻结资金";
                    }
                    else if (row.type == "11") {
                        type = "信用卡充值";
                    }  else if (row.type == "12") {
                        type = "人民币充值";
                    } else if (row.type == "13") {
                    
                        type = "管理员充值";
                    }
                    else
                    {
                    	type="";
                    }

                    return type;
                }
            }
            ,
            {
                name: 'state', index: 'state', width: 60, formatter: function (cellValue, option, row) {
                    if (row.state == "0") {
                        return "未充值";
                    } else if (row.state == "1") {
                        return "<span style='color:green;'>充值成功</span>";

                    } else if (row.state == "2") {
                        return "<span style='color:red'>充值失败</span>";
                    } else if (row.state == "3") {
                        return "<span >更改状态</span>";
                    }
                }
            },
             {
                 name: 'modifyDate', index: 'modifyDate', width: 80
             },

             {
                 name: 'remark', index: 'remark', width: 120,// formatter: function (cellValue, option, row) {
                   //  var chongzhi = "<button style='margin-left:5px;margin-top:1px;' class='btn btn-purple btn-sm' type='button'  onclick=\"chongzhi('" + row.id + "')\"  ><span class='icon-credit-card'></span>充值</button>";
                    // return "";
                // }
             },
             {
					name : 'state',
					index : 'state',
					width : 80,
					formatter : function(cellvalue, options,
							row) {
						// 若是动态生成，请从后台中返回到前端页面隐藏起来
						
						if(row.type=="13")
						{
							var select = '<select name="confirm_state_'+row.id+'"><option value="0">未确认</option><option value="1">已确认</option></select>';
							var mySelect = select;
							var option = $(select).find(
									"option[value='" + row.confirm_state
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
						else
						{
							return "";
						}
					}
				},
				  {
					name : 'admin_remark',
					index : 'admin_remark',
					width : 150,
					formatter : function(cellvalue, options,
							row) {
						// 若是动态生成，请从后台中返回到前端页面隐藏起来
						
						if(row.type=="13")
						{
							var inputstr = '<input name="admin_remark_'+row.id+'" value="'+removenull(row.admin_remark)+'"/>';
							
							return inputstr;
						}
						else
						{
							return "";
						}
					}
				},
				{
					name : 'op',
					index : 'op',
					width : 100,
					formatter : function(cellvalue, options,
							row) {
						// 返回修改按钮
						if(row.type=="13")
						{
						var edit = "";// 修改状态是任何人都可以修改自己运单的状态
						// if (row.state < 2) {
						edit = "<a style='width:60px;margin-left:2px;margin-top:2px' class='btn btn-purple btn-sm'  href='javascript:void(0)' onclick='updateConfirmState("
								+ row.id
								+ ")' ><span class='icon-edit'></span>修改</a>";
						
						return edit;
						}
						else
						{
							return "";
						}
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
        rowList: [10, 30, 50,100],
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

            //var hiddenIds = [];
            if (data.code == "200") {
                if ((removenull(data.data) != "") && removenull(data.data.datas) != "") {
                    jsonData = data.data.datas;
                   /* for (var i = 0; i < jsonData.length; i++) {
                        var row = jsonData[i];
                        if (row.type!="13") {
                            hiddenIds.push(row.id);
                        }
                    }
                    hideJgridCheckbox(yundangrid_selector, hiddenIds);*/

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
        	 var ids = $(yundangrid_selector).jqGrid('getGridParam', 'selarrrow');
           	 
                 $("#chooseNos").html(ids.length);
                 var qnumber=0;
                 var rm_money=0;
                 var usa_money=0;
                 for(var i=0;i<ids.length;i++)
  	           	{
  	            	var id=ids[i];
  	            	var row = jsonData.getData(id);
  	            	if(row.currency=="美元")
  	            	{
  	            		if((!isNaN(row.amount))&&parseFloat(row.amount)>0)
  	            		{
  	            			usa_money+=parseFloat(row.amount);
  	            		}
  	            		else
  	            		{
  	            			qnumber++;
  	            		}
  	            	}
  	            	else if(row.currency=="人民币")
  	            	{
  	            		if((!isNaN(row.amount))&&parseFloat(row.amount)>0)
  	            		{
  	            			rm_money+=parseFloat(row.amount);
  	            		}
  	            		else
  	            		{
  	            			qnumber++;
  	            		}
  	            	}
  	            	else
  	            	{
  	            		qnumber++;
  	            	}
  	           	}
             	 usa_money = Math.round(usa_money * 100) / 100;

            	 usa_money = usa_money.toString();
            	var pos_decimal = usa_money.indexOf('.');
            	if (pos_decimal < 0) {
            		pos_decimal = usa_money.length;
            		usa_money += '.';
            	}
            	while (usa_money.length <= pos_decimal + 2) {
            		usa_money += '0';
            	}
            	
            	usa_money = Math.round(usa_money * 100) / 100;

            	rm_money = rm_money.toString();
           	var pos_decimal = rm_money.indexOf('.');
           	if (pos_decimal < 0) {
           		pos_decimal = rm_money.length;
           		rm_money += '.';
           	}
           	while (rm_money.length <= pos_decimal + 2) {
           		rm_money += '0';
           	}
           	rm_money = Math.round(rm_money * 100) / 100;
           	
                 $("#choose_usa").html(usa_money);
                 $("#choose_rmb").html(rm_money);
                 $("#choose_q_no").html(qnumber);
        },
        onSelectAll: function (aRowids, status) {
       	 var ids = aRowids;
       	 
         $("#chooseNos").html(ids.length);
         var qnumber=0;
         var rm_money=0;
         var usa_money=0;
         for(var i=0;i<ids.length;i++)
         	{
          	var id=ids[i];
          	var row = jsonData.getData(id);
          	if(row.currency=="美元")
          	{
          		if((!isNaN(row.amount))&&parseFloat(row.amount)>0)
          		{
          			usa_money+=parseFloat(row.amount);
          		}
          		else
          		{
          			qnumber++;
          		}
          	}
          	else if(row.currency=="人民币")
          	{
          		if((!isNaN(row.amount))&&parseFloat(row.amount)>0)
          		{
          			rm_money+=parseFloat(row.amount);
          		}
          		else
          		{
          			qnumber++;
          		}
          	}
          	else
          	{
          		qnumber++;
          	}
         	}
     	 usa_money = Math.round(usa_money * 100) / 100;

    	 usa_money = usa_money.toString();
    	var pos_decimal = usa_money.indexOf('.');
    	if (pos_decimal < 0) {
    		pos_decimal = usa_money.length;
    		usa_money += '.';
    	}
    	while (usa_money.length <= pos_decimal + 2) {
    		usa_money += '0';
    	}
    	
    	usa_money = Math.round(usa_money * 100) / 100;

    	rm_money = rm_money.toString();
   	var pos_decimal = rm_money.indexOf('.');
   	if (pos_decimal < 0) {
   		pos_decimal = rm_money.length;
   		rm_money += '.';
   	}
   	while (rm_money.length <= pos_decimal + 2) {
   		rm_money += '0';
   	}
   	rm_money = Math.round(rm_money * 100) / 100;
   	
         $("#choose_usa").html(usa_money);
         $("#choose_rmb").html(rm_money);
         $("#choose_q_no").html(qnumber);
},
        caption: "交易记录",
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

    //点击高级搜索
    $("#btnAdvanceSearch").click(function () {
        var data = getEelementData("#divAdvanceSearch");
        refreshDataGrid(data);
    });

    $("#divAdvanceSearch #userId").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnAdvanceSearch").click();
        }
    }));
    $("#divAdvanceSearch #key").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnAdvanceSearch").click();
        }
    }));
   
});


//提交js对象参数刷新列表数据
function refreshDataGrid(data) {
    $(yundangrid_selector).jqGrid("setGridParam", { postData: data }).trigger("reloadGrid");
}



function chongzhi(id) {
    var data = {};
    data.userId = id;
    //userId: 1221
    //realAmount: 12
    //currency: 11
    //remark: 11

    data.realAmount = $(yundangrid_selector).find("[id=" + id + "]").find("input[name='realAmount']").val();
    data.currency = $(yundangrid_selector).find("[id=" + id + "]").find("select[name='currency']").val();
    data.remark = $(yundangrid_selector).find("[id=" + id + "]").find("input[name='remark']").val();

    if (data.realAmount <= 0) { alert("充值金额不能小于等于0"); return; }
    $.ajax({
        type: "post",
        url: "/admin/msrecharge",
        data: data,
        success: function (response) {
            var code = response.code;

            if ("200" == code) {
                alert("充值成功");
                refreshDataGrid(postData);
            } else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                top.location.href = "../admin/login.html";
            }
            else {
                alert("充值出错，原因：" + response.message);
            }

        }
    });

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
        $("#goodsItemBody").append("<tr id=itemTr" + i + ">" + teplament + "</tr>");
        setSpanText("#itemTr" + i, row.detail[i]);
    }

    $("#divDetail span").each(function () {
        if ($(this).attr("name")) {
            $(this).css({ "display": "inline-block", "min-width": "60px" });
        }
    });

    $("#typeName").html(mordertypemap(row.type));
    $("#stateName").html(morderstatemap(row.state));
    $("#paywayName").html(morderpaywaymap(row.payWay));
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
    if (removenull(row.user) != "") {
        setSpanText("#belonguserid", row.user);
    }
}



function loadseachinfo1() {

    $.ajax({
        type: "post",
        url: "/admin/warehouse/all",
        success: function (response) {
            var code = response.code;

            if ("200" == code) {

                var str = "";
                if (response.data != null) {
                	storelist_jason=response.data;
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
                    
                    $("select[id='storeIdselect']").html(str);
                 

                 
                   
                    

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

//更新确认状态
function updateConfirmState(id) {
	
	
	var confirm_state=$(yundangrid_selector).find("[id=" + id + "]").find("select[name='confirm_state_"+id+"']").val();//确认状态
	var admin_remark=$(yundangrid_selector).find("[id=" + id + "]").find("input[name='admin_remark_"+id+"']").val();//确认备注
	
	$.ajax({
		type : "post",
		url : "/admin/account/modify_detail_confirm_state",
		data : {
			"id" : id,
			"type" : "13",
			"confirm_state" : confirm_state,
			"admin_remark" : admin_remark
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

//打印财务清单
function printmoneylist()
{
	 var ids = $(yundangrid_selector).jqGrid('getGridParam', 'selarrrow');
     //alert(ids);
     
     if((removenull(ids)=="")||(ids.length<1))
     {
     	alert("必须选择要打印的清单!");
     	return false;
     }
     
     $("#iframePrintmoneylist").attr("src", "printmoneypagelist.html?id="+ids+"&ran="+Math.random());
     return false;
    
}

//获取统计信息

function getallnumbers()
{
	$.ajax({
		type : "post",
		url : "/admin/account/get_detail_no",
	
		success : function(response) {
			var code = response.code;
			
			if ("200" == code) {

				if(response.data!=null)
				{
					$("#all_spend_money").html(response.data.allcustomerMoney);
					$("#all_spend_money_no").html(response.data.allcustomerMoney_no);
					
					$("#all_changestate_money").html(response.data.allchangestateMoney);
					$("#all_changestate_money_no").html(response.data.allchangestateMoney_no);
					
					$("#all_credit_usa_money").html(response.data.allCredit_usa);
					$("#all_credit_usa_money_no").html(response.data.allCredit_usa_no);
					
					$("#all_credit_rm_money").html(response.data.allCredit_rm);
					$("#all_credit_rm_money_no").html(response.data.allCredit_rm_no);
					
					$("#no_alipay_money").html(response.data.alpayNopay);
					$("#no_alipay_money_no").html(response.data.alpayNopay_no);
					
				
					$("#success_alipay_money").html(response.data.alpaysuccesspay);
					$("#success_alipay_money_no").html(response.data.alpaysuccesspay_no);
					
					$("#fail_alipay_money").html(response.data.alpayfailedpay);
					$("#fail_alipay_money_no").html(response.data.alpayfailedpay_no);
					
					
					$("#no_admin_no_money").html(response.data.admin_nosure_no);
					$("#no_admin_rm_money").html(response.data.admin_nosure_rm);
					$("#no_admin_usa_money").html(response.data.admin_nosure_usa);
					
					$("#confirm_admin_no_money").html(response.data.admin_sure_no);
					$("#confirm_admin_rm_money").html(response.data.admin_sure_rm);
					$("#confirm_admin_usa_money").html(response.data.admin_sure_usa);
					
					
				}
				//alert("获取统计数据成功");
				
			}  else if("607" == code) {
				alert("您尚未登录或登录已失效！");
				top.location.href = "../admin/login.html";
			}
			else {
				alert("获取统计数据出错，原因："+response.message);
			}
			
		}
	});	
}