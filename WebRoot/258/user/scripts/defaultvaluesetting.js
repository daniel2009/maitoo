$(function () {
	 getselfsetting();
    //loadstorelist();
});

//装载门店及相关联的数据
function loadstorelist(selfobject) {
	
    $.ajax({
        type: "get",
        url: "/user/warehouse/all",
        success: function (response) {
            var code = response.code;
            if (code == '200') {
                if (response.data != null) {
                    var str = "<option value='-1'>选择门店</option>";
                    var strs = "<option value='-1'>选择门店</option>";//上门收货门店
                    $.each(response.data, function () {
                    	if(this.showstore=="1"||this.showstore==1)
                    	{
                    		str += "<option value='" + this.id + "'>" + this.name + "</option>";
                    		if(this.callOrderAvailable=="1"||this.callOrderAvailable==1)//上门收货功能
                    		{
                    			strs += "<option value='" + this.id + "'>" + this.name + "</option>";
                    		}
                    	}
                    });
                    $("select[name='s_storelists']").html(strs);
                    $("select[name='z_storelists']").html(str);
                    
                    if(selfobject!=null)
                    {
                    	$("select[name='s_storelists']").val(selfobject.s_store);
                    	$("select[name='z_storelists']").val(selfobject.z_store);
                    }

                    $("select[name='z_storelists']").change(loadChannelSelectOptionbyuser1);
                    loadChannelSelectOptionbyuser(selfobject);
                   // getDefaultValue(1);
                   // getDefaultValue(3);
                   
                   
                }
            }
        }
    });
}


//装载渠道选择
function loadChannelSelectOptionbyuser(selfobject) {
    var wid = $("select[name='z_storelists']").val();
    if ((removenull(wid) == "") || (wid == "-1")) {
        var str1 = "<option value='-1'>选择渠道</option>";
        $("select[name='channellist']").html(str1);
        //getselfsetting();
        return false;
    }
    $.ajax({
        type: "get",
        url: "/channel/get_by_store_available",
        data: {
            "wid": wid
        },
        success: function (response) {
            var code = response.code;
            if ("200" == code) {

                // showCommodityList(response.data);;
                var str = "";
                if (response.data != null) {
                    $.each(response.data, function () {
                        str += "<option value='" + this.id
								 + "'>"
								+ this.name + "</option>";
                    });
                    $("select[name='channellist']").html(str);
                    
                    if(removenull(selfobject)!="")
                    {
                    	$("select[name='channellist']").val(selfobject.z_cid);
                    }
                    
                   // getDefaultValue(2);
                    
                }
                else {
                    var str1 = "<option value='-1'>选择渠道</option>";
                    $("select[name='channellist']").html(str1);

                    //return false;
                }

            } else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                userlogout();
            }
        }
    });
}


//装载渠道选择
function loadChannelSelectOptionbyuser1() {
    var wid = $("select[name='z_storelists']").val();
    if ((removenull(wid) == "") || (wid == "-1")) {
        var str1 = "<option value='-1'>选择渠道</option>";
        $("select[name='channellist']").html(str1);
        //getselfsetting();
        return false;
    }
    $.ajax({
        type: "get",
        url: "/channel/get_by_store_available",
        data: {
            "wid": wid
        },
        success: function (response) {
            var code = response.code;
            if ("200" == code) {

                // showCommodityList(response.data);;
                var str = "";
                if (response.data != null) {
                    $.each(response.data, function () {
                        str += "<option value='" + this.id
								 + "'>"
								+ this.name + "</option>";
                    });
                    $("select[name='channellist']").html(str);
                    
                   
                    
                   // getDefaultValue(2);
                    
                }
                else {
                    var str1 = "<option value='-1'>选择渠道</option>";
                    $("select[name='channellist']").html(str1);

                    //return false;
                }

            } else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                userlogout();
            }
        }
    });
}



function updateonlinesetting()
{
	var wid = $("select[name='z_storelists']").val();
	var cid = $("select[name='channellist']").val();
    $.ajax({
        type: "post",
        url: "/user/user_setting/update_online",
        data: {
            "storeId": wid,
            "cid": cid
        },
        success: function (response) {
            var code = response.code;
            if ("200" == code) {
            	alert("保存成功");
            } else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                userlogout();
            }else
            {
            	alert("保存失败："+response.message);
            }
        }
    });
}

function updatewanshanghuo()
{
	var wid = $("select[name='s_storelists']").val();

    $.ajax({
        type: "post",
        url: "/user/user_setting/update_sm",
        data: {
            "storeId": wid
        },
        success: function (response) {
            var code = response.code;
            if ("200" == code) {
            	alert("保存成功");
            } else if ("607" == code) {
                alert("您尚未登录或登录已失效！");
                userlogout();
            }else
            {
            	alert("保存失败："+response.message);
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
	            	loadstorelist(response.data);
	            	
	            } else if ("607" == code) {
	                alert("您尚未登录或登录已失效！");
	                userlogout();
	            }else
	            {
	            	loadstorelist(null);
	            	
	            }
	        }
	    });
}