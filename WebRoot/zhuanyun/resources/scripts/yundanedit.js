
//premium_rate提交保险的比例
//premium_max_value保险的保额最大值
//lowest_weight_value_flag重量的最小值，即多少磅起
//jw_commodity_rate 商品的进位值，即大于等于此值，将会进位计算
//cur_usa_cn 当前的汇率
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
        url: "../../admin/globalargs/getcontents",
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


function removenull(str) {
    if ((str == "") || (str == "null") || (str == "undefined") || (str == null)) {
        return "";
    }
    else {
        return str;
    }
}

$(function () {
    $("input[type=checkbox]").bootstrapSwitch();

})

var receiverRowData;

//初始化收件人列表数据
$(function ($) {

	//loadChannelSelectOption_edit();//装载渠道信息
    getglobalargs_commrate();//获取全局变量 
    $(":text[name='cquantity']").change(commuditydataChange_edit);
    $(":text[name='cweight']").change(commuditydataChange_edit);
    $(":text[name='cvalue']").change(commuditydataChange_edit);
    $(":text[name='ctariff']").change(commuditydataChange_edit);
    $(":text[name='cother']").change(commuditydataChange_edit);
    $(":text[name='insurance']").change(countpremiummoney_user);

});

//添加商品条目
function addGoodsItem() {
    var teplament = $("#goodsItemTeplamentEdit").html();
    $("#divEdit #goodsItemBody").append("<tr>" + teplament + "</tr>");
    
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
}

//删除商品条目
function delGoodsItem(obj) {
    $(obj).parent().parent().remove();
}

//显示隐藏身份证按钮字样变换
function displayReceiverIdTable(source) {
    var table = $("#divEdit #receiverIdTable");
    if (table.css("display") == "block") {
        table.hide();
        $(source).html(' <span class="ace-icon fa fa-home icon-on-right bigger-110"></span>显示身份证信息');
        if (receiverRowData != null) {

        }
    } else {
        table.show();
        $(source).html(' <span class="ace-icon fa fa-home icon-on-right bigger-110"></span>隐藏身份证信息');
    }
}


function upload() {
    $("#uploadshenfenzheng").show();
};
function hcShow() {
    $("#uploadhcTd").show();
    $("#uploadzfTd").hide();
}

function zfShow() {
    $("#uploadhcTd").hide();
    $("#uploadzfTd").show();
}




//商品变化时，计算对应的值
function commuditydataChange_edit() {
    var allquantity = 0;
    var allsjweight = 0;
    var allcvalue = 0;
    var allcother = 0;
    var allctariff = 0;
    $("select[name='commodityName']").each(
			function () {




			    var cquantity = $(this).parent().parent().find(":text[name='quantity']").val();
			    if ((cquantity == "") || (cquantity.trim() == "")) {

			    }
			    else {
			        allquantity = parseFloat(allquantity) + parseFloat(cquantity);
			    }

			    var cweight = $(this).parent().parent().find(":text[name='weight']").val();
			    if ((cweight == "") || (cweight.trim() == "")) {

			    }
			    else {
			        allsjweight = parseFloat(allsjweight) + parseFloat(cweight);
			    }

			    var cvalue = $(this).parent().parent().find(":text[name='value']").val();
			    if ((cvalue == "") || (cvalue.trim() == "")) {

			    }
			    else {
			        allcvalue = parseFloat(allcvalue) + parseFloat(cvalue);
			    }

			    var ctariff = $(this).parent().parent().find(":text[name='tariff']").val();
			    if ((ctariff == "") || (ctariff.trim() == "")) {

			    }
			    else {
			        allctariff = parseFloat(allctariff) + parseFloat(ctariff);
			    }

			    var cother = $(this).parent().parent().find(":text[name='other']").val();
			    if ((cother == "") || (cother.trim() == "")) {

			    }
			    else {
			        allcother = parseFloat(allcother) + parseFloat(cother);
			    }
			});

    $("#divEdit #yundanTableInfo [name='sjweight']").val(allsjweight);
    $("#divEdit #yundanTableInfo [name='quantity']").val(allquantity);
    $("#divEdit #yundanTableInfo [name='value']").val(allcvalue);
    $("#divEdit #yundanTableInfo [name='other']").val(allcother);
    $("#divEdit #yundanTableInfo [name='tariff']").val(allctariff);


    if (parseFloat(lowest_weight_value_flag) >= parseFloat(allsjweight))//小于最低计费重量
    {
    	$("#divEdit #yundanTableInfo [name='weight']").val(lowest_weight_value_flag);
    }
    else {
        var j = parseInt(allsjweight);
        var rate = parseFloat(allsjweight) - parseInt(allsjweight);
        if (parseFloat(jw_commodity_rate) < parseFloat(rate)) {
        	$("#divEdit #yundanTableInfo [name='weight']").val(j + 1);
        }
        else {
            if (price_carry_flag == "1" || price_carry_flag == "3")//退位计算
            {
            	$("#divEdit #yundanTableInfo [name='weight']").val(j);
            }
            else if (price_carry_flag == "2" || price_carry_flag == "4")//实际计算
            {
            	$("#divEdit #yundanTableInfo [name='weight']").val(allsjweight);
            }
            else
            {
            	$("#divEdit #yundanTableInfo [name='weight']").val(allsjweight);
            }
        }
    }
}

