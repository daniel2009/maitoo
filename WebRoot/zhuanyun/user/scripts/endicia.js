(function ($) {

	$("select[name='tocountry']")
	.change(
			function() {
				
				
				var usoption='<option value="StandardPost">标准邮件</option>';
				usoption+='<option value="PriorityExpress">特快专递</option>';
				usoption+='<option value="First">第一类邮件包裹</option>';
				usoption+='<option value="LibraryMail">图书馆邮件</option>';
				usoption+='<option value="MediaMail">媒体邮件</option>';
				usoption+='<option value="ParcelSelect">轻型包裹</option>';
				usoption+='<option value="Priority">优先邮件</option>';
				usoption+='<option value="CriticalMail">重要邮件</option>';
				
				var International='<option value="PriorityMailExpressInternational">国际3-5天件</option>';
				International+='<option value="FirstClassMailInternational">一级国际邮件</option>';
				International+='<option value="FirstClassPackageInternationalService">一级国际包裹邮件服务</option>';
				International+='<option value="PriorityMailInternational">国际6-10天件</option>';
				var country=$("select[name='tocountry']").val();
				$(":text[name='r_zipcode']").val("");
				if(country=="US")
				{
					$("select[name='MailClass']").html(usoption);
					$(":text[name='r_zipcode']").attr("maxlength","5");
					
				}
				else
				{
					$("select[name='MailClass']").html(International);
					$(":text[name='r_zipcode']").attr("maxlength","6");
				}
			});
	
})(jQuery);

var printing_flag=false;

//打印endicia,价格标识
function print_Endicia_label()
{
	
	if(printing_flag==true)
	{
		return false;
	}
	
	var s_name=$("input[name='s_name']").val();
	var s_phone=$("input[name='s_phone']").val();
	var s_addr=$("input[name='s_addr']").val();
	var s_city=$("input[name='s_city']").val();
	var s_state=$("input[name='s_state']").val();
	var s_zipcode=$("input[name='s_zipcode']").val();
	var s_zip4=$("input[name='s_zip4']").val();
	//var r_name=$("input[name='r_name']").val();
	//var r_phone=$("input[name='r_phone']").val();
	var tocountry=$("select[name='tocountry']").find("option:selected").attr("name");
	var toCountryCode=$("select[name='tocountry']").val();
	
	var labelType="Default";
	if(toCountryCode!="US")
	{
		labelType="International";
	}
	
	
	var r_name=$("input[name='r_name']").val();
	var r_phone=$("input[name='r_phone']").val();
	var r_toCompany=$("input[name='r_toCompany']").val();
	
	var r_addr=$("input[name='r_addr']").val();
	var r_city=$("input[name='r_city']").val();
	var r_state=$("input[name='r_state']").val();
	var r_zipcode=$("input[name='r_zipcode']").val();
	var r_zip4=$("input[name='r_zip4']").val();
	
	
	var description=$("textarea[name='description']").val();
	var weight=$("input[name='weight']").val();
	
	//var weightoz=$(":text[name='weightoz']").val();
	var weightoz=parseFloat(weight)*16;
	if(weightoz=="NaN")
	{
		alert("重量填写错误!");
		return false;
	}
	var quantity=$("input[name='quantity']").val();
	var value=$("input[name='value']").val();
	
	var MailClass=$("select[name='MailClass']").val();
	
	
	printing_flag=true;
	
	
	$.ajax({
		post : "post",
		url : "/endicial/user_check_price",
		data : {
		"labelType" : labelType,
		"dateAdvance" : "0",
		"weightOz" : weightoz,
		"mailpieceShape" : "Parcel",
		"description" : description,
		"quantity" : quantity,
		"weight" : weight,
		"toName" : r_name,
		"toCompany" : r_toCompany,
		"toAddress1" : r_addr,
		"toCity" : r_city,
		"toState" : r_state,
		"toCountry" : tocountry,
		"toCountryCode" : toCountryCode,
		"toPostalCode" : r_zipcode,
		"toZIP4" : r_zip4,
		"toPhone" : r_phone,
		"value" : value,
		
		"fromName" : s_name,
		"returnAddress1" : s_addr,
		"fromCity" : s_city,
		"fromState" : s_state,
		"fromPostalCode" : s_zipcode,
		"fromZIP4" : s_zip4,
		"fromPhone" : s_phone,
		"mailClass" : MailClass
	},success : function(response) {
		printing_flag=false;
		var code = response.code;
		if ("200" == code) {
			
			if (confirm("你打印的label价格为："+response.data+" 是否确定打印？")) {
				$.ajax({
					post : "post",
					url : "/endicial/user_label_print_url",
					data : {
					"labelType" : labelType,
					"dateAdvance" : "0",
					"weightOz" : weightoz,
					"mailpieceShape" : "Parcel",
					"description" : description,
					"quantity" : quantity,
					"weight" : weight,
					"toName" : r_name,
					"toCompany" : r_toCompany,
					"toAddress1" : r_addr,
					"toCity" : r_city,
					"toState" : r_state,
					"toCountry" : tocountry,
					"toCountryCode" : toCountryCode,
					"ToPostalCode" : r_zipcode,
					"toZIP4" : r_zip4,
					"toPhone" : r_phone,
					"value" : value,
					
					"fromName" : s_name,
					"returnAddress1" : s_addr,
					"fromCity" : s_city,
					"fromState" : s_state,
					"fromPostalCode" : s_zipcode,
					"fromZIP4" : s_zip4,
					"fromPhone" : s_phone,
					"mailClass" : MailClass
				},success : function(response) {
					var code = response.code;
					//alert(response);
					//alert(code);
					if ("200" == code) {
						
						alert("操作成功，请查看打开的文档或查看历史记录!");
						
						if((response.data!=null)&&(response.data!="null")&&(response.data!="undefined")&&(response.data!=""))
						{
							var url=response.data.split(";");
							$.each(url,function(){
								if(this!="")
								{
									var url0=this;
									
									window.open(url0, "_blank");
								}
							});
							
						}
						
						searchLabelList(1);
						//alert(url);
						//window.localtion=url;
						//alert("操作成功，请查看下载的文档或查看历史记录!");
						
					} else if ("607" == code) {
						alert("您尚未登录或登录已失效！");
						logout();
					}
					else {
						alert("提交出错，原因是:"
								+ response.message);
						
					}
				}
				});
			}
			
			
		} else if ("607" == code) {
			alert("您尚未登录或登录已失效！");
			logout();
		}
		else {
			alert("提交出错，原因是:"
					+ response.message);
		}
	}
	});
	
}