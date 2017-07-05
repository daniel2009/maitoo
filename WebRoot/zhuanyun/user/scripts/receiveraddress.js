
$(function () {
    $("input[type=checkbox]").bootstrapSwitch();
});

var addressgrid_selector = "#addressgrid-table";
//查询数据时向后台提交的参数
var postData = {};
var jsonData;
var addresspager_selector = "#addressgrid-pager";
var init = false;


//初始列表数据
$(function ($) {
	$("#divAdd input[name='cardId']").change(verfycardid);
	
	$("#divEdit input[name='cardId']").change(divEdit_verfycardid);
	
	if ($(window).width() < 640) {
	    initPhoneGrid();
	} else {
	    initGrid();
	}
    initAutocomplete();
    initClickEvent();

    //添加表单验证并提交,选择器的内容一定要是form表单，里面一定要有一个submit的按钮
    $("#divAdd form").validate({
        //验证规则
        //http://www.runoob.com/jquery/jquery-plugin-validate.html
        debug: true,//提交表单，true:不提交表单,手动ajax
        rules: {
            name: { required: true },
           // state: { required: true },
           // city: { required: true },
           // dist: { required: true },
            streetAddress: { required: true },
            phone: { required: true },

        }, submitHandler: function (form) {
            //var data = getEelementData("#divAdd");
           // top.window.infoDialog("提交", "数据是1" + data, true);
            //ajaxSubmit 提交到后台操作，操作完成后调用下面方法
        	
        	var cardUrl=$("#senderTableInfo").find(":file[name='cardUrl']").val();//正反面必须同时上传
        	var cardurlother=$("#senderTableInfo").find(":file[name='cardurlother']").val();//正反面必须同时上传
        	if(cardUrl!=""&&cardUrl!=null)
        	{
        		if((cardurlother==null)||(cardurlother==""))
        		{
        			alert("正反面必须同时上传！");
        			return false;
        		}
        	}
        	
        	if(cardurlother!=""&&cardurlother!=null)
        	{
        		if((cardUrl==null)||(cardUrl==""))
        		{
        			alert("正反面必须同时上传！");
        			return false;
        		}
        	}
        	
        	var cardId=$("#divAdd input[name='cardId']").val();
       	 cardId=cardId.toUpperCase();
       	 $("#divAdd input[name='cardId']").val(cardId);
       	    
       	    
       	    
       	    
       	    
       	    var str= checkIdcard(cardId);
       	    
       	    
       	    
       	    if(str=="验证通过!"||cardId=="")
       	    {
       	    	//	$("#cardId_result").attr("style","color:blue;");
       	    }
       	    else
       	    {
       	    		$("#cardidresult").attr("style","color:red;");
       	    		alert("身份证号错误："+str);
       	    		return false;
       	    }
       	
        	
        	
        	$("#addrevaddress").ajaxSubmit({
        		type : 'post',
        		//dataType : 'text',
        		url : "/user/rev_user/add",
        		success: function (data) {
        		    if (typeof (data) != "object") {
        		    	if(data.indexOf("<PRE>")!=-1)//返回包含前后结束符，在ie的情况下，有的会自动加上,所以要判断是否包含<PRE></PRE>，返回的，ie5,ie7,ie8是大小的，ie9是小写的，ie10之后不会添加此前后缀，所以要统一处理
                    	{
                    		data=data.substr(data.indexOf("<PRE>")+5, data.indexOf("</PRE>")-5); 
                    	}
                    	else if(data.indexOf("<pre>")!=-1)
                    	{
                    		data=data.substr(data.indexOf("<pre>")+5, data.indexOf("</pre>")-5); 
                    	}
        		    	
        		        data = $.parseJSON(data);
        		    }
        			if(data.code=="200")
        			{
        				alert("保存成功!");
        				clearInputData("#divAdd");
        				$("#btnSearch").click();
        			}else if ("607" == data.code) {
                        alert("您尚未登录或登录已失效！");
                        userlogout();
                    } else
        			{
        				alert("保存失败，原因："+data.message);
        			}
        		}
        		});
            
            
           
            //刷新列表数据
            //refreshDataGrid(postData);
        }
    });

    //编辑表单验证并提交,选择器的内容一定要是form表单，里面一定要有一个submit的按钮
    $("#divEdit form").validate({
        //验证规则
        //http://www.runoob.com/jquery/jquery-plugin-validate.html
        debug: true,//提交表单，true:不提交表单,手动ajax
        rules: {
            name: { required: true },
           // state: { required: true },
          //  city: { required: true },
          //  dist: { required: true },
            streetAddress: { required: true },
            phone: { required: true },

        }, submitHandler: function (form) {
            var data = getEelementData("#divEdit");
            //top.window.infoDialog("提交", "数据是2" + data, true);
            //ajaxSubmit 提交到后台操作，操作完成后调用下面方法
            
            
        	var cardUrl=$("#senderTableInfo").find(":file[name='cardUrl']").val();//正反面必须同时上传
        	var cardurlother=$("#senderTableInfo").find(":file[name='cardurlother']").val();//正反面必须同时上传
        	if(cardUrl!=""&&cardUrl!=null)
        	{
        		if((cardurlother==null)||(cardurlother==""))
        		{
        			alert("正反面必须同时上传！");
        			return false;
        		}
        	}
        	
        	if(cardurlother!=""&&cardurlother!=null)
        	{
        		if((cardUrl==null)||(cardUrl==""))
        		{
        			alert("正反面必须同时上传！");
        			return false;
        		}
        	}
        	
        	var cardId=$("#divEdit input[name='cardId']").val();
       	 cardId=cardId.toUpperCase();
       	 $("#divEdit input[name='cardId']").val(cardId);
       	    
        	
        	   var str= checkIdcard(cardId);
          	    
          	    
          	    
          	    if(str=="验证通过!"||cardId=="")
          	    {
          	    	//	$("#cardId_result").attr("style","color:blue;");
          	    }
          	    else
          	    {
          	    		$("#divEdit_verfycardid").attr("style","color:red;");
          	    		alert("身份证号错误："+str);
          	    		return false;
          	    }
        	
        	
        	$("#modifyrevaddress").ajaxSubmit({
        	   
        		type : 'post',
        		
        		url : "/user/rev_user/modify",
        		success: function (data) {
        		    if (typeof (data) != "object") {
        		    	if(data.indexOf("<PRE>")!=-1)//返回包含前后结束符，在ie的情况下，有的会自动加上,所以要判断是否包含<PRE></PRE>，返回的，ie5,ie7,ie8是大小的，ie9是小写的，ie10之后不会添加此前后缀，所以要统一处理
                    	{
                    		data=data.substr(data.indexOf("<PRE>")+5, data.indexOf("</PRE>")-5); 
                    	}
                    	else if(data.indexOf("<pre>")!=-1)
                    	{
                    		data=data.substr(data.indexOf("<pre>")+5, data.indexOf("</pre>")-5); 
                    	}
        		        data = $.parseJSON(data);
        		    }
        			if(data.code=="200")
        			{
        			    alert("修改成功!");
        			    $("#btnSearch").click();
        				// clearInputData("#divEdit");
        			}
        			else if ("607" == data.code) {
                        alert("您尚未登录或登录已失效！");
                        userlogout();
                    } else
        			{
        				alert("修改失败，原因："+data.message);
        			}
        		}
        		});
            

            //clearInputData("#divAdd");
            //刷新列表数据
            //refreshDataGrid(postData);
        }
    });
});

