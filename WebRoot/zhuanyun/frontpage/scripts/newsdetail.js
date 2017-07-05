
//.post-title 

$(function () {
    var type = request("type");
    var id = request("id");
    if (type == undefined || type == "") { type = 1; }
    if (id == undefined || id == "" || id * 1 <= 0) { alert("参数错误"); }
    getcontent(type, id, request("pid"), request("nid"));
})


function getcontent(type, id, pid, nid) {
    var burl = "/gonggao/getbyid?id=";
    var url = burl + id;
    var t = "公司公告";

    if (type == "2") {
        burl = "/news/getbyid?id=";
        url = burl + id;
        t = "行业新闻";
    }

    $(".entry pre").html("");

    $.ajax({
        type: "get",
        url: url,
        success: function (response) {
            var code = response.code;
            if (code == "200") {
                var str = "";
                if (response.data != null) {
                    $(".post-title").html(response.data.title);
                    $(".entry .entry").html(response.data.content);
                    var modifytime = response.data.modifytime;
                    if (modifytime && modifytime.length > 10) { modifytime = modifytime.substr(0, 10) }
                    var meta = '时间：<span>' + modifytime + '</span> &nbsp;&nbsp;&nbsp;&nbsp; 分类：<a href="news.html?type=' + type + '" title="' + t + '">' + t + '</a>';

                    $(".postmeta").html(meta);
                    if (removenull(response.data.img) != "") {
                        $("#detail_img").attr("src", response.data.img);

                    }
                    else {
                        $("#detail_img").hide();
                    }
                }
                $(".postlist").html(str);

                $(".post-prev-next").html("");

                $.ajax({
                    async: false,
                    type: "get",
                    url: burl+pid,
                    success: function (response) {
                        var code = response.code;
                        if (code == "200" && response.data != null) {
                            //上一篇：<a href="#">' + response.data.title + '</a> <br>
                            $(".post-prev-next").append('上一篇：' + response.data.title + ' <br>');
                           
                        } else {
                            $(".post-prev-next").append('上一篇：没有<br>');
                        }

                        $.ajax({
                            async: false,
                            type: "get",
                            url: burl + nid,
                            success: function (response) {
                                var code = response.code;
                                if (code == "200" && response.data != null) {
                                    var title = response.data.title;
                                    $(".post-prev-next").append('下一篇：' + title + ' <br>');
                                } else {
                                    $(".post-prev-next").append('下一篇：没有 <br>');
                                }
                            }
                        });
                    }
                });
            }
        }
    });
}
