//bar pic
$(function () {
    $("#sliderbox").hover(function () {
        $("#sliderbox .bx-prev").fadeIn();
        $("#sliderbox .bx-next").fadeIn()
    }, function () {
        $("#sliderbox .bx-prev").fadeOut();
        $("#sliderbox .bx-next").fadeOut()
    });
    $('#slideshow').bxSlider({
        mode: 'fade',
        controls: true,
        nextSelector: '#slider-next',
        prevSelector: '#slider-prev',
        prevText: '上一张',
        nextText: '下一张',
        auto: true,
        speed: 500,
        pause: 5000,
        pager: true
    })
});

//合作伙伴
$(function () {
    var a = 0.70;
    var b = $('.pic-scroll-list .slide').width();
    if ($(window).width() > 960) {
        $('#home-row-scroll #ticker').bxSlider({
            wrapperClass: 'ticker-wrapper',
            slideWidth: 5000,
            pager: false,
            auto: true,
            minSlides: 5,
            maxSlides: 6,
            pause: 4000,
            speed: 800,
            slideMargin: 40
        })
    } else if ($(window).width() <= 960 && $(window).width() >= 768) {
        $('#home-row-scroll #ticker').bxSlider({
            wrapperClass: 'ticker-wrapper',
            slideWidth: 5000,
            pager: false,
            auto: true,
            minSlides: 4,
            maxSlides: 4,
            pause: 4000,
            speed: 800,
            slideMargin: 20
        })
    } else if ($(window).width() < 768 && $(window).width() > 600) {
        $('#home-row-scroll #ticker').bxSlider({
            wrapperClass: 'ticker-wrapper',
            slideWidth: 5000,
            pager: false,
            auto: true,
            minSlides: 3,
            maxSlides: 3,
            pause: 4000,
            speed: 800,
            slideMargin: 20
        })
    } else if ($(window).width() < 600) {
        $('#home-row-scroll #ticker').bxSlider({
            wrapperClass: 'ticker-wrapper',
            slideWidth: 5000,
            pager: false,
            auto: true,
            minSlides: 2,
            maxSlides: 2,
            pause: 4000,
            speed: 800,
            slideMargin: 20
        })
    }

});

$(function () {
    $("#newlist").html("");
    getgonggaobyguest_index();
});

function getgonggaobyguest_index() {
    //获取公告信息
    $.ajax({
        type: "get",
        url: "/gonggao/search_byguest",
        data: {
            size: "3"//获取条数
        },
        success: function (response) {
            var code = response.code;
            if (code == "200") {
                var str = "";
                if (response.data != null) {

                    $.each(response.data.datas, function () {
                        str += "<li><a href='newsdetail.html?id=" + this.id + "&type=1'><h4>" + this.title + "</h4></a><span>" + this.modifytime + "</span></li>";

                    });
                }
                $("#newlist").append(str);
                getnewsbyguest_index();
            }
        }
    });
}

function getnewsbyguest_index() {
    //获取新闻信息
    $.ajax({
        type: "get",
        url: "/news/search_byguest",
        data: {
            size: "2"//获取条数
        },
        success: function (response) {
            var code = response.code;
            if (code == "200") {
                var str = "";
                if (response.data != null) {
                    $.each(response.data.datas, function () {
                        //if (this.modifytime && this.modifytime.length > 10) { this.modifytime = this.modifytime.substr(0, 10) }
                        str += "<li><a href='newsdetail.html?id=" + this.id + "&type=2'><h4>" + this.title + "</h4></a><span>" + this.modifytime + "</span></li>";
                      
                    });
                }
                $("#newlist").append(str);
            }
        }
    });
}

