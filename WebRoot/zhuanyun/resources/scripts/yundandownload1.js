
var yundangrid_selector = "#yundangrid-table";
//查询数据时向后台提交的参数
var postData = {};
//后台返回的结果
var jsonData = [];

//初始列表数据
$(function ($) {

    var yundanpager_selector = "#yundangrid-pager";
    var init = false;
    $(window).resize(function () {
        $(yundangrid_selector).setGridWidth($(".col-xs-12").width() - 20, true);
    });
    loadseachinfo();//装载其它信息

    jQuery(yundangrid_selector).jqGrid({
       url: "../../admin/m_order/search",
       // url: "WebForm1.aspx",
        datatype: "json",
        height: "500",
         mtype : "post",
        postData: postData,
        colNames: ['运单号','发件人','收件人','状态', '类型', '支付方式', '身份证号', "正面图","反面图", "合成图"],
        colModel: [
            //{ name: 'state', index: 'state', width: 0, hidden: "true" },
            {
                name: 'orderId', index: 'orderId', width: 100,
                formatter: function (cellvalue, options, row) {
                    return '<a href="javascript:void(0)" onclick="showdetail(' + row.id + ')">' + cellvalue + '</a>';
                }
            },
            { name: 'suserId', index: 'suserId', width: 110,//发件人信息
            	
            	formatter: function (cellvalue, options, row) {
                  if(removenull(row.suser)!="")//发件人用户id不为空
                  {
                	  return removenull(row.suser.name)+"/"+removenull(row.suser.phone);
                  }
                  else
                 {
                	  return "";
                 }
            		
                }
            },
            { name: 'ruserId', index: 'ruserId', width: 110,//收件人信息
            	
            	formatter: function (cellvalue, options, row) {
            		//alert(row.ruser);
                  if(removenull(row.ruser)!="")//发件人用户id不为空
                  {
                	  return removenull(row.ruser.name)+"/"+removenull(row.ruser.phone);
                  }
                  else
                 {
                	  return "";
                 }
            		
                }
            },
        
            
            {
                name: 'state', index: 'state', width: 80,
                formatter: function (cellvalue, options, row) {
                	
                	return morderstatemap(cellvalue);
                }
            },
            {
                name: 'type', index: 'type', width: 80,
                formatter: function (cellvalue, options, row) {
                	return mordertypemap(cellvalue);
                }
            },
           
           
            {
                name: 'payWay', index: 'payWay', width: 80,
                formatter: function (cellvalue, options, row) {
                    if (cellvalue == "1") {
                        return "现金支付";
                    } else if (cellvalue == "0") {
                        return "余额支付";
                    }else if (cellvalue == "2") {
                        return "用户自付";
                    } else {
                        return "非法支付";
                    }
                }
            },
            {
                name: 'cardid', index: 'cardid', width: 80,
                formatter: function (cellvalue, options, row) {
	                if(removenull(row.ruser)!="")
	                {
	                	 return row.ruser.cardid;
	                }
	                else
	                {
	                	return "";
	                }
                }
            },
            {
                name: 'cardurl', index: 'cardurl', width: 80,
                formatter: function (cellvalue, options, row) {
	                if(removenull(row.ruser)!="")
	                {
	                	var str="";
	                	if(removenull(row.ruser.cardurl)=="")
	                	{
	                		return "";
	                	}
	                	else
	                	{
	                		str="<img src='"+row.ruser.cardurl+"' style='height:40px;'/>";
	                		
	                		str+='<button type="button" style="margin-top:8px;" id="upload" class="btn btn-primary btn-sm table-button" onclick="showbigpic(this)"><i class="ace-icon fa fa-home"></i><strong>大图</strong></button>';
	                		return str;
	                	}
	                	
	                	
	                }
	                else
	                {
	                	return "";
	                }
                }
            },
             
            {
                name: 'cardother', index: 'cardother', width: 80,
                formatter: function (cellvalue, options, row) {
	                if(removenull(row.ruser)!="")
	                {
	                	var str="";
	                	if(removenull(row.ruser.cardother)=="")
	                	{
	                		return "";
	                	}
	                	else
	                	{
	                		str="<img src='"+row.ruser.cardother+"' style='height:40px;'/>";
	                		str+='<button type="button" style="margin-top:8px;" id="upload" class="btn btn-primary btn-sm table-button" onclick="showbigpic(this)"><i class="ace-icon fa fa-home"></i><strong>大图</strong></button>';
	                		return str;
	                	}
	                	
	                	
	                }
	                else
	                {
	                	return "";
	                }
                }
            },
            {
                name: 'cardtogether', index: 'cardtogether', width: 80,
                formatter: function (cellvalue, options, row) {
	                if(removenull(row.ruser)!="")
	                {
	                	var str="";
	                	if(removenull(row.ruser.cardtogether)=="")
	                	{
	                		return "";
	                	}
	                	else
	                	{
	                		str="<img src='"+row.ruser.cardtogether+"' style='height:40px;'/>";
	                		str+='<button type="button" style="margin-top:8px;" id="upload" class="btn btn-primary btn-sm table-button" onclick="showbigpic(this)"><i class="ace-icon fa fa-home"></i><strong>大图</strong></button>';
	                		return str;
	                	}
	                	
	                	
	                }
	                else
	                {
	                	return "";
	                }
                }
            },
        ],
        jsonReader: {
            root: "data.datas",
            page: "data.pageNow",
            total: "data.pageCount",
            records: "data.rowCount"
        },
        viewrecords: true,
        rowNum: 25,
        rowList: [25,50,100,500, 1000, 2000,4000],
        pager: yundanpager_selector,
        altRows: true,
        multiselect: true,
        //multikey: "ctrlKey",
        multiboxonly: true,

        loadComplete: function (json) {
        	
        	
        	 
            
        	
        	
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
            //隐藏不可删除和编辑的复选框
            var hiddenIds = [];
            if(json.code=="200")
            {
	            if((removenull(json.data)!="")&&removenull(json.data.datas)!="")
	            {
		            jsonData = json.data.datas;
		            
		            
		            var rows = jsonData.length;
		              $("#orders_no").html(rows);
		              $("#orderschecked_no").html("0");
		              //防止出现水平滚动条
		            
		            /*for (var i = 0; i < jsonData.length; i++) {
		                var row = jsonData[i];
		                if (row.state >= 2) {
		                    hiddenIds.push(row.id);
		                }
		            }
		            hideJgridCheckbox(yundangrid_selector, hiddenIds);*/
	            }
            }
            else if("607" == json.code) {
				alert("您尚未登录或登录已失效！");
				top.location.href = "../admin/login.html";
			}
            else
            {
            	alert("读取出错，原因："+json.message);
            }
        },
        gridComplete: function () {
            //隐藏 city=已揽收的 复选框

        },
        onSelectRow: function (rowid, status) {
        	
        	 var ids = $(yundangrid_selector).jqGrid('getGridParam', 'selarrrow');
        	 $(":hidden[name='checked_ids']").val(ids);
             $("#orderschecked_no").html(ids.length);
             var allmoney=0;//总费用
             var allcost=0;//总成本 
             var allweight=0;//总计费重量
             var allsjweight=0;//总实际重量
             var allwrongNo=0;
             for(var i=0;i<ids.length;i++)
	         { 
            	 var id=ids[i];
            	 var row = jsonData.getData(id);
            	 var flag=true;
            	 if(isNaN(parseFloat(row.totalmoney))||parseFloat(row.totalmoney)<=0)
            	 {
            		 flag=false;
            	 }
            	 else
            	{
            		 allmoney+=parseFloat(row.totalmoney);
            	}

            	 if(flag==true)
            	 {
            		 if(isNaN(parseFloat(row.totalcost))||parseFloat(row.totalcost)<=0)
                	 {
                		 flag=false;
                	 }
                	 else
                	{
                		 allcost+=parseFloat(row.totalcost);
                	}
            	 }
            	 if(flag==true)
            	 {
            		 if(isNaN(parseFloat(row.weight))||parseFloat(row.weight)<=0)
                	 {
                		 flag=false;
                	 }
                	 else
                	{
                		 allweight+=parseFloat(row.weight);
                	}
            	 }

            	 if(flag==true)
            	 {
            		 if(isNaN(parseFloat(row.sjweight))||parseFloat(row.sjweight)<=0)
                	 {
                		 flag=false;
                	 }
                	 else
                	{
                		 allsjweight+=parseFloat(row.sjweight);
                	}
            	 }
            	 
            	 if(flag==false)
            	 {
            		 allwrongNo++;
            	 }
	         }
             allmoney=Math.round(allmoney*100)/100; 
             allcost=Math.round(allcost*100)/100;
             allweight=Math.round(allweight*100)/100;
             allsjweight=Math.round(allsjweight*100)/100;
            
             
             $("#orderschecked_allmoney").html(allmoney);
             $("#orderschecked_allcost").html(allcost);
             $("#orderschecked_allweight").html(allweight);
             $("#orderschecked_allsjweight").html(allsjweight);
             $("#orderschecked_allwrong").html(allwrongNo);
             
        },
        
       
        onSelectAll: function (aRowids, status) {
            var rows = aRowids.length;
            if (status == false) { rows = 0; }
            $("#orderschecked_no").html(rows);
            if (status != false)
            {
            	$(":hidden[name='checked_ids']").val(aRowids);
            }
            else
            {
            	$(":hidden[name='checked_ids']").val("");
            }
            
            
            
       	 var ids = aRowids;
    	 $(":hidden[name='checked_ids']").val(ids);
         $("#orderschecked_no").html(ids.length);
         var allmoney=0;//总费用
         var allcost=0;//总成本 
         var allweight=0;//总计费重量
         var allsjweight=0;//总实际重量
         var allwrongNo=0;
         for(var i=0;i<ids.length;i++)
         { 
        	 var id=ids[i];
        	 var row = jsonData.getData(id);
        	 var flag=true;
        	 if(isNaN(parseFloat(row.totalmoney))||parseFloat(row.totalmoney)<=0)
        	 {
        		 flag=false;
        	 }
        	 else
        	{
        		 allmoney+=parseFloat(row.totalmoney);
        	}

        	 if(flag==true)
        	 {
        		 if(isNaN(parseFloat(row.totalcost))||parseFloat(row.totalcost)<=0)
            	 {
            		 flag=false;
            	 }
            	 else
            	{
            		 allcost+=parseFloat(row.totalcost);
            	}
        	 }
        	 if(flag==true)
        	 {
        		 if(isNaN(parseFloat(row.weight))||parseFloat(row.weight)<=0)
            	 {
            		 flag=false;
            	 }
            	 else
            	{
            		 allweight+=parseFloat(row.weight);
            	}
        	 }

        	 if(flag==true)
        	 {
        		 if(isNaN(parseFloat(row.sjweight))||parseFloat(row.sjweight)<=0)
            	 {
            		 flag=false;
            	 }
            	 else
            	{
            		 allsjweight+=parseFloat(row.sjweight);
            	}
        	 }
        	 
        	 if(flag==false)
        	 {
        		 allwrongNo++;
        	 }
         }
         allmoney=Math.round(allmoney*100)/100; 
         allcost=Math.round(allcost*100)/100;
         allweight=Math.round(allweight*100)/100;
         allsjweight=Math.round(allsjweight*100)/100;
        
         
         $("#orderschecked_allmoney").html(allmoney);
         $("#orderschecked_allcost").html(allcost);
         $("#orderschecked_allweight").html(allweight);
         $("#orderschecked_allsjweight").html(allsjweight);
         $("#orderschecked_allwrong").html(allwrongNo);
        },
        
        
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
                //alert(ids);
                
                if((removenull(ids)=="")||(ids.length<1))
                {
                	alert("必须选择要删除的运单!");
                	return false;
                }
                
               // var aid = new Array(ids);
                //alert(aid);
                //var aaa=ids.split(",");
                //alert(aaa[0]);
                //alert(aaa[1]);
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
                
                
                
               
            }
        }
    );


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
    	var card_id=$("#divAdvanceSearch").find(":checkbox[name='card_id']").is(":checked");
    	var cardinfo="";
    	if(card_id==true)
    	{
    		cardinfo="0";
    	}
    	var card_url=$("#divAdvanceSearch").find(":checkbox[name='card_url']").is(":checked");
    	if(card_url==true)
    	{
    		if(cardinfo!="")
    		{
    			cardinfo=cardinfo+",1";
    		}
    		else
    		{
    			cardinfo="1";
    		}
    	}
    	
    	var card_other=$("#divAdvanceSearch").find(":checkbox[name='card_other']").is(":checked");
    	if(card_other==true)
    	{
    		if(cardinfo!="")
    		{
    			cardinfo=cardinfo+",2";
    		}
    		else
    		{
    			cardinfo="2";
    		}
    	}
    	var card_together=$("#divAdvanceSearch").find(":checkbox[name='card_together']").is(":checked");
    	if(card_together==true)
    	{
    		if(cardinfo!="")
    		{
    			cardinfo=cardinfo+",3";
    		}
    		else
    		{
    			cardinfo="3";
    		}
    	}
    	var card_upload=$("#divAdvanceSearch").find(":checkbox[name='card_upload']").is(":checked");
    	if(card_upload==true)
    	{
    		if(cardinfo!="")
    		{
    			cardinfo=cardinfo+",4";
    		}
    		else
    		{
    			cardinfo="4";
    		}
    	}
    	$("#divAdvanceSearch").find(":hidden[name='cardinfo']").val(cardinfo);
        var data = getEelementData("#divAdvanceSearch");
        refreshDataGrid(data);
    });

});

