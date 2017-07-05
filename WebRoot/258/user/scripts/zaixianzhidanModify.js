
var order_postData= {};
var receiverGrid, jsonData;
var senderGrid, senderData;

$(function () {
    initEvent();

    $("[name=radioupload]").click(function () {
        if ($(this).val() == "hc") {
            $("#divZM").hide();
            $("#divFM").hide();
            $("#divHC").show();
        } else {
            $("#divZM").show();
            $("#divFM").show();
            $("#divHC").hide();
        }
    });
    getMorderbyid(request("id"));

    initReceiverGrid();
    initSenderGrid();
  
    //获取全局变量
    getglobalargs_commrate();
    
	$("input[name='insurance']").change(countpremiummoney_user_1);//计算保险
	$("input[name='cquantity']").change(commuditydataChangeUser);//计算总重量和数量
	$("input[name='cvalue']").change(commuditydataChangeUser);//计算总重量和数量
	
	$("input[name='cardId']").change(verfycardid);
});

function verfycardid()
{
	var cardid=$("input[name='cardId']").val();
	if(cardid!=null||cardid!="undefined")
	{
		cardid=cardid.toUpperCase();
	}
	cardid=cardid.trim();
	if(cardid=="")
	{
		$("#cardidresult").html("");
		return false;
	}
    var str= checkIdcard(cardid);
   
    if(str=="验证通过!")
    {
    	$("#cardidresult").html("");
    }
    else
    {
    	 	$("#cardidresult").html(str);
    		$("#cardidresult").attr("style","color:red;");
    		
    }
}

function initEvent() {
    //显示收件人选择框
    $("#selectReceiver").click(function () {
        var t = $(this);
        var x = t.offset().left;
        var y = t.offset().top;
        $("#divKehu").css({ "left": x, "top": y + 30 }).toggle();
    });

    $(document).bind('mousedown', function (event) {
        var $target = $(event.target);
        if ((!($target.parents().andSelf().is('#divKehu')))) {
            $("#divKehu").hide();
        }
    });
    
    //搜索收件人
    $("#btnSearchReceiver").click(function () {
    	receiverGrid.setGridParam({ postData: { keyword: $("#divKehu [name=keyword]").val() } }).trigger("reloadGrid");
    });
    //收件人搜索
    $("#divKehu [name=keyword]").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnSearchReceiver").click();
        }
    }));
    //==========================寄件人========================
    //显示寄件人选择框
    $("#selectSender").click(function () {
        var t = $(this);
        var x = t.offset().left;
        var y = t.offset().top;
        $("#divSenderList").css({ "left": x, "top": y + 30 }).toggle();
    });

    $(document).bind('mousedown', function (event) {
        var $target = $(event.target);
        if ((!($target.parents().andSelf().is('#divSenderList')))) {
            $("#divSenderList").hide();
        }
    });
    //搜索寄件人
    $("#btnSearchSender").click(function () {
    	senderGrid.setGridParam({ postData: { keyword: $("#divSenderList [name=keyword]").val() } }).trigger("reloadGrid");
    });
    //寄件人搜索
    $("#divSenderList [name=keyword]").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnSearchSender").click();
        }
    }));
}

//收件人列表数据查询
function initReceiverGrid() {
	jsonData="";
	var receivergrid_selector = "#receivergrid-table";
	var receiverpager_selector = "#receivergrid-pager";
    receiverGrid = jQuery(receivergrid_selector).jqGrid({
        url: "/consignee/search",
        datatype: "json",
        height: 150,
        shrinkToFit: true,
        colNames: ['姓名', '电话', "城市"],
        colModel: [

            { name: 'name', index: 'name', width: 120 },
             { name: 'phone', index: 'phone', width: 120 },
             { name: 'city', index: 'city', width: 100 },
        ],

        viewrecords: true,
        rowNum: 10,
        rowList: [5, 10, 30],
        pager: receiverpager_selector,
        altRows: true,

        multiselect: true,
        //multikey: "ctrlKey",
        multiboxonly: true,
        //postData:jsonData,
        jsonReader: {
            root: "data.datas",
            page: "data.pageNow",
            total: "data.pageCount",
            records: "data.rowCount"
        },
        loadComplete: function (data) {
            var table = this;
            //jsonData = data.data.datas;
            
            if(data.code=="200")
            {
            	if(data.data!=null)
            	{
            	    jsonData = data.data.datas;
            	    updatePagerIcons(table);
            	}
            } else if ("607" == data.code) {
				alert("您尚未登录或登录已失效！");
				userlogout();
			} else {
				alert("获取发件人信息失败:" + data.message);
			}
            
            
            $(".ui-jqgrid-labels [role=checkbox]").hide();
            $(receivergrid_selector + " [role=checkbox]").attr({ "type": "radio", "name": "receivergrid-radio" });

        },
        onSelectRow: function (rowid, status) {
            $("#divKehu").hide();
            var row = jsonData.getData(rowid); 
            setInputData("#receiverInfo", row);
            setImgSrc("#receiverInfo", row);
            $("#divPreviewSFZ").show();
            if((removenull(row.cardUrl)=="")&&(removenull(row.cardurlother)=="")&&(removenull(row.cardurltogether)==""))
            {
            	$("#divPreviewSFZ").hide();
            }
        },
        autowidth: true
    });

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
}

