
$(function () {
    $("#code").click(function () {
        $(this).attr("src", "/code/generate?ran=" + Math.random());
    });

    $("#divlogin").keydown(function (event) {
        if (event.which == 13) {
            $("#login").click();
        }
    });

    $("#login").click(function () {

        var account = $.trim($("[name=username]").val());
        var code = $.trim($("[name=checkcode]").val());
        var password = $.trim($("[name=password]").val());

        var msg = "";
        if (account == "") { msg = "用户名不能为空<br/>"; }
        if (password == "") { msg = msg + "密码不能为空<br/>"; }
        if (code == "") { msg = msg + "验证码不能为空"; }

        if (msg != "") {
            $("#loginMessage").html(msg);
            return;
        }

        if ((!checkUserPhone(account) && !checkUserEmailRegex(account))) {
            msg = "手机号或者邮箱是无效的,请输入正确的手机或者邮箱.";
        }
        if (!checkUserPassword(password)) {
            msg += "<br/>密码长度不对";
        }

        if (msg != "") {
            $("#loginMessage").html(msg);
            return;
        }

        $("#loginMessage").html("");
        $.ajax({
            type: "post",
            url: "/user/login",
            data: {
                "account": account,
                "password": password,
                "vcode": code
            },
            success: function (response) {
                var code = response.code;

                if (code == "200") {
                    window.location.href = "../user/index.html";

                } else {
                    $("#loginMessage").html("登录失败，失败原因是:" + response.message);
                    $("#code").click();
                }
            }
        });
    });
});
