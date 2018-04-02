$(".settings_orders_list_modify").click(function(){
	if($(this).text() != "Closed"){
		var object = $(this);
		var id = object.attr("id");
		$.post("/closeOrder", {orderId:id}, function(result){
			object.text("Closed").animate({"margin-left":"713px"}, 100);
		});
	}
});