function initClickEvent() {
    //点击搜索
    $("#btnSearch").click(function () {
        postData = getEelementData("#divaddress");
        $(addressgrid_selector).jqGrid("setGridParam", { postData: postData }).trigger("reloadGrid");
    });
    //绑定输入框的enter事件进行搜索
    $("#keyword").bind("keypress", (function (e) {
        if (e.keyCode == 13) {
            $("#btnSearch").click();
        }
    }));


    $("#btnAdd").click(function () {
        $("#divList").hide();
        $("#divEdit").hide();
        $("#divAdd").show();
    });

    $("#cancelAdd").click(function () {
        $("#divList").show();
        $("#divEdit").hide();
        $("#divAdd").hide();
       
    });

    $("#cancelEdit").click(function () {
        $("#divList").show();
        $("#divEdit").hide();
        $("#divAdd").hide();
    });

    $("#divAdd #hc").click(function () {
        $("#divAdd span[name=hc]").show();
        $("#divAdd span[name=dl]").hide();
    });

    $("#divAdd #dl").click(function () {
        $("#divAdd span[name=hc]").hide();
        $("#divAdd span[name=dl]").show();
    });

    $("#divEdit #hc").click(function () {
        $("#divEdit span[name=hc]").show();
        $("#divEdit span[name=dl]").hide();
    });

    $("#divEdit #dl").click(function () {
        $("#divEdit span[name=hc]").hide();
        $("#divEdit span[name=dl]").show();
    });
}

