(function ($) {
	

	var id=request("id");
	var type=request("type");
	if(type=="0")//新闻
	{
		$("#detail_type").html("新闻");
		getnewsbyid(id);
		
	}
	else//公告
	{
		$("#detail_type").html("公告");
		//$("#detail_img").hide();
		getgonggaobyid(id);
	}
	
})(jQuery);

function getnewsbyid(id)
{

	//获取新闻信息
    $.ajax({
        type: "get",
        url: "/news/getbyid",
        data:{
        	"id":id
        },
        success: function (response) {
            var code = response.code;
            if (code == "200") {
            	if(response.data!=null)
            	{
	            	$("#detail_title").html(response.data.title);
	            	$("#detail_content").html(response.data.content);
	            	if(removenull(response.data.picture)!="")
	            	{
	            		$("#detail_img").attr("src",response.data.picture);
	            		
	            	}
	            	else
	            	{
	            		$("#detail_img").hide();
	            	}
	            		
            	}
            }
        }
    });
}



function getgonggaobyid(id)
{//获取公告信息
    $.ajax({
        type: "get",
        url: "/gonggao/getbyid",
        data:{
        	"id":id
        },
        success: function (response) {
            var code = response.code;
            if (code == "200") {
            	if(response.data!=null)
            	{
	            	$("#detail_title").html(response.data.title);
	            	$("#detail_content").html(response.data.content);
	            	if(removenull(response.data.img)!="")
	            	{
	            		$("#detail_img").attr("src",response.data.img);
	            		
	            	}
	            	else
	            	{
	            		$("#detail_img").hide();
	            	}
            	}
            }
        }
    });
    
}