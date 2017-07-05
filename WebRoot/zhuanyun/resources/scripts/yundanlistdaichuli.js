var default_print_button_id = "";//用于控制回车时选择触发的按钮
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
    printbuttoncontrol1();

    jQuery(yundangrid_selector).jqGrid({
        url: "/admin/m_order/search_wait",
        //url: "testdata/yundanlistdata.html",
        datatype: "json",
        height: "auto",
        mtype: "get",
        postData: postData,
        colNames: ['订单号','发件人', '转运单号 | 仓位','商品','备注',  '收件人', '收件人地址', "创建时间", "操作"],
        colModel: [

            {
                name: 'orderId', index: 'orderId', width: 80,
                formatter: function (cellvalue, options, row) {
                    return '<a href="javascript:void(0)" onclick="showdetail(' + row.id + ')">' + cellvalue + '</a>';
                }
            },
            {
                name: 'suserId', index: 'suserId', width: 100,//发件人信息

                formatter: function (cellvalue, options, row) {
                    if (removenull(row.suser) != "")//发件人用户id不为空
                    {
                        return removenull(row.suser.name) + "/" + removenull(row.suser.phone);
                    }
                    else {
                        return "";
                    }

                }
            },
            { name: 'employeeName', index: 'employeeName', width: 180,formatter:function(cellValue,option,row){
            	var str="";
            	if(row.torders!=null)
            	{
            		
            		$.each(row.torders,function(){
            			
            			if(str=="")
            			{
            				str=this.torderId;
            				if(this.position!=null)
            				{
            					str=str+" | <span style='color:blue'>"+this.position.position+"</span>";
            				}
            				str=str+" <span style='color:red'>("+this.i_weight+" lb)</span>";
            			}
            			else
            			{
            				str=str+"<br/>"+this.torderId;
            				if(this.position!=null)
            				{
            					str=str+" | <span style='color:blue'>"+this.position.position+"</span>";
            				}
            				str=str+" <span style='color:red'>("+this.i_weight+" lb)</span>";
            			}
            		});
            	}
            	return str;
            }},//
              {
                  name: 'detail', index: 'detail', width: 180, formatter: function (cellvalue, options, row) {
                      var text = "";
                      if (row.detail != null) {
                    	  $.each(row.detail,function(cellVall,option,row){
                    		 if(text=="")
                    		 {
                    			 text=this.name+":<span style='color:blue'>"+this.productName+"</span> | "+this.brands+" * <span style='color:red'>"+this.quantity+"</span>&nbsp;"+this.remark;
                    		 }
                    		 else
                    		 {
                    			 text=text+"<br/>"+this.name+":<span style='color:blue'>"+this.productName+"</span> | "+this.brands+" * <span style='color:red'>"+this.quantity+"</span>&nbsp;"+this.remark;
                    		 }
                    	  });
                      }
                      return text;
                  }
              },
              { name: 'remark', index: 'remark', width: 100 },//备注
           
            {
                name: 'ruserId', index: 'ruserId', width: 100,//收件人信息

                formatter: function (cellvalue, options, row) {
                    //alert(row.ruser);
                    if (removenull(row.ruser) != "")//发件人用户id不为空
                    {
                        return removenull(row.ruser.name) + "/" + removenull(row.ruser.phone);
                    }
                    else {
                        return "";
                    }

                }
            },
            {
                name: 'raddress', index: 'ruserId', width: 100,//收件人地址

                formatter: function (cellvalue, options, row) {
                    if (removenull(row.ruser) != "")//发件人用户id不为空
                    {
                        return removenull(row.ruser.state) + removenull(row.ruser.city) + removenull(row.ruser.dist) + removenull(row.ruser.address);
                    }
                    else {
                        return "";
                    }

                }
            },

             { name: 'createDate', index: 'createDate', width: 60 },//创建时间
             {
                 name: 'operation', index: 'operation', width: 60, //align: "center",
                 formatter: function (cellvalue, options, row) {
                     //返回修改按钮
                     var edit = "";
                     //只有已揽收之前的状态或未付款状态可以修改
                     if (row.state < 2 || (row.payornot != "1")) {
                         edit = "<a style='width:60px;margin-left:5px;margin-top:1px;' class='btn btn-danger  btn-sm'  onclick='showEdit(" + row.id + ")' ><span class='icon-edit'></span>修改</a>";
                     }
                     var a4Print = "<a style='width:70px;margin-left:5px;margin-top:1px;' class='btn btn-purple btn-sm'  onclick=\"a4Print(" + row.id + ")\"  ><span class='icon-print'></span>A4打印</a>";
                     var fhot4x6 = "<a style='width:85px;margin-left:5px;margin-top:1px;' class='btn btn-success btn-sm'  onclick=\"hot4x6(" + row.id + ")\"  ><span class='icon-print'></span>热敏(4X6)</a>";
                     var haiguanPrint = "<a style='width:85px;margin-left:5px;margin-top:1px;' class='btn  btn-primary btn-sm'  onclick=\"haiguanPrint(" + row.id + ")\"  ><span class='icon-print'></span> 海关(4X6)</a>";

                     return edit;
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
        rowNum: 10,
        rowList: [10, 30, 50],
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
            if (json.code == "200") {
                if ((removenull(json.data) != "") && removenull(json.data.datas) != "") {
                    jsonData = json.data.datas;
                    for (var i = 0; i < jsonData.length; i++) {
                        var row = jsonData[i];
                        if (row.state >= 2) {
                            hiddenIds.push(row.id);
                        }
                    }
                    hideJgridCheckbox(yundangrid_selector, hiddenIds);
                }
            }
            else if ("607" == json.code) {
                alert("您尚未登录或登录已失效！");
                top.location.href = "../admin/login.html";
            }
            else {
                alert("读取出错，原因：" + json.message);
            }
        },
        gridComplete: function () {
            //隐藏 city=已揽收的 复选框

        },
        onSelectRow: function (rowid, status) {
            //设置没复选框的行不给选中
            if (status == true) {
                var display = $(yundangrid_selector).find("[id=" + rowid + "]").find("[role=checkbox]:hidden");
                if (display.length > 0) { $(yundangrid_selector).setSelection(rowid, false); }
            }

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

                if ((removenull(ids) == "") || (ids.length < 1)) {
                    alert("必须选择要删除的运单!");
                    return false;
                }

                // var aid = new Array(ids);
                //alert(aid);
                //var aaa=ids.split(",");
                //alert(aaa[0]);
                //alert(aaa[1]);
                $.ajax({
                    type: "post",
                    url: "/admin/m_order/delete",
                    data: {
                        "ids": ids
                    },
                    success: function (response) {
                        var code = response.code;
                        if ("200" == code) {
                            alert("删除成功!");
                            //todo 这里补充 和后台交互的代码
                            //删除完成后 在success回调中调用下面方法刷新数据列表
                            refreshDataGrid(postData);
                        }
                        else if ("607" == code) {
                            alert("您尚未登录或登录已失效！");
                            top.location.href = "../admin/login.html";
                        }
                        else {
                            alert("删除失败，原因是：" + response.message);
                        }
                    }
                });




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

    loadShelves();
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

    if (row.payornot == "0") {
        $("span[name='payornotName']").html("未付款");
    }
    else if (row.payornot == "1") {
        $("span[name='payornotName']").html("已付款");
    }
    else {
        $("span[name='payornotName']").html("状态异常");
    }
    dialog({
        title: "运单详细信息",
        content: $("#divDetail").html(),
        width: $(window).width() - 60,
        padding: "20px",
        fixed: false,
        zIndex: 9999,
        // height: $("#divDetail").height() + 160,
        //cancelValue: '关闭',
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

//================================打印相关============================
var LODOP;
$(function () {
    setTimeout(function () {
        LODOP = getLodop();
    }, 2000);
});

function getMyLodop() {
    if (LODOP == undefined) {
        LODOP = getLodop();
    }
}

// printMode参数
//“Full-Width” –宽度按纸张的整宽缩放；
//“Full-Height”–高度按纸张的整高缩放：
//“Full-Page” –按整页缩放，也就是既按整宽又按整高缩放；
//此外还可以按具体百分比例，格式为“Width:XX%;Height:XX%”或“XX%”

function beginPrint(iframeid, printMode) {
    getMyLodop();
    var win = {};
    win = document.getElementById(iframeid).contentWindow;

    if (win.printornot == false)//不进行打印
    {
        alert(win.printornot);
        return false;
    }

    var isNotPrint = true;

    //if (printMode == undefined) { printMode = "100%" }
    if (printMode == undefined) { printMode = "Full-Page"; }
    var number = 0;
    var t = setInterval(function () {
        if (isNotPrint && win.isLoad) {
            try {
                if (LODOP == undefined) {
                    isNotPrint = false;
                    win.print();
                    clearInterval(t);
                } else {
                    isNotPrint = false;
                    LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT", printMode);
                    ////ADD_PRINT_HTM(Top,Left,Width,Height,strHtmlContent)
                    LODOP.ADD_PRINT_HTM("0.5cm", "0.5cm", "RightMargin:0.5cm", "BottomMargin:0.5cm", win.document.documentElement.innerHTML);
                    // LODOP.PREVIEW();//预览打印
                    LODOP.PRINT();//直接打印
                    clearInterval(t);
                }
            } catch (e) {
                console.log(e.message);
                isNotPrint = true;
            }
            //clearInterval(t);
        }

        if (number > 20) {
            clearInterval(t);
            alert("时间过长，打印失败！");
            return false;
        }
        number++;
    }, 300);
}

function a4Print(id) {
    if (confirm("是否确定打印A4纸？")) { } else { return false; }
    var iframeid = "iframePrint1";
    document.getElementById(iframeid).contentWindow.document.write("");
    $("#" + iframeid).attr("src", "printpage.html?id=" + id + "&ran=" + Math.random());
    beginPrint(iframeid);

}


function hot4x6(id) {
    if (confirm("是否确定打印热敏4x6纸？")) { } else { return false; }
    var iframeid = "iframePrint3";
    document.getElementById(iframeid).contentWindow.document.write("");
    $("#" + iframeid).attr("src", "printpage64.html?id=" + id + "&ran=" + Math.random());
    beginPrint(iframeid);
}

function haiguanPrint(id) {
    if (confirm("是否确定打印海关单？")) { } else { return false; }
    var iframeid = "iframePrint2";
    document.getElementById(iframeid).contentWindow.document.write("");
    $("#" + iframeid).attr("src", "printpageremin.html?id=" + id + "&ran=" + Math.random());
    // beginPrint(iframeid, "Full-Page");
    beginPrint(iframeid, "Full-Page");
    alert("打印成功！");
    /* $("#" + iframeid).load(function(){
        // alert("54545454");
         beginPrint(iframeid, "Full-Width");
       });*/
}
var storelist_jason = {};
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
                   // $("select[name='wid']").html(str);
                    $("select[name='uploadwid']").html(str);
                    $("select[name='wid']").html(str);
                    $("select[name='modifystorelist']").html(str);

                    showChannelTablesearchwid();
                    showChannelTableupload();


                    $("select[name='modifystorelist']").change(loadChannelSelectOption);
                    $("select[name='uploadwid']").change(showChannelTableupload);
                    $("select[name='wid']").change(showChannelTablesearchwid);



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

//搜索的渠道选择
function showChannelTablesearchwid() {
    var wid = $("select[name='wid']").val();
    if (wid == "-1") {
        $("select[name='cid']").html("<option value='-1'>请选择渠道</option>");
        return "";
    }

    //alert(wid);

    //获取所有的渠道，目的是防止后来关闭的渠道查找不到
    $.ajax({
        type: "post",
        url: "/admin/channel/getall_unlimit",
        /*
        data: {
            "wid": wid
        },*/
        success: function (response) {
            var code = response.code;
            if ("200" == code) {
                if (response.data == null) {
                    //alert("门店中没有该运单的渠道，请联系系统管理员！");
                } else {
                    var str = "<option value='-1'>请选择渠道</option>";
                    $.each(response.data, function () {
                        str += '<option value="' + this.id + '" additiveprice="' + this.additivePrice + '">' + this.name
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

//上传文件的渠道选择，此渠道必须是授权的
function showChannelTableupload() {
    var wid = $("select[name='uploadwid']").val();
    if (wid == "-1") {
        $("select[name='uploadcid']").html("<option value='-1'>请选择渠道</option>");
        return "";
    }

    //alert(wid);


    $.ajax({
        type: "post",
        url: "/admin/channel/get_by_store_available",
        data: {
            "wid": wid
        },
        success: function (response) {
            var code = response.code;
            if ("200" == code) {
                if (response.data == null) {
                    //alert("门店中没有该运单的渠道，请联系系统管理员！");
                } else {
                    var str = "<option value='-1'>请选择渠道</option>";
                    $.each(response.data, function () {
                        str += '<option value="' + this.id + '" additiveprice="' + this.additivePrice + '">' + this.name
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

var editDialog = {};
function showEdit(id) {



    $(":checkbox[name='payway']").bootstrapSwitch('destroy');
    var row = jsonData.getData(id);
    $("select[name='modifystorelist']").val(row.storeId);

    loadChannelSelectOption_edit(row.storeId, id);
    return false;
    $("#divEdit [name=orderId]").text(row.orderId);
    //var typeName = "";
    //if (row.type == 1) {
    //   typeName = "系统订单";
    // }
    $("#divEdit [name=type]").text(mordertypemap(row.type));
    $("#divEdit [name=curstate]").text(morderstatemap(row.state));
    if (row.payornot == "0") {
        $("#divEdit [name=curpayornot]").text("未付款");
        $("#divEdit select[name=newpayornot]").val("0");
    }
    else if (row.payornot == "1") {
        $("#divEdit [name=curpayornot]").text("已付款");
        $("#divEdit select[name=newpayornot]").val("1");
    }
    else {
        $("#divEdit [name=curpayornot]").text("状态异常");
        $("#divEdit select[name=newpayornot]").val("-1");
    }





    setInputData("#divEdit #senderTableInfo", row.suser);
    setInputData("#divEdit #receiverInfoTable", row.ruser);
    setImgSrc("#divEdit #receiverInfoTable", row.ruser);//添加身份证图片

    $("#divEdit #senderTableInfo :checkbox[name='payway']").removeAttr("checked");
    if (row.user != "") {
        var str = row.user.realName + "/" + row.user.phone + "/" + row.user.id;
        $("#divEdit [name=orderIduserinfo]").text(str);
        $("#divEdit #senderTableInfo").find("input[name='id']").val(row.user.id);
        str = "$" + removenull(row.user.usdBalance) + "/￥" + removenull(row.user.rmbBalance);
        $("#divEdit #senderTableInfo").find("input[name='usermoney']").val(str);
    }
    else {
        $("#divEdit [name=orderIduserinfo]").text("");
        $("#divEdit #senderTableInfo").find("input[name='usermoney']").val("");
        $("#divEdit #senderTableInfo").find("input[name='id']").val("");
    }

    // setSpanText("#receiverInfoTable", row.ruser);

    $("#divEdit #goodsItemBody").html("");
    for (var i = 0; i < row.detail.length; i++) {
        var teplament = $("#divEdit #goodsItemTeplamentEdit").html();
        $("#divEdit #goodsItemBody").append("<tr id='itemTr" + i + "'>" + teplament + "</tr>");
        setInputData("#divEdit #itemTr" + i, row.detail[i]);
        $("#divEdit #itemTr" + i + " [name=commodityName]").val(row.detail[i].ctype);
    }

    setInputData("#divEdit #yundanTableInfo", row);

    editDialog = dialog({
        zIndex: 99999,
        fixed: false,
        padding: "10px",
        title: "修改运单",
        //url: "yundanedit.html?id=" + id,
        content: document.getElementById("divEdit"),
        width: $(window).width() - 60,
        height: $("body").height() - 55,//默认为auto

    });
    editDialog.show();

}


//装载渠道选择
function loadChannelSelectOption() {
    var wid = $("select[name='modifystorelist']").val();
    if ((wid == "-1") || removenull(wid) == "") {
        $("select[name='channellist']").html("请选渠道");
        return false;
    }
    $.ajax({
        post: "get",
        url: "/admin/channel/get_by_store_available",
        data: { "wid": wid },
        success: function (response) {
            var code = response.code;
            if ("200" == code) {

                // showCommodityList(response.data);;
                var str = "";
                $.each(response.data, function () {
                    str += "<option value='" + this.id
							+ "' additivePrice='" + this.additivePrice + "'>"
							+ this.name + "</option>";
                });
                $("select[name='channellist']").html(str);
                $("select[name='channellist']").change(loadCommoditySelectOption);


                //要装载历史运单的渠道查找信息
                var strh = "<option value='-1'>请选择渠道</option>" + str;
                $("select[name='history_cid']").html(strh);

                loadCommoditySelectOption();
            } else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                adminlogout();
            }
        }
    });
}
//装载渠道选择
function loadChannelSelectOption_edit(wid, id) {

    $.ajax({

        type: "post",
        url: "/admin/channel/get_by_store_available",
        data: { "wid": wid },
        success: function (response) {
            var code = response.code;
            if ("200" == code) {

                // showCommodityList(response.data);;
                var str = "";
                $.each(response.data, function () {
                    str += "<option value='" + this.id
					+ "' additivePrice='" + this.additivePrice + "'>"
					+ this.name + "</option>";
                });
                $("select[name='channellist']").html(str);
                $("select[name='channellist']").change(loadCommoditySelectOption);
                var row = jsonData.getData(id);
                $("select[name='channellist']").val(row.channelId);
                loadCommoditySelectOption_endit(id);
            } else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                adminlogout();
            }
        }
    });
}

function showEdit_end(id) {

    $("#divEdit :hidden[name='editorder_id']").val(id);

    $(":checkbox[name='payway']").bootstrapSwitch('destroy');
    
   
   
    $("#divEdit #yundanTableInfo input[name='tmoney']").val("0");
   

    

    //$(":checkbox[name='sendmessage']").bootstrapSwitch('setState', false);

    //$(":checkbox[name='sendmessage']").removeAttr("checked");

    var row = jsonData.getData(id);
    //$("select[name='modifystorelist']").val(row.storeId);
    // loadChannelSelectOption_edit(row.storeId);
    $("#divEdit [name=orderId]").text(row.orderId);
    $("#divEdit [name=e_totalmoney]").text(row.totalmoney);

    if(row.torders!=null)
    {
    	
		var wstr="";
		var tstr="";
		$.each(row.torders,function(){
			if(this.position!=null)
	    	{
				if(wstr=="")
				{
					wstr=this.position.position;
				}
				else
				{
					wstr=wstr+" | "+this.position.position;
				}
	    		
	    	}
			
			if(tstr=="")
			{
				tstr=this.torderId+"("+this.i_weight+" lb)";
			}
			else
			{
				tstr=tstr+" | "+this.torderId+"("+this.i_weight+" lb)";;
			}
	    	
		});
		$("span[name='w_position']").html(wstr);
		$("span[name='tran_torders']").html(tstr);
    	
    }
    
    
    //var typeName = "";
    //if (row.type == 1) {
    //   typeName = "系统订单";
    // }
    $("#divEdit [name=type]").text(mordertypemap(row.type));
    $("#divEdit [name=curstate]").text(morderstatemap(row.state));
    if (row.payornot == "0") {
        $("#divEdit [name=curpayornot]").text("未付款");
        $("#divEdit select[name=newpayornot]").val("0");
    }
    else if (row.payornot == "1") {
        $("#divEdit [name=curpayornot]").text("已付款");
        $("#divEdit select[name=newpayornot]").val("1");
    }
    else {
        $("#divEdit [name=curpayornot]").text("状态异常");
        $("#divEdit select[name=newpayornot]").val("-1");
    }

    if (row.state == "-10") {
        $("#divEdit [name=newstate]").val("2");
    }
    else {
        $("#divEdit [name=newstate]").val(row.state);
    }


    setInputData("#divEdit #senderTableInfo", row.suser);
    setInputData("#divEdit #receiverInfoTable", row.ruser);
    setInputData("#divEdit #receiverIdTable", row.ruser);
    setImgSrc("#divEdit #receiverIdTable", row.ruser);//添加身份证图片


    $("#divEdit #senderTableInfo :checkbox[name='payway']").removeAttr("checked");
    if (row.user != null) {
        var str = row.user.realName + "/" + row.user.phone + "/" + row.user.id;
        $("#divEdit [name=orderIduserinfo]").text(str);
        $("#divEdit #senderTableInfo").find("input[name='id']").val(row.user.id);
        str = "$" + removenull(row.user.usdBalance) + "/￥" + removenull(row.user.rmbBalance);
        $("#divEdit #senderTableInfo").find("input[name='usermoney']").val(str);
    }
    else {
        $("#divEdit [name=orderIduserinfo]").text("");
        $("#divEdit #senderTableInfo").find("input[name='usermoney']").val("");
        $("#divEdit #senderTableInfo").find("input[name='id']").val("");
    }

    // setSpanText("#receiverInfoTable", row.ruser);

    $("#divEdit #goodsItemBody").html("");
    for (var i = 0; i < row.detail.length; i++) {
        var teplament = $("#divEdit #goodsItemTeplamentEdit").html();
        $("#divEdit #goodsItemBody").append("<tr id='itemTr" + i + "'>" + teplament + "</tr>");
        
        if(row.detail[i]!=null)
        {
        	if(removenull(row.detail[i].tariff)=="")
        	{
        		row.detail[i].tariff=0;
        	}
        	if(removenull(row.detail[i].other)=="")
        	{
        		row.detail[i].other=0;
        	}
        }
        
        setInputData("#divEdit #itemTr" + i, row.detail[i]);
        var commodityId = row.detail[i].commodityId;
        // $("#divEdit #itemTr" + i).find("select[name='commodityName']").val(commodityId);
        // $("#divEdit #itemTr" +i+" [name=commodityName]").val(row.detail[i].ctype);
        $("#divEdit #itemTr" + i + " [name=commodityName]").val(commodityId);
    }

    setInputData("#divEdit #yundanTableInfo", row);
    $("#divEdit #yundanTableInfo [name='totalremark']").val(row.remark);


    $(":text[name='quantity']").unbind();
    $(":text[name='weight']").unbind();
    $(":text[name='value']").unbind();
    $(":text[name='tariff']").unbind();
    $(":text[name='other']").unbind();

    $(":text[name='quantity']").change(commuditydataChange_edit);
    $(":text[name='weight']").change(commuditydataChange_edit);
    $(":text[name='value']").change(commuditydataChange_edit);
    $(":text[name='tariff']").change(commuditydataChange_edit);
    $(":text[name='other']").change(commuditydataChange_edit);

    if(removenull(row.tmoney)=="")
    {
    	$("#divEdit #yundanTableInfo input[name='tmoney']").val("0");
    }
    else
    {
    	$("#divEdit #yundanTableInfo input[name='tmoney']").val(row.tmoney);
    }


    //账号余额
    
    if(row.user!=null)
    {
    	var str00="$"+row.user.usdBalance+"/￥"+row.user.rmbBalance;
    	$("span[name='user_account_money']").html(str00);
    }
    //当前运单相关的冻结金额
    if(row.freezeMoney!=null)
    {
    	var str00="$"+row.freezeMoney.usa+"/￥"+row.freezeMoney.rmb;
    	$("span[name='freeze_account_money']").html(str00);
    }
    
    editDialog = dialog({
        zIndex: 99999,
        fixed: false,
        padding: "5px",
        title: "修改运单",
        //url: "yundanedit.html?id=" + id,
        content: document.getElementById("divEdit"),
        width: $(window).width() - 5,
        //   height: $("body").height() - 55,//默认为auto


    });
    editDialog.show();
    countpremiummoney_user();
    var flag = 0;
    if (removenull(row.ruser.cardid) == "") {
    }
    else {
        flag = 1;
    }
    if (removenull(row.ruser.cardtogether) == "") {
    }
    else {
        flag = 1;
    }

    if (removenull(row.ruser.cardurl) == "") {
    }
    else {
        flag = 1;
    }

    if (removenull(row.ruser.cardother) == "") {
    }
    else {
        flag = 1;
    }

    if (flag == 1)//包含身份证信息，展开
    {
        var showblock = $("#divEdit #receiverIdTable").attr("style");
        if (showblock == "display: block;" || showblock == "")//
        {
            //$("#cardinfoshow").click();
        }
        else//原来是隐藏的，要展示出来
        {
            $("#divEdit #cardinfoshow").click();
        }
    }

}


//装载商品信息
function loadCommoditySelectOption_endit(id) {

    $("select[name='commodityName']").html("");
    var wid = $("select[name='modifystorelist']").val();
    var cid = $("select[name='channellist']").val();
    var additivePrice = $("select[name='channellist']").find("option:selected").attr("additivePrice");
    $(":text[name='addtivePrice']").val(additivePrice);
    $.ajax({
        post: "get",
        url: "/admin/commudityPrice/getpricebycidandwid",
        data: {
            "wid": wid,
            "cid": cid
        },
        success: function (response) {
            var code = response.code;
            if ("200" == code) {

                // showCommodityList(response.data);;
                var str = "";
                $.each(response.data, function () {
                    str += "<option value='" + this.id + "'>"
							+ this.commudityAdmin.name + "</option>";
                });
                $("select[name='commodityName']").html(str);


                showEdit_end(id);
                //msOrderCalcFreight();
            } else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                adminlogout();
            }
        }
    });
}

//装载商品信息
function loadCommoditySelectOption() {

    $("select[name='commodityName']").html("");
    var wid = $("select[name='modifystorelist']").val();
    var cid = $("select[name='channellist']").val();
    if (wid == "-1" || (cid == "-1")) {
        return false;
    }
    var additivePrice = $("select[name='channellist']").find("option:selected").attr("additivePrice");
    $(":text[name='addtivePrice']").val(additivePrice);
    $.ajax({
        post: "get",
        url: "/admin/commudityPrice/getpricebycidandwid",
        data: {
            "wid": wid,
            "cid": cid
        },
        success: function (response) {
            var code = response.code;
            if ("200" == code) {

                // showCommodityList(response.data);;
                var str = "";
                $.each(response.data, function () {
                    str += "<option value='" + this.id + "'>"
							+ this.commudityAdmin.name + "</option>";
                });
                $("select[name='commodityName']").html(str);
                //msOrderCalcFreight();
            } else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                adminlogout();
            }
        }
    });
}
function getStorelistbyId() {

}

function savemodifyinfo() {
    var id = $("#divEdit :hidden[name='editorder_id']").val();
    saveOneMorder("0", id);
}

function payOrderinfo() {

    var id = $("#divEdit :hidden[name='editorder_id']").val();
    saveOneMorder("1", id);
}
//pay=1表示支付运单，其它表示保存运单，id表示当前所在行
function saveOneMorder(pay, id) {

    var row = jsonData.getData(id);
    if (removenull(row) == "") {
        alert("参数出错，提交失败！");
        return false;
    }
    var formData = new FormData();
    var payornot = $("#divEdit select[name='newpayornot']").val();
    var newstate = $("#divEdit select[name='newstate']").val();
    formData.append("payornot", payornot);//保存运单号id
    formData.append("state", newstate);//保存运单号id
    if (pay == 1) {
        if (row.payornot == "1") {
            alert("已经支付的运单不能再次支付");
            return false;
        }


        if (payornot != "1") {
            alert("提交支付必须选择支付状态为\"已付款\"");
            return false;
        }


        if (newstate == "0") {
            alert("状态为\"运单作废\"的不能进行支付");
            return false;
        }
        //formData.append("payflag", "1");//设为运单支付方式
    }
    else {
        //formData.append("payflag", "0");//设为保存运单标志
    }
    formData.append("id", id);//保存运单号id
    commuditydataChange_edit();//在提交之前，还是要算一下，要不回车的话，可能会出错

    var validate = true;


    var printway = "0";




    formData.append("printway", printway);
    //构造发件人数据
   /* var name = $("#divEdit #senderTableInfo").find(":text[name='name']").val();
    var phone = $("#divEdit #senderTableInfo").find(":text[name='phone']").val();
    var address = $("#divEdit #senderTableInfo").find(":text[name='address']").val();
    var city = $("#divEdit #senderTableInfo").find(":text[name='city']").val();
    var state = $("#divEdit #senderTableInfo").find(":text[name='state']").val();
    var zipcode = $("#divEdit #senderTableInfo").find(":text[name='zipcode']").val();
    var email = $("#divEdit #senderTableInfo").find(":text[name='email']").val();
    var company = $("#divEdit #senderTableInfo").find(":text[name='company']").val();*/


    //选择支付方式0是余额支付，1是现金支付
    var payway1 = $("#divEdit #senderTableInfo").find(":checkbox[name='payway']").is(":checked");
    var userId = $("#divEdit #senderTableInfo").find(":text[name='id']").val();
    var payway = 0;
    if (payway1 == true)//余额付款
    {
        if (row.user == null) {
            alert("运单不属于会员，不能余额付款！");
            return false;
        }

        /*payway=0;//余额支付要确认用户存在
		if(removenull(userId)=="")
		{
			alert("只有选择会员才能够进行余额支付!");
			return false;
		}*/
    }
    else {
        payway = 1;
    }

    formData.append("payWay", payway);//0表示余额支付，1表示现金支付
    formData.append("userId", userId);//当选择会员时，此用于标识归属的id


    /*if (name == "" || name == null) {
        $("#divEdit #senderTableInfo").find(":text[name='name']").css({
            "border-color": "red"
        });
        $("#divEdit #senderTableInfo").find(":text[name='name']").change(
				function () {
				    if ($("#divEdit #senderTableInfo").find(":text[name='name']")
							.val() != null)
				        $("#divEdit #senderTableInfo").find(":text[name='name']")
								.css({
								    "border-color": ""
								});
				});
        validate = false;
    }

    if (phone == "" || phone == null) {
        $("#divEdit #senderTableInfo").find(":text[name='phone']").css({
            "border-color": "red"
        });
        $("#divEdit #senderTableInfo").find(":text[name='phone']").change(
				function () {
				    if ($("#divEdit #senderTableInfo").find(":text[name='phone']")
							.val() != null)
				        $("#divEdit #senderTableInfo").find(":text[name='phone']")
								.css({
								    "border-color": ""
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
    formData.append("suser.company", company);*/


    //构造收件人数据
    var name = $("#divEdit #receiverInfoTable").find(":text[name='name']").val();
    var phone = $("#divEdit #receiverInfoTable").find(":text[name='phone']").val();
    var address = $("#divEdit #receiverInfoTable").find(":text[name='address']").val();
    var dist = $("#divEdit #receiverInfoTable").find(":text[name='dist']").val();
    var city = $("#divEdit #receiverInfoTable").find(":text[name='city']").val();
    var state = $("#divEdit #receiverInfoTable").find(":text[name='state']").val();
    var zipcode = $("#divEdit #receiverInfoTable").find(":text[name='zipcode']").val();
    var cardid = $("#divEdit #receiverIdTable").find(":text[name='cardid']").val();//身份证号
    var hcimage = $("#divEdit #receiverIdTable").find("img[id='hcimage1']").attr("src");//身份证合成图
    var zmimage = $("#divEdit #receiverIdTable").find("img[id='zmimage1']").attr("src");//身份证正面图
    var fmimage = $("#divEdit #receiverIdTable").find("img[id='fmimage1']").attr("src");//身份证反面图
    var cardurlfile = "";
    var cardurlotherfile = "";
    var cardurltogetherfile = "";

    var filetype = $("#divEdit :radio[name='shenfenzhenradio']:checked").val();
    //$(":radio[name='userId']:checked").val()

    if (filetype == "hc")//合成图文件
    {

        cardurltogetherfile = $("#divEdit :file[name='hcpicture']").val();


        $("#divEdit :file[name='zmpicture']").val("");//正面图
        $("#divEdit :file[name='fmpicture']").val("");//反面图

        if ((removenull(cardurltogetherfile) != "")) {
            if (removenull(cardid) == "") {


                $("#divEdit #receiverIdTable").find(":text[name='cardid']").css({
                    "border-color": "red"
                });
                $("#divEdit #receiverIdTable").find(":text[name='cardid']").change(
						function () {
						    if ($("#divEdit #receiverIdTable").find(":text[name='cardid']")
									.val() != null)
						        $("#divEdit #receiverIdTable").find(":text[name='cardid']")
										.css({
										    "border-color": ""
										});
						});
                validate = false;



                alert("上传身份证必须同时上传身份证号!");
                return false;
            }
            var picture = document.getElementById("hcpicture");
            //alert(picture.value);
            //return false;
            if (picture == "" || picture == null)
            { }
            else
            {
                formData.append("cardurltogetherfile", picture.files[0]);
            }
        }



    }
    else if (filetype == "zf")//上传正反两面图
    {
        $("#divEdit :file[name='hcpicture']").val("");
        cardurlfile = $("#divEdit :file[name='zmpicture']").val();//正面图
        cardurlotherfile = $("#divEdit :file[name='fmpicture']").val();//反面图
        if ((removenull(cardurlfile) == "") && (removenull(cardurlotherfile) != "") && (removenull(zmimage) == "")) {
            alert("必须同时上传正反两面身份证图片!");
            return false;
        }
        if ((removenull(cardurlotherfile) == "") && (removenull(cardurlfile) != "") && (removenull(fmimage) == "")) {
            alert("必须同时上传正反两面身份证图片!");
            return false;
        }
        if ((removenull(cardurlotherfile) != "") || (removenull(cardurlfile) != "")) {
            if (removenull(cardid) == "") {


                $("#receiverIdTable").find(":text[name='cardid']").css({
                    "border-color": "red"
                });
                $("#receiverIdTable").find(":text[name='cardid']").change(
						function () {
						    if ($("#receiverIdTable").find(":text[name='cardid']")
									.val() != null)
						        $("#receiverIdTable").find(":text[name='cardid']")
										.css({
										    "border-color": ""
										});
						});
                validate = false;



                alert("上传身份证必须同时上传身份证号!");
                return false;
            }

        }
        var picture = document.getElementById("zmpicture");
        if (picture == "" || picture == null)
        { }
        else
        {
            formData.append("cardurlfile", picture.files[0]);
        }

        picture = document.getElementById("fmpicture");
        if (picture == "" || picture == null) { }
        else {
            formData.append("cardurlotherfile", picture.files[0]);
        }
    }

    if (phone == "" || phone == null) {
        $("#divEdit #receiverInfoTable").find(":text[name='phone']").css({
            "border-color": "red"
        });
        $("#divEdit #receiverInfoTable").find(":text[name='phone']").change(
				function () {
				    if ($("#divEdit #receiverInfoTable").find(":text[name='phone']")
							.val() != null)
				        $("#divEdit #receiverInfoTable").find(":text[name='phone']")
								.css({
								    "border-color": ""
								});
				});
        validate = false;
    }

    if (name == "" || name == null) {
        $("#divEdit #receiverInfoTable").find(":text[name='name']").css({
            "border-color": "red"
        });
        $("#divEdit #receiverInfoTable").find(":text[name='name']").change(
				function () {
				    if ($("#divEdit #receiverInfoTable").find(":text[name='name']")
							.val() != null)
				        $("#divEdit #receiverInfoTable").find(":text[name='name']")
								.css({
								    "border-color": ""
								});
				});
        validate = false;
    }


    if (state == "" || state == null) {
        $("#divEdit #receiverInfoTable").find(":text[name='state']").css({
            "border-color": "red"
        });
        $("#divEdit #receiverInfoTable").find(":text[name='state']").change(
				function () {
				    if ($("#divEdit #receiverInfoTable").find(":text[name='state']")
							.val() != null)
				        $("#divEdit #receiverInfoTable").find(":text[name='state']")
								.css({
								    "border-color": ""
								});
				});
        validate = false;
    }
    if (city == "" || city == null) {
        $("#divEdit #receiverInfoTable").find(":text[name='city']").css({
            "border-color": "red"
        });
        $("#divEdit #receiverInfoTable").find(":text[name='city']").change(
				function () {
				    if ($("#divEdit #receiverInfoTable").find(":text[name='city']")
							.val() != null)
				        $("#divEdit #receiverInfoTable").find(":text[name='city']")
								.css({
								    "border-color": ""
								});
				});
        validate = false;
    }

    if (dist == "" || dist == null) {
        $("#divEdit #receiverInfoTable").find(":text[name='dist']").css({
            "border-color": "red"
        });
        $("#divEdit #receiverInfoTable").find(":text[name='dist']").change(
				function () {
				    if ($("#divEdit #receiverInfoTable").find(":text[name='dist']")
							.val() != null)
				        $("#divEdit #receiverInfoTable").find(":text[name='dist']")
								.css({
								    "border-color": ""
								});
				});
        validate = false;
    }


    if (address == "" || address == null) {
        $("#divEdit #receiverInfoTable").find(":text[name='address']").css({
            "border-color": "red"
        });
        $("#divEdit #receiverInfoTable").find(":text[name='address']").change(
				function () {
				    if ($("#divEdit #receiverInfoTable").find(":text[name='address']")
							.val() != null)
				        $("#divEdit #receiverInfoTable").find(":text[name='address']")
								.css({
								    "border-color": ""
								});
				});
        validate = false;
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



    //开始构造商品类信息
    var flag = -1;
    var ii = 0;
    $("#divEdit select[name='commodityName']").each(
			function () {

			    if (flag >= 0)//第一行为隐藏行的添加模板，不计入商品信息
			    {


			        var commodityid = $(this).val();//获取商品id
			        var commodityname = $(this).find("option:selected").text();//获取商品名称
			        var cproductname = $(this).parent().parent().find(":text[name='productName']").val();//获取品名
			        var cbrandname = $(this).parent().parent().find(":text[name='brands']").val();//获取品牌
			        var cquantity = $(this).parent().parent().find(":text[name='quantity']").val();//获取数量
			        var cweight = $(this).parent().parent().find(":text[name='weight']").val();//获取重量
			        var cvalue = $(this).parent().parent().find(":text[name='value']").val();//获取价值
			        var ctariff = $(this).parent().parent().find(":text[name='tariff']").val();//获取价值
			        var cother = $(this).parent().parent().find(":text[name='other']").val();//获取其它费用
			        var cremark = $(this).parent().parent().find(":text[name='remark']").val();//获取备注

			        if ((removenull(cproductname) == "") && (removenull(cbrandname) == "") && (removenull(cquantity) == "") && (removenull(cweight) == "") && (removenull(cvalue) == ""))
			        { }
			        else
			        {
			            if (cproductname == "" || cproductname == null) {
			                $(this).parent().parent().find(":text[name='productName']").css({
			                    "border-color": "red"
			                });
			                $(this).parent().parent().find(":text[name='productName']").change(
                                    function () {
                                        if ($(this).parent().parent().find(":text[name='productName']")
                                                .val() != null)
                                            $(this).parent().parent().find(":text[name='productName']")
                                                    .css({
                                                        "border-color": ""
                                                    });
                                    });
			                validate = false;
			            }
			            if (cbrandname == "" || cbrandname == null) {
			                $(this).parent().parent().find(":text[name='brands']").css({
			                    "border-color": "red"
			                });
			                $(this).parent().parent().find(":text[name='brands']").change(
                                    function () {
                                        if ($(this).parent().parent().find(":text[name='brands']")
                                                .val() != null)
                                            $(this).parent().parent().find(":text[name='brands']")
                                                    .css({
                                                        "border-color": ""
                                                    });
                                    });
			                validate = false;
			            }

			            if (cquantity == "" || cquantity == null || isNaN(parseFloat(cquantity)) || (parseFloat(cquantity) <= 0) || (isInteger(cquantity) == false)) {
			                $(this).parent().parent().find(":text[name='quantity']").css({
			                    "border-color": "red"
			                });
			                $(this).parent().parent().find(":text[name='quantity']").change(
                                    function () {
                                        if ($(this).parent().parent().find(":text[name='quantity']")
                                                .val() != null)
                                            $(this).parent().parent().find(":text[name='quantity']")
                                                    .css({
                                                        "border-color": ""
                                                    });
                                    });
			                validate = false;
			            }

			            if (cweight == "" || cweight == null || isNaN(parseFloat(cweight)) || (parseFloat(cweight) <= 0)) {
			                $(this).parent().parent().find(":text[name='weight']").css({
			                    "border-color": "red"
			                });
			                $(this).parent().parent().find(":text[name='weight']").change(
                                    function () {
                                        if ($(this).parent().parent().find(":text[name='weight']")
                                                .val() != null)
                                            $(this).parent().parent().find(":text[name='weight']")
                                                    .css({
                                                        "border-color": ""
                                                    });
                                    });
			                validate = false;
			            }

			            if (cvalue == "" || cvalue == null || isNaN(parseFloat(cvalue)) || (parseFloat(cvalue) <= 0)) {
			                $(this).parent().parent().find(":text[name='value']").css({
			                    "border-color": "red"
			                });
			                $(this).parent().parent().find(":text[name='value']").change(
                                    function () {
                                        if ($(this).parent().parent().find(":text[name='value']")
                                                .val() != null)
                                            $(this).parent().parent().find(":text[name='value']")
                                                    .css({
                                                        "border-color": ""
                                                    });
                                    });
			                validate = false;
			            }

			            if (ctariff == "" || ctariff == null || isNaN(parseFloat(ctariff)) || (parseFloat(ctariff) < 0)) {
			                $(this).parent().parent().find(":text[name='tariff']").css({
			                    "border-color": "red"
			                });
			                $(this).parent().parent().find(":text[name='tariff']").change(
                                    function () {
                                        if ($(this).parent().parent().find(":text[name='tariff']")
                                                .val() != null)
                                            $(this).parent().parent().find(":text[name='tariff']")
                                                    .css({
                                                        "border-color": ""
                                                    });
                                    });
			                validate = false;
			            }
			            if (cother == "" || cother == null || isNaN(parseFloat(cother)) || (parseFloat(cother) < 0)) {
			                $(this).parent().parent().find(":text[name='other']").css({
			                    "border-color": "red"
			                });
			                $(this).parent().parent().find(":text[name='other']").change(
                                    function () {
                                        if ($(this).parent().parent().find(":text[name='other']")
                                                .val() != null)
                                            $(this).parent().parent().find(":text[name='other']")
                                                    .css({
                                                        "border-color": ""
                                                    });
                                    });
			                validate = false;
			            }
			            formData.append("detail[" + ii + "].commodityId", commodityid);
			            formData.append("detail[" + ii + "].name", commodityname);
			            formData.append("detail[" + ii + "].productName", cproductname);
			            formData.append("detail[" + ii + "].brands", cbrandname);
			            formData.append("detail[" + ii + "].quantity", cquantity);
			            formData.append("detail[" + ii + "].weight", cweight);
			            formData.append("detail[" + ii + "].value", cvalue);
			            formData.append("detail[" + ii + "].tariff", ctariff);
			            formData.append("detail[" + ii + "].other", cother);
			            formData.append("detail[" + ii + "].remark", cremark);
			            ii = ii + 1;
			        }

			    }

			    flag = flag + 1;


			});


    if (ii == 0) {
        alert("至少录入一条商品信息!");
        return false;
    }
    //获取参数
    var weight = $("#divEdit #yundanTableInfo :text[name='weight']").val();//计费总重量
    formData.append("weight", weight);
    if (parseFloat(weight) <= 0) {
        alert('\"重量\"输入错误！');
        return false;
    }

    if (isNaN(parseFloat(weight))) {
        alert('\"重量\"输入错误！');
        return false;
    }

    var allsjweight = $("#divEdit #yundanTableInfo :text[name='sjweight']").val();//实际总重量
    formData.append("sjweight", allsjweight);

    var allquantity = $("#divEdit #yundanTableInfo :text[name='quantity']").val();//数量
    formData.append("quantity", allquantity);
    
    var tmoney=$("#divEdit #yundanTableInfo input[name='tmoney']").val();//转运费用
    formData.append("tmoney", tmoney);
    
    if (parseFloat(tmoney) < 0||isNaN(parseFloat(tmoney))) {
        alert('\"转运费用\"输入错误！');
        return false;
    }
    var allquantity = $("#divEdit #yundanTableInfo :text[name='quantity']").val();//价值
    if (parseFloat(allquantity)< 0||isNaN(parseFloat(allquantity)) ) {
        alert('\"数量\"输入错误！');
        return false;
    }
    if (isNaN(parseFloat(allquantity))||parseFloat(allquantity)<1) {
        alert('\"数量\"输入错误！');
        return false;
    }
    var allcvalue = $("#divEdit #yundanTableInfo :text[name='value']").val();//价值
    formData.append("value", allcvalue);
    if (isNaN(parseFloat(allcvalue)) || parseFloat(allcvalue)< 0) {
        alert('\"价值\"输入错误！');
        return false;
    }
    if (isNaN(parseFloat(allcvalue))) {
        alert('\"价值\"输入错误！');
        return false;
    }

    var insurance = $(":text[name='insurance']").val();//保险
    formData.append("insurance", insurance);

    if (parseFloat(insurance) < 0) {
        alert('\"保险\"输入错误！');
        return false;
    }
    if (isNaN(parseFloat(insurance))) {
        alert('\"保险\"输入错误！');
        return false;
    }
    var allcother = $("#divEdit #yundanTableInfo :text[name='other']").val();//其它费用
    formData.append("other", allcother);
    if (parseFloat(allcother) < 0) {
        alert('\"其它费用\"输入错误！');
        return false;
    }
    if (isNaN(parseFloat(allcother))) {
        alert('\"其它费用\"输入错误！');
        return false;
    }

    var allctariff = $("#divEdit #yundanTableInfo :text[name='tariff']").val();//关税
    formData.append("tariff", allctariff);
    if (parseFloat(allctariff) < 0) {
        alert('\"关税\"输入错误！');
        return false;
    }
    if (isNaN(parseFloat(allctariff))) {
        alert('\"关税\"输入错误！');
        return false;
    }
    var totalremark = $("#divEdit #yundanTableInfo :text[name='totalremark']").val();//备注
    formData.append("remark", totalremark);

    var qremark = $("#divEdit #yundanTableInfo :text[name='qremark']").val();//问题备注
    formData.append("qremark", qremark);

    var channelId = $("select[name='channellist']").val();//渠道id
    formData.append("channelId", channelId);
    if ((removenull(channelId) == "") || (isNaN(channelId)) || (parseFloat(channelId) <= 0)) {
        alert('\"渠道\"信息错误！');
        return false;
    }

    var storeId = $("select[name='modifystorelist']").val();//门店标识
    formData.append("storeId", storeId);
    if ((removenull(storeId) == "") || (isNaN(storeId)) || (parseFloat(storeId) <= 0)) {
        alert('\"门店\"选择错误！');
        return false;
    }


    if (validate == false) {
        alert("信息填写不完整，请填写完整后提交!");
        return false;
    }


    //admin/m_order/list_modify_order

    //判断是否发送短信
    var automessage = 0;
    if ($(":checkbox[name='sendmessage']").is(':checked') == true) {
        automessage = 1;
    }

    formData.append("automessage", automessage);

    //支付运单要先核实价格


    var httpRequest = new XMLHttpRequest();
    var url = "/admin/m_order/process_torder_submit";
    httpRequest.onreadystatechange = function () {
        if (4 == httpRequest.readyState) {
            if (200 == httpRequest.status) {
                //alert(httpRequest.responseText);
                obj = JSON.parse(httpRequest.responseText);


                if (obj.code == "200") {
                	
                //  alert("提交成功,结果："+obj.message);
                	//alert(obj.data);
                	
                	
                	 var strprint="";
               	  if (default_print_button_id == "0")//打印A4纸
                     {
                         //$("#iframePrint").attr("src", "printpage.html?id="+obj1.data+"&ran="+Math.random());
                         //$("#iframePrint").attr("src", "").attr("src", "printpage.html?id=" + obj1.data + "&ran=" + Math.random());
                         var iframeid = "iframePrint";
                         document.getElementById(iframeid).contentWindow.document.write("");
                         $("#" + iframeid).attr("src", "").attr("src", "printpage.html?id=" + obj.data + "&ran=" + Math.random());
                         beginPrint(iframeid);
                         strprint="A4打印";

                        // alert("打印A4运单成功！", focusprint(online));
                        // clearmenshidadan();
                     }
                     else//2及其它表示打印普通4*6运单
                     {
                         //$("#iframePrint").attr("src", "printpage64.html?id="+obj1.data+"&ran="+Math.random());
                         var iframeid = "iframePrint";
                         document.getElementById(iframeid).contentWindow.document.write("");
                         $("#" + iframeid).attr("src", "").attr("src", "printpage64.html?id=" + obj.data + "&ran=" + Math.random());
                         beginPrint(iframeid);
                         strprint="热敏4x6打印";
                       //  alert("打印普通热敏运单成功！", focusprint(online));
                       //  clearmenshidadan();
                     }
               	  
                	alert(strprint+","+obj.message);
                	
                  refreshDataGrid(postData);
                }
                //shelvesId
                else if(obj.code=="20006")//余额不足
                {
                	  var message = removenull(obj.message);
                      if (confirm("余额不足，是否生成未付款运单？结果：" + message)) {
                    	  
                    	  var shelvesId=$("#divEdit select[name='shelvesNo']").val();
                    	  
                          formData.append("shelvesId", shelvesId);//录入失败存入的仓位信息
                          var httpRequest1 = new XMLHttpRequest();
                          var url1 = "/admin/m_order/process_torder_submit_nopay";
                          httpRequest1.onreadystatechange = function () {
                              if (4 == httpRequest1.readyState) {
                                  if (200 == httpRequest1.status) {
                                      //alert(httpRequest.responseText);
                                      obj = JSON.parse(httpRequest1.responseText);


                                      if (obj.code == "200") {
                                    	  
                                    	  var strprint="";
                                    	  if (default_print_button_id == "0")//打印A4纸
                                          {
                                              //$("#iframePrint").attr("src", "printpage.html?id="+obj1.data+"&ran="+Math.random());
                                              //$("#iframePrint").attr("src", "").attr("src", "printpage.html?id=" + obj1.data + "&ran=" + Math.random());
                                              var iframeid = "iframePrint";
                                              document.getElementById(iframeid).contentWindow.document.write("");
                                              $("#" + iframeid).attr("src", "").attr("src", "printpage.html?id=" + obj.data + "&ran=" + Math.random());
                                              beginPrint(iframeid);
                                              strprint="A4打印";

                                             // alert("打印A4运单成功！", focusprint(online));
                                             // clearmenshidadan();
                                          }
                                          else//2及其它表示打印普通4*6运单
                                          {
                                              //$("#iframePrint").attr("src", "printpage64.html?id="+obj1.data+"&ran="+Math.random());
                                              var iframeid = "iframePrint";
                                              document.getElementById(iframeid).contentWindow.document.write("");
                                              $("#" + iframeid).attr("src", "").attr("src", "printpage64.html?id=" + obj.data + "&ran=" + Math.random());
                                              beginPrint(iframeid);
                                              strprint="热敏4x6打印";
                                            //  alert("打印普通热敏运单成功！", focusprint(online));
                                            //  clearmenshidadan();
                                          }
                                    	  
                                    	  
                                    	  
                                    	  
                                    	  
                                    	  
                                          alert(strprint+","+obj.message);
                                    	 // alert(obj.data);
                                          refreshDataGrid(postData);
                                      }
                                      else {
                                          alert("支付失败，原因：" + obj.message);
                                      }
                                  }
                              }
                          }
                          httpRequest1.open("post", url1, true);
                          httpRequest1.send(formData);
                      }
                }
                else
                {
                	alert("操作失败，"+obj.message);
                }
            }
        }
    }
    httpRequest.open("post", url, true);
    httpRequest.send(formData);



    return false;




}

function checksumbitformforupload() {
    var wid = $("#import_uploadmorders_ex_form select[name=uploadwid]").val();
    var cid = $("#import_uploadmorders_ex_form select[name=uploadcid]").val();
    var file = $("#import_uploadmorders_ex_form input[name=uploadfile]").val();
    if (removenull(wid) == "" || wid == "-1") {
        alert("必须选择要导入的门店！");
        return false;
    }
    if (removenull(cid) == "" || cid == "-1") {
        alert("必须选择要导入的渠道！");
        return false;
    }
    if (removenull(file) == "") {
        alert("必须选择要导入的excel文件！");
        return false;
    }
    return true;
}

//加载货架
function loadShelves() {
    $.ajax({
        type: "get",
        url: "/admin/Shelves/get_by_local",
      
        success: function (response) {
            var code = response.code;
            if ("200" == code) {

                // showCommodityList(response.data);;
                var str = "<option value='-1'>请选择未付款货架</option>";
                $.each(response.data, function () {
                    var shelvesName = this.shelvesNo;
                    if (this.nickName != null) { shelvesName = this.nickName + "-" + this.shelvesNo; }
                    str += "<option value='" + this.id + "'>"
							+ shelvesName + "</option>";
                });
                $("select[name='shelvesNo']").html(str);
                getfailShelvesNo();
                //msOrderCalcFreight();
            } else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                adminlogout();
            }
        }
    });
}

//设置为默认值
function saveShelvesNo() {
    $.ajax({
        type: "post",
        url: "/admin/store_control/savebyflag",
        data: { value:$("select[name='shelvesNo']").val(),flag:"order_nopay_shelvesId"},
        success: function (response) {
            var code = response.code;
            if ("200" == code) {
                alert("保存成功");
            } else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                adminlogout();
            }
            else
            {
            	alert("保存失败："+response.message);
            }
        }
    });
}


//设置为默认值
function getfailShelvesNo() {
    $.ajax({
        type: "post",
        url: "/admin/store_control/getbyself",
        data: {flag:"order_nopay_shelvesId"},
        success: function (response) {
            var code = response.code;
            if ("200" == code) {
               // alert("保存成功");
            	if(response.data!=null)
            	{
            		$("select[name='shelvesNo']").val(response.data.value);
            	}
            } else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                adminlogout();
            }
            else
            {
            	//alert("保存失败："+response.message);
            }
        }
    });
}

//fail_order_selves_no
   
function printbuttoncontrol1() {
    $.ajax({
        type: "post",
        url: "/admin/ms_control/getself",
        success: function (response) {
            var code = response.code;
            if ("200" == code) {
                var self = response.data;

                if (self != null) {
                    
                    default_print_button_id = self.keydownItem;//设置默认打印的运单类型
                    

                }
            }
            else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                top.location.href = "../admin/login.html";
            }
            else {
                alert("获取数据失败，原因是：" + response.message);
            }
        }
    });

}

//printMode参数
//“Full-Width” –宽度按纸张的整宽缩放；
//“Full-Height”–高度按纸张的整高缩放：
//“Full-Page” –按整页缩放，也就是既按整宽又按整高缩放；
//此外还可以按具体百分比例，格式为“Width:XX%;Height:XX%”或“XX%”
/*
function beginPrint(iframeid, printMode) {
  getMyLodop();
  var win = {};
  win = document.getElementById(iframeid).contentWindow;
  var isNotPrint = true;

  //if (printMode == undefined) { printMode = "100%" }
  if (printMode == undefined) { printMode = "Full-Page"; }
  var number = 0;
  var t = setInterval(function () {
      if (isNotPrint && win.isLoad) {
          try {
              if (LODOP == undefined) {
                  isNotPrint = false;
                  win.print();
              } else {
                  isNotPrint = false;
                  LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT", printMode);
                  ////ADD_PRINT_HTM(Top,Left,Width,Height,strHtmlContent)
                  LODOP.ADD_PRINT_HTM("0.5cm", "0.5cm", "RightMargin:0.5cm", "BottomMargin:0.5cm", win.document.documentElement.innerHTML);
                  // LODOP.PREVIEW();//预览打印
                 LODOP.PRINT();//直接打印

              }
              clearInterval(t);
          } catch (e) {
              isNotPrint = true;
              console.log(e.message);
          }
      }
      number++;
      if (number > 20) {
          clearInterval(t);
          alert("时间过长，打印失败！");
          return false;
      }
  }, 300);
}*/
