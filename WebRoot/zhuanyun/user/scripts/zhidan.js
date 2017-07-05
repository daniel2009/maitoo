
//获取附加信息及处理
var premium_rate = 0;
var premium_max_value = 0;
var lowest_weight_value_flag = 0;
var jw_commodity_rate = 0;
var price_carry_flag = 0;//计算方法
var cur_usa_cn = 0;
function getglobalargs_commrate() {
    var flags = "premium_rate,premium_max_value,lowest_weight_value_flag,jw_commodity_rate,price_carry_flag,cur_usa_cn";//获取商品进位值
    $.ajax({
        type: "post",
        url: "/user/globalargs/getcontents",
        data: {
            "flags": flags
        },
        success: function (response) {
            var code = response.code;

            if ("200" == code) {

                if (response.data != null) {
                    premium_rate = response.data[0];	//提交保险的比例
                    premium_max_value = response.data[1];//保险的保额最大值
                    lowest_weight_value_flag = response.data[2];//重量的最小值，即多少磅起
                    jw_commodity_rate = response.data[3];//商品的进位值
                    price_carry_flag = response.data[4];//当前的计算方法
                    cur_usa_cn = response.data[5];//当前的汇率

                    //alert(jw_commodity_rate);
                } else {

                }
            } else {

            }

        }
    });
}



var receiverGrid, jsonData;
var packageGrid, packageData;
var packagegrid_selector = "#packagegrid-table";
var showReceiverSelectObj;//记录弹出收件人的按钮，后续用来对分箱赋值用

$(function () {
	getuserinfo();
	getglobalargs_commrate();
    loadcommunitysInfo();
    getcounts();
    $("#divAdvanceSearch").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            searchPackageData();
        }
    }));

    $("#btnSearch").click(function () {
        searchPackageData();
    });

    
    if ($(window).width() < 640) {
        initPhonePackageGrid();
        $(".phoneHide").hide();
    } else {
        initPackageGrid();
    }
    initReceiverGrid();
    initEvent();
    loadSearchInfo();
});

function initEvent() {
    $(document).bind('mousedown', function (event) {
        var $target = $(event.target);
        if ((!($target.parents().andSelf().is('#divKehu')))) {
            $("#divKehu").hide();
        }
    })

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

    $("input[name='cquantity']").change(commuditydataChangeUser);//计算总重量和数量
    $("input[name='cvalue']").change(commuditydataChangeUser);//计算总重量和数量

    $("input[name='insurance']").change(countpremiummoney_user);//保险改变
    
    
}


function showReceiverSelect(obj) {
    showReceiverSelectObj = obj;
    var t = $(obj);
    var x = t.offset().left;
    var y = t.offset().top;
    $("#divKehu").css({ "left": x, "top": y + 30 }).toggle();
    if ($("#divKehu:visible")) {
        $("#btnSearchReceiver").click();
    }
}

//添加商品条目
function addGoodsItem(obj) {
    var teplament = $("#goodsItemTeplament").html();
    $(obj).closest("[body=goodsItemBody]").append(teplament);
    var select = $(obj).closest("[body=goodsItemBody]").find("[name=commodityName]:first option:selected");
    var sva=select.val();
  //  $(obj).closest("[body=goodsItemBody]").find("[name=commodityName]:gt(0)").html(select[0].outerHTML);
    $(obj).closest("[body=goodsItemBody]").find("[name=commodityName]:last").val(sva);

    $("input[name='cquantity']").unbind();
    $("input[name='cvalue']").unbind();
    $("select[name='commodityName']").unbind();
    $("input[name='cquantity']").change(commuditydataChangeUser);//计算总重量和数量
    $("input[name='cvalue']").change(commuditydataChangeUser);//计算总重量和数量
    $("select[name='commodityName']").change(commuditydataChangeUser);//商品改变
}

//删除商品条目

function delGoodsItem(obj) {
    $(obj).parent().parent().remove();
    //重新计算总价
    $(".boxInfo").each(function () {
        commuditydataChangeUser($(this), 1);
    });
}

