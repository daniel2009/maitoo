//匹配省份的简称
/** 
 * 根据省份名称返回省份简称 
 * @param province 
 * @returns {String} 
 */  
function provinceForShort(province){  
    if(province == "北京市"||province == "北京")  
        return "京";  
    else if(province == "天津市"||province == "天津")  
        return "津";  
    else if(province == "重庆市"||province == "重庆")  
        return "渝";  
    else if(province == "上海市"||province == "上海")  
        return "沪";  
    else if(province == "河北省"||province == "河北")  
        return "冀";  
    else if(province == "山西省"||province == "山西")  
        return "晋";  
    else if(province == "辽宁省"||province == "辽宁")  
        return "辽";  
    else if(province == "吉林省"||province == "吉林")  
        return "吉";  
    else if(province == "黑龙江省"||province == "黑龙江")  
        return "黑";  
    else if(province == "江苏省"||province == "江苏")  
        return "苏";  
    else if(province == "浙江省"||province == "浙江")  
        return "浙";  
    else if(province == "安徽省"||province == "安徽")  
        return "皖";  
    else if(province == "福建省"||province == "福建")  
        return "闽";  
    else if(province == "江西省"||province == "江西")  
        return "赣";  
    else if(province == "山东省"||province == "山东")  
        return "鲁";  
    else if(province == "河南省"||province == "河南")  
        return "豫";  
    else if(province == "湖北省"||province == "湖北")  
        return "鄂";  
    else if(province == "湖南省"||province == "湖南")  
        return "湘";  
    else if(province == "广东省"||province == "广东")  
        return "粤";  
    else if(province == "海南省"||province == "海南")  
        return "琼";  
    else if(province == "四川省"||province == "四川")  
        return "川/蜀";  
    else if(province == "贵州省"||province == "贵州")  
        return "黔/贵";  
    else if(province == "云南省"||province == "云南")  
        return "云/滇";  
    else if(province == "陕西省"||province == "陕西")  
        return "陕/秦";  
    else if(province == "甘肃省"||province == "甘肃")  
        return "甘/陇";  
    else if(province == "青海省"||province == "青海")  
        return "青";  
    else if(province == "台湾省"||province == "台湾")  
        return "台";  
    else if(province == "内蒙古自治区"||province == "内蒙古"||province == "内蒙古省")  
        return "内蒙古";  
    else if(province == "广西壮族自治区"||province == "广西"||province == "广西省")  
        return "桂";  
    else if(province == "宁夏回族自治区"||province == "宁夏"||province == "宁夏省")  
        return "宁";  
    else if(province == "新疆维吾尔自治区"||province == "新疆"||province == "新疆省")  
        return "新";  
    else if(province == "西藏自治区"||province == "西藏"||province == "西藏省")  
        return "藏";  
    else if(province == "香港特别行政区"||province == "香港")  
        return "港";  
    else if(province == "澳门特别行政区"||province == "澳门")  
        return "澳";  
    else 
    	{return province;  }
      
}  