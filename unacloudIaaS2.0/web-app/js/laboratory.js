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
			    message: "<div class=\"monitor_modal\" style= \"margin:auto\">"+
						  "<p>The list below contains four options to control monitoring in physical machines.</p>"+
						  "<form id=\"form_modal\" role=\"form\">"+
						    "<div class=\"radio input-group\">"+
						      "<label><input id='startM' type=\"radio\" name=\"optradio\" value=\"start\">Start Monitoring</label>"+
						    "</div>"+
						    "<div class=\"radio\">"+
						     "<label><input id='stoptM' type=\"radio\" name=\"optradio\" value=\"stop\">Stop Monitoring</label>"+
						    "</div>"+
						    "<div class=\"radio disabled\">"+
						      "<label><input id='updateC' type=\"radio\" name=\"optradio\" value=\"update\">Update Configuration</label>"+
						    "</div>"+
						    "<div class=\"radio disabled\">"+
						      "<label><input id='enableM' type=\"radio\" name=\"optradio\" value=\"enable\">Enable Monitoring</label>"+
						    "</div>"+
						  "</form>"+
						  "<div class=\"monitor_label_error\"></div>"+
						"</div>",	
				buttons: {
					success: {
						label: "Send Request",
						className: "btn-success",
						callback: function () {
							var option = $('input[name="optradio"]:checked', '#form_modal').val(); 
							if(option){
								configMonitoring(option, form);
							}
							else showMonitorModal(true);
						}
					}
				}			
			});
			if(recursive)addLabel(".monitor_label_error", "You must select at least one option", true);
		}
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
	function configMonitoring(option, form){
		var input = $("<input>")
        .attr("type", "hidden")
        .attr("name", "option").val(option);
         form.append($(input));
		 showLoading();
		 $.post('../updateMonitoring', form.serialize(), function(data){
			 showMessage(data,'All selected agents has been request to <b>'+option+'</b> monitoring processes.');		
			 hideLoading();
		 }, 'json');		
	}
}