//点击状态搜索
function searchState(state) {
    refreshDataGrid({ state: state });
}

//提交js对象参数刷新列表数据
function refreshDataGrid(data) {
    $(yundangrid_selector).jqGrid("setGridParam", { postData: data }).trigger("reloadGrid");
}

//显示详细
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

function a4Print(id) {
    $("#iframePrint").attr("src", "printpage.html?id="+id+"&ran="+Math.random());
}

function reminPrint(id) {
    $("#iframePrint").attr("src", "printpageremin.html?id=" + id + "&ran=" + Math.random());
}

function hot4x6(id) {
    $("#iframePrint").attr("src", "printpage64.html?id="+id+"&ran="+Math.random());
}

function loadseachinfo()
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
					$("select[name='uploadwid']").html(str);
					$("select[name='wid']").html(str);
					
					showChannelTablesearchwid();
					showChannelTableupload();
					
					$("select[name='uploadwid']").change(showChannelTableupload);
					$("select[name='wid']").change(showChannelTablesearchwid);
						
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
function showChannelTablesearchwid() {
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

//上传文件的渠道选择
function showChannelTableupload() {
	var wid = $("select[name='uploadwid']").val();
	if(wid=="-1")
	{
		$("select[name='uploadcid']").html("<option value='-1'>请选择渠道</option>");
		return "";
	}
	
	//alert(wid);
	
	
	$.ajax({
		type : "post",
		url : "/admin/channel/get_by_store_available",
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
					$("select[name='uploadcid']").html(str);
					

				}

			} else if ("607" == code) {
				//alert("您尚未登录或登录已失效！");
				adminlogout();
			}
		}
	});
}

function downloadchecksumbitform()
{
	
	
	var ids=$(":hidden[name='checked_ids']").val();
	//alert(ids);
	if(removenull(ids)==""||removenull(ids=="[]"))
	{
		alert("请选择要操作的运单!");
		return false;
	}
	return true;
	
	
}

function download_direct_form()
{
	if (confirm("确认根据高级搜索条件直接下载运单?"))
	{
		return true;
	}
    return false;	
}

function download_ordersbyorderIds_form()
{
	if(removenull($(":file[name='inputfileorderId']")).val()=="")
	{
		alert("必须上传运单号excel文件!");
		return false;
	}
	
    return true;	
}