//添加分箱
function addBox() {
    var t = $(".boxTemplate").html();
    var sn = $(".boxitem:last .sn").html() * 1 + 1;
    $("#packagecount").html($("#packagecount").html() * 1 + 1);
    $(".page-content").append(t);
    $(".boxitem:last .sn").html(sn);
    $("body").animate({ scrollTop: $(".boxitem:last").offset().top }, 10);
    event.stopPropagation();
    $("input[name='cquantity']").change(commuditydataChangeUser);//计算总重量和数量
    $("input[name='cvalue']").change(commuditydataChangeUser);//计算总重量和数量
    $("input[name='insurance']").unbind();
    $("input[name='insurance']").change(countpremiummoney_user);//保险改变
}

//移除分箱
function removeBox(obj) {
    $(obj).parent().parent().parent().remove();
    event.stopPropagation();
    $("#packagecount").html($("#packagecount").html() * 1 - 1);
}


//收件人列表数据查询
function initReceiverGrid() {
    jsonData = "";
    var receivergrid_selector = "#receivergrid-table";
    var receiverpager_selector = "#receivergrid-pager";
    receiverGrid = jQuery(receivergrid_selector).jqGrid({
        url: "/consignee/search",
        datatype: "json",
        height: 150,
        //shrinkToFit: true,
        mtype: 'get',
        colNames: ['姓名', '电话', "城市"],
        colModel: [

            { name: 'name', index: 'name', width: 80 },
             { name: 'phone', index: 'phone', width: 120 },
             {
                 name: 'address', index: 'address', width: 100, formatter: function (cellValue, option, row) {
                     //商品
                     var str = "";
                     if (removenull(row.province) != "") {
                         str += row.province;
                     }
                     if (removenull(row.city) != "") {
                         str += row.city;
                     }
                     if (removenull(row.district) != "") {
                         str += row.district;
                     }
                     if (removenull(row.streetAddress) != "") {
                         str += row.streetAddress;
                     }
                     if (removenull(row.zipCode) != "") {
                         str += " " + row.zipCode;
                     }

                     return str;
                 }
             },
        ],

        viewrecords: true,
        rowNum: 10,
        rowList: [5, 10, 30],
        pager: receiverpager_selector,
        altRows: true,

        multiselect: true,
        //multikey: "ctrlKey",
        multiboxonly: true,
        // postData: jsonData,
        jsonReader: {//rowCount":32,"pageCount":4,"pageSize":10,"pageNow":
            root: "data.datas",
            page: "data.pageNow",
            total: "data.pageCount",
            records: "data.rowCount"
        },
        loadComplete: function (data) {
            var table = this;
            //jsonData = data.data.datas;
            setTimeout(function () {
                updatePagerIcons(table);
            }, 0);

            if (data.code == "200") {
                if (data.data != null) {
                    jsonData = data.data.datas;
                }
            } else if ("607" == data.code) {
                alert("您尚未登录或登录已失效！");
                userlogout();
            } else {
                alert("获取收件人信息失败:" + data.message);
            }


            $(".ui-jqgrid-labels [role=checkbox]").hide();
            $(receivergrid_selector + " [role=checkbox]").attr({ "type": "radio", "name": "receivergrid-radio" });
            
            


        },
        onSelectRow: function (rowid, status) {
            $("#divKehu").hide();
            var row = jsonData.getData(rowid);
            var $receiverInfo = $(showReceiverSelectObj).closest(".boxInfo").find("span[name=receiver]");
            var input = '<input type="hidden" name="receiveruserid" value="' + row.id + '"/>';
            //province":"河北省2","city":"张家口市2","district":"宣化县2","streetAddress"
            var displayInfo = "<span>" + row.name + "/" + row.phone + "/" + removenull(row.province) + removenull(row.city) + removenull(row.district) + removenull(row.streetAddress) + " " + removenull(row.zipCode) + "/" + row.cardId + "/身份证：" + "<a href='" + row.cardUrl + "'><img width=18 height=18 src='" + row.cardUrl + "'/></a>&nbsp;" + "<a href='" + row.cardurlother + "'><img width=18 height=18 src='" + row.cardurlother + "'/></a>&nbsp;" + "<a href='" + row.cardurltogether + "'><img width=18 height=18 src='" + row.cardurltogether + "'/></a>" + "</span>";
            $receiverInfo.html(input + displayInfo);

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

//包裹列表数据查询
function initPackageGrid() {
    packageData = "";
    packageGrid = jQuery(packagegrid_selector).jqGrid({
        url: "/user/t_order/get_t_order_5",
        datatype: "json",
        height: 120,
        //shrinkToFit: true,
        mtype: 'get',
        colNames: ['美国转运单号', '商品备注', "到库时间", "重量", "入库仓库", "仓位"],
        colModel: [

            { name: 'torderId', index: 'torderId', width: 100 },
             { name: 'remark', index: 'remark', width: 120 },
             { name: 'i_date', index: 'i_date', width: 100 },
             { name: 'i_weight', index: 'i_weight', width: 60 },
             { name: 'i_storeName', index: 'i_storeName', width: 80 },
             {
                 name: 'position', index: 'position', width: 60, formatter: function (cellValue, option, row) {
                     if (row.position != null) {
                         return removenull(row.position.position);
                     }
                     else {
                         return "";
                     }
                 }
             },
        ],
        heigth: 120,
        viewrecords: true,
        rowNum: 100,
        rowList: [5, 10, 30],
        altRows: true,

        multiselect: true,
        multiboxonly: true,
        jsonReader: {
            root: "data.datas",
            page: "data.pageNow",
            total: "data.pageCount",
            records: "data.rowCount"
        },
        loadComplete: function (data) {
            var table = this;
            
            if (data.code == "200") {
                if (data.data != null) {
                    packageData = data.data.datas;
                    // $(".ui-jqgrid-labels [role=checkbox]").hide();
                    // $(packagegrid_selector + " [role=checkbox]").attr({ "type": "radio", "name": "packagegrid-radio" });
                }
                else if(first_load_flag==true)
                {
                	$(".ui-jqgrid-bdiv").css({ "font-size": "32px", "text-align": "center","line-height":"100px" });
                    $(".ui-jqgrid-bdiv div div").html("没有已入库转运单");
                }
                
                
              
                first_load_flag=false;
                
            } else if ("607" == data.code) {
                alert("您尚未登录或登录已失效！");
                userlogout();
            } else {
                alert("获取包裹信息失败:" + data.message);
            }
        },
        onSelectRow: function (rowid, status) {
            var ids = $(packagegrid_selector).jqGrid('getGridParam', 'selarrrow');
            $("#zhuanyundancount").html(ids.length);

            if (status == false) { return; }
            var row1 = packageData.getData(rowid);

            for (var i = 0; i < ids.length; i++) {
                var id = ids[i];
                var row = packageData.getData(id);
                if (id != rowid) {
                    if (row1.i_storeId != row.i_storeId)//所选门店必须相同
                    {
                        //把rowid选择中的取消
                        $(packagegrid_selector).setSelection(rowid, false);
                        $("#zhuanyundancount").html($("#zhuanyundancount").html() * 1 - 1);
                        alert("必选择相同仓库的包裹!");
                        break;
                    }
                }
            }
        },
        autowidth: true
    });
}
var first_load_flag=true;
function initPhonePackageGrid() {
    packageData = "";
    packageGrid = jQuery(packagegrid_selector).jqGrid({
        url: "/user/t_order/get_t_order_5",
        datatype: "json",
        height: 120,
        //shrinkToFit: true,
        mtype: 'get',
        colNames: ['美国转运单号', '商品备注',  "入库仓库"],
        colModel: [

            { name: 'torderId', index: 'torderId', width: 100 },
             { name: 'remark', index: 'remark', width: 120 },
             
             { name: 'i_storeName', index: 'i_storeName', width: 80 },
            
        ],
        heigth: 120,
        viewrecords: true,
        rowNum: 100,
        rowList: [5, 10, 30],
        altRows: true,

        multiselect: true,
        multiboxonly: true,
        jsonReader: {
            root: "data.datas",
            page: "data.pageNow",
            total: "data.pageCount",
            records: "data.rowCount"
        },
        loadComplete: function (data) {
            var table = this;

            if (data.code == "200") {
                if (data.data != null) {
                    packageData = data.data.datas;
                    // $(".ui-jqgrid-labels [role=checkbox]").hide();
                    // $(packagegrid_selector + " [role=checkbox]").attr({ "type": "radio", "name": "packagegrid-radio" });
                }
                else if(first_load_flag==true){
                    $(".ui-jqgrid-bdiv").css({ "font-size": "32px", "text-align": "center", "line-height": "100px" });
                    $(".ui-jqgrid-bdiv div div").html("没有已入库转运单");
                }
                first_load_flag=false;
            } else if ("607" == data.code) {
                alert("您尚未登录或登录已失效！");
                userlogout();
            } else {
                alert("获取包裹信息失败:" + data.message);
            }
        },
        onSelectRow: function (rowid, status) {
            var ids = $(packagegrid_selector).jqGrid('getGridParam', 'selarrrow');
            $("#zhuanyundancount").html(ids.length);

            if (status == false) { return; }
            var row1 = packageData.getData(rowid);

            for (var i = 0; i < ids.length; i++) {
                var id = ids[i];
                var row = packageData.getData(id);
                if (id != rowid) {
                    if (row1.i_storeId != row.i_storeId)//所选门店必须相同
                    {
                        //把rowid选择中的取消
                        $(packagegrid_selector).setSelection(rowid, false);
                        $("#zhuanyundancount").html($("#zhuanyundancount").html() * 1 - 1);
                        alert("必选择相同仓库的包裹!");
                        break;
                    }
                }
            }
        },
        autowidth: true
    });
}

//加载入库门店
function loadSearchInfo() {

    $.ajax({
        type: "get",
        url: "/store/guest_store_list",
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
                    $("select[name='storeId']").change(searchPackageData);
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

//转运列表查询
function searchPackageData() {
    var data = getEelementData("#divAdvanceSearch");
    refreshPackageGrid(data);
}

//刷新转运列表数据
function refreshPackageGrid(data) {
    $(packagegrid_selector).jqGrid("setGridParam", { postData: data }).trigger("reloadGrid");
}

//设计总数量，总商品价值
function commuditydataChangeUser() {
    var $source = $(this).closest(".boxInfo");//change触发
    if (arguments.length == 2) {//主动传参,传过一个参数来区分
        $source = arguments[0];
    } else {

    }
    var allquantity = 0;
    var allcvalue = 0;
   // var prechannelid="";
   // var preCommudity="";//商品类型名称
    $source.find("select[name='commodityName']").each(
			function () {
			    var cquantity = $(this).parent().parent().find("input[name='cquantity']").val();

			    if ((parseFloat(cquantity) < 0) || isNaN(cquantity) || (isInteger(cquantity) == false)) {
			        alert("只能是正整数数字!");

			        $(this).parent().parent().find("input[name='cquantity']").css({
			            "border-color": "red"
			        });
			        $(this).parent().parent().find("input[name='cquantity']").change(
							function () {
							    if ($(this).parent().parent().find("input[name='cquantity']")
										.val() != null)
							        $(this).parent().parent().find("input[name='cquantity']")
											.css({
											    "border-color": ""
											});
							});
			        $(this).parent().parent().find("input[name='cquantity']").val(0);
			        return false;

			    }

			    if ((cquantity == "") || (cquantity.trim() == "")) {

			    }
			    else {
			        allquantity = parseFloat(allquantity) + parseFloat(cquantity);
			    }



			    var cvalue = $(this).parent().parent().find("input[name='cvalue']").val();

			    if ((parseFloat(cvalue) < 0) || isNaN(cvalue)) {
			        alert("价值不能小于0或非数字！");

			        $(this).parent().parent().find("input[name='cvalue']").css({
			            "border-color": "red"
			        });
			        $(this).parent().parent().find("input[name='cvalue']").change(
							function () {
							    if ($(this).parent().parent().find("input[name='cvalue']")
										.val() != null)
							        $(this).parent().parent().find("input[name='cvalue']")
											.css({
											    "border-color": ""
											});
							});
			        $(this).parent().parent().find("input[name='cvalue']").val(0);
			        return false;

			    }


			    if ((cvalue == "") || (cvalue.trim() == "")) {

			    }
			    else {
			        allcvalue = parseFloat(allcvalue) + parseFloat(cvalue);
			    }
			    
			    
			    //确认商品是不是同一个渠道的
			  /* var channelid= $(this).find("option:selected").attr("channelid");//获取到渠道id
			   if(prechannelid=="")
			   {
				   prechannelid=channelid;
				   preCommudity=$(this).find("option:selected").text();//获取商品名称
			   }
			   else
			   {
				   if(prechannelid!=channelid)//商品不能混装
				   {
					   var name=$(this).find("option:selected").text();//获取商品名称
					   alert("商品("+name+")不能与其它商品混合，如包含此类商品，请选择分箱处理!");
					   $(this).find("option:selected").val()
					   return false;
				   }
			   }*/

			});
    $source.find("input[name='allquantity']").val(allquantity);
    $source.find("input[name='allcvalue']").val(allcvalue);

}

function changecommudity(obj)
{

   /* var $source = $(this).closest(".boxInfo");//change触发
    if (arguments.length == 2) {//主动传参,传过一个参数来区分
        $source = arguments[0];
    } else {

    }*/

   // var value=$(obj).find("option:selected").val();//
    var prechannelid=$(obj).find("option:selected").attr("channelid");;
    var preCommudity=$(obj).find("option:selected").text();//获取商品名称
    $(obj).parent().parent().parent().find("select[name='commodityName']").each(
			function () {
			  
			    
			    
			    //确认商品是不是同一个渠道的
			   var channelid= $(this).find("option:selected").attr("channelid");//获取到渠道id
			   var value= $(this).find("option:selected").val();
			   if(prechannelid=="")
			   {
				   prechannelid=channelid;
				   preCommudity=$(this).find("option:selected").text();//获取商品名称
			   }
			   else
			   {
				   if(prechannelid!=channelid)//商品不能混装
				   {
					  // var name=$(this).find("option:selected").text();//获取商品名称
					   alert("商品("+preCommudity+")不能与其它商品混合，如包含此类商品，请选择分箱处理!");
					   $(obj).val(value);
					   return false;
				   }
			   }

			});
    //$source.find("input[name='allquantity']").val(allquantity);
  //  $source.find("input[name='allcvalue']").val(allcvalue);

	
}



var addAddressDialog;
function addReceiverAddress() {
    var width = $(window).width();
    if (width > 1000) { width = 1000; } else { width = width - 10; }
    addAddressDialog = dialog({
        title: "添加收件人",
        content: $("#divAdd"),
        width: width,
        height: "auto",
        //cancelVal: '关闭',
        //cancel: true //为true等价于function(){}
    }).show();
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



//装载商品信息
function loadcommunitysInfo() {

    $.ajax({
        type: "get",
        url: "/commudityPrice/tran/commudity",
        success: function (response) {
            var code = response.code;

            if ("200" == code) {

                var str = "";
                if (response.data != null) {
                    storelist_jason = response.data;
                    //只有一个渠道的商品
                    var str = "";
                    if (response.data.number == 1 || response.data.number == "1") {
                        $.each(response.data.commudityList[0],
								function () {


								    str += "<option value='" + this.id + "' channelId=" + this.channelId + ">" + this.name + "</option>";
								});
                    }
                    else {
                        //超过两个渠道的商品
                        for (var i = 0; i < response.data.number; i++) {
                            $.each(response.data.commudityList[i],
    								function () {


    								    str += "<option value='" + this.id + "' channelId=" + this.channelId + ">" + i + "-" + this.name + "</option>";
    								});
                        }

                    }

                    $("select[name='commodityName']").html(str);
                    // $("select[name='storeId']").change(searchPackageData);
                } else {

                }
            } else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                top.location.href = "../admin/login.html";
            }
            else {
                alert("读取商品信息出错，原因：" + response.message);
            }

        }
    });
}


//提交制单
function submitOnlineOrder() {
	
	//要选择同意协议
	var checkbox=$(":checkbox[name='checkbox_agree']").is(":checked");
	if(checkbox==true||checkbox=="true")
	{}
	else
	{
		alert("请选择\"同意本站协议\"后再行提交，谢谢!");
		return false;
	}
	
	
    commuditydataChangeUser();//在提交之前，还是要算一下
    $("[class=boxitem] input").css("border-color", "#d5d5d5");
    var formData = new FormData();

    //转运单信息
    var ids = $(packagegrid_selector).jqGrid('getGridParam', 'selarrrow');
    if (ids.length == 0) { alert("请先选择转运单再提交"); return; }
    for (var i = 0; i < ids.length; i++) {
        formData.append("torderIds[" + i + "]", ids[i]);
    }

    

    var boxitems = $("[class=boxitem]:gt(0)");
    for (var i = 0; i < boxitems.length; i++) {
        var boxitem =$(boxitems[i]);

        //收件人信息
        var ruserId = boxitem.find("input[name=receiveruserid]").val();//保存收件人用到的地址薄id
        if (ruserId == undefined || ruserId == "") { alert("装箱【" + (i + 1) + "】没有选择收货人地址"); return; }
        formData.append("morder[" + i + "].ruserId", ruserId);

        //商品信息
        var goodsitemvalid = true;
        var goodsitems = boxitem.find("[body=goodsItemBody]");

        var commodityNameitems=boxitem.find("[body=goodsItemBody]").find("[name=commodityName]");
        for (var j = 0; j < commodityNameitems.length; j++) {
            //var gooditem = $(goodsitems[j]);
        	var gooditem=$(commodityNameitems[j]).parent().parent();
        	
        	var commodityid=$.trim(gooditem.find("[name=commodityName]").val());//获取商品id
            var commodityName = $.trim(gooditem.find("[name=commodityName]").find("option:selected").text());//获取商品类型名
            var cproductname = $.trim(gooditem.find("[name=cproductname]").val());
            var cbrandname = $.trim(gooditem.find("[name=cbrandname]").val());
            var cquantity = $.trim(gooditem.find("[name=cquantity]").val());
            var cvalue = $.trim(gooditem.find("[name=cvalue]").val());
            var cremark = $.trim(gooditem.find("[name=cremark]").val());
            
            
            
        
            
            
            formData.append("morder[" + i + "].detail[" + j + "].commodityId", commodityid);
            formData.append("morder[" + i + "].detail[" + j + "].name", commodityName);
            formData.append("morder[" + i + "].detail[" + j + "].productName", cproductname);
            formData.append("morder[" + i + "].detail[" + j + "].brands", cbrandname);
            formData.append("morder[" + i + "].detail[" + j + "].quantity", cquantity);
            formData.append("morder[" + i + "].detail[" + j + "].value", cvalue);
            formData.append("morder[" + i + "].detail[" + j + "].remark", cremark);

            if (commodityName == "") { gooditem.find("[name=commodityName]").css("border-color","red"); goodsitemvalid = false; }
            if (cproductname == "") { gooditem.find("[name=cproductname]").css("border-color","red"); goodsitemvalid = false; }
            if (cbrandname == "") { gooditem.find("[name=cbrandname]").css("border-color","red"); goodsitemvalid = false; }
            if (cquantity == "" || cquantity * 1 <= 0) { gooditem.find("[name=cquantity]").css("border-color","red"); goodsitemvalid = false; }
            if (cvalue == "" || cvalue * 1 <= 0) { gooditem.find("[name=cvalue]").css("border-color","red"); goodsitemvalid = false; }
        }

        //allquantity  allcvalue  insurance  maxinsurance totalremark
        var allquantity = $.trim(boxitem.find("input[name=allquantity]").val());
        var allcvalue = $.trim(boxitem.find("input[name=allcvalue]").val());
        var insurance = $.trim(boxitem.find("input[name=insurance]").val());
        var maxinsurance = $.trim(boxitem.find("input[name=maxinsurance]").val());
        var totalremark = $.trim(boxitem.find("textarea[name=totalremark]").val());
        
        
        

        

        formData.append("morder[" + i + "].quantity", allquantity);
        formData.append("morder[" + i + "].value", allcvalue);
        formData.append("morder[" + i + "].insurance", insurance);
       // formData.append("morder[" + i + "].maxinsurance", maxinsurance);
        formData.append("morder[" + i + "].remark", totalremark);
        
        
      

        if (allquantity == "" || allquantity * 1 == 0||isNaN(parseFloat(allquantity))) { boxitem.find("input[name=allquantity]").css("border-color","red"); goodsitemvalid = false; }
        if (allcvalue == "" || allcvalue * 1 == 0||isNaN(parseFloat(allcvalue))) { boxitem.find("input[name=allcvalue]").css("border-color","red"); goodsitemvalid = false; }
        if (insurance == "" || insurance * 1 < 0||isNaN(parseFloat(insurance))) { boxitem.find("input[name=insurance]").css("border-color","red"); goodsitemvalid = false; }
       // if (maxinsurance == "" || maxinsurance * 1 == 0) { boxitem.find("input[name=maxinsurance]").css("border-color","red"); goodsitemvalid = false; }


        if (goodsitemvalid == false) { return; }
    }


    var httpRequest = new XMLHttpRequest();
    //var url = "/user/t_order/submit_torder";
    var url="/user/t_order/submit_torder_check_price";
    httpRequest.open("post", url, true);
    httpRequest.send(formData);

    httpRequest.onreadystatechange = function () {
        if (4 == httpRequest.readyState) {
            if (200 == httpRequest.status) {
                //alert(httpRequest.responseText);
                obj = JSON.parse(httpRequest.responseText);

                if (obj.code == "200") {
                	
                	if (confirm(obj.message+",确定提交？")) {
                		
                		
                		
                	    var httpRequest1 = new XMLHttpRequest();
                	    var url1 = "/user/t_order/submit_torder";
                	    //var url1="/user/t_order/submit_torder_check_price";
                	    httpRequest1.open("post", url1, true);
                	    httpRequest1.send(formData);

                	    httpRequest1.onreadystatechange = function () {
                	        if (4 == httpRequest1.readyState) {
                	            if (200 == httpRequest1.status) {
                	                //alert(httpRequest.responseText);
                	                obj = JSON.parse(httpRequest1.responseText);

                	                if (obj.code == "200") {
                	                	getcounts();
                	                	alert("保存成功."+obj.message);
                	                	window.location.href = "zhidan.html";
                	                } else if ("607" == obj.code) {
                	                    alert("您尚未登录或登录已失效！");
                	                    userlogout();
                	                }

                	                else {
                	                    alert("提交出错，出错原因：" + obj.message);
                	                }
                	            }
                	        }
                	    };
                		
                		
                		
                		 //window.location.href = "zhidan.html";
					}
                	
                	

                   
                } else if ("607" == obj.code) {
                    alert("您尚未登录或登录已失效！");
                    userlogout();
                }

                else {
                    alert("提交出错，出错原因：" + obj.message);
                }
            }
        }
    };
}


//保险额度计算
function countpremiummoney_user() {
	
    var rate = premium_rate;
    if ((rate == "0") || (removenull(rate) == null)) {
    	//$source.find("input[name='maxinsurance']").val(0);
        return false;
    }
    rate = parseFloat(rate);
    if (isNaN(rate)) {
        return false;
    }
    
    var $source = $(this).closest(".boxInfo");//change触发
    
    if (arguments.length == 2) {//主动传参,传过一个参数来区分
        $source = arguments[0];
    } else {

    }
 
    
    
    var premium1 = parseFloat($source.find("input[name='insurance']").val());
    if (isNaN(premium1)) {
        return false;
    }
    if (premium1 > 0)
    { }
    else if (premium1 < 0) {
        alert("保险不能小于0!");
        $source.find("input[name='insurance']").val(0);
        $source.find("input[name='maxinsurance']").val(0);
        return false;
    }
    else {
    	  $source.find("input[name='insurance']").val(0);
          $source.find("input[name='maxinsurance']").val(0);
        return false;
    }
    var premiumt = parseFloat(premium1 / rate);
    if (isNaN(premiumt)) {
        return false;
    }
    if (parseFloat(premium_max_value) != "NaN") {
        if (premiumt > parseFloat(premium_max_value)) {
            alert("最大保险额度不能超过" + premium_max_value + "美元!");
            $source.find("input[name='insurance']").val(premium_max_value * rate);
            $source.find("input[name='maxinsurance']").val(premium_max_value);
        }
        else {
        	$source.find("input[name='maxinsurance']").val(premiumt);
        }
    }
    else {
    	$source.find("input[name='maxinsurance']").val(premiumt);
    }

}


function getuserinfo()
{
    //获取用户信息
    $.ajaxEx({
        type: "get",
        url: "/user/get_self",
        success: function (response) {
            var code = response.code;
            console.log("get_self,code:"+code);
            if (code == "200") {
            	//$("#usermoney").html("$"+response.data.usdBalance+",￥"+response.data.rmbBalance);
            	getfreezemoney("$"+response.data.usdBalance+",￥"+response.data.rmbBalance)
            	
              //  setSpanText("#userinfo", response.data);
              //  $("#userinfo span[name='usertype']").html(getusertype(response.data.type));
               
              //  top.window.document.getElementById("username").innerHTML=response.data.realName;
            } else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                userlogout();
            } else {

                alert("获取个人信息失败，原因是:" + response.message);
            }
        }
    });	
}

function getfreezemoney(balance)
{
	 //获取用户冻结的钱数
    $.ajax({
		type : "post",
		url : "/user/get_freezmoney",
		success : function(response) {
			var code = response.code;

			if ("200" == code) {

				if (response.data != null) {
					//setSpanText("#orderQuantity", response.data);
					var str="";
					if(removenull(response.data.usa)=="")
					{
						str+="$0";
					}
					else
					{
						str+="$"+response.data.usa;
					}
					if(removenull(response.data.rmb)=="")
					{
						str+="￥0";
					}
					else
					{
						str+=",￥"+response.data.rmb;
					}
					//$("span[name='freemoney']").html("(冻结金额："+str+")");
					$("#usermoney").html(balance+" (冻结金额："+str+")");

				} else {

				}
			} else {

			}
			
		}
	});	
}

function getcounts() { 
	
	$.ajax({
	type : "post",
	url : "/t_order/get_counts",
	success: function (response) {
	    var code = response.code;

	    if ("200" == code) {

	        if (response.data != null) {
	            setSpanText("#orderQuantity", response.data);
	            //已入库的数量
	            top.window.document.getElementById("t_haveinputQ").innerText = response.data.t_haveinputQ;
	            top.window.document.getElementById("t_totalQ").innerText = response.data.t_totalQ;
	            
	            
	        }
	    }
	}
});

$.ajax({
	type : "post",
	url : "/m_order/get_counts",
	success : function(response) {
		var code = response.code;

		if ("200" == code) {

			if (response.data != null) {
				setSpanText("#orderQuantity", response.data);
				top.window.document.getElementById("tranwaitpQ").innerText = response.data.tranwaitpQ;
				top.window.document.getElementById("totalQ").innerText = response.data.totalQ;
				top.window.document.getElementById("nopayQ").innerText = response.data.nopayQ;

			} else {

			}
		} else {

		}
		
	}
});
}