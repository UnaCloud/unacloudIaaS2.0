function createCluster(){
	$('#button-create').click(function (event){		
		cleanLabel('#label-message');
		var form = document.getElementById("form-new");
		if(form["name"].value&&form["name"].value.length > 0){	
			if(form["images"].value&&form["images"].value> 0){	
				form.submit();			 
			}else addLabel('#label-message', 'At least one image must be selected', true);	
		}else addLabel('#label-message', 'The name for your new cluster is required', true);
	});
}