//重写alert方法
window.alert = function (msg, callback) {
    if (arguments.length == 2 && typeof (callback) == "function") {
        top.window.infoDialog2(msg, "提示信息", callback);
    } else {
        top.window.infoDialog("提示信息", msg, true);
    }
}

//add by hzj 2016-05-10
String.prototype.trim = function () {
    return this.replace(/(^\s*)|(\s*$)/g, "");
};

//add by hzj 2016-05-11 扩展ajax方法为ajaxEx
(function ($) {
    $.extend({
        ajaxEx: function (options) {
            var defaultOptions = {
                type: 'post',
                contentType: 'application/json; charset=utf-8',
                error: function (XMLHttpRequest, textStatus) {
                    alert("服务器出错：" + XMLHttpRequest.responseText);
                }
            };

            if (options.data != undefined) {
                options.data = $.toJSON(options.data);
            }
            if (options.url != undefined) {
                if (options.url.indexOf("?") > 0) {
                    options.url += "&ran=" + Math.random();
                } else {
                    options.url += "?ran=" + Math.random();
                }
            }
            $.extend(defaultOptions, options);
            $.ajax(defaultOptions);
        }
    });

})(jQuery)


//对某个标签内的input进行赋值
function setInputData(selector, obj) {
    var propertyNames = getObjectPropNameArr(obj);
    $(selector + " select," + selector + " input," + selector + " textarea").each(function (j) {
        for (var i = 0; i < propertyNames.length; i++) {
            var name = $(this).attr("name");
            try {
                if (name != undefined && name == propertyNames[i]) {
                    $(this).val(obj[propertyNames[i]]);
                    break;
                }
            } catch (e) {
                console.log(e);
            }
        }
    });
}

//对某个标签内的input进行赋值
function setSpanText(selector, obj) {
    var propertyNames = getObjectPropNameArr(obj);
    $(selector + " span").each(function (j) {
        for (var i = 0; i < propertyNames.length; i++) {
            try {
                if ($(this).attr("name") == propertyNames[i]) {
                    $(this).html(obj[propertyNames[i]]);
                    break;
                }
            } catch (e) {
                console.log(e);
            }
        }
    });
}

//对某个标签内的img的src进行赋值
function setImgSrc(selector, obj) {
    var propertyNames = getObjectPropNameArr(obj);
    $(selector + " img").each(function (j) {
        for (var i = 0; i < propertyNames.length; i++) {
            if ($(this).attr("name") == propertyNames[i]) {
                //$(this).html(obj[propertyNames[i]]);
                $(this).attr("src", obj[propertyNames[i]]);
                break;
            }
        }
    });
}

//获取某个标签内的input select的值返回对象
function getEelementData(selector) {
    var obj = {};
    $(selector + " select," + selector + " input," + selector + " textarea").each(function (j) {
        obj[$(this).attr("name")] = $.trim($(this).val());
    });
    return obj;
}



//获取jqgrid选中行的数据
function getGridSelectData(selector) {
    var rowid = $(selector).jqGrid('getGridParam', 'selrow');
    var rowData = $(selector).getRowData(rowid);
    return rowData;
}

//隐藏checkbox
function hideJgridCheckbox(yundangrid_selector, ids) {
    for (var i = 0; i < ids.length; i++) {
        var id = ids[i];
        $(yundangrid_selector).find("[id=" + id + "]").find("[role=checkbox]").hide().attr("disabled", true);
    }
}


//返回js对象的属性名称
function getObjectPropNameArr(o) {
    var tmpArr = [];
    for (var item in o) {
        tmpArr.push(item);
    }
    return tmpArr;
}

function clearInputData(selector) {
    if (selector == undefined) { selector = "body"; }
    $(selector + " select," + selector + " input," + selector + " textarea").each(function (j) {
        $(this).val("");
    });
}

$(function () {
    $("[dateformat]").each(function () {
        $(this).datetimepicker({
            format: $(this).attr("dateformat"),
            autoclose: true,
            language: "zh-CN",
            forceParse: false,
            startView: 2,
            minView: 2
        });
        //console.trace($(this));
    });

});

//当光标不在文本框时屏蔽backSpace
window.onload = function () {
    document.getElementsByTagName("body")[0].onkeydown = function () {

        //获取事件对象
        var elem = event.relatedTarget || event.srcElement || event.target || event.currentTarget;

        if (event.keyCode == 8) {//判断按键为backSpace键

            //获取按键按下时光标做指向的element
            var elem = event.srcElement || event.currentTarget;

            //判断是否需要阻止按下键盘的事件默认传递
            var name = elem.nodeName;

            if (name != 'INPUT' && name != 'TEXTAREA') {
                return _stopIt(event);
            }
            var type_e = elem.type.toUpperCase();
            if (name == 'INPUT' && (type_e != 'TEXT' && type_e != 'TEXTAREA' && type_e != 'PASSWORD' && type_e != 'FILE')) {
                return _stopIt(event);
            }
            if (name == 'INPUT' && (elem.readOnly == true || elem.disabled == true)) {
                return _stopIt(event);
            }
        }
    }
}

//数组的某个元素移除
Array.prototype.remove = function (value) {
    var arr = [];
    for (var i = 0; i < this.length; i++) {
        if (this[i] != value) {
            arr.push(this[i]);
        }
    }
    return arr;
}