function initGrid() {
    $(window).resize(function () {
        $(addressgrid_selector).setGridWidth($(".page-content").width() - 2, true);
    });

    jQuery(addressgrid_selector).jqGrid({
        url: "/consignee/search",
        datatype: "json",
        height: "auto",
        mtype: "get",

        colNames: ['收货人', '电话', '国家/地区', "详细地址", "身份证号", "身份证正面图", "身份证反面图", "身份证合成图", "操作", ],
        colModel: [

            {
                name: 'name', index: 'name', width: 60, formatter: function (cellvalue, options, row) {
                    return '<a href="javascript:void(0)" onclick="showdetail(' + row.id + ')">' + cellvalue + '</a>';
                }
            },
            { name: 'phone', index: 'phone', width: 70, },
            {
                name: 'province', index: 'province', width: 120, formatter: function (cellvalue, options, row) {
                    return row.province + row.city + row.district;
                }
            },
            { name: 'streetAddress', index: 'streetAddress', width: 120, },
            { name: 'cardId', index: 'cardId', width: 100, },
            {
                name: 'cardUrl', index: 'cardUrl', width: 60, formatter: function (cellvalue, options, row) {
                	 return '<a target="_blank" href="'+cellvalue+'">'+'<img src="' + cellvalue + '" width="45" height="30"/></a>';
                }
            },
            {
                name: 'cardurlother', index: 'cardurlother', width: 60, formatter: function (cellvalue, options, row) {
                    return '<a target="_blank" href="' + cellvalue + '">' + '<img src="' + cellvalue + '" width="45" height="30"/></a>';
                }
            },
             {
                 name: 'cardurltogether', index: 'cardurltogether', width: 60, formatter: function (cellvalue, options, row) {
                     return '<a target="_blank" href="' + cellvalue + '">' + '<img src="' + cellvalue + '" width="45" height="30"/></a>';
                 }
             },
             {
                 name: 'op', index: 'op', width: 120, formatter: function (cellValue, option, row) {
                     //返回修改按钮
                     var edit = "<button  style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"update('" + row.id + "')\"  ><span class='icon-edit'></span>修改</button>";
                     var del = "<button style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"del('" + row.id + "')\"  ><span class='fa fa-times'></span>删除</button>";
                     return edit + del;
                 }
             }
        ],

        jsonReader: {
            root: "data.datas",
            page: "data.pageNow",
            total: "data.pageCount",
            records: "data.rowCount"
        },
        viewrecords: true,
        rowNum: 10,
        rowList: [10, 30, 50],
        pager: addresspager_selector,
        altRows: true,

        multiselect: false,
        //multikey: "ctrlKey",
        multiboxonly: true,

        loadComplete: function (data) {
        	if(data.code=="200")//返回成功
        	{
        		if(data.data!=null)
        		{
        			jsonData = data.data.datas;
        		}
        		
        	}
        	else if ("607" == data.code) {
                alert("您尚未登录或登录已失效！");
                userlogout();
            } else
			{
				alert("修改失败，原因："+data.message);
			}
            
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
            }, 0);
            //防止出现水平滚动条
            if (init == false) {
                $(".ui-jqgrid-bdiv").css({ 'overflow-x': 'hidden' });
                $(".ui-jqgrid-htable th:last").css({ "width": $(".ui-jqgrid-htable th:last").width() - 2 });
                $(".jqgfirstrow td:last").css({ "width": $(".jqgfirstrow td:last").width() - 2 });
                init = true;
            }
        },
        gridComplete: function () {

        },
        onSelectRow: function (rowid, status) {

        },
        onSelectAll: function (aRowids, status) {

        },
        caption: "",
        autowidth: true
    });


    //navButtons
    jQuery(addressgrid_selector).jqGrid('navGrid', addresspager_selector,
        { 	//navbar options
            edit: false,
            editicon: 'icon-pencil blue',
            add: false,
            addicon: 'icon-plus-sign purple',
            del: false,
            delicon: 'icon-trash red',
            search: false,
            searchicon: 'icon-search orange',
            refresh: true,
            refreshicon: 'icon-refresh green',
            view: false,
            viewicon: 'icon-zoom-in grey',

            //删除
            delfunc: function () {

            }
        }
    );


    //replace icons with FontAwesome icons like above
    function updatePagerIcons(table) {
        var replacement =
        {
            'ui-icon-seek-first': 'icon-double-angle-left bigger-140',
            'ui-icon-seek-prev': 'icon-angle-left bigger-140',
            'ui-icon-seek-next': 'icon-angle-right bigger-140',
            'ui-icon-seek-end': 'icon-double-angle-right bigger-140'
        };
        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
            var icon = $(this);
            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

            if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
        });
    }
}

