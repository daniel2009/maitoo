$(function () {
    //多选框开关
    //$("input[type=checkbox]").bootstrapSwitch();
	$("#code").click(function() {
		resetValidateCode(this);
	});
    //点击提交
    $("from [type=submit]").click(function () {
        var valid = $("from").validate().valid();
        return valid;
    });

  
	$("#bt_reset_admin_pwd").click(
			function() {
				var password = $("#password").val();
				var confirmPassword = $("#confirmpwd").val();
				var oldPwd = $("#oldpwd").val();
				var vcode = $("#verycode").val();

				if (!checkEmpPassword(password)) {
					alert("新密码格式错误，密码由6到20位非空字符组成。");
				} else if (!checkEmpPassword(confirmPassword)) {
					alert("确认密码格式错误，密码由6到20位非空字符组成。");
				} else if (!checkEmpPassword(oldPwd)) {
					alert("原密码格式错误，密码由6到20位非空字符组成。");
				} else if (password != confirmPassword) {
					alert("新密码和确认密码不一致，请重新输入!");
				} else {
					// 开始修改密码
					$("#bt_reset_pwd").attr("disabled", true);
					$.ajax( {
						type : "post",
						url : "/admin/emp/reset_pwd",
						data : {
							"password" : password,
							"oldpwd" : oldPwd,
							"vcode" : vcode
						},
						success : function(response) {
							var code = response.code;
							if ("200" == code) {
								alert("修改密码成功,请重新登陆");
								adminlogout();
							} else if ("607" == code) {
								adminlogout();
							} else {
								alert("修改密码失败，失败原因是:" + response.message);
								resetValidateCode($("#code"));
								$("#bt_reset_admin_pwd").attr("disabled", false);
								$("#reset").click();
							}
							return false;
						}
					});
				}
				return false;
			});
    //receiverGrid();
});


function resetValidateCode(event) {
	$(event).attr('src','/code/generate?timestamp=' + new Date().getTime());
}

function adminlogout() {
	$.get("../../admin/emp/logout", function(response) {
		top.location.href = "../admin/login.html";
});
return false;
}
