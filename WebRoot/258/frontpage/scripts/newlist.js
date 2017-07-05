(function ($) {
    var type = request("type");
    if (type == "0")//新闻
    {
        $("#gonggaotab").removeClass("active");
        $("#new").addClass("active");
        $("#newtab").addClass("active");
        $("#gonggao").removeClass("active");
    }
    else//公告
    {
        $("#newtab").removeClass("active");
        $("#gonggaotab").addClass("active");
        $("#new").removeClass("active");
        $("#gonggao").addClass("active");
    }
	getnewsbyguest_index(1,true);//获取新闻信息
	getgonggaobyguest_index(1,true);//获取公告
	
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
            	    $("#newtab span").html(response.data.rowCount);
            	    $.each(response.data.datas, function () {
            	       
            	        if (this.modifytime && this.modifytime.length > 10) { this.modifytime = this.modifytime.substr(0, 10) }
	            	    str += "<tr><td><p><a href='newsdetail.html?id=" + this.id + "&type=0'>" + this.title + "</a></p></td><td class='date'>" + this.modifytime + "</td></tr>";
	            	});
	            	if (isFirst) {
	            	    var pageCount = Math.ceil(response.data.rowCount / 15.0);
	            	    $("#new .tcdPageCode").createPage({
	            	        pageCount: pageCount,//总页数
	            	        current: pageIndex,//当前页
	            	        backFn: function (p) {
	            	            //单击回调方法，p是当前页码
	            	            getnewsbyguest_index(p,false);
	            	        }
	            	    });
	            	}
            	}
            	$("#new tbody").html(str);
            }
        }
    });
}

//

function getgonggaobyguest_index(pageIndex,isFirst)
{
	//获取新闻信息
    $.ajax({
        type: "get",
        url: "/gonggao/search_byguest",
        data:{
            size: "15"//获取条数
           , pageIndex: pageIndex
        },
        success: function (response) {
            var code = response.code;
            if (code == "200") {
                var str = "";
                if (response.data != null) {
                    $("#gonggaotab span").html(response.data.rowCount);
                    $.each(response.data.datas, function () {
                      
                        if (this.modifytime && this.modifytime.length > 10) { this.modifytime = this.modifytime.substr(0, 10) }
                        str += "<tr><td><p><a href='newsdetail.html?id=" + this.id + "&type=1'>" + this.title + "</a></p></td><td class='date'>" + this.modifytime + "</td></tr>";
                    });
                    var pageCount = Math.ceil(response.data.rowCount / 15.0);
                    if (isFirst) {
                        $("#gonggao .tcdPageCode").createPage({
                            pageCount: pageCount,//总页数
                            current: pageIndex,//当前页
                            backFn: function (p) {
                                //单击回调方法，p是当前页码
                                getnewsbyguest_index(p,false);
                            }
                        });
                    }
                }
                $("#gonggao tbody").html(str);
            }
        }
    });
}

