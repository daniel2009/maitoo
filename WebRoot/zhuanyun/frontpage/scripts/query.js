
$(function () {
    $("#query").click(function () {
        $("#pcsearch").toggle();
        if ($("#pcsearch").css("display") == "none") {
            $(this).text("展开运单查询窗口");
        } else {
            $(this).text("收起运单查询窗口");
        }
    });
    var yundanhaos = request("billno");
    if (yundanhaos == "" || yundanhaos == "undefined" || yundanhaos == null) { return; }//没有传值过来不处理
    searchlist(yundanhaos);
});


function searchlist(yundanhaos) {
    //alert(yundanhaos);
    var array = yundanhaos.split(',');
    for (var i = 0 ; i < array.length; i++) {
        if (array[i] == "" || typeof (array[i]) == "undefined" || array[i].trim() == "") {
            array.splice(i, 1);
            i = i - 1;
        }
    }

    //alert(array);
    $.ajax({
        url: "/m_route/guest_routes",
        type: "post",
        data: { oids: array },
        traditional: true,
        success: function (data) {
            //根据data 构造tr
            //$(".table body").append(tr);

            createroutetable(data);
        },
        error: function (XMLHttpRequest, textStatus) {
            alert("查询发生错误");
        },
        complete: function () {

        }
    });
}

function createroutetable(table) {
    $("#orderlistshow").html("");
    if (table == null || (table == "undefined") || (table == "")) {
        alert("查询结果出错！");
        return false;
    }

    if (table.code == "200")//表示查询成功
    {
        if (table.data != null) {
            for (var i = 0; i < table.data.length; i++)//第一条运单返回的数据
            {
                if (table.data[i].route != null) {
                    if (table.data[i].route.code == "200") {
                        var colortr = "";
                        if (i % 2 == 1) {
                            colortr = "background-color:rgba(79, 224, 5, 0.27);";
                        }
                        //alert(table.data[i].orderId);
                        var alllength = table.data[i].route.data.length;

                        var ii = 0;
                        if (alllength == "0") {
                            var str3 = "<tr style='" + colortr + "'>";
                            str3 += "<td style='text-align:center;vertical-align:middle;'>" + table.data[i].orderId + "</td>";
                            str3 += "<td></td>";
                            str3 += "<td></td>";
                            str3 += "<td></td>";
                            str3 += "<td style='color:red;'>" + "查找失败！" + "</td>";
                            str3 += "</tr>";
                            $("#orderlistshow").append(str3);
                            continue;
                        }
                        $.each(table.data[i].route.data, function () {
                            var str = "<tr style='" + colortr + "'>";
                            if (ii == 0) {
                                str += "<td rowspan='" + alllength + "' style='text-align:center;vertical-align:middle;'>" + table.data[i].orderId + "</td>";
                            }
                            else {
                                if (ii < alllength)
                                { }
                                else
                                {
                                    str += "<td  style='text-align:center;vertical-align:middle;'>" + this.orderId + "</td>";
                                }
                            }
                            str += "<td>" + removenull(this.date) + "</td>";
                            str += "<td>" + removenull(this.state) + "</td>";
                            str += "<td>" + removenull(this.stateRemark) + "</td>";
                            str += "<td>" + removenull(this.remark) + "</td>";
                            str += "</tr>";
                            ii++;

                            $("#orderlistshow").append(str);
                            
                        });

                    }
                    else {
                        var str1 = "<tr>";
                        str1 += "<td style='text-align:center;vertical-align:middle;'>" + table.data[i].orderId + "</td>";
                        str1 += "<td></td>";
                        str1 += "<td></td>";
                        str1 += "<td></td>";
                        str1 += "<td style='color:red;'>" + table.data[i].route.message + "</td>";
                        str1 += "</tr>";
                        $("#orderlistshow").append(str1);
                    }
                }
                else {
                    var str2 = "<tr>";
                    str2 += "<td style='text-align:center;vertical-align:middle;'>" + table.data[i].orderId + "</td>";
                    str2 += "<td></td>";
                    str2 += "<td></td>";
                    str2 += "<td></td>";
                    str2 += "<td style='color:red;'>查询失败</td>";
                    str2 += "</tr>";
                    $("#orderlistshow").append(str2);
                }
            }

            hideTd();
        }
        else {
            alert("查询结果出错！");
            return false;
        }
    }
    else {
        alert("查询出错，原因：" + table.message);
    }
   
}

function search_query(selector) {
    if (selector == undefined) { selector = "#txtbill"; }
    var billnos = checkBill(selector, 9, 15);

    if (billnos != false) {
        searchlist(billnos.toString());
    }
}

function hideTd() {
    if ($(window).width() < 640) {
        {
            //手机查询列表
            $("#alternatecolor tr th").first().hide();
            $("#alternatecolor tr th:eq(3)").hide();

            var i = 1;
            $("#alternatecolor tr").each(function () {
                if (i > 2) {
                    $(this).find("td:eq(2)").hide();
                } else {
                    $(this).find("td:eq(0)").hide();
                    $(this).find("td:eq(3)").hide();
                }
                i++;
            });
        }
    }
}