function initPhoneGrid() {
    $(window).resize(function () {
        $(addressgrid_selector).setGridWidth($(".page-content").width() - 2, true);
    });

    jQuery(addressgrid_selector).jqGrid({
        url: "/consignee/search",
        datatype: "json",
        height: "auto",
        mtype: "get",

        colNames: ['收货人', '电话', "操作" ],
        colModel: [

            {
                name: 'name', index: 'name', width: 50, formatter: function (cellvalue, options, row) {
                    return '<a href="javascript:void(0)" onclick="showdetail(' + row.id + ')">' + cellvalue + '</a>';
                }
            },
            { name: 'phone', index: 'phone', width: 85, },
            
             {
                 name: 'op', index: 'op', width: 130, formatter: function (cellValue, option, row) {
                     //返回修改按钮
                     var edit = "<button  style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"update('" + row.id + "')\"  ><span class='icon-edit'></span>修改</button>";
                     var del = "<button style='margin:1px 0px 0px 5px;padding:0px 3px;width:60px' class='btn btn-purple btn-sm' type='button'  onclick=\"del('" + row.id + "')\"  ><span class='fa fa-times'></span>删除</button>";
                     return edit + del;
                 }
             }
        ],

        jsonReader: {
            root: "data.datas",
            page: "data.pageNow",
            total: "data.pageCount",
            records: "data.rowCount"
        },
        viewrecords: true,
        rowNum: 10,
        rowList: [10, 30, 50],
        pager: addresspager_selector,
        altRows: true,

        multiselect: false,
        //multikey: "ctrlKey",
        multiboxonly: true,

        loadComplete: function (data) {
            if (data.code == "200")//返回成功
            {
                if (data.data != null) {
                    jsonData = data.data.datas;
                }

            }
            else if ("607" == data.code) {
                alert("您尚未登录或登录已失效！");
                userlogout();
            } else {
                alert("修改失败，原因：" + data.message);
            }

            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
            }, 0);
            //防止出现水平滚动条
            if (init == false) {
                $(".ui-jqgrid-bdiv").css({ 'overflow-x': 'hidden' });
                $(".ui-jqgrid-htable th:last").css({ "width": $(".ui-jqgrid-htable th:last").width() - 2 });
                $(".jqgfirstrow td:last").css({ "width": $(".jqgfirstrow td:last").width() - 2 });
                init = true;
            }
        },
        gridComplete: function () {

        },
        onSelectRow: function (rowid, status) {

        },
        onSelectAll: function (aRowids, status) {

        },
        caption: "",
        autowidth: true
    });


    //navButtons
    jQuery(addressgrid_selector).jqGrid('navGrid', addresspager_selector,
        { 	//navbar options
            edit: false,
            editicon: 'icon-pencil blue',
            add: false,
            addicon: 'icon-plus-sign purple',
            del: false,
            delicon: 'icon-trash red',
            search: false,
            searchicon: 'icon-search orange',
            refresh: true,
            refreshicon: 'icon-refresh green',
            view: false,
            viewicon: 'icon-zoom-in grey',

            //删除
            delfunc: function () {

            }
        }
    );


    //replace icons with FontAwesome icons like above
    function updatePagerIcons(table) {
        var replacement =
        {
            'ui-icon-seek-first': 'icon-double-angle-left bigger-140',
            'ui-icon-seek-prev': 'icon-angle-left bigger-140',
            'ui-icon-seek-next': 'icon-angle-right bigger-140',
            'ui-icon-seek-end': 'icon-double-angle-right bigger-140'
        };
        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
            var icon = $(this);
            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

            if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
        });
    }
}


