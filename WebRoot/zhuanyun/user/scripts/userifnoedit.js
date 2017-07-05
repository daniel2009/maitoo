
$(function ($) {

    //获取用户信息
    $.ajaxEx({
        type: "get",
        url: "/user/get_self",
        success: function (response) {
            var code = response.code;
            if (code == "200") {
                setInputData("form", response.data);
            } else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
            } else {

                alert("获取个人信息失败，原因是:" + response.message);
            }
        }
    });

    var validator = $("form").validate({
        //验证规则
        //http://www.runoob.com/jquery/jquery-plugin-validate.html
        debug:true,//不提交表单
        rules: {
            realName: { required: true },
            phone: { required: true, minlength: 4 },
           // email: { required: false, type: "email" },
        }, submitHandler: function (form) {
            var data = getEelementData("form");
            data.name = data.nickName;
            //top.window.infoDialog("提交", "数据是" + data, true);
            $.ajax({
                type: "post",
                url: "/user/save_info",
                data:data,
                success: function (response) {
                    var code = response.code;
                    if (code == "200") {
                        alert("修改成功");
                        //window.location.href = window.location.href;
                    } else if ("607" == code) {
                        alert("您尚未登录或登录已失效！");
                    } else {

                        alert("保存失败，原因是:" + response.message);
                    }
                }
            });
        }
    });
});



