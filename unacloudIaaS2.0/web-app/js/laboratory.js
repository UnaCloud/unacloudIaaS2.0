function getLab(){
	$('#selectAll').click(function (event) {		
	        var selected = this.checked;
	        // Iterate each checkbox
	        $('.all:checkbox').each(function () {  this.checked = selected; });
	 });
	$('.updateMachines').click(function (event){
		var selected = false;
		$('.all:checkbox').each(function () {  
			if(this.checked){
				selected = true;
				return;
			}
		});
		if(selected){
			
		}else{
			bootbox.alert('At least one physical machine should be chosen.');
		}
	});
}