//寄件人列表数据查询
function initSenderGrid() {
	senderData="";
	var receivergrid_selector = "#sendergrid-table";
	var senderpager_selector = "#sendergrid-pager";
    senderGrid = jQuery(receivergrid_selector).jqGrid({
        url: "/consignee_sendperson/search",
        datatype: "json",
        height: 150,
        shrinkToFit: true,
        colNames: ['姓名', '电话', "城市"],
        colModel: [

            { name: 'name', index: 'name', width: 120 },
             { name: 'phone', index: 'phone', width: 120 },
             { name: 'city', index: 'city', width: 100 },
        ],

        viewrecords: true,
        rowNum: 10,
        rowList: [5, 10, 30],
        pager: senderpager_selector,
        altRows: true,

        multiselect: true,
        //multikey: "ctrlKey",
        multiboxonly: true,
        //postData:senderData,
        jsonReader: {
            root: "data.datas",
            page: "data.pageNow",
            total: "data.pageCount",
            records: "data.rowCount"
        },
        loadComplete: function (data) {
            var table = this;
            if(data.code=="200")
            {
            	if(data.data!=null)
            	{
            		senderData = data.data.datas;
            	}
            } else if ("607" == data.code) {
				alert("您尚未登录或登录已失效！");
				userlogout();
			} else {
				alert("获取发件人信息失败:" + data.message);
			}
            $(".ui-jqgrid-labels [role=checkbox]").hide();
            $(receivergrid_selector + " [role=checkbox]").attr({ "type": "radio", "name": "receivergrid-radio" });

        },
        onSelectRow: function (rowid, status) {
        	
            $("#divSenderList").hide();
            var row = senderData.getData(rowid);
            setInputData("#senderInfo_detail", row);
            //把差异的补上
            $("#senderInfo_detail").find("input[name='state']").val(row.province);
            $("#senderInfo_detail").find("input[name='zipcode']").val(row.zipCode);
            $("#senderInfo_detail").find("input[name='address']").val(row.streetAddress);

        },
        autowidth: true
    });

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
}


//添加商品条目
function addGoodsItem() {


	
    var teplament = $("#goodsItemTeplament").html();
    $("#goodsItemBody").append(teplament);
	$("input[name='cquantity']").unbind();
	$("input[name='cvalue']").unbind();
	$("input[name='cquantity']").change(commuditydataChangeUser);//计算总重量和数量
	$("input[name='cvalue']").change(commuditydataChangeUser);//计算总重量和数量
}

//删除商品条目
function delGoodsItem(obj) {
    $(obj).parent().parent().remove();
}

//省市区选择
$(function () {

    //选择省
    $("#receiverInfo input[name=province]").bigAutocomplete({
        data: $.areaData.p,
        //url: "areadata.html",//可以请求到后台，返回的格式{"data":[{"title":"广西"}]}
        callback: function (p) {
        }
    });


    //选择市
    $("#receiverInfo input[name=city]").focusin(function () {
        $(this).bigAutocomplete({
            data: $.areaData.c[$("#receiverInfo input[name=province]").val()]
        });
    });

    //县区
    $("#receiverInfo input[name=district]").focusin(function () {
        $(this).bigAutocomplete({
            data: $.areaData.a[$("#receiverInfo input[name=province]").val() + "-" + $("#receiverInfo input[name=city]").val()]
        });
    });
});









//获取附加信息及处理
var premium_rate=0;
var premium_max_value=0;
var lowest_weight_value_flag=0;
var jw_commodity_rate=0;
var price_carry_flag=0;//计算方法
var cur_usa_cn=0;
function getglobalargs_commrate() {
	var flags = "premium_rate,premium_max_value,lowest_weight_value_flag,jw_commodity_rate,price_carry_flag,cur_usa_cn";//获取商品进位值
	$.ajax({
		type : "post",
		url : "/user/globalargs/getcontents",
		data : {
			"flags" : flags
		},
		success : function(response) {
			var code = response.code;

			if ("200" == code) {

				if (response.data != null) {
					premium_rate=response.data[0];	//提交保险的比例
					premium_max_value=response.data[1];//保险的保额最大值
					lowest_weight_value_flag=response.data[2];//重量的最小值，即多少磅起
					jw_commodity_rate=response.data[3];//商品的进位值
					price_carry_flag=response.data[4];//当前的计算方法
					cur_usa_cn=response.data[5];//当前的汇率
					
					//alert(jw_commodity_rate);
				} else {

				}
			} else {

			}
			
		}
	});
}


//装载门店及相关联的数据
function loadstorelist()
{
	$.ajax({
		type:"get",
		url:"/user/warehouse/all",
		success:function(response){
			var code = response.code;
			if (code == '200') {
				if(response.data != null) {
					var str = "<option value='-1'>选择门店</option>";
					$.each(response.data,function(){
						str += "<option value='" + this.id + "'>" + this.name + "</option>";
					});
					$("select[name='storelists']").html(str);
					
					
					
					$("select[name='storelists']").val(order_postData.storeId);
					
					$("select[name='storelists']").change(loadChannelSelectOptionbyuser_change);
					loadChannelSelectOptionbyuser();
					
					//showChannelTableInTM();
					
					//$("select[name='storelists']").change(loadChannelSelectOptionbyuser_change());
					//loadCommoditySelectOption1();
				}
			}
		}
	});	
}


