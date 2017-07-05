
$(function ($) {
    //  /user/reset_pwd_byuser?oldcode=1111111&newcode1=111&newcode2=111
    var validator = $("form").validate({
        //验证规则
        //http://www.runoob.com/jquery/jquery-plugin-validate.html
        debug:true,//不提交表单
        rules: {
            oldcode: { required: true },
            newcode1: { required: true, minlength: 6,maxlength:20 },
            newcode2: { required: true, minlength: 6, equalTo: "[name=newcode1]" }
        }, submitHandler: function (form) {
            var data = getEelementData("form");
           
            $.ajax({
                type: "get",
                url: "/user/reset_pwd_byuser",
                data: data,
                success: function (response) {
                    var code = response.code;
                    if (code == "200") {
                        alert("修改成功,请重新登录系统");
                        
                    } else if ("607" == code) {
                        alert("您尚未登录或登录已失效！");
                    } else {

                        alert("修改失败，原因是:" + response.message);
                    }
                }
            });
        }
    });
});



