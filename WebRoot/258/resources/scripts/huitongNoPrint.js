
//初始列表数据
$(function ($) {

    //绑定运单输入框的enter事件进行搜索
    $("input[name='orderId']").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btprintorder").click();
        }
    }));
});

//打印汇通单号
function PrintHuitong() {
    var date = $("input[name='date']").val();
    var orderId = $("input[name='orderId']").val();
    if ((date == "") || (date.trim() == "")) {
        alert("必须选择打印日期");
        return false;
    }
    if ((orderId == "") || (orderId.trim() == "")) {
        alert("运单号不能为空");
        return false;
    }
    orderId = orderId.trim();
    if (orderId.length < 5) {
        alert("运单号太短，请检查是否正确!");
        return false;
    }


    var iframeid = "iframePrint_huitong";
    document.getElementById(iframeid).contentWindow.document.write("");
    $("#" + iframeid).attr("src", "").attr("src", "yz_printpageremin.html?orderId=" + orderId + "&date=" + date + "&ran=" + Math.random());
    $("#print_result").html("");
    $("#print_result").html("打印：" + orderId);
    beginPrint(iframeid, "Full-Page");

    // beginPrint(iframeid, "Full-Page");

    // alert("打印成功！");

}


function getMyLodop() {
    if (LODOP == undefined) {
        LODOP = getLodop();
    }
}

//printMode参数
//“Full-Width” –宽度按纸张的整宽缩放；
//“Full-Height”–高度按纸张的整高缩放：
//“Full-Page” –按整页缩放，也就是既按整宽又按整高缩放；
//此外还可以按具体百分比例，格式为“Width:XX%;Height:XX%”或“XX%”

function beginPrint(iframeid, printMode) {

    getMyLodop();
    var win = document.getElementById(iframeid).contentWindow;
    var isNotPrint = true;

    //if (printMode == undefined) { printMode = "100%" }
    if (printMode == undefined) { printMode = "Full-Width"; }
    var t = setInterval(function () {
        try {
            if (isNotPrint && win.isLoad && win.printornot == true) {
                if (LODOP == undefined) {
                    win.printornot = false;
                    isNotPrint = false;
                    win.print();
                    $("input[name='orderId']").val("");
                } else {
                    win.printornot = false;
                    isNotPrint = false;
                    LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT", printMode);
                    ////ADD_PRINT_HTM(Top,Left,Width,Height,strHtmlContent)
                    LODOP.ADD_PRINT_HTM("0.5cm", "0.5cm", "RightMargin:0.1cm", "BottomMargin:0.5cm", win.document.documentElement.innerHTML);
                    // LODOP.PREVIEW();//预览打印
                    LODOP.PRINT();
                    $("input[name='orderId']").val("");
                }
                clearInterval(t);
            }
            
        } catch (e) {
            isNotPrint = true;
            console.log(e.message);
        }

    }, 300);
    $("input[name='orderId']").val("");
}