//装载渠道选择
function loadChannelSelectOptionbyuser() {
	var wid = $("select[name='storelists']").val();
	if((removenull(wid)=="")||(wid=="-1"))
	{
		var str1 = "<option value='-1'>选择渠道</option>";
		$("select[name='channellist']").html(str1);
		return false;
	}
	$.ajax({
		type : "post",
		url : "/channel/get_by_store_available",
		data:{
			"wid":wid
		},
		success : function(response) {
			var code = response.code;
			if ("200" == code) {

				// showCommodityList(response.data);;
				var str = "";
				if(response.data!=null)
				{
					$.each(response.data, function() {
						str += "<option value='" + this.id 	
								+ "' additivePrice='" + this.additivePrice+"'>"
								+ this.name + "</option>";
					});
					$("select[name='channellist']").html(str);
					
				}
				else
				{
					var str1 = "<option value='-1'>选择渠道</option>";
					$("select[name='channellist']").html(str1);
					
					//return false;
				}
				
				$("select[name='channellist']").val(order_postData.channelId);
				
				$("select[name='channellist']").change(loadCommoditySelectOptionbyuser_change);
				loadCommoditySelectOptionbyuser();
			} else if ("607" == code) {
				alert("您尚未登录或登录已失效！");
				userlogout();
			}
		}
	});
}


//装载渠道选择
function loadChannelSelectOptionbyuser_change() {
	var wid = $("select[name='storelists']").val();
	if((removenull(wid)=="")||(wid=="-1"))
	{
		var str1 = "<option value='-1'>选择渠道</option>";
		$("select[name='channellist']").html(str1);
		return false;
	}
	$.ajax({
		type : "get",
		url : "/channel/get_by_store_available",
		data:{
			"wid":wid
		},
		success : function(response) {
			var code = response.code;
			if ("200" == code) {

				// showCommodityList(response.data);;
				var str = "";
				if(response.data!=null)
				{
					$.each(response.data, function() {
						str += "<option value='" + this.id 	
								+ "' additivePrice='" + this.additivePrice+"'>"
								+ this.name + "</option>";
					});
					$("select[name='channellist']").html(str);
					
				}
				else
				{
					var str1 = "<option value='-1'>选择渠道</option>";
					$("select[name='channellist']").html(str1);
					
					//return false;
				}
				
				
				
				$("select[name='channellist']").change(loadCommoditySelectOptionbyuser_change);
				loadCommoditySelectOptionbyuser_change();
			} else if ("607" == code) {
				alert("您尚未登录或登录已失效！");
				userlogout();
			}
		}
	});
}


//装载商品信息
function loadCommoditySelectOptionbyuser() {
	
	
	//var cid=$("select[name='channelidname']").val();
	
	$("select[name='commodityName']").html("");
	var wid = $("select[name='storelists']").val();
	var cid = $("select[name='channellist']").val();
	if((removenull(cid)=="")||(cid=="-1"))
	{
		return false;
	}
	
	
	//var additivePrice=$("select[name='channellist']").find("option:selected").attr("additivePrice");
	//$(":text[name='addtivePrice']").val(additivePrice);
	$.ajax({
		type : "post",
		url : "/commudityPrice/getpricebycidandwid",
		data : {
			wid : wid,
			cid : cid //设置渠道
		},
		success : function(response) {
			var code = response.code;
			if ("200" == code) {

				// showCommodityList(response.data);;
				var str = "";
				if(response.data!=null)
				{
					$.each(response.data, function() {
						str += "<option value='" + this.id + "' >"
						+ this.commudityAdmin.name + "</option>";
					});
				}
				$("select[name='commodityName']").html(str);
				
				if(order_postData.detail!=null)
				{
					var length=order_postData.detail.length;
					if(length>1)
					{
						for(var i=0;i<length-1;i++)
						{
							addGoodsItem();
						}
					}
					
					var flag=0;
					
					$("select[name='commodityName']").each(
					function() {
						if(flag==0)//第一个是隐藏的商品信息
						{
							
							flag++;
						}
						else{
							$(this).val(order_postData.detail[flag-1].commodityId);
							$(this).parent().parent().find("input[name='cproductname']").val(order_postData.detail[flag-1].productName);
							$(this).parent().parent().find("input[name='cbrandname']").val(order_postData.detail[flag-1].brands);
							$(this).parent().parent().find("input[name='cquantity']").val(order_postData.detail[flag-1].quantity);
							$(this).parent().parent().find("input[name='cvalue']").val(order_postData.detail[flag-1].value);
							$(this).parent().parent().find("input[name='cremark']").val(order_postData.detail[flag-1].remark);
							
							flag++;
						}
					});
				}
				
				
				//msOrderCalcFreight();
			} else if ("607" == code) {
				alert("您尚未登录或登录已失效！");
				userlogout();
			}
			
		}
	});
}

