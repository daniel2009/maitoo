//添加商品条目
function addGoodsItem() {
    var teplament = $("#goodsItemTeplament").html();
    $("#goodsItemBody").append(teplament);
}

//删除商品条目
function delGoodsItem(obj) {
    $(obj).parent().parent().remove();
}