//提交js对象参数刷新列表数据
function refreshDataGrid(data) {
    $(addressgrid_selector).jqGrid("setGridParam", { postData: data }).trigger("reloadGrid");
}

//点击编辑
function update(id) {
    var row = jsonData.getData(id);
    
    //显示图片操作
   
    var cardUrl=row.cardUrl;
    var cardurlother=row.cardurlother;
   var cardurltogether=row.cardurltogether;
   row.cardurltogether="";
   row.cardurlother="";
   row.cardUrl="";
   setInputData("#divEdit", row);
   $("#divEdit").find(":hidden[name='precardUrl']").val(cardUrl);//正面图片
   $("#divEdit").find(":hidden[name='precardurlother']").val(cardurlother);//反面图片
  $("#divEdit").find(":hidden[name='precardurltogether']").val(cardurltogether);//合成图片
   if(removenull(cardUrl)=="")
   {
	   $("#cardUrl_m_show").attr("href","");
	   $("#cardUrl_m_show img").attr("src","");
   }
   else
   {
	    $("#cardUrl_m_show").attr("href",cardUrl);
	    $("#cardUrl_m_show img").attr("src",cardUrl);
   }
   if(removenull(cardurlother)=="")
   {
	   $("#cardurlother_m_show").attr("href","");
	    $("#cardurlother_m_show img").attr("src","");
   }
   else
   {
	    $("#cardurlother_m_show").attr("href",cardurlother);
	    $("#cardurlother_m_show img").attr("src",cardurlother);
   }
   if(removenull(cardurltogether)=="")
   {
	    $("#cardurltogether_m_show").attr("href","");
	    $("#cardurltogether_m_show img").attr("src","");
   }
   else
   {
	    $("#cardurltogether_m_show").attr("href",cardurltogether);
	    $("#cardurltogether_m_show img").attr("src",cardurltogether);
   }

    
    
    $("#divList").hide();
    $("#divEdit").show();
    $("#divAdd").hide();
}