//装载商品信息
function loadCommoditySelectOptionbyuser_change() {
	
	
	//var cid=$("select[name='channelidname']").val();
	
	$("select[name='commodityName']").html("");
	var wid = $("select[name='storelists']").val();
	var cid = $("select[name='channellist']").val();
	if((removenull(cid)=="")||(cid=="-1"))
	{
		return false;
	}
	
	
	//var additivePrice=$("select[name='channellist']").find("option:selected").attr("additivePrice");
	//$(":text[name='addtivePrice']").val(additivePrice);
	$.ajax({
		type : "post",
		url : "/commudityPrice/getpricebycidandwid",
		data : {
			wid : wid,
			cid : cid //设置渠道
		},
		success : function(response) {
			var code = response.code;
			if ("200" == code) {

				// showCommodityList(response.data);;
				var str = "";
				if(response.data!=null)
				{
					$.each(response.data, function() {
						str += "<option value='" + this.id + "' >"
						+ this.commudityAdmin.name + "</option>";
					});
				}
				$("select[name='commodityName']").html(str);
				
				
				
				
				//msOrderCalcFreight();
			} else if ("607" == code) {
				alert("您尚未登录或登录已失效！");
				userlogout();
			}
			
		}
	});
}

//保险额度计算
function countpremiummoney_user_1()
{
	var rate=premium_rate;
	if((rate=="0")||(removenull(rate)==null))
	{
		return false;
	}
	rate = parseFloat(rate);
	if (isNaN(rate))
	{
		return false;
	}
	var premium1=parseFloat($("input[name='insurance']").val());
	if(isNaN(premium1))
	{
		return false;
	}
	if(premium1>0)
	{}
	else if(premium1<0)
	{
		alert("保险不能小于0!");
		$("input[name='insurance']").val(0);
		$("input[name='maxinsurance']").val(0);
		return false;
	}
	else
	{
		return false;
	}
	var premiumt=parseFloat(premium1/rate);
	if (isNaN(premiumt))
	{
		return false;
	}
	if(parseFloat(premium_max_value)!="NaN")
	{
		if(premiumt>parseFloat(premium_max_value))
		{
			alert("最大保险额度不能超过"+premium_max_value+"美元!");
			$("input[name='insurance']").val(premium_max_value*rate);
			$("input[name='maxinsurance']").val(premium_max_value);
		}
		else 
		{
			$("input[name='maxinsurance']").val(premiumt);
		}
	}
	else
	{
		$("input[name='maxinsurance']").val(premiumt);
	}
	
}

//设计总数量，总商品价值
function commuditydataChangeUser()
{

	var allquantity=0;

	var allcvalue=0;

	$("select[name='commodityName']").each(
			function() {
				var cquantity=$(this).parent().parent().find("input[name='cquantity']").val();
				
				
				if((parseFloat(cquantity)<0)||isNaN(cquantity)||(isInteger(cquantity)==false))
				{
					alert("只能是正整数数字!");
					
					$(this).parent().parent().find("input[name='cquantity']").css({
						"border-color" : "red"
					});
					$(this).parent().parent().find("input[name='cquantity']").change(
							function() {
								if ($(this).parent().parent().find("input[name='cquantity']")
										.val() != null)
									$(this).parent().parent().find("input[name='cquantity']")
											.css({
												"border-color" : ""
											});
							});
					$(this).parent().parent().find("input[name='cquantity']").val(0);
					return false;
					
				}
				
				
				
				if((cquantity=="")||(cquantity.trim()==""))
				{
					
				}
				else
				{
					allquantity=parseFloat(allquantity)+parseFloat(cquantity);
				}
				
			
				
				var cvalue=$(this).parent().parent().find("input[name='cvalue']").val();
				
				if((parseFloat(cvalue)<0)||isNaN(cvalue))
				{
					alert("价值不能小于0或非数字！");
					
					$(this).parent().parent().find("input[name='cvalue']").css({
						"border-color" : "red"
					});
					$(this).parent().parent().find("input[name='cvalue']").change(
							function() {
								if ($(this).parent().parent().find("input[name='cvalue']")
										.val() != null)
									$(this).parent().parent().find("input[name='cvalue']")
											.css({
												"border-color" : ""
											});
							});
					$(this).parent().parent().find("input[name='cvalue']").val(0);
					return false;
					
				}
				
				
				if((cvalue=="")||(cvalue.trim()==""))
				{
					
				}
				else
				{
					allcvalue=parseFloat(allcvalue)+parseFloat(cvalue);
				}
				
				
			});


	$("input[name='allquantity']").val(allquantity);
	$("input[name='allcvalue']").val(allcvalue);

}

