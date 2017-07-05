
var receiverGrid, jsonData;
var packageGrid, packageData;
var packagegrid_selector = "#packagegrid-table";
var showReceiverSelectObj;//记录弹出收件人的按钮，后续用来对分箱赋值用

$(function () {

    $("#divAdvanceSearch").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            searchPackageData();
        }
    }));

    $("#btnSearch").click(function () {
        searchPackageData();
    });

    initPackageGrid();
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
    $(obj).closest("[ body=goodsItemBody]").append(teplament);
    $("input[name='cquantity']").unbind();
    $("input[name='cvalue']").unbind();
    $("input[name='cquantity']").change(commuditydataChangeUser);//计算总重量和数量
    $("input[name='cvalue']").change(commuditydataChangeUser);//计算总重量和数量
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

    $(".page-content").append(t);
    $(".boxitem:last .sn").html(sn);
    $("body").animate({ scrollTop: $(".boxitem:last").offset().top }, 10);
    event.stopPropagation();
    $("input[name='cquantity']").change(commuditydataChangeUser);//计算总重量和数量
    $("input[name='cvalue']").change(commuditydataChangeUser);//计算总重量和数量
}

//移除分箱
function removeBox(obj) {
    $(obj).parent().parent().parent().remove();
    event.stopPropagation();
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
            var input = '<input type="hidden" name="receiveruserid" value="' + $receiverInfo.id + '"/>';
            //province":"河北省2","city":"张家口市2","district":"宣化县2","streetAddress"
            var displayInfo = "<span>" + row.name + "/" + row.phone + "/" + row.province + row.city + row.district + row.streetAddress + "/" + row.cardId + "</span>";
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
        url: "/consignee/search",
        datatype: "json",
        height: 120,
        //shrinkToFit: true,
        mtype: 'get',
        colNames: ['美国转运单号', '货物说明', "到库时间", "重量"],
        colModel: [

            { name: 'name', index: 'name', width: 100 },
             { name: 'phone', index: 'phone', width: 120 },
             { name: 'city', index: 'city', width: 100 },
             { name: 'city', index: 'city', width: 60 },
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
            } else if ("607" == data.code) {
                alert("您尚未登录或登录已失效！");
                userlogout();
            } else {
                alert("获取包裹信息失败:" + data.message);
            }
        },
        onSelectRow: function (rowid, status) {
            var row = packageData.getData(rowid);

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

                    $("select[name='wid']").html(str);
                    $("select[name='wid']").change(searchPackageData);
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


			});
    $source.find("input[name='allquantity']").val(allquantity);
    $source.find("input[name='allcvalue']").val(allcvalue);

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
