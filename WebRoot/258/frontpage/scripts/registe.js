
$(function () {

    //下面先屏蔽邮箱注册
    $(".title li").click(function () {
         $(this).addClass("current").siblings().removeClass("current");
        if ($(this).text() == "手机注册") {
            $("#phoneReg").show();
            $("#emailReg").hide();
        } else {
            $("#phoneReg").hide();
            $("#emailReg").show();
        }
    });

    $("#phoneReg").keydown(function (event) {
        if (event.which == 13) {
            $("#phoneRegSubmit").click();
        }
    });

    $("#emailReg").keydown(function (event) {
        if (event.which == 13) {
            $("#emailRegSubmit").click();
        }
    });

    $("#btnGetCode").click(function () {
        $("#phoneRegMessage").html("");
        var phone = $.trim($("#phoneReg [name=phone]").val());
        if (checkUserPhone(phone) == false) {
            $("#phoneRegMessage").html("请输入正确的手机号<br/>");
            return;
        }

        $.ajax({
            type: "post",
            url: "/code/phone_reg",
            data: {
                "phone": phone
            },
            success: function (response) {
                var code = response.code;
                if (code == "200") {
                    alert("发送成功，请查看手机收到的验证码");
                }
                else {
                    alert("发送失败！" + response.message);
                }
            }
        });
    });

    $("#phoneRegSubmit").click(function () {
        $("#phoneRegMessage").html("");
        var msg = "";
        if ($(":checkbox[name='accept_phone_terms']").is(':checked') == false) {
            msg = "请选择用户注册协议";
            $("#phoneRegMessage").html(msg);
            return;
        }


        var data = {};
        data.isloging = 1;
        data.recommender = "-1",
        data.phone = $.trim($("#phoneReg [name=phone]").val());
        data.vcode = $.trim($("#phoneReg [name=checkcode]").val());
        data.password = $.trim($("#phoneReg [name=password]").val());


        if (checkUserPhone(data.phone) == false) { msg = "请输入正确的手机号<br/>"; }
        //if (data.checkcode == "") { msg = msg + "验证码不能为空<br/>"; }
        if (data.password.length < 6) { msg = msg + "密码长度不能小于6位"; }

        if (msg != "") {
            $("#phoneRegMessage").html(msg);
        } else {
            $("#phoneRegMessage").html("");
            $.ajax({
                url: "/user/register",
                type: "post",
                data: data,
                success: function (data) {
                	if(data.code=="200")
                	{
	                    alert("注册成功.");
	                    window.location.href = "../user/index.html";
                	}
                	else
                	{
                		alert("失败原因："+data.message);
                	}
                },
                error: function (XMLHttpRequest, textStatus) {
                    alert("注册发生错误");
                },
                complete: function () {

                }
            });
        }
    });

    $("#emailRegSubmit").click(function () {
        $("#emailRegMessage").html("");
        var msg = "";
        if ($(":checkbox[name='accept_email_terms']").is(':checked') == false) {
            msg = "请选择用户注册协议";
            $("#emailRegMessage").html(msg);
            return;
        }

        var data = {};
        data.recommender = "-1";
        data.isloging = "1";
        data.email = $.trim($("#emailReg [name=email]").val());
        data.password = $.trim($("#emailReg [name=password]").val());

        var msg = "";
        var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
        if (filter.test(data.email) == false) { msg = "邮箱格式不对<br/>"; }
        if (data.password.length < 6) { msg = msg + "密码长度不能小于6位"; }

        if (msg != "") {
            $("#emailRegMessage").html(msg);
        } else {
            $("#emailRegMessage").html("");

            $.ajax({
                url: "/user/email_register",
                type: "post",
                data: data,
                success: function (data) {
                	if(data.code=="200")
                	{
	                    alert("注册成功.");
	                    window.location.href = "../user/index.html";
                	}
                	else
                	{
                		alert("失败原因："+data.message);
                	}
                },
                error: function (XMLHttpRequest, textStatus) {
                    alert("注册发生错误");
                },
                complete: function () {

                }
            });
        }

    });
});

