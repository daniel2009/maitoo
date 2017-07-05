$(function () {
    //获取用户信息
    $.ajaxEx({
        type: "get",
        url: "/user/get_self",
        success: function (response) {
            var code = response.code;
            console.log("get_self,code:"+code);
            if (code == "200") {
                setSpanText("#userinfo", response.data);
                $("#userinfo span[name='usertype']").html(getusertype(response.data.type));
               
                top.window.document.getElementById("username").innerHTML=response.data.realName;
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
						$("#rmb_usa_rate").html("汇率："+response.data[0]);
						$("#rmb_usa_rate").show();
					}	

				} else {

				}
			} else {

			}
			
		}
	});
    
    
    $.ajax({
		type : "post",
		url : "/m_order/get_counts",
		success : function(response) {
			var code = response.code;

			if ("200" == code) {

				if (response.data != null) {
					setSpanText("#orderQuantity", response.data);
					

				} else {

				}
			} else {

			}
			
		}
	});
    getstorelistnfo();
    
});

function addonlineorder()
{
	window.location.href = "zaixianzhidan.html";
}

function getstorelistnfo()
{
	
	
	


	//获取渠道显示信息
    $.ajax({
        type: "get",
        url: "/store/guest_store_list",
        success: function (response) {
            var code = response.code;
            if (code == "200") {
            
            	var str="";
            	if(response.data!=null)
            	{
            		$.each(response.data,function(){
            			
            			if(((this.showstore==1)||(this.showstore=="1")))
	                    {
		                    		
	            			var address1="";
	            			var mapaddress="";
	            			var str='<div class="profile-contact-links align-left addressbox">';
	            			str+='<div class="addresstitle">';
	            			str+=this.name;
	            			str+='</div>';
	            			str+=' <table><tbody>';
	            			str+=' <tr><td class="name">';
	            			str+="地址/Address:";
	            			str+="</td>";
	            			str+="<td class='value'>";	
	            			str+=this.address;
	            			str+="</td>";
	            			str+="</tr>";
	        
	            			
	            			str+=' <tr><td class="name">';
	            			str+="城市/City:";
	            			str+="</td>";
	            			str+="<td class='value'>";	
	            			str+=this.city;
	            			str+="</td>";
	            			str+="</tr>";
	            			
	            			str+=' <tr><td class="name">';
	            			str+="州/State:";
	            			str+="</td>";
	            			str+="<td class='value'>";	
	            			str+=this.province;
	            			str+="</td>";
	            			str+="</tr>";
	            			
	            			str+=' <tr><td class="name">';
	            			str+="邮编/Zip Code:";
	            			str+="</td>";
	            			str+="<td class='value'>";	
	            			str+=this.zipCode;
	            			str+="</td>";
	            			str+="</tr>";
	            			
	            			str+=' <tr><td class="name">';
	            			str+="电话/Tel:";
	            			str+="</td>";
	            			str+="<td class='value'>";	
	            			str+=this.telephone;
	            			str+="</td>";
	            			str+="</tr>";
	            			str+=" </tbody></table></div>";	
	            			
	                        
	            			   $("#storelist_0").append(str);
	                    }
            		});
            		
            	}
            	
            	
            }
        }
    });
}
