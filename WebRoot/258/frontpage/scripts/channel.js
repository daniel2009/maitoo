(function ($) {
	

	getchannelinfo();
	
})(jQuery);

function getchannelinfo()
{

	//获取渠道显示信息
    $.ajax({
        type: "get",
        url: "/channel/get_show_info",
        success: function (response) {
            var code = response.code;
            if (code == "200") {
            	$("#showchannelinfo tbody").html("");
            	var str="";
            	if(response.data!=null)
            	{
            		$.each(response.data,function(){
            			str+="<tr>";
                		str+="<td style='text-align:center;width:15%'><strong>";
                		str+=removenull(this.channelName);
                		str+="</strong></td>";
                		str+="<td style='width:15%'>";
                		str+=removenull(this.show_type);
                		str+="</td>";
                		
                		str+="<td>";
                		var cstr="";
                		if(this.commudityName!=null)
                		{
                			for(var i=0;i<this.commudityName.length;i++)
                			{
                				if(cstr=="")
                				{
                					cstr=this.commudityName[i];
                				}
                				else
                				{
                					cstr=cstr+"、"+this.commudityName[i];
                				}
                			}
                		}
                		
                		if(removenull(cstr)!="")
                		{
                			cstr=cstr+"等。";
                		}
                		str+=cstr;
                		str+="</td>";
                		str+="<td style='width:15%'>";
                		str+=removenull(this.show_remark);
                		str+="</td>";
                		str+="</tr>";
            		});
            		
            	}
            	
            	$("#showchannelinfo tbody").html(str);
            }
        }
    });
}

