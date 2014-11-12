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
	xhr.open("POST", "../virtualMachineImage/upload")	
    xhr.send(formData);
}