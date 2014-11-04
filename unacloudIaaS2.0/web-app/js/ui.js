$(function() {
	$(".content_alert_button").hide();
	$(".alert_button").on("click",function(e) {
		 var text = $(this).find("div .content_alert_button").text();
		 if(!text||text==(''))text = "You must use a 'div' with class 'content_alert_button' inside your click tag, and inside div write the text to show here.";
		 bootbox.alert(text);
	 });	
	$(".title_html_dialog_button").hide();
	$(".content_html_dialog_button").hide();
	$(".html_dialog_button").on("click",function(e){
		var tit = $(this).find("p.title_html_dialog_button").text();
		if(!tit||tit==(''))tit = "You must use a 'p' tag with class 'title_html_dialog_button' inside your click tag, and inside p write the text to show here.";
		var content = $(this).find("div.content_html_dialog_button").html();
		if(!content||content==(''))content = "You must use a 'div' tag with class 'content_html_dialog_button' inside your click tag, and inside div write the html to show here.";
		bootbox.dialog({
			  title: tit,
			  message: content
		});
	});	
});

function showAlert(message){
	if(message)bootbox.alert(message);
}
function showDialog(title,message){
	if(message && title){
		bootbox.dialog({
			  title: tit,
			  message: message
		});
	}
}
function showError(tit,content){
	if(content && tit){
		bootbox.dialog({
			  title: tit,
			  message: '<div class="alert alert-error"><i class="icon-exclamation-sign"></i><small>'+content+'</small></div>'
		});
	}
}

function addLabel(div, message, error){
	$(div).html('<i class="icon-exclamation-sign"></i><small>'+message+'</small>');
	$(div).css("font-size","14px");
	if(error)$(div).addClass('alert alert-error')
	else $(div).addClass('alert alert-info') 	
}
function cleanLabel(div){
	$(div).html('');
	$(div).removeClass();
}