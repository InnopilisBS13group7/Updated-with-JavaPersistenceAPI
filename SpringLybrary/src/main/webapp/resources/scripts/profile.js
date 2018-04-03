$(document).ready(function(){
	$(".books").mouseenter(function(){
		$(this).find(".books_inside").animate({"opacity":"1"}, 150);
	});
	function open_alert(text){
		$("#history_alert").text(text).animate({"height":"20px", "border-bottom":"8px solid white", "padding-top":"8px", "padding-bottom":"8px"}, 100).delay(1500).animate({"height":"0px", "border-bottom":"0px solid white", "padding-top":"0px", "padding-bottom":"0px"}, 100);
	}
	$(".return_book").click(function(){
		$.post("/returnDocument", {orderId:$(this).attr("id")}, function(result){
			open_alert(result);
		});
	});
	$(".renew_book").click(function(){
		$.post("/renewDocument", {orderId:$(this).attr("id")}, function(result){
			open_alert(result);
		});
	});
	$(".accept_book").click(function(){
		$.post("/acceptDocument", {orderId:$(this).attr("id")}, function(result){
			open_alert(result);
		});
	});
});