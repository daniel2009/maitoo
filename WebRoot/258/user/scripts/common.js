//用户退出登陆的处理方式
function userlogout() {
    $.get("/user/logout", function (response) {
        console.log("user/logout：" + response.code);
        top.location.href = "../frontpage/login.html";
    });

}