//删除
function del(id) {

    if (id == undefined) { top.window.tips("请选择需要删除的行", false); return; }

    top.window.confirmDialog("确定删除选择的数据？", "删除提示", function () {
    	
    	

		$.ajax({
			type : "post",
			url : "/consignee/del",
			data : {
				"id" : id
			},
			success : function(response) {
				var code = response.code;
				if (code == "200") {
					 window.tips("数据已删除", false);
					//todo 这里补充 和后台交互的代码
				    //删除完成后 在success回调中调用下面方法刷新数据列表
				     refreshDataGrid(postData);
				} else if ("607" == code) {
					alert("您尚未登录或登录已失效！");
					userlogout();
				} else {
					// 失败
					alert("删除信息失败，原因是:" + response.message);
				}
			}
		});
	
    	
       
        
    });

}

function showdetail(id) {

}

//新增修改省市区选择
function initAutocomplete() {

    //选择省
    $("#divAdd input[name=province]").bigAutocomplete({
        data: $.areaData.p,
        //url: "areadata.html",//可以请求到后台，返回的格式{"data":[{"title":"广西"}]}
        callback: function (p) {
        }
    });


    //选择市
    $("#divAdd input[name=city]").focusin(function () {
        $(this).bigAutocomplete({
            data: $.areaData.c[$("#divAdd input[name=province]").val()]
        });
    });

    //县区
    $("#divAdd input[name=district]").focusin(function () {
        $(this).bigAutocomplete({
            data: $.areaData.a[$("#divAdd input[name=province]").val() + "-" + $("#divAdd input[name=city]").val()]
        });
    });




    //编辑时选择省
    $("#divEdit input[name=province]").bigAutocomplete({
        data: $.areaData.p,
        //url: "areadata.html",//可以请求到后台，返回的格式{"data":[{"title":"广西"}]}
        callback: function (p) {
        }
    });


    //选择市
    $("#divEdit input[name=city]").focusin(function () {
        $(this).bigAutocomplete({
            data: $.areaData.c[$("#divEdit input[name=province]").val()]
        });
    });

    //县区
    $("#divEdit input[name=district]").focusin(function () {
        $(this).bigAutocomplete({
            data: $.areaData.a[$("#divEdit input[name=province]").val() + "-" + $("#divEdit input[name=city]").val()]
        });
    });
}

//选择正反面清空选择
function click_choose_tpe()
{
	
	var a=$(":radio[name='sfz']:checked").val();
	if(a=="0")//正反合成，清空之前的图片
	{
		$(":file[name='cardUrl']").val("");
		$(":file[name='cardurlother']").val("");
		
	}
	else
	{
		$(":file[name='cardurltogether']").val("");
	
	}
}


function clearrecimg(obj)
{
	if (confirm("确认清空所有图片?"))
	{
		$(obj).parent().find('img').attr('src',"");
		$(":hidden[name='precardUrl']").val("");
		$(":hidden[name='precardurlother']").val("");
		$(":hidden[name='precardurltogether']").val("");
	}
	
}


function verfycardid()
{
	var cardid=$("#divAdd input[name='cardId']").val();
	if(cardid!=null||cardid!="undefined")
	{
		cardid=cardid.toUpperCase();
	}
	
	if(cardid=="")
	{
		$("#cardidresult").html("");
		return false;
	}
    var str= checkIdcard(cardid);
   
    if(str=="验证通过!")
    {
    	$("#cardidresult").html("");
    }
    else
    {
    	 	$("#cardidresult").html(str);
    		$("#cardidresult").attr("style","color:red;");
    		
    }
}

function divEdit_verfycardid()
{
	var cardid=$("#divEdit input[name='cardId']").val();
	if(cardid!=null||cardid!="undefined")
	{
		cardid=cardid.toUpperCase();
	}
	
	if(cardid=="")
	{
		$("#divEdit_cardidresult").html("");
		return false;
	}
    var str= checkIdcard(cardid);
   
    if(str=="验证通过!")
    {
    	$("#divEdit_cardidresult").html("");
    }
    else
    {
    	 	$("#divEdit_cardidresult").html(str);
    		$("#divEdit_cardidresult").attr("style","color:red;");
    		
    }
}