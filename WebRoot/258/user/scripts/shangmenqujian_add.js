

function initWarehouse() {
    var select = $("#warehouseSelect");
    $.ajax({
        type: "get",
        url: "/user/warehouse/all",
        success: function (response) {
            var code = response.code;
            if (code == "200") {

                var data = response.data;
                var options = "";
                
                for (var i = 0; i < data.length; i++) {
                    var row = data[i];
                    if(row.callOrderAvailable=="1")
                    {
                    	options += ' <option value="' + row.id + '" country="' + row.country + '" province="' + row.province + '" callordercity="' + row.callOrderCity + '">' + row.name + '</option>'
                
                    }
                }
                if(options=="")
                {
                	alert("对不起，暂时开能上门收货业务!");
                	window.location.href="index.html";
                	return false;
                }
                select.html(options);
                changeWarehouseSelect();
                init_to_sender();
                
                getselfsetting();
            } else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                userlogout();

            } else {
                alert("获取仓库失败，原因是:" + response.message);
            }
        }
    });
}

function cancelCallOrder(id) {
    top.window.confirmDialog("确定删除预约？", "取消提示", function () {
        $.ajax({
            type: "post",
            url: "/sm_order/user_delete",
            data: {
                id: id
            },
            success: function (response) {
                var code = response.code;
                if (code == "200") {
                    alert("取消成功");
                    refreshDataGrid(postData);
                } else if ("607" == code) {
                    alert("您尚未登录或登录已失效！");
                    userlogout();
                } else {
                    // 失败
                    alert("删除信息失败，原因是:" + response.message);
                }
            }
        });
    });
}
function changeWarehouseSelect() {
    var option = $("#warehouseSelect option:selected");
    $("#warehouseCountry").html(option.attr("country"));
    $("#warehouseProvince").val(option.attr("province"));

    $("#swarehouseCountry").val(option.attr("country"));
    $("#swarehouseProvince").val(option.attr("province"));

    var addOption = "<option>";
    if (option.attr("callOrderCity").replace(/\s+/g, "") != "") {
        addOption += option.attr("callOrderCity").replace(/[\n\r]+/g, "</option><option>");
    }
    addOption += "</option>";
    var regExp = new RegExp("(<option></option>)+");
    addOption = addOption.replace(regExp, "");
    $("#warehouseCallOrderCity").html(addOption);
}


//添加

$(function () {
	
	$("#cancel").click(function(){
		$("#divAdd input[name='name']").val("");
		$("#divAdd input[name='phone']").val("");
		$("#divAdd input[name='appointmentDate']").val("");
		$("#divAdd input[name='address']").val("");
		$("#divAdd input[name='zipCode']").val("");
		$("#divAdd input[name='quantity']").val("");
		$("#divAdd input[name='weight']").val("");
		$("#divAdd input[name='remark']").val("");
		window.location.href="shangmenqujian.html";
	});
	
	
    initWarehouse();
    //添加表单验证并提交,选择器的内容一定要是form表单，里面一定要有一个submit的按钮
    $("form").validate({
        //验证规则
        //http://www.runoob.com/jquery/jquery-plugin-validate.html
        debug: true,//不提交表单
        rules: {
            name: { required: true },
            appointmentDate: { required: true },
            storeId: { required: true },

            city: { required: true },
            address: { required: true },
            zipCode: { required: true },
            phone: { required: true },
            quantity: { required: true, number: true, range: [1, 1000] },
            weight: { required: true, number: true }
        },
        submitHandler: function (form) {
            var data = getEelementData("form");

            $.ajax({
                type: "post",
                url: "/sm_order/user_add",
                data: data,
                success: function (response) {
                    var code = response.code;
                    if (code == "200") {
                        alert("预约成功");
                        //隐藏新增div
                       /* $("#divAdd").hide();
                        $("#divToolbar").show();
                        $("#divGrid").show();*/
                        //清空输入框
                        clearInputData("#divAdd");
                        $("#divAdd textarea[name='remark']").val("");
                        //刷新列表数据
                       // refreshDataGrid(postData);
                    } else if ("607" == code) {
                        alert("您尚未登录或登录已失效！");
                        userlogout();
                    } else {
                        // 失败
                        alert("预约失败，原因是:" + response.message);
                    }
                }
            });
        }
    });
})



function init_to_sender()
{
	//获取用户信息
    $.ajaxEx({
        type: "get",
        url: "/user/get_self",
        success: function (response) {
            var code = response.code;
            if (code == "200") {
            	if((removenull(response.data.realName)=="")||(removenull(response.data.phone)==""))
            	{
            		alert("完善资料后才能够在线置单!");
            		window.location.href="userifnoedit.html";
            		return false;
            	}
            	//setInputData("#divAdd", response.data);
                $("#divAdd").find("input[name='name']").val(removenull(response.data.realName));
                $("#divAdd").find("input[name='phone']").val(removenull(response.data.phone));
            	
            	
            
            } else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                userlogout();
            } else {

                alert("获取个人信息失败，原因是:" + response.message);
            }
        }
    });
}


//获取数据
function getselfsetting()
{
	 $.ajax({
	        type: "post",
	        url: "/user/user_setting/getself",
	        success: function (response) {
	            var code = response.code;
	            
	            if ("200" == code) {
	            	//获取成功
	            	if(response.data!=null)
	            	{
	            		$("#warehouseSelect").val(response.data.s_store);
	            	}
	            } else if ("607" == code) {
	                alert("您尚未登录或登录已失效！");
	                userlogout();
	            }else
	            {
	            	
	            	
	            }
	        }
	    });
}
