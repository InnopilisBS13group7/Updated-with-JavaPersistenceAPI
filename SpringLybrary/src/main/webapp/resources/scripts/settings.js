$(document).ready(function(){
	$.getScript('../resources/scripts/close_order.js');
	$("#settings_block").height($("#settings_type_menu").height() + $("#settings_profile").height());
	$("#settings_type_profile").click(function(){
		$("#settings_profile").animate({"margin-left":"0"}, 300);
		$("#settings_type_line").animate({"margin-left":"0px"}, 300);
		$("#settings_users").animate({"margin-left":"800px"}, 300);
		$("#settings_orders").animate({"margin-left":"1600px"}, 300);
		$("#settings_history").animate({"margin-left":"2400px"}, 300);
		$("#settings_block").animate({"height":$("#settings_type_menu").height() + $("#settings_profile").height()}, 300);
	});
	$("#settings_type_users").click(function(){
		$("#settings_profile").animate({"margin-left":"-800px"}, 300);
		$("#settings_type_line").animate({"margin-left":"200px"}, 300);
		$("#settings_users").animate({"margin-left":"0px"}, 300);
		$("#settings_orders").animate({"margin-left":"800px"}, 300);
		$("#settings_history").animate({"margin-left":"1600px"}, 300);
		$("#settings_block").animate({"height":$("#settings_type_menu").height() + $("#settings_users").height()}, 300);
	});
	$("#settings_type_orders").click(function(){
		$("#settings_profile").animate({"margin-left":"-1600px"}, 300);
		$("#settings_type_line").animate({"margin-left":"400px"}, 300);
		$("#settings_users").animate({"margin-left":"-800px"}, 300);
		$("#settings_orders").animate({"margin-left":"0px"}, 300);
		$("#settings_history").animate({"margin-left":"800px"}, 300);
		$("#settings_block").animate({"height":$("#settings_type_menu").height() + $("#settings_orders").height()}, 300);
	});
	$("#settings_type_history").click(function(){
		$("#settings_profile").animate({"margin-left":"-2400px"}, 300);
		$("#settings_type_line").animate({"margin-left":"600px"}, 300);
		$("#settings_users").animate({"margin-left":"-1600px"}, 300);
		$("#settings_orders").animate({"margin-left":"-800px"}, 300);
		$("#settings_history").animate({"margin-left":"0px"}, 300);
		$("#settings_block").animate({"height":$("#settings_type_menu").height() + $("#settings_history").height()}, 300);
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
	$(".settings_users_list_delete").click(function(){
		var object20 = $(this);
		var id = $(this).attr("id");
		$.post("/deleteUser", {id:id}, function(result){
			object20.text("Deleted").animate({"margin-left":"708px"}, 100);
		});
	});
	$("#search_all").click(function(){
		//alert($(this).css("border"));
		if($(this).css("border") == "2px solid rgb(0, 0, 0)")
			$("#search_all, #search_closed, #search_open, #search_finished, #search_queue, #search_renewed").css({"border":"2px solid #ff000d"});
		else
			$("#search_all, #search_closed, #search_open, #search_finished, #search_queue, #search_renewed").css({"border":"2px solid black"});
	});
	$("#search_closed, #search_open, #search_finished, #search_queue, #search_renewed").click(function(){
		if($(this).css("border") == "2px solid rgb(0, 0, 0)")
			$(this).css({"border":"2px solid #ff000d"});
		else{
			$(this).css({"border":"2px solid black"});
			$("#search_all").css({"border":"2px solid black"});
		}
	});
	$("#search_user").click(function(){
		if($(this).css("border") == "2px solid rgb(0, 0, 0)")
			$(this).css({"border":"2px solid #ff000d"});
		else
			$(this).css({"border":"2px solid black"});
	});
	$("#search_button").click(function(){
		var type = "";
		$(".search_buttons").each(function(){
			if($(this).text() != "All" && $(this).css("border") == "2px solid rgb(255, 0, 13)"){
				if($(this).text() == "Queue")
					var text = "waitForAccept"
				else text = $(this).text();
				type += text + ",";
			}
		});
		// alert(type);
		$.post("/ordersSearch", {type:type.slice(0, -1), id:$("#search_user_id").val()}, function(result){
			$("#list_box").html(result);
			$.getScript('../resources/scripts/close_order.js');
		});
	});
	$("#search_users").keypress(function(e){
	    if(e.keyCode==13){
	    	$.post("/searchUsers", {text:$(this).val(), type:$("#settings_search_select").val()}, function(result){
	    		alert(result);
      			if(result == ""){
  					$("#alert_message").text("Not found");
                	$("#alert_back").slideDown(0).animate({"opacity":"1"}, 200);
  				}
  				else{
  					$(".settings_list_users").remove();
					$("#settings_users").append(result);
  				}
    		});
	    }
	});
});