function getLab(){
	$('#selectAll').click(function (event) {		
	        var selected = this.checked;
	        $('.all:checkbox').each(function () {  this.checked = selected; });
	 });
	$('.updateMachines').click(function (event){		
		if(checkSelected()){			
			 var form = $('#form_machines');
			 $.post('/unacloud2/Laboratory/updateMachines', form.serialize(), function(data){
				 showMessage(data,'All selected agents has been updated.');				
			 }, 'json')			
		}
	});
	$('.clearCache').click(function (event){		
		if(checkSelected()){			
			 var form = $('#form_machines');
			 $.post('/unacloud2/Laboratory/clearCache', form.serialize(), function(data){
				 showMessage(data,'Virtual Machine Cache from all selected agents has been cleared.');				
			 }, 'json')			
		}
	});
	$('.stopMachines').click(function (event){		
		if(checkSelected()){			
			 var form = $('#form_machines');
			 $.post('/unacloud2/Laboratory/stopMachines', form.serialize(), function(data){
				 showMessage(data,'All selected agents has been stopped.');				
			 }, 'json')			
		}
	});
	function showMessage(data, message){
		 if(data.success){
			 addLabel('#label-message',message,false);	
		 }else{
			 var verb = '';
			 if(data.count==1)verb = ' is'
			 else verb = 's are'
			 addLabel('#label-message',data.count+' agent'+verb+' unreachable.',true)
		 }
	}
	function checkSelected(){
		cleanLabel('#label-message');
		var selected = false;
		$('.all:checkbox').each(function () {  
			if(this.checked){
				selected = true;
				return;
			}
		});
		if(!selected){
			addLabel('#label-message','At least one physical machine should be selected.',true);		
		}
		return selected;
	}
}

