function getLab(){
	$('#selectAll').click(function (event) {		
	        var selected = this.checked;
	        $('.all:checkbox').each(function () {  this.checked = selected; });
	});
	$('.updateMachines').click(function (event){		
		if(checkSelected()){			
			 var form = $('#form_machines');
			 showLoading();
			 $.post('../updateMachines', form.serialize(), function(data){
				 showMessage(data,'All selected agents has been updated.');		
				 hideLoading();
			 }, 'json')			
		}
	});
	$('.monitorConfig').click(function (event){		
		showMonitorModal(false);
	});
	$('.clearCache').click(function (event){		
		if(checkSelected()){			
			 var form = $('#form_machines');
			 showLoading();
			 $.post('../clearCache', form.serialize(), function(data){
				 showMessage(data,'Virtual Machine Cache from all selected agents has been cleared.');	
				 hideLoading();
			 }, 'json')			
		}
	});
	$('.stopMachines').click(function (event){		
		if(checkSelected()){			
			 var form = $('#form_machines');
			 showLoading();
			 $.post('../stopMachines', form.serialize(), function(data){
				 showMessage(data,'All selected agents has been stopped.');		
				 hideLoading();
			 }, 'json')			
		}
	});
	function showMonitorModal(recursive){
		if(checkSelected()){	
			var form = $('#form_machines');
			bootbox.dialog({//disable to block option
				title: "Monitoring Service",
			    message: "<div class=\"monitor_modal col-sm-6\" style= \"margin:auto\">"+
						  "<p>The list below contains four options to control monitoring in physical machines.</p>"+
							  "<table class='table table-monitor' ><thead>" +							  
								  "<tr>" +								 
									  "<td></td>" +
									  "<td><label>Energy Metrics</label></td>" +
									  "<td><label>CPU Metrics</label></td>" +
									  "<td></td>" +								 
								  "</tr></thead>" +
								  "<tr>" +								  	 
								  	  "<td><label>Start Monitoring</label></td>" +
									  "<td><input id='start-energy' type=\"checkbox\" value=\"start\"></td>" +
									  "<td><input id='start-cpu' type=\"checkbox\"  value=\"start\"></td>" +
									  "<td><button id='start-monitoring' type='button' class='btn btn-primary'>Submit</button></td>"+								
								  "</tr>" +
								  "<tr>" +
									  "<td><label>Stop Monitoring</label></td>" +
									  "<td><input id='stop-energy' type=\"checkbox\" value=\"stop\"></td>" +
									  "<td><input id='stop-cpu' type=\"checkbox\"  value=\"stop\"></td>" +
									  "<td><button id='stop-monitoring'  type='button' class='btn btn-primary'>Submit</button></td>"+
								  "</tr>" +
								  "<tr>" +
									  "<td><label>Update Configuration</label></td>" +
									  "<td><input id='update-energy' type=\"checkbox\" value=\"update\"></td>" +
									  "<td><input id='update-cpu' type=\"checkbox\" value=\"update\"></td>" +
									  "<td><button id='update-monitoring' type='button' class='btn btn-primary'>Submit</button></td>"+
								  "</tr>" +
								  "<tr>" +
									  "<td><label>Enable Monitoring</label></td>" +
									  "<td><input id='enable-energy' type=\"checkbox\" value=\"enable\"></td>" +
									  "<td><input id='enable-cpu' type=\"checkbox\" value=\"enable\"></td>" +
									  "<td><button id='enable-monitoring' type='button' class='btn btn-primary'>Submit</button></td>"+
								  "</tr>" +							 
							  "</table>"+
							  "<div class=\"monitor_label_error\"></div>"+
						"</div>",
						
				buttons: {
					main: {
						label: "Cancel",
						className: "btn-danger",
					}
				}			
			});
			addMonitoringClicks(form);
			if(recursive)addLabel(".monitor_label_error", "You must select at least one option", true);
		}
	}
	function addMonitoringClicks(form){
		$('#start-monitoring').click(function (event){		
			if($("#start-energy").is(':checked')||$("#start-cpu").is(':checked'))
				configMonitoring('start',form,$("#start-energy").is(':checked'),$("#start-cpu").is(':checked'))
		});
		$('#stop-monitoring').click(function (event){		
			if($("#stop-energy").is(':checked')||$("#stop-cpu").is(':checked'))
				configMonitoring('stop',form,$("#stop-energy").is(':checked'),$("#stop-cpu").is(':checked'))
		});
		$('#update-monitoring').click(function (event){		
			if($("#update-energy").is(':checked')||$("#update-cpu").is(':checked'))
				configMonitoring('update',form,$("#update-energy").is(':checked'),$("#update-cpu").is(':checked'))
		});
		$('#enable-monitoring').click(function (event){		
			if($("#enable-energy").is(':checked')||$("#enable-cpu").is(':checked'))
				configMonitoring('enable',form,true,true)
		});
	}
	function showMessage(data, message){
		 if(data.success){
			 addLabel('#label-message',message,false);	
			 unselect();
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
	function unselect(){
		$('#selectAll').checked=false;
		$('.all:checkbox').each(function () {  this.checked = false; });
	}
	function configMonitoring(option, form, energy, cpu){
		var formSend = form.clone()
		var input = $("<input>").attr("type", "hidden").attr("name", "option").val(option);
		var input2 = $("<input>").attr("type", "hidden").attr("name", "checkEnergy").val(energy);
		var input3 = $("<input>").attr("type", "hidden").attr("name", "checkCPU").val(cpu);
		formSend.append($(input));
		formSend.append($(input2));
		formSend.append($(input3));
		hideLoading(); 
		showLoading();
		$.post('../updateMonitoring', formSend.serialize(), function(data){
			  showMessage(data,'All selected agents has been request to <b>'+option+'</b> monitoring processes.');		
			  hideLoading();
		}, 'json');		
	}
}

