

function checksumbitform()
{
	var uploadstate=$(":file[name='uploadstate']").val();
	
	if(removenull(uploadstate)=="")
	{
		alert("必须上传文件!");
		return false;
	}
	return true;
}

