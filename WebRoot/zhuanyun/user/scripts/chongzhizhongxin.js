$(function () {
    //获取用户信息
    $.ajaxEx({
        type: "get",
        url: "/user/get_self",
        success: function (response) {
            var code = response.code;
            if (code == "200") {
            	$("#rmb_m").html(response.data.rmbBalance);
            	$("#usa_m").html(response.data.usdBalance);
            ;
                //setSpanText("#userinfo", response.data);
                //top.window.document.getElementById("username").innerHTML=response.data.realName;
            } else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                userlogout();
            } else {

                alert("获取个人信息失败，原因是:" + response.message);
            }
        }
    });
    
    var flags = "cur_usa_cn";//获取当前人民币汇率
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
					if(removenull(response.data[0])!="")
					{
						$("#rmb_usa_rate").html(response.data[0]);
					}	

				} else {

				}
			} else {

			}
			
		}
	});
    
    
    $("input[name='alipay_amount'").change(alipayamountchange);
    
});
//支付宝支付提交
function alipay_paysubmit()
{
	var amount1=$("input[name='alipay_amount'").val();
	var amount=parseFloat(amount1);
	if(isNaN(amount)||(amount<=0))
	{
		alert("只能转入大于0的数字!");
		return false;
	}
	else if((amount>10000))
	{
		alert("每次最大值不能超过10000元人民币!");
		return false;
	}
	
	window.location.href="/user/alipay/recharge?amount="+amount;
	
	/*$.ajax({
		type : "get",
		url : "/user/alipay/recharge",
		data : $.param({
			"amount" : "1",
		}, true),
		success : function(response) {
			
		
			return false;
		}
	});*/
}

function alipayamountchange()
{
	var amount1=$("input[name='alipay_amount'").val();
	var amount=parseFloat(amount1);
	if(isNaN(amount)||(amount<=0))
	{
		alert("不能输入非数字或小于0的值!");
		$("input[name='alipay_amount'").val(0);
		return false;
	}
}
