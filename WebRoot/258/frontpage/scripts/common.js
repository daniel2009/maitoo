//add by hzj 2016-05-10
String.prototype.trim = function () {
    return this.replace(/(^\s*)|(\s*$)/g, "");
};

$(document).ready(function () {
    //返回顶部
    $("#gototop").click(function () {
        $("html,body").animate({ scrollTop: 0 }, 800); return false;
    });
    $("#gotocate").click(function () {
        $("html,body").animate({ scrollTop: $(".navbar-brand").offset().top }, 800); 
        $("button[data-target=#navbar]").click();
        return false;
    });


    // 搜索
    $("#small_search").click(function () {
        $("#topsearch").slideToggle();
    });

    if ($(window).width() > 768) {
        //鼠标划过就展开子菜单
        $('ul.nav li.dropdown').hover(function () {
            $(this).find('.dropdown-menu').stop(true, true).slideDown();
        }, function () {
            $(this).find('.dropdown-menu').stop(true, true).slideUp();
        });

        //scrollTop
        // $(window).scroll(function(){
        //     var scrolls = $(window).scrollTop()
        //     if (scrolls > 10) {
        //       $(".navbar").addClass("small-nav")
        //     }else{
        //       $(".navbar").removeClass("small-nav")
        //     }
        // });

    }

    //左侧导航菜单
    // if ($("#firstpane .menu_body:eq(0)").text().replace(/[\r\n ]/g,"").length>0) {
    //   $("#firstpane .menu_body:eq(0)").show().prev().html("-").prev().addClass("left_active");
    // };
    $("ul.menu_body").each(function () {
        if ($(this).text().replace(/[\r\n ]/g, "").length <= 0) { $(this).prev().remove(); } //去掉span
    });

    $("#firstpane span.menu_head").click(function () {
        var spanatt = $(this).next("ul.menu_body").css('display');
        if (spanatt == "block") {
            var spantext = "+";
            $(this).prev().removeClass("left_active");
        } else {
            var spantext = "-";
            $(this).prev().addClass("left_active");
        }
        $(this).html(spantext).addClass("current").next("ul.menu_body").slideToggle(300).siblings("ul.menu_body");
    });

    $("#navbar li").each(function () {
        var href = $(this).find("a").attr("href");
        if (window.location.href.indexOf(href) > 0) {
            $(this).addClass("navactive");
           
        }
    });

   
});

function clean() {
    $('#txtbill').val('');
}

function search(selector) {
    if (selector == undefined) { selector = "#txtbill"; }
    var billnos = checkBill(selector,9, 15);
   // alert(billnos);
    if (billnos != false) {
        window.location.href = "query.html?billno=" + billnos.toString();
    }
}

//验证单号
function checkBill(selector, minlength,maxlength) {
    var txtbill = $.trim($(selector).val());
    if (txtbill == "") {
        alert("请输入运单号码 !!");
        return false;
    }

	 //替换所有空格
	txtbill = txtbill.replace(/\s+/g,'\r\n');
    var listI = txtbill.split("\r\n");
    var listF = txtbill.split("\n");
    //var listF = txtbill.split("\r");
    var list = null;
    if (listI.length > listF.length) {
        list = listI;
    } else {
        list = listF;
    }
    if (list.length > 20) {
        alert('一次最多只能查询20个快件单号');
        return false;
    }
    var msg = "";
    
    if (length != undefined) {

    	var j=0;
        for (var i = 0; i < list.length; i++) {
            var len = j + 1;
            var bill = sTrim($.trim(list[i]), "g");
           
            if(bill.length<minlength)
            {
            	 msg += '第' + len + '个快件单号[' + bill + ']长度必须大于等于' + minlength + '!\n';
            }
            else if(bill.length>maxlength)
            {
            	msg += '第' + len + '个快件单号[' + bill + ']长度必须小于等于' + maxlength + '!\n';
            }
            
            j++;
           /* else if (isNaN(bill)) {
                msg += '第' + len + '个快件单号必须是数字,且为半角输入法!\n';
            }*/
            //if (bill.length != length && bill.length != 0) {
             //   msg += '第' + len + '个快件单号[' + bill + ']长度必须等于' + length + '!\n';
          //  }
            //else if (isNaN(bill)) {
            //    msg += '第' + len + '个快件单号必须是数字,且为半角输入法!\n';
            //}
        }
    }
    if (msg != "") {
        alert("亲，出现错误啦！\r\n" + msg);
        return false;
    } else {
        return list;
    }
}
function sTrim(str, isglobal) {
    var result;
    result = str.replace(/(^\s+)|(\s+$)/g, "");
    if (isglobal.toLowerCase() == "g")
        result = result.replace(/\s/g, "");
    return result;
}

function request(strParame) {
    var args = new Object();
    var query = location.search.substring(1);

    var pairs = query.split("&"); // Break at ampersand 
    for (var i = 0; i < pairs.length; i++) {
        var pos = pairs[i].indexOf('=');
        if (pos == -1) continue;
        var argname = pairs[i].substring(0, pos);
        var value = pairs[i].substring(pos + 1);
        value = decodeURIComponent(value);
        args[argname] = value;
    }
    return args[strParame];
}

function removenull(str)
{
    if(str==""||(str==null)||(str=="null")||(str=="undefined"))	
    {
    	return "";
    }
    else
    {
    	return str;
    }
}

$(function () {
    if ($(window).width() > 640) {
        var wh = $(window).outerHeight();
        var mh = $("main").outerHeight();
        var hh = $("header").outerHeight();
        var fh = $("footer").outerHeight();
        if (mh + hh + fh < wh) {
            $("main").css("min-height",wh - hh - fh-34);
        }
    }
    console.log("wh:" + wh + ",mh:" + mh + ",hh:" + hh + ",fh:" + fh)
})