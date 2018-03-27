$(document).ready(function(){
	$("#settings_block").height($("#settings_type_menu").height() + $("#settings_profile").height());
	$("#settings_type_profile").click(function(){
		$("#settings_profile").animate({"margin-left":"0"}, 300);
		$("#settings_type_line").animate({"margin-left":"0px"}, 300);
		$("#settings_users").animate({"margin-left":"800px"}, 300);
		$("#settings_orders").animate({"margin-left":"1600px"}, 300);
		$("#settings_block").animate({"height":$("#settings_type_menu").height() + $("#settings_profile").height()}, 300);
	});
	$("#settings_type_users").click(function(){
		$("#settings_profile").animate({"margin-left":"-800px"}, 300);
		$("#settings_type_line").animate({"margin-left":"266px"}, 300);
		$("#settings_users").animate({"margin-left":"0px"}, 300);
		$("#settings_orders").animate({"margin-left":"800px"}, 300);
		$("#settings_block").animate({"height":$("#settings_type_menu").height() + $("#settings_users").height()}, 300);
	});
	$("#settings_type_orders").click(function(){
		$("#settings_profile").animate({"margin-left":"-1600px"}, 300);
		$("#settings_type_line").animate({"margin-left":"532px"}, 300);
		$("#settings_users").animate({"margin-left":"-800px"}, 300);
		$("#settings_orders").animate({"margin-left":"0px"}, 300);
		$("#settings_block").animate({"height":$("#settings_type_menu").height() + $("#settings_orders").height()}, 300);
	});
	$("#new_user_bottom").click(function(){
		if($(this).text() == "+ Add a new user"){
			$(this).text("Close").css({"color":"#ff4751"});
			$("#new_user").animate({"height":"238px"}, 150);
			$("#settings_block").animate({"height":"+=218px"}, 150);
		}
		else if($(this).text() == "Close"){
			$(this).text("+ Add a new user").css({"color":"black"});
			$("#new_user").animate({"height":"20px"}, 150);
			$("#settings_block").animate({"height":"-=218px"}, 150);
		}
	});
	$("#new_user_save").click(function(){
		if($("#new_user_inputs_name").val() == "")
			$("#new_user_inputs_name").css({"border-bottom":"2px solid rgb(255, 71, 81)"});
		else{
			if($("#new_user_inputs_surname").val() == "")
				$("#new_user_inputs_surname").css({"border-bottom":"2px solid rgb(255, 71, 81)"});
			else{
				if($("#new_user_inputs_email").val() == "")
					$("#new_user_inputs_email").css({"border-bottom":"2px solid rgb(255, 71, 81)"});
				else{
					if($("#new_user_inputs_password").val() == "")
						$("#new_user_inputs_password").css({"border-bottom":"2px solid rgb(255, 71, 81)"});
					else{
						if($("#new_user_inputs_type").val() == "")
							$("#new_user_inputs_type").css({"border-bottom":"2px solid rgb(255, 71, 81)"});
						else{
							$.post("/addNewUser", {name:$("#new_user_inputs_name").val(), surname:$("#new_user_inputs_surname").val(), email:$("#new_user_inputs_email").val(), password:$("#new_user_inputs_password").val(), status:$("#new_user_inputs_type").val()}, function(result){
								$("#new_user_alert").animate({"height":"22px", "padding-top":"8px", "padding-bottom":"8px"}, 150).delay(1000).animate({"height":"0px", "padding-top":"0px", "padding-bottom":"0px"}, 150);
								setTimeout(function(){
									$("#new_user_bottom").text("+ Add a new user").css({"color":"black"});
									$("#new_user").animate({"height":"20px"}, 150);
									$("#settings_block").animate({"height":"-=218px"}, 150);
								}, 1000);
							});
						}
					}
				}
			}
		}
	});
	$("#settings_profile_save").click(function(){
		$.post("/profileSettings", {name:$("#settings_name").val(), surname:$("#settings_surname").val(), currentPassword:$("#settings_current_password").val(), newPassword:$("#settings_new_password").val(), address:$("#settings_adress").val(), phone:$("#settings_phone").val()}, function(result){
			$("#settings_alert").animate({"height":"22px", "padding-top":"8px", "padding-bottom":"8px"}, 150).delay(1000).animate({"height":"0px", "padding-top":"0px", "padding-bottom":"0px"}, 150);
		});
	});
	$(".settings_users_list_modify").click(function(){
		var object = $(this).parent();
		var object20 = $(this);
		var id = $(this).attr("id");
		$.post("/modifyUser", {id:id, name:object.find(".settings_inputs_users_name").val(), address:object.find(".settings_inputs_users_adress").val(), phone:object.find(".settings_inputs_users_phone").val(), type:object.find(".settings_inputs_users_type").val()}, function(result){
			object20.text("Saved").animate({"margin-left":"720px"}, 100);
		});
	});
	$(".settings_orders_list_modify").click(function(){
		var object = $(this);
		var id = object.attr("id");
		$.post("/closeOrder", {orderId:id}, function(result){
			object.text("Closed").animate({"margin-left":"713px"}, 100);
		});
	});
});