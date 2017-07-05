$(function () {
    if ($(window).width() >= 768) {
        $(".navi li").hover(function () {
            $(this).find('ul:first').slideDown("fast").css({
                display: "block"
            })
        }, function () {
            $(this).find('ul:first').slideUp("fast").css({
                display: "none"
            })
        })
    }
});
$(function () {
    if ($(window).width() > 768) {
        $(window).scroll(function () {
            if ($(window).scrollTop() >= 90) {
                $(".header").addClass("head-pinned")
            } else {
                $(".header").removeClass("head-pinned")
            }
        })
    }
});
$(function () {
    $('#mobile-menu').click(function () {
        $(".main-menu").slideToggle("fast");
        $(this).toggleClass('opacity');
        $("#search-box").css({
            display: "none"
        });
        $("#mobile-so").removeClass('opacity')
    })
});
$(function () {
    $("#btn-so").click(function () {
        $("#search-box").slideToggle("fast");
        $(this).toggleClass('opacity')
    });
    $("#mobile-so").click(function () {
        $("#search-box").slideToggle("fast");
        $(this).toggleClass('opacity');
        $(".main-menu").css({
            display: "none"
        });
        $('#mobile-menu').removeClass('opacity')
    })
});
$(function () {
    $(".postlist li:last").addClass("nb");
    $(".foot-rt img").addClass("fadeInLeft wow animated")
});



$(function () {
    var b = "",
        
		$backToTopEle = $('<a class="backToTop" title="返回顶部"></a>').appendTo($("body")).text(b).attr("title", b).click(function () {
		    $("html, body").animate({
		        scrollTop: 0
		    }, 120)
		}),
		$backToTopFun = function () {
		    var a = $(document).scrollTop(),
				winh = $(window).height();
		    (a > 0) ? $backToTopEle.fadeIn() : $backToTopEle.fadeOut();
		    if (!window.XMLHttpRequest) {
		        $backToTopEle.css("top", a + winh - 166)
		    }
		};
    $(window).bind("scroll", $backToTopFun);
    $(function () {
        $backToTopFun()
    })
});

/*在线客服*/
$(function () {
    $('#close_im').bind('click', function () {
        $('#main-im').css("height", "0");
        $('#im_main').hide();
        $('#open_im').show();
    });
    $('#open_im').bind('click', function (e) {
        $('#main-im').css("height", "272");
        $('#im_main').show();
        $(this).hide();
    });
    $('.go-top').bind('click', function () {
        $(window).scrollTop(0);
    });
    $(".weixing-container").bind('mouseenter', function () {
        $('.weixing-show').show();
    })
    $(".weixing-container").bind('mouseleave', function () {
        $('.weixing-show').hide();
    });
});