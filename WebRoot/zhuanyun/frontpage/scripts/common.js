//add by hzj 2016-05-10
String.prototype.trim = function () {
    return this.replace(/(^\s*)|(\s*$)/g, "");
};

$(document).ready(function () {

    $("#navbar li").each(function () {
        var href = $(this).find("a").attr("href");
        if (window.location.href.indexOf(href) > 0) {
            $(this).addClass("navactive");
        }
    });
    //手机查单
    $("#phoneSearch").click(function () {
        search("#txtPhoneBill");
    });
});

function clean() {
    $('#txtbill').val('');
}

function search(selector) {
    if (selector == undefined) { selector = "#txtbill"; }
    var billnos = checkBill(selector, 9, 15);
    // alert(billnos);
    if (billnos != false) {
        window.location.href = "query.html?billno=" + billnos.toString();
    }
}

//验证单号
function checkBill(selector, minlength, maxlength) {
    var txtbill = $.trim($(selector).val());
    if (txtbill == "") {
        alert("请输入运单号码 !!");
        return false;
    }

    //替换所有空格
    txtbill = txtbill.replace(/\s+/g, '\r\n');
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

        var j = 0;
        for (var i = 0; i < list.length; i++) {
            var len = j + 1;
            var bill = sTrim($.trim(list[i]), "g");

            if (bill.length < minlength) {
                msg += '第' + len + '个快件单号[' + bill + ']长度必须大于等于' + minlength + '!\n';
            }
            else if (bill.length > maxlength) {
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

function removenull(str) {
    if (str == "" || (str == null) || (str == "null") || (str == "undefined")) {
        return "";
    }
    else {
        return str;
    }
}

$(function () {
    if ($(window).width() > 640) {
        var wh = $(window).outerHeight();
        var mh = $(".container").outerHeight();
        var hh = $("header").outerHeight();
        var fh = $("footer").outerHeight();
        var sh = $("#sliderbox").outerHeight() || 0;
        if (mh + hh + fh + sh < wh) {
            $(".container").first().css("min-height", wh - hh - fh - 100);
        }
    }
    console.log("WindowHeight:" + wh + ",ContainerHeight:" + mh + ",HeaderHeight:" + hh + ",FooterHeight:" + fh)
});


(function ($) {
    $.extend({
        setCookie: function (c_name, value, exdays) {
            try {
                if (!c_name) return false;
                var exdate = new Date();
                exdate.setDate(exdate.getDate() + exdays);
                var c_value = escape(value) + ((exdays == null) ? "" : "; expires=" + exdate.toUTCString());
                document.cookie = c_name + "=" + c_value;
            }
            catch (err) {
                return '';
            };
            return '';
        }
    });

    $.extend({
        getCookie: function (c_name) {
            try {
                var i, x, y,
                    ARRcookies = document.cookie.split(";");
                for (i = 0; i < ARRcookies.length; i++) {
                    x = ARRcookies[i].substr(0, ARRcookies[i].indexOf("="));
                    y = ARRcookies[i].substr(ARRcookies[i].indexOf("=") + 1);
                    x = x.replace(/^\s+|\s+$/g, "");
                    if (x == c_name) return (y);
                };
            }
            catch (err) {
                return '';
            };
            return '';
        }
    });
})(jQuery);