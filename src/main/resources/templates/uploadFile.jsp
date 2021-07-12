<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<script src="js/jquery-1.12.0.min.js"></script>
<script src="js/jquery.form.js"></script>
<body>
	<form id="upfile">
		选择一个文件:
		<input type="file" name="file" id="upload" />
		<br/><br/>
		<input id="uploadFile" value="上传" type="button"/>
	</form>

	<div id="upFile"></div>
</body>
<script type="text/javascript">

$("#uploadFile").click(function(){
	var formData = new FormData($("#upfile")[0]);
	//formData.set('file', document.getElementById("upload").files[0]);
	$.ajax({
        url: '${pageContext.request.contextPath}/uploadFile/upload',
        type: 'POST',
        cache: false,
        data: formData,
        processData: false,
        contentType: false
    }).done(function(res) {

    });
});
</script>
</html>
