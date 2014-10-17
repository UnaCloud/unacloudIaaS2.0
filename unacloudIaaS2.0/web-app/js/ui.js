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
