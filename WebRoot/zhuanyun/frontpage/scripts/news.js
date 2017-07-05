
$(function () {
    var type = request("type");
    if (type == undefined || type == "") { type = 1; }
    getcontent(type, 1);
})


function getcontent(type, pageIndex) {
    var url = "/gonggao/search_byguest";
    $("#newtitle").attr("title", "公司公告").html("公司公告");

    if (type == "2") {
        url = "/news/search_byguest";
        $("#newtitle").attr("title", "行业新闻").html("行业新闻");
    }

    $.ajax({
        type: "get",
        url: url,
        data: {
            size: "10"//获取条数
           , pageIndex: pageIndex
        },
        success: function (response) {
            var code = response.code;
            if (code == "200") {
                var str = "";
                if (response.data != null) {
                    var count = response.data.rowCount;
                    $.each(response.data.datas, function () {

                    });

                    var d = response.data.datas;
                    for (var i = 0; i < d.length; i++) {
                        var item = d[i];
                        if (item.modifytime && item.modifytime.length > 10) { item.modifytime = item.modifytime.substr(0, 10) }
                        var nid = 0, pid = 0;
                        if (i > 0) { pid = d[i - 1].id; }
                        if (i < d.length - 1) { nid = d[i + 1].id; }
                        str += ' <li><a href="newsdetail.html?id=' + item.id + '&type=' + type + '&nid=' + nid + '&pid=' + pid + '"  "title="' + item.title + '">' + item.title + '</a><span>' + item.modifytime + '</span></li>';
                    }
                }
                $(".postlist").html(str);
                pageinfo(type, count, 10, pageIndex);
            }
        }
    });
}

function pageinfo(type, rowCount, pageSize, pageIndex) {
    var html = "";
    $(".pageinfo").html(html);
    if (rowCount <= pageSize) {
        return;
    }
    var endPageIndex = Math.ceil(rowCount / pageSize);
    var first = "<a href='javascript:void(0)' onclick='getcontent(" + type + ",1)'>首页</a>";
    var prev = "<a href='javascript:void(0)' onclick='getcontent(" + type + "," + (pageIndex == 1 ? 1 : pageIndex - 1) + ")'>上一页</a>";
    var mid = ""
    var next = "<a href='javascript:void(0)' onclick='getcontent(" + type + "," + (pageIndex == endPageIndex ? endPageIndex : pageIndex + 1) + ")'>下一页</a>";
    var end = "<a href='javascript:void(0)' onclick='getcontent(" + type + "," + endPageIndex + ")'>末页</a>";


    mid = "<a style='background:#446BB8;color:#fff' href='javascript:void(0)' onclick='getcontent(" + type + "," + pageIndex + ")'>" + pageIndex + "</a>";

    html = first + prev + mid + next + end;
    $(".pageinfo").html(html);
}