//通过id返回datas中的行数据
Array.prototype.getData = function (id) {
    for (var i = 0; i < this.length; i++) {
        if (this[i].id == undefined && this[i] == id) {
            return this[i];
        }
        if (this[i].id != undefined && this[i].id == id) {
            return this[i];
        }
    }
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

//退出后台
function adminlogout() {
    $.get("../../admin/emp/logout?ran="+Math.random(), function (response) {
        // top.location.href = "../admin/login.html";
    });
    top.location.href = "../admin/login.html";
};

//退出后台
/*function userlogout() {
    $.get("/user/logout", function (response) {
        top.location.href = "../frontpage/login.html";
    });
    return false;
}*/


//去掉空字符
function removenull(str) {
    if ((str == "") || (str == null) || (str == "null") || (str == "undefined")) {
        return "";
    }
    else {
        return str;
    }
}

//输入状态值返回状态描述文字
function morderstatemap(statevalue) {
    if (statevalue == "-10") {
        return "在线置单";
    }
    if (statevalue == "-9") {
        return "转运置单";
    }
    else if (statevalue == "0") {
        return "运单作废";
    }
    else if (statevalue == "1") {
        return "包裹异常";
    }
    else if (statevalue == "2") {
        return "已揽收";
    }
    else if (statevalue == "3") {
        return "送往集散中心";
    }
    else if (statevalue == "4") {
        return "已打件";
    }
    else if (statevalue == "5") {
        return "送往机场";
    }
    else if (statevalue == "6") {
        return "空运中";
    }
    else if (statevalue == "7") {
        return "海关清关";
    }
    else if (statevalue == "8") {
        return "美淘中国分部已揽收";
    }
    else if (statevalue == "9") {
        return "收件人已接收";
    }
    else {
        return "未知状态";
    }


}

//输入状态值返回状态描述文字
function mordertypemap(typevalue) {



    if (typevalue == "1") {
        return "门市运单";
    }
    else if (typevalue == "2") {
        return "转运运单";
    }
    else if (typevalue == "3") {
        return "网上置单";
    }
    else if (typevalue == "4") {
        return "第三方运单";
    }
    else if (typevalue == "5") {
        return "上门收货";
    }
    else if (typevalue == "6") {
        return "空运单";
    }
    else if (typevalue == "7") {
        return "批量生成门市运单";
    }
    else {
        return "未知运单";
    }


}

//会员信息



function getusertype(cellvalue) {
    if (cellvalue == "0") {
        return "普通会员";
    }
    else if (cellvalue == "1") {
        return "白银VIP会员";
    }
    else if (cellvalue == "2") {
        return "黄金VIP会员";
    }
    else if (cellvalue == "3") {
        return "白金VIP会员";
    }
    else if (cellvalue == "4") {
        return "钻石VIP会员";
    }
    else if (cellvalue == "5") {
        return "至尊VIP1会员";
    }
    else if (cellvalue == "6") {
        return "至尊VIP2会员";
    }
    else if (cellvalue == "7") {
        return "至尊VIP3会员";
    }
    else if (cellvalue == "8") {
        return "至尊VIP4会员";
    }
    else {
        return "会员未定";
    }
}
//支付方式映射
function morderpaywaymap(value) {
    if (value == "1") {
        return "现金支付";
    } else if (value == "0") {
        return "余额支付";
    } else if (value == "2") {
        return "用户自付";
    } else {
        return "非法支付";
    }
}


//弹出显示大图片
function showbigpic(obj) {

    //alert($(obj).parent().find('img').attr('src'));
    //<img id="popimg" src="" height="50" width="70"/>

    var src = $(obj).parent().find('img').attr('src');
    if (removenull(src) == "") {
        return false;
    }
    //alert(src);
    $("#popimg").attr("src", src);
    dialog({
        title: "运单详细信息",
        content: $("#divbigimg").html(),
        //width: $(window).width() - 60,
        height: $("#divbigimg").height() + 60,
        //cancelVal: '关闭',
        //cancel: true //为true等价于function(){}
    }).show();



}


//删除原有图片
function deletepics(obj) {

    //alert($(obj).parent().find('img').attr('src'));
    //<img id="popimg" src="" height="50" width="70"/>
    if (confirm("确认删除此图片?")) {
        $(obj).parent().find('img').attr('src', "");
    }


}
//用户退出登陆的处理方式
function userlogout() {
    $.get("/user/logout?ran=" + Math.random(), function (response) {
        console.log("logout:", response.code);
        top.location.href = "../frontpage/login.html";
    });
    //top.location.href = "../frontpage/login.html";
}
//判断是不是整数，true是，false不是

function isInteger(obj) {

    return obj % 1 === 0;
}

//修复左侧菜单点击
$(function () {
    $("[data-toggle=dropdown]").click(function () {
        var thisUl = $(this).parent().find("ul");
        var v = thisUl.is(':visible');
        if ($(this).attr("id") != "topdropdown") {
            $("[data-toggle=dropdown]").parent().find("ul").hide();
        }

        if (v) {
            thisUl.hide();
        } else {
            thisUl.show();
        }
    });
});



//输入状态值返回状态描述文字
function torderstatemap(statevalue) {
    if (statevalue == "0") {
        return "用户预报";
    }
    else if (statevalue == "1") {
        return "录入失败";
    }
    else if (statevalue == "2") {
        return "包裹异常";
    }
    else if (statevalue == "3") {
        return "转运入库";
    }
    else if (statevalue == "4") {
        return "转运出库";
    }
    else if (statevalue == "5") {
        return "已入仓库";
    }
    else if (statevalue == "6") {
        return "已完成";
    }
    else if (statevalue == "7") {
        return "已自提";
    }
    else {
        return "未知状态";
    }


}
