function newUploadImage(){
	$('#button-submit').click(function (event){		
		cleanLabel('#label-message');
		var form = document.getElementById("form-new");
		if(form["name"].value&&form["name"].value.length > 0&&
			form["user"].value&&form["user"].value.length > 0&&
				form["password"].value&&form["password"].value.length > 0&&
					form["accessProtocol"].value&&form["accessProtocol"].value.length > 0 ){
			
			if(form["files"].value&&form["files"].value.length > 0){
			  uploadForm(form);		
			}
			else addLabel('#label-message', 'File(s) to upload is/are missing.', true);
		}else addLabel('#label-message', 'All fields are required', true);
	});
}
function changeUploadImage(){
	$('#button-submit').click(function (event){		
		cleanLabel('#label-message');
		var form = document.getElementById("form-change");		
		if(form["files"].value&&form["files"].value.length > 0){
			 uploadForm(form);		
		}
		else addLabel('#label-message', 'File(s) to upload is/are missing.', true);		
	});
}
function uploadForm(form){	
	var formData = new FormData(form);
	var xhr = new XMLHttpRequest();
	xhr.upload.onprogress = function(e) {
		updateUploading(e);
    };
    xhr.onload = function() {
    	hideLoading();	    	
        if (xhr.status == 200) {   
        	var jsonResponse = JSON.parse(xhr.responseText);
        	console.log(jsonResponse);
        	if(jsonResponse.success)window.location.href = jsonResponse.redirect;
        	else addLabel('#label-message', jsonResponse.message, true);        	
        }else showError('Error!','Upload failed: '+xhr.response);       
    };
    xhr.onerror = function() {
    	showError('Error!','Upload failed. Can not connect to server.')
    };
    showLoadingUploading();
	xhr.open("POST", form.action)	
    xhr.send(formData);
}
function editImage(){
	$('#button-submit').click(function (event){		
		cleanLabel('#label-message');
		var form = document.getElementById("form-edit");
		if(form["name"].value&&form["name"].value.length > 0&&
			form["user"].value&&form["user"].value.length > 0&&
				form["password"].value&&form["password"].value.length > 0){	
			form.submit();
		}else addLabel('#label-message', 'All fields are required', true);
	});
}
function loadImages(){
	$('.deleteImages').click(function (event){	
		var data = $(this).data("id");
		showConfirm('Confirm','This Image will be deleted from server. Are you sure?', function(){
			 $.get('delete', {id:data}, function(data){
				if(data.success)window.location.href = data.redirect;
				else if(data.message) showError('Error!',data.message); 
				else showError('Error!','Delete process failed, check server logs for more information'); 
			 }, 'json')		
		});
	});
}
