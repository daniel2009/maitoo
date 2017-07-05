(function ($) {
   
	
})(jQuery);

function getnewsbyguest_index(pageIndex,isFirst)
{
	//获取新闻信息
    $.ajax({
        type: "get",
        url: "/news/search_byguest",
        data:{
            size: "15"//获取条数
            ,pageIndex:pageIndex
        },
        success: function (response) {
            var code = response.code;
            if (code == "200") {
            	var str="";
            	if(response.data!=null)
            	{
            	   
            	}
            	
            }
        }
    });
}

