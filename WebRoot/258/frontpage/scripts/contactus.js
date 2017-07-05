(function ($) {


    getstorelistnfo();

})(jQuery);


function getstorelistnfo() {

    //获取渠道显示信息
    $.ajax({
        type: "get",
        url: "/store/guest_store_list",
        success: function (response) {
            var code = response.code;
            if (code == "200") {

            	
            	//<div class="panel panel-info" "=""><div class="panel-heading" onclick="$(this).parent().find('.panel-collapse').toggle()"><h2 class="panel-title" style="font-size:18px; padding-left:20px">俄勒冈分公司免税州（OR）仓库</h2></div><div class="panel-collapse collapse in" style="display: none;"><div class="panel-body"><h4>410 Beavercreek Rd Suite 512, Oregon City, OR, US, 97045</h4><h4>联系电话：(503)828-9915</h4><iframe style="width: 100%;height:100%; margin:0; padding: 0; border: 0;" id="main" name="main" src="https://www.google.com/maps/embed/v1/place?key=AIzaSyBrtxUx8fgII5xnKRScxv2hVKq2uvvKUrU&amp;q=410 Beavercreek Rd Suite 512, Oregon City, OR, US, 97045"></iframe> </div> </div> </div>
            	
                var str = "";
                var no=0;
                if (response.data != null) {
                    $.each(response.data, function () {
                    	
                    	if(((this.showstore==1)||(this.showstore=="1")))
	                    {
	                    		
	                    	
		                        var address1 = "";
		                        var mapaddress = "";
		                        
		                        
		                        
		                        var str = '<div class="panel panel-info" ">';
		                        str += '<div class="panel-heading" onclick="$(this).parent().find(\'.panel-collapse\').toggle()">';
		                        //data-toggle="collapse" aria-expanded="true"
		                        str += '<h2 class="panel-title"  style="font-size:18px; padding-left:20px" >';
		                        str += this.name;
		                        str += '</h2>';
		                        str += '</div>';
		                        if(no==0)
		                        {
		                        	 str += '<div class="panel-collapse collapse in" >';//aria-expanded="true"
		                        }
		                        else
		                        {
		                        	 str += '<div class="panel-collapse collapse in" style="display:none" >';//aria-expanded="true"
		                        }
		                        no++;
		                        str += '<div class="panel-body">';
		                        str += "<h4>"
		                        address1 = removenull(this.address);
		
		
		                        if (removenull(this.city) != "") {
		                            if (address1 == "") {
		                                address1 = removenull(this.city);
		                            }
		                            else {
		                                address1 = address1 + ", " + removenull(this.city);
		                            }
		                        }
		
		                        if (removenull(this.province) != "") {
		                            if (address1 == "") {
		                                address1 = removenull(this.province);
		                            }
		                            else {
		                                address1 = address1 + ", " + removenull(this.province);
		                            }
		                        }
		
		                        if (removenull(this.country) != "") {
		                            if (address1 == "") {
		                                address1 = removenull(this.country);
		                            }
		                            else {
		                                address1 = address1 + ", " + removenull(this.country);
		                            }
		                        }
		                        if (removenull(this.zipCode) != "") {
		                            if (address1 == "") {
		                                address1 = removenull(this.zipCode);
		                            }
		                            else {
		                                address1 = address1 + ", " + removenull(this.zipCode);
		                            }
		                        }
		                        str += address1;
		                        str += "</h4>";
		
		                        if (removenull(this.telephone) != "") {
		                            str += "<h4>";
		                            str += "联系电话：" + this.telephone;
		                            str += "</h4>";
		                        }
		address1=address1.replace("#", "%23");//替换所有#号，否则可能找不到
		
		                       str += '<iframe style="width: 100%;height:100%; margin:0; padding: 0; border: 0;" id="main" name="main" src="https://www.google.com/maps/embed/v1/place?key=AIzaSyBrtxUx8fgII5xnKRScxv2hVKq2uvvKUrU&q=' + address1 + '"></iframe>';
		                        str += " </div> </div> </div>";
		
		                        $("#all_contact_address").append(str);
	                    }
	                   });

                }
            }
        }
    });
}