//保险额度计算
function countpremiummoney_user() {
    var rate = premium_rate;
    if ((rate == "0") || (removenull(rate) == null)) {
        return false;
    }
    rate = parseFloat(rate);
    if (isNaN(rate)) {
        return false;
    }
    var premium1 = parseFloat($(":text[name='insurance']").val());
    if (isNaN(premium1)) {
        return false;
    }
    if (premium1 > 0)
    { }
    else if (premium1 < 0) {
        alert("保险不能小于0!");
        return false;
    }
    else {
        return false;
    }
    var premiumt = parseFloat(premium1 / rate);
    if (isNaN(premiumt)) {
        return false;
    }
    if (parseFloat(premium_max_value) != "NaN") {
        if (premiumt > parseFloat(premium_max_value)) {
            alert("最大保险额度不能超过" + premium_max_value + "美元!");
            $(":text[name='insurance']").val(premium_max_value * rate);
            $(":text[name='maxinsurance']").val(premium_max_value);
        }
        else {
            $(":text[name='maxinsurance']").val(premiumt);
        }
    }
    else {
        $(":text[name='maxinsurance']").val(premiumt);
    }

}


function submitmsorderA4() {
    submitmsorder("0");//表示打印A4运单 
}

function submitmsorder46() {
    submitmsorder("1");//表示打印4x6运单 
}
//提交运单处理
function submitmsorder(type) {


    var validate = true;

    var formData = new FormData();
    var printway = type;

    formData.append("printway", printway);

    //构造发件人数据
    var name = $("#senderTableInfo").find(":text[name='name']").val();
    var phone = $("#senderTableInfo").find(":text[name='phone']").val();
    var address = $("#senderTableInfo").find(":text[name='address']").val();
    var city = $("#senderTableInfo").find(":text[name='city']").val();
    var state = $("#senderTableInfo").find(":text[name='state']").val();
    var zipcode = $("#senderTableInfo").find(":text[name='zipcode']").val();
    var email = $("#senderTableInfo").find(":text[name='email']").val();
    var company = $("#senderTableInfo").find(":text[name='company']").val();


    if (name == "" || name == null) {
        $("#senderTableInfo").find(":text[name='name']").css({
            "border-color": "red"
        });
        $("#senderTableInfo").find(":text[name='name']").change(
				function () {
				    if ($("#senderTableInfo").find(":text[name='name']")
							.val() != null)
				        $("#senderTableInfo").find(":text[name='name']")
								.css({
								    "border-color": ""
								});
				});
        validate = false;
    }

    if (phone == "" || phone == null) {
        $("#senderTableInfo").find(":text[name='phone']").css({
            "border-color": "red"
        });
        $("#senderTableInfo").find(":text[name='phone']").change(
				function () {
				    if ($("#senderTableInfo").find(":text[name='phone']")
							.val() != null)
				        $("#senderTableInfo").find(":text[name='phone']")
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
    formData.append("suser.company", company);


    //构造收件人数据
    var name = $("#receiverInfoTable").find(":text[name='name']").val();
    var phone = $("#receiverInfoTable").find(":text[name='phone']").val();
    var address = $("#receiverInfoTable").find(":text[name='address']").val();
    var dist = $("#receiverInfoTable").find(":text[name='dist']").val();
    var city = $("#receiverInfoTable").find(":text[name='city']").val();
    var state = $("#receiverInfoTable").find(":text[name='state']").val();
    var zipcode = $("#receiverInfoTable").find(":text[name='zipcode']").val();


    if (phone == "" || phone == null) {
        $("#receiverInfoTable").find(":text[name='phone']").css({
            "border-color": "red"
        });
        $("#receiverInfoTable").find(":text[name='phone']").change(
				function () {
				    if ($("#receiverInfoTable").find(":text[name='phone']")
							.val() != null)
				        $("#receiverInfoTable").find(":text[name='phone']")
								.css({
								    "border-color": ""
								});
				});
        validate = false;
    }

    if (name == "" || name == null) {
        $("#receiverInfoTable").find(":text[name='name']").css({
            "border-color": "red"
        });
        $("#receiverInfoTable").find(":text[name='name']").change(
				function () {
				    if ($("#receiverInfoTable").find(":text[name='name']")
							.val() != null)
				        $("#receiverInfoTable").find(":text[name='name']")
								.css({
								    "border-color": ""
								});
				});
        validate = false;
    }

    if (address == "" || address == null) {
        $("#receiverInfoTable").find(":text[name='address']").css({
            "border-color": "red"
        });
        $("#receiverInfoTable").find(":text[name='address']").change(
				function () {
				    if ($("#receiverInfoTable").find(":text[name='address']")
							.val() != null)
				        $("#receiverInfoTable").find(":text[name='address']")
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




    //开始构造商品类信息
    var flag = -1;
    var ii = 0;
    $("select[name='commodityName']").each(
			function () {

			    if (flag >= 0)//第一行为隐藏行的添加模板，不计入商品信息
			    {


			        var commodityid = $(this).val();//获取商品id
			        var commodityname = $(this).find("option:selected").text();//获取商品名称
			        var cproductname = $(this).parent().parent().find(":text[name='cproductname']").val();//获取品名
			        var cbrandname = $(this).parent().parent().find(":text[name='cbrandname']").val();//获取品牌
			        var cquantity = $(this).parent().parent().find(":text[name='cquantity']").val();//获取数量
			        var cweight = $(this).parent().parent().find(":text[name='cweight']").val();//获取重量
			        var cvalue = $(this).parent().parent().find(":text[name='cvalue']").val();//获取价值
			        var ctariff = $(this).parent().parent().find(":text[name='ctariff']").val();//获取价值
			        var cother = $(this).parent().parent().find(":text[name='cother']").val();//获取其它费用
			        var cremark = $(this).parent().parent().find(":text[name='cremark']").val();//获取备注

			        if ((removenull(cproductname) == "") && (removenull(cbrandname) == "") && (removenull(cquantity) == "") && (removenull(cweight) == "") && (removenull(cvalue) == ""))
			        { }
			        else
			        {
			            if (cproductname == "" || cproductname == null) {
			                $(this).parent().parent().find(":text[name='cproductname']").css({
			                    "border-color": "red"
			                });
			                $(this).parent().parent().find(":text[name='cproductname']").change(
                                    function () {
                                        if ($(this).parent().parent().find(":text[name='cproductname']")
                                                .val() != null)
                                            $(this).parent().parent().find(":text[name='cproductname']")
                                                    .css({
                                                        "border-color": ""
                                                    });
                                    });
			                validate = false;
			            }
			            if (cbrandname == "" || cbrandname == null) {
			                $(this).parent().parent().find(":text[name='cbrandname']").css({
			                    "border-color": "red"
			                });
			                $(this).parent().parent().find(":text[name='cbrandname']").change(
                                    function () {
                                        if ($(this).parent().parent().find(":text[name='cbrandname']")
                                                .val() != null)
                                            $(this).parent().parent().find(":text[name='cbrandname']")
                                                    .css({
                                                        "border-color": ""
                                                    });
                                    });
			                validate = false;
			            }

			            if (cquantity == "" || cquantity == null || isNaN(cquantity) || (parseFloat(cquantity) <= 0)) {
			                $(this).parent().parent().find(":text[name='cquantity']").css({
			                    "border-color": "red"
			                });
			                $(this).parent().parent().find(":text[name='cquantity']").change(
                                    function () {
                                        if ($(this).parent().parent().find(":text[name='cquantity']")
                                                .val() != null)
                                            $(this).parent().parent().find(":text[name='cquantity']")
                                                    .css({
                                                        "border-color": ""
                                                    });
                                    });
			                validate = false;
			            }

			            if (cweight == "" || cweight == null || isNaN(cweight) || (parseFloat(cweight) <= 0)) {
			                $(this).parent().parent().find(":text[name='cweight']").css({
			                    "border-color": "red"
			                });
			                $(this).parent().parent().find(":text[name='cweight']").change(
                                    function () {
                                        if ($(this).parent().parent().find(":text[name='cweight']")
                                                .val() != null)
                                            $(this).parent().parent().find(":text[name='cweight']")
                                                    .css({
                                                        "border-color": ""
                                                    });
                                    });
			                validate = false;
			            }

			            if (cvalue == "" || cvalue == null || isNaN(cvalue) || (parseFloat(cvalue) <= 0)) {
			                $(this).parent().parent().find(":text[name='cvalue']").css({
			                    "border-color": "red"
			                });
			                $(this).parent().parent().find(":text[name='cvalue']").change(
                                    function () {
                                        if ($(this).parent().parent().find(":text[name='cvalue']")
                                                .val() != null)
                                            $(this).parent().parent().find(":text[name='cvalue']")
                                                    .css({
                                                        "border-color": ""
                                                    });
                                    });
			                validate = false;
			            }

			            if (ctariff == "" || ctariff == null || isNaN(ctariff) || (parseFloat(ctariff) < 0)) {
			                $(this).parent().parent().find(":text[name='ctariff']").css({
			                    "border-color": "red"
			                });
			                $(this).parent().parent().find(":text[name='ctariff']").change(
                                    function () {
                                        if ($(this).parent().parent().find(":text[name='ctariff']")
                                                .val() != null)
                                            $(this).parent().parent().find(":text[name='ctariff']")
                                                    .css({
                                                        "border-color": ""
                                                    });
                                    });
			                validate = false;
			            }
			            if (cother == "" || cother == null || isNaN(cother) || (parseFloat(cother) < 0)) {
			                $(this).parent().parent().find(":text[name='cother']").css({
			                    "border-color": "red"
			                });
			                $(this).parent().parent().find(":text[name='cother']").change(
                                    function () {
                                        if ($(this).parent().parent().find(":text[name='cother']")
                                                .val() != null)
                                            $(this).parent().parent().find(":text[name='cother']")
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
    var weight = $(":text[name='allsjweight']").val();//计费总重量
    formData.append("weight", weight);
    if (parseFloat(weight) < 0) {
        alert('\"重量\"输入错误！');
        return false;
    }

    if (isNaN(weight)) {
        alert('\"重量\"输入错误！');
        return false;
    }

    var allsjweight = $(":text[name='allsjweight']").val();//实际总重量
    formData.append("sjweight", allsjweight);

    var allquantity = $(":text[name='allquantity']").val();//数量
    formData.append("quantity", allquantity);
    if (parseFloat(allcvalue) < 0) {
        alert('\"数量\"输入错误！');
        return false;
    }
    if (isNaN(allquantity)) {
        alert('\"数量\"输入错误！');
        return false;
    }
    var allcvalue = $(":text[name='allcvalue']").val();//价值
    formData.append("value", allcvalue);
    if (parseFloat(allcvalue) < 0) {
        alert('\"价值\"输入错误！');
        return false;
    }
    if (isNaN(allcvalue)) {
        alert('\"价值\"输入错误！');
        return false;
    }

    var insurance = $(":text[name='insurance']").val();//保险
    formData.append("insurance", insurance);

    if (parseFloat(insurance) < 0) {
        alert('\"保险\"输入错误！');
        return false;
    }
    if (isNaN(insurance)) {
        alert('\"保险\"输入错误！');
        return false;
    }
    var allcother = $(":text[name='allcother']").val();//价值
    formData.append("other", allcother);
    if (parseFloat(allcother) < 0) {
        alert('\"其它费用\"输入错误！');
        return false;
    }
    if (isNaN(allcother)) {
        alert('\"其它费用\"输入错误！');
        return false;
    }

    var allctariff = $(":text[name='allctariff']").val();//关税
    formData.append("tariff", allctariff);
    if (parseFloat(allctariff) < 0) {
        alert('\"关税\"输入错误！');
        return false;
    }
    if (isNaN(allctariff)) {
        alert('\"关税\"输入错误！');
        return false;
    }
    var totalremark = $(":text[name='totalremark']").val();//备注
    formData.append("remark", totalremark);

    var channelId = $("select[name='channellist']").val();//渠道id
    formData.append("channelId", channelId);
    if ((removenull(channelId) == "") || (isNaN(channelId)) || (parseFloat(channelId) <= 0)) {
        alert('\"渠道\"信息错误！');
        return false;
    }


    if (validate == false) {
        alert("信息填写不完整，请填写完整后提交!");
        return false;
    }


    //选择支付方式0是余额支付，1是现金支付
    formData.append("payWay", 1);//预先设置为现金支付

    //判断是否发送短信
    var automessage = 0;
    if ($(":checkbox[name='sendmessage']").is(':checked') == true) {
        automessage = 1;
    }

    formData.append("automessage", automessage);
    var httpRequest = new XMLHttpRequest();
    var url = "../../admin/m_order/check_price";

    httpRequest.onreadystatechange = function () {
        if (4 == httpRequest.readyState) {
            if (200 == httpRequest.status) {
                //alert(httpRequest.responseText);
                obj = JSON.parse(httpRequest.responseText);

                if (obj.code == "200") {
                    //alert("核实价格:"+obj.data+"美元");
                    if (confirm("核实价格:" + obj.data + "美元,是否继续？")) {
                        var httpRequest1 = new XMLHttpRequest();
                        var url1 = "../../admin/m_order/ms_add";
                        httpRequest1.onreadystatechange = function () {
                            if (4 == httpRequest1.readyState) {
                                if (200 == httpRequest1.status) {
                                    var obj1 = JSON.parse(httpRequest1.responseText);
                                    if (obj1.code == "200") {

                                        //window.location.href = "../admin/printpage.html"+"?"+"id="+obj1.data;
                                        //alert(obj1.data);
                                        if (printway == "1")//海关热敏4*6打印
                                        {
                                            $("#iframePrint").attr("src", "printpageremin.html?id=" + obj1.data);
                                        }
                                        else {
                                            $("#iframePrint").attr("src", "printpage.html?id=" + obj1.data + "&ran=" + Math.random());
                                        }
                                    } else if ("607" == obj1.code) {
                                        alert("您尚未登录或登录已失效！");
                                        adminlogout();
                                    }
                                    else {
                                        alert("提交出错，出错原因：" + obj1.message);
                                    }
                                }

                            }
                        }
                        httpRequest1.open("post", url1, true);
                        httpRequest1.send(formData);

                    }

                } else if ("607" == obj.code) {
                    alert("您尚未登录或登录已失效！");
                    adminlogout();
                }
                else {
                    alert("提交出错，出错原因：" + obj.message);
                }

                //nav95Click();
            }
        }
    };
    httpRequest.open("post", url, true);
    httpRequest.send(formData);

    return false;


}
