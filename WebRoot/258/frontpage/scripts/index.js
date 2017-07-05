(function ($) {

    /*-----------------
		CAROUSELS
	-----------------*/

	getnewsbyguest_index();//首页获取新闻信息
	getgonggaobyguest_index();//获取公告
    $('#owl-brands').owlCarousel({
        items: 5,
        itemWidth: 250,
        itemsDesktop: [1260, 4],
        itemsTablet: [1024, 3],
        itemsTabletSmall: [860, 2],
        itemsMobile: [460, 1],
        navigation: true,
        navigationText: false
    });

    function registerCarousels(carousels) {
        for (var i = 0; i < carousels.length; i++) {
            var id = carousels[i],
				owl = $(id).data('owlCarousel');

            $(id).siblings('.slide-controls').find('.button.next').bind('click', { owlObject: owl }, nextSlide);
            $(id).siblings('.slide-controls').find('.button.prev').bind('click', { owlObject: owl }, prevSlide);
        }
    }

    function nextSlide(e) {
        e.preventDefault();
        e.data.owlObject.next();
    }

    function prevSlide(e) {
        e.preventDefault();
        e.data.owlObject.prev();
    }

    var carousels = ['#owl-brands'];

    registerCarousels(carousels);



    /*-----------------  News -----------------*/
    function getNews() {
        $.ajax({
            type: "get",
            url: "/admin/news/search",
            success: function (response) {
                var code = response.code;

                if (code == "200") {
                    

                } else {
                   console.log("获取新闻失败")
                }
            }
        });
    }

})(jQuery);

function getnewsbyguest_index()
{
	$("#newlist").html("");
	//获取新闻信息
    $.ajax({
        type: "get",
        url: "/news/search_byguest",
        data:{
        	size:"4"//获取条数
        },
        success: function (response) {
            var code = response.code;
            if (code == "200") {
            	//
            	var str="";
            	if(response.data!=null)
            	{
            		var num=0;
	            	$.each(response.data.datas,function(){
	            	    num++;
	            	    if (this.modifytime && this.modifytime.length > 10) { this.modifytime = this.modifytime.substr(0, 10) }
	            		if(num>2)
	            		{
	            			str+="<li><span>"+this.modifytime+"</span><a href='newsdetail.html?id="+this.id+"&type=0'>"+this.title+"</a></li>";
	            		}
	            		else
	            		{
	            			str+="<li><span>"+this.modifytime+"</span><a href='newsdetail.html?id="+this.id+"&type=0' class='new'>"+this.title+"</a></li>";
	            		}
	            		
	            	});
            	}
            	$("#newlist").html(str);
            }
        }
    });
}



function getgonggaobyguest_index()
{
	$("#gonggaolist").html("");
	//获取新闻信息
    $.ajax({
        type: "get",
        url: "/gonggao/search_byguest",
        data:{
        	size:"4"//获取条数
        },
        success: function (response) {
            var code = response.code;
            if (code == "200") {
            	//
            	var str="";
            	if(response.data!=null)
            	{
            		var num=0;
            		$.each(response.data.datas, function () {
            		    if (this.modifytime && this.modifytime.length > 10) { this.modifytime = this.modifytime.substr(0, 10) }
	            		num++;
	            		if(num>1)
	            		{
	            			str+="<li><span>"+this.modifytime+"</span><a href='newsdetail.html?id="+this.id+"&type=1'>"+this.title+"</a></li>";
	            		}
	            		else
	            		{
	            			str+="<li><span>"+this.modifytime+"</span><a href='newsdetail.html?id="+this.id+"&type=1' class='new'>"+this.title+"</a></li>";
	            		}
	            	});
            	}
            	$("#gonggaolist").html(str);
            }
        }
    });
}