//提交网上置单
//提交运单处理
function submitOnlineOrder(){
	commuditydataChangeUser();//在提交之前，还是要算一下

	var validate=true;
	
	var formData = new FormData();
	
	
	formData.append("printway", "0");
	
	//构造发件人数据
	var name=$("#senderInfo_detail").find("input[name='name']").val();
	var phone=$("#senderInfo_detail").find("input[name='phone']").val();
	var address=$("#senderInfo_detail").find("input[name='address']").val();
	var city=$("#senderInfo_detail").find("input[name='city']").val();
	var state=$("#senderInfo_detail").find("input[name='state']").val();
	var zipcode=$("#senderInfo_detail").find("input[name='zipcode']").val();
	var email=$("#senderInfo_detail").find("input[name='email']").val();
	var company=$("#senderInfo_detail").find("input[name='company']").val();
	
	
	//选择支付方式0是余额支付，1是现金支付,2表示用户自己支付，空运单没有支付的，用-10来表示

	var payway="-10";
	
	
	formData.append("payWay", payway);//0表示余额支付，1表示现金支付
	formData.append("userId", "");//当选择会员时，此用于标识归属的id
	
	
	if (name == "" || name == null) {
		$("#senderInfo_detail").find("input[name='name']").css({
			"border-color" : "red"
		});
		$("#senderInfo_detail").find("input[name='name']").change(
				function() {
					if ($("#senderInfo_detail").find("input[name='name']")
							.val() != null)
						$("#senderInfo_detail").find("input[name='name']")
								.css({
									"border-color" : ""
								});
				});
		validate = false;
	}
	
	if (phone == "" || phone == null) {
		$("#senderInfo_detail").find("input[name='phone']").css({
			"border-color" : "red"
		});
		$("#senderInfo_detail").find("input[name='phone']").change(
				function() {
					if ($("#senderInfo_detail").find("input[name='phone']")
							.val() != null)
						$("#senderInfo_detail").find("input[name='phone']")
								.css({
									"border-color" : ""
								});
				});
		validate = false;
	}
	formData.append("suser.name", name);
	formData.append("suser.phone", phone);
	formData.append("suser.address", address);
	formData.append("suser.city", city);
	formData.append("suser.state", state);
	formData.append("suser.zipcode", zipcode);
	formData.append("suser.email", email);
	formData.append("suser.company", company);
	formData.append("suser.id", order_postData.suser.id);
	
	//构造收件人数据
	var name=$("#receiverInfo").find("input[name='name']").val();
	var phone=$("#receiverInfo").find("input[name='phone']").val();
	var address=$("#receiverInfo").find("input[name='streetAddress']").val();
	var dist=$("#receiverInfo").find("input[name='district']").val();
	var city=$("#receiverInfo").find("input[name='city']").val();
	var state=$("#receiverInfo").find("input[name='province']").val();
	var zipcode=$("#receiverInfo").find("input[name='zipCode']").val();
	var cardid=$("#receiverInfo").find("input[name='cardId']").val();//身份证号
	var hcimage=removenull($("#receiverInfo").find("img[name='cardurltogether']").attr("src"));//身份证合成图
	var zmimage=removenull($("#receiverInfo").find("img[name='cardUrl']").attr("src"));//身份证正面图
	var fmimage=removenull($("#receiverInfo").find("img[name='cardurlother']").attr("src"));//身份证反面图
	var cardurlfile="";
	var cardurlotherfile="";
	var cardurltogetherfile="";
	
	if(removenull(cardid)!="")
    {
    	cardid=cardid.toUpperCase();
    }
	
	var filetype=$(":radio[name='radioupload']:checked").val();
	//$(":radio[name='userId']:checked").val()
	
	if(filetype=="hc")//合成图文件
	{
	
		cardurltogetherfile=$(":file[name='hcpicture']").val();
		
		
		$(":file[name='zmpicture']").val("");//正面图
		$(":file[name='fmpicture']").val("");//反面图
		
		if((removenull(cardurltogetherfile)!=""))
		{
			if(removenull(cardid)=="")
			{
				

				$("#receiverInfo").find("input[name='cardId']").css({
					"border-color" : "red"
				});
				$("#receiverInfo").find("input[name='cardId']").change(
						function() {
							if ($("#receiverInfo").find("input[name='cardId']")
									.val() != null)
								$("#receiverInfo").find("input[name='cardId']")
										.css({
											"border-color" : ""
										});
						});
				validate = false;
			
				
				
				alert("上传身份证必须同时上传身份证号!");
				return false;
			}
			var picture = document.getElementById("hcpicture");
			//alert(picture.value);
			//return false;
			if(picture==""||(picture==null))
			{}
			else
			{
				formData.append("cardurltogetherfile", picture.files[0]);
			}
		}
		
		
		
	}
	else if(filetype=="zf")//上传正反两面图
	{
		$(":file[name='hcpicture']").val("");
		cardurlfile=$(":file[name='zmpicture']").val();//正面图
		cardurlotherfile=$(":file[name='fmpicture']").val();//反面图
		if((removenull(cardurlfile)=="")&&(removenull(cardurlotherfile)!="")&&(removenull(zmimage)==""))
		{
			alert("必须同时上传正反两面身份证图片!");
			return false;
		}
		if((removenull(cardurlotherfile)=="")&&(removenull(cardurlfile)!="")&&(removenull(fmimage)==""))
		{
			alert("必须同时上传正反两面身份证图片!");
			return false;
		}
		if((removenull(cardurlotherfile)!="")||(removenull(cardurlfile)!=""))
		{
			if(removenull(cardid)=="")
			{
				

				$("#receiverInfo").find("input[name='cardId']").css({
					"border-color" : "red"
				});
				$("#receiverInfo").find("input[name='cardId']").change(
						function() {
							if ($("#receiverInfo").find("input[name='cardId']")
									.val() != null)
								$("#receiverInfo").find("input[name='cardId']")
										.css({
											"border-color" : ""
										});
						});
				validate = false;
			
				
				
				alert("上传身份证必须同时上传身份证号!");
				return false;
			}
			
		}
		var picture = document.getElementById("zmpicture");
		if(picture==""||picture==null)
		{
			
		}
		else
		{
			formData.append("cardurlfile", picture.files[0]);
		}
		 picture = document.getElementById("fmpicture");
		 if(picture==""||picture==null)
			{
				
			}
			else
			{
				formData.append("cardurlotherfile", picture.files[0]);
			}
		 
		
	}
	
	if (phone == "" || phone == null) {
		$("#receiverInfo").find("input[name='phone']").css({
			"border-color" : "red"
		});
		$("#receiverInfo").find("input[name='phone']").change(
				function() {
					if ($("#receiverInfo").find("input[name='phone']")
							.val() != null)
						$("#receiverInfo").find("input[name='phone']")
								.css({
									"border-color" : ""
								});
				});
		validate = false;
	}
	
	if (name == "" || name == null) {
		$("#receiverInfo").find("input[name='name']").css({
			"border-color" : "red"
		});
		$("#receiverInfo").find("input[name='name']").change(
				function() {
					if ($("#receiverInfo").find("input[name='name']")
							.val() != null)
						$("#receiverInfo").find("input[name='name']")
								.css({
									"border-color" : ""
								});
				});
		validate = false;
	}
	
	

	
	
	if (address == "" || address == null) {
		$("#receiverInfo").find("input[name='streetAddress']").css({
			"border-color" : "red"
		});
		$("#receiverInfo").find("input[name='streetAddress']").change(
				function() {
					if ($("#receiverInfo").find("input[name='streetAddress']")
							.val() != null)
						$("#receiverInfo").find("input[name='streetAddress']")
								.css({
									"border-color" : ""
								});
				});
		validate = false;
	}
	
	if (dist == "" || dist == null) {
		$("#receiverInfo").find("input[name='district']").css({
			"border-color" : "red"
		});
		$("#receiverInfo").find("input[name='district']").change(
				function() {
					if ($("#receiverInfo").find("input[name='district']")
							.val() != null)
						$("#receiverInfo").find("input[name='district']")
								.css({
									"border-color" : ""
								});
				});
		validate = false;
	}
	

	
	if (city == "" || city == null) {
		$("#receiverInfo").find("input[name='city']").css({
			"border-color" : "red"
		});
		$("#receiverInfo").find("input[name='city']").change(
				function() {
					if ($("#receiverInfo").find("input[name='city']")
							.val() != null)
						$("#receiverInfo").find("input[name='city']")
								.css({
									"border-color" : ""
								});
				});
		validate = false;
	}
	if (state == "" || state == null) {
		$("#receiverInfo").find("input[name='province']").css({
			"border-color" : "red"
		});
		$("#receiverInfo").find("input[name='province']").change(
				function() {
					if ($("#receiverInfo").find("input[name='province']")
							.val() != null)
						$("#receiverInfo").find("input[name='province']")
								.css({
									"border-color" : ""
								});
				});
		validate = false;
	}
	
	
	
    if(cardid!=null||cardid!="undefined")
	{
		cardid=cardid.toUpperCase();
		
	}
	cardid=cardid.trim();
	$("input[name='cardId']").val(cardid);
	if(cardid=="")
	{
		$("#cardidresult").html("");
		//return false;
	}
	else
	{
        var str= checkIdcard(cardid);
       
        if(str=="验证通过!")
        {
        	$("#cardidresult").html("");
        }
        else
        {
        	 	$("#cardidresult").html(str);
        		$("#cardidresult").attr("style","color:red;");
        		alert("身份证号码错误："+str);
        		return false;
        }
	}
	
	formData.append("ruser.name", name);
	formData.append("ruser.phone", phone);
	formData.append("ruser.address", address);
	formData.append("ruser.dist", dist);
	formData.append("ruser.city", city);
	formData.append("ruser.state", state);
	formData.append("ruser.zipcode", zipcode);
	
	formData.append("ruser.cardid", cardid);
	formData.append("ruser.cardtogether", hcimage);
	formData.append("ruser.cardurl", zmimage);
	formData.append("ruser.cardother", fmimage);
	formData.append("ruser.id", order_postData.ruser.id);

	
	//开始构造商品类信息
	var flag=-1;
	var ii=0;
	$("select[name='commodityName']").each(
			function() {
				
				if(flag>=0)//第一行为隐藏行的添加模板，不计入商品信息
				{
					
					
						var commodityid=$(this).val();//获取商品id
						var commodityname=$(this).find("option:selected").text();//获取商品名称
						var cproductname=$(this).parent().parent().find("input[name='cproductname']").val();//获取品名
						var cbrandname=$(this).parent().parent().find("input[name='cbrandname']").val();//获取品牌
						var cquantity=$(this).parent().parent().find("input[name='cquantity']").val();//获取数量
						var cweight="0";
						var cvalue=$(this).parent().parent().find("input[name='cvalue']").val();//获取价值
						var ctariff="0";
						var cother="0";
						var cremark=$(this).parent().parent().find("input[name='cremark']").val();//获取备注
						
						if((removenull(cproductname)=="")&&(removenull(cbrandname)=="")&&(removenull(cquantity)=="")&&(removenull(cweight)=="")&&(removenull(cvalue)==""))
						{}
						else
						{
							if (cproductname == "" || cproductname == null) {
								$(this).parent().parent().find("input[name='cproductname']").css({
									"border-color" : "red"
								});
								$(this).parent().parent().find("input[name='cproductname']").change(
										function() {
											if ($(this).parent().parent().find("input[name='cproductname']")
													.val() != null)
												$(this).parent().parent().find("input[name='cproductname']")
														.css({
															"border-color" : ""
														});
										});
								validate = false;
							}
							if (cbrandname == "" || cbrandname == null) {
								$(this).parent().parent().find("input[name='cbrandname']").css({
									"border-color" : "red"
								});
								$(this).parent().parent().find("input[name='cbrandname']").change(
										function() {
											if ($(this).parent().parent().find("input[name='cbrandname']")
													.val() != null)
												$(this).parent().parent().find("input[name='cbrandname']")
														.css({
															"border-color" : ""
														});
										});
								validate = false;
							}
							
							if (cquantity == "" || cquantity == null||isNaN(parseFloat(cquantity))||(parseFloat(cquantity)<=0)||(isInteger(cquantity)==false)) {
								$(this).parent().parent().find("input[name='cquantity']").css({
									"border-color" : "red"
								});
								$(this).parent().parent().find("input[name='cquantity']").change(
										function() {
											if ($(this).parent().parent().find("input[name='cquantity']")
													.val() != null)
												$(this).parent().parent().find("input[name='cquantity']")
														.css({
															"border-color" : ""
														});
										});
								validate = false;
							}
							
							
							
							if (cvalue == "" || cvalue == null||isNaN(parseFloat(cvalue))||(parseFloat(cvalue)<=0)) {
								$(this).parent().parent().find("input[name='cvalue']").css({
									"border-color" : "red"
								});
								$(this).parent().parent().find("input[name='cvalue']").change(
										function() {
											if ($(this).parent().parent().find("input[name='cvalue']")
													.val() != null)
												$(this).parent().parent().find("input[name='cvalue']")
														.css({
															"border-color" : ""
														});
										});
								validate = false;
							}
							
						
							formData.append("detail["+ii+"].commodityId", commodityid);
							formData.append("detail["+ii+"].name", commodityname);
							formData.append("detail["+ii+"].productName", cproductname);
							formData.append("detail["+ii+"].brands", cbrandname);
							formData.append("detail["+ii+"].quantity", cquantity);
							formData.append("detail["+ii+"].weight", cweight);
							formData.append("detail["+ii+"].value", cvalue);
							formData.append("detail["+ii+"].tariff", ctariff);
							formData.append("detail["+ii+"].other", cother);
							formData.append("detail["+ii+"].remark", cremark);
							ii=ii+1;
						}
					
				}
				
				flag=flag+1;
				
				
			});
	
	
	if(ii==0)
	{
		alert("至少录入一条商品信息!");
		return false;
	}
	//获取参数
	var weight="0";
	formData.append("weight", weight);
	if(parseFloat(weight)<0)
	{
		alert('\"重量\"输入错误！');
		return false;
	}
	
	if (isNaN(parseFloat(weight))) {
		alert('\"重量\"输入错误！');
		return false;
	}
	
	var allsjweight="0";
	formData.append("sjweight", allsjweight);
	
	var allquantity=$("input[name='allquantity']").val();//数量
	formData.append("quantity", allquantity);
	if(parseFloat(allquantity)<0)
	{
		alert('\"数量\"输入错误！');
		return false;
	}
	if (isNaN(parseFloat(allquantity))) {
		alert('\"数量\"输入错误！');
		return false;
	}
	var allcvalue=$("input[name='allcvalue']").val();//价值
	formData.append("value", allcvalue);
	if(parseFloat(allcvalue)<0)
	{
		alert('\"价值\"输入错误！');
		return false;
	}
	if (isNaN(parseFloat(allcvalue))) {
		alert('\"价值\"输入错误！');
		return false;
	}
	
	var insurance=$("input[name='insurance']").val();//保险
	formData.append("insurance", insurance);
	
	if(parseFloat(insurance)<0)
	{
		alert('\"保险\"输入错误！');
		return false;
	}
	if (isNaN(parseFloat(insurance))) {
		alert('\"保险\"输入错误！');
		return false;
	}
	var allcother="0";//价值
	formData.append("other", allcother);
	if(parseFloat(allcother)<0)
	{
		alert('\"其它费用\"输入错误！');
		return false;
	}
	if (isNaN(parseFloat(allcother))) {
		alert('\"其它费用\"输入错误！');
		return false;
	}
	
	var allctariff="0";//关税
	formData.append("tariff", allctariff);
	if(parseFloat(allctariff)<0)
	{
		alert('\"关税\"输入错误！');
		return false;
	}
	if (isNaN(parseFloat(allctariff))) {
		alert('\"关税\"输入错误！');
		return false;
	}
	var totalremark=$("textarea[name='totalremark']").val();//备注
	formData.append("remark", totalremark);
	if(validate == false)
	{
		alert("信息填写不完整，请填写完整后提交!");
		return false;
	}
	var channelId=$("select[name='channellist']").val();//渠道id
	formData.append("channelId", channelId);
	if((removenull(channelId)=="")||(isNaN(channelId))||(parseFloat(channelId)<=0))
	{
		alert('\"渠道\"信息错误，请选择正确的渠道！');
		return false;
	}
	var storeId=$("select[name='storelists']").val();//门店id
	if((removenull(storeId)=="")||(isNaN(storeId))||(parseFloat(storeId)<=0))
	{
		alert('\"门店\"信息错误，请选择正确的门店！');
		return false;
	}
	formData.append("storeId", storeId);
	
	
	

	
	//判断是否发送短信
	var automessage=0;
	if($(":checkbox[name='save_sender_flag']").is(':checked')==true)
	{
		formData.append("savesenderflag", "1");//保存发件人	
	}
	else
	{
		formData.append("savesenderflag", "0");//不保存发件人
	}
	//保存收件人
	if($(":checkbox[name='save_rever_flag']").is(':checked')==true)
	{
		formData.append("savereverflag", "1");//保存收件人	
	}
	else
	{
		formData.append("savereverflag", "0");//不保存收件人
	}
	formData.append("id", order_postData.id);
	
	formData.append("automessage", automessage);
	var httpRequest = new XMLHttpRequest();
	var url = "/m_order/online_modify";
	
	httpRequest.onreadystatechange = function(){
		if(4 == httpRequest.readyState){
			if(200 == httpRequest.status){
				//alert(httpRequest.responseText);
				obj = JSON.parse(httpRequest.responseText);
				
				if(obj.code=="200")
				{
					alert("修改成功！");
					window.location.href = "zaixianzhidan.html?id=0";
					
				} else if ("607" == obj.code) {
					alert("您尚未登录或登录已失效！");
					userlogout();
				}
				else if(obj.code=="40001")//发件人或收件人的地址薄中电话已存在，通知是不继续
				{
					if (confirm("你的地址簿中，"+obj.message+"已存在，是否继续？")) 
					{
						formData.append("reflag", "1");//标识继续保存重复数据
						httpRequest.open("post", url, true);
						httpRequest.send(formData);
					}
				}
				else
				{
					alert("提交出错，出错原因："+obj.message);
				}
				
				//nav95Click();
			}
		}
	};
	httpRequest.open("post", url, true);
	httpRequest.send(formData);
	
return false;
	
	
}


