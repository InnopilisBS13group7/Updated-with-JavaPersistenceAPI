$(document).ready(function(){
	$(".books").mouseenter(function(){
		$(this).find(".books_inside").animate({"opacity":"1"}, 150);
	});
	$(".return_book").click(function(){
		$.post("/returnDocument", {orderId:$(this).attr("id")}, function(result){
			alert(result);
		});
	});
<<<<<<< HEAD
	$(".renew_book").click(function(){
		$.post("/renewDocument", {orderId:$(this).attr("id")}, function(result){
=======
		$.post("/renewBook", {orderId:$(this).attr("id")}, function(result){
>>>>>>> 97345b19783acc000f31554c2029a8336576937c
			alert(result);
		});
	});
	$(".accept_book").click(function(){
		$.post("/acceptDocument", {orderId:$(this).attr("id")}, function(result){
=======
		$.post("/acceptBook", {orderId:$(this).attr("id")}, function(result){
>>>>>>> 97345b19783acc000f31554c2029a8336576937c
			alert(result);
		});
	});
=======
>>>>>>> parent of fdc4cb3... ready for d3
});