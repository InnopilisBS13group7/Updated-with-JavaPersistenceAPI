$(document).ready(function(){
	$(".books").mouseenter(function(){
		$(this).find(".books_inside").animate({"opacity":"1"}, 150);
	});
	$(".return_book").click(function(){
		$.post("/returnDocument", {orderId:$(this).attr("id")}, function(result){
			alert(result);
		});
	});
	$(".renew_book").click(function(){
		$.post("/renewBook", {orderId:$(this).attr("id")}, function(result){
			alert(result);
		});
	});
	$(".accept_book").click(function(){
		$.post("/acceptBook", {orderId:$(this).attr("id")}, function(result){
			alert(result);
		});
	});
});