function getMorderbyid(id)
{
	$.ajax({
		type : "get",
		url : "/m_order/get_one_by_user",
		data : {
					"id" : id
				},
		success : function(
						response) {
					var code = response.code;
					if(code=="200")
					{
						if(response.data!=null)
						{
							order_postData=response.data;
							
							//装载发件人
							setInputData("#senderInfo_detail", response.data.suser);
							
							//装载接收人
							setInputData("#receiverInfo", response.data.ruser);
							$("#receiverInfo").find("input[name='zipCode']").val(response.data.ruser.zipcode);
							$("#receiverInfo").find("input[name='province']").val(response.data.ruser.state);
							$("#receiverInfo").find("input[name='district']").val(response.data.ruser.dist);
							$("#receiverInfo").find("input[name='streetAddress']").val(response.data.ruser.address);
							$("#receiverInfo").find("input[name='cardId']").val(response.data.ruser.cardid);
							//setImgSrc("#receiverInfo", response.data.ruser);
							$("#receiverInfo").find("img[name='cardUrl']").attr("src",response.data.ruser.cardurl);
							$("#receiverInfo").find("img[name='cardurlother']").attr("src",response.data.ruser.cardother);
							$("#receiverInfo").find("img[name='cardurltogether']").attr("src",response.data.ruser.cardtogether);
				            $("#divPreviewSFZ").show();
				            if((removenull(response.data.ruser.cardurl)=="")&&(removenull(response.data.ruser.cardother)=="")&&(removenull(response.data.ruser.cardtogether)==""))
				            {
				            	$("#divPreviewSFZ").hide();
				            }
				            $("input[name='allquantity']").val(response.data.quantity);
				            $("input[name='allcvalue']").val(response.data.value);
				            $("input[name='insurance']").val(response.data.insurance);
				            $("textarea[name='totalremark']").val(response.data.remark);
				            countpremiummoney_user_1();
							  //装载后台如门店渠道商品等信息
						    loadstorelist();
						}
						else
						{
							alert("获取信息失败");
							window.location.href = "zaixianzhidanlist.html";
						}
						
					
						
					} else if ("607" == code) {
						alert("您尚未登录或登录已失效！");
						userlogout();
					}
					else
					{
						alert("获取信息失败，出错原因："+response.message);
						window.location.href = "zaixianzhidanlist.html";
					}
			}});	
}