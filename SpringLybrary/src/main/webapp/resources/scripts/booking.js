$(document).ready(function(){
	function update(){
		$.post("/bookingUpdate", function(result){
			$(".books_box").remove();
			$("#more_box20").append(result);
		});
	}
	$(document).on('click', ".bookit", function(){
		$.post("/takeItem", {documentId:$(this).attr("id")}, function(result){
    		$("#alert_message").text(result);
    		$("#alert_back").slideDown(0).animate({"opacity":"1"}, 200);
    		update();
		});
	});
	$(document).on('click', ".deleteit", function(){
		$.post("/deleteItem", {documentId:$(this).attr("id")}, function(result){
    		$("#alert_message").text(result);
    		$("#alert_back").slideDown(0).animate({"opacity":"1"}, 200);
    		update();
		});
	});
	$(document).on('click', ".modifyit", function(){
		var object = $(this).parent();
		var id = $(this).attr("id");
		$.post("/modifyDocument", {documentId:id, title:object.find(".books_inputs_title").val(), author:object.find(".books_inputs_author").val(), publisher:object.find(".books_inputs_publisher").val(), year:object.find(".books_inputs_year").val(), edition:object.find(".books_inputs_edition").val(), note:object.find(".books_inputs_note").val()}, function(result){
			$("#alert_message").text(result);
    		$("#alert_back").slideDown(0).animate({"opacity":"1"}, 200);
    		update();
		});
	});
	$("#add_save_book").click(function(){
		var object = $(this);
		$.post("/addDocument", {number:$("#add_book_number").val(), title:$("#add_book_title").val(), author:$("#add_book_author").val(), publisher:$("#add_book_publisher").val(), year:$("#add_book_year").val(), edition:$("#add_book_edition").val(), note:$("#add_book_note").val()}, function(result){
			object.text("The book is added");
			setTimeout(function(){
				$("#new_book").text("+ Add a new book").css({"color":"black"});
				$("#add_block_book").animate({"height":"0px"}, 150);
				setTimeout(function(){
					object.text("Add");
				}, 200);
			}, 1500);
			update();
		});
	});
	$("#add_save_av").click(function(){
		var object = $(this);
		$.post("/addAV", {number:$("#add_book_number").val(), title:$("#add_av_title").val(), author:$("#add_av_author").val()}, function(result){
			object.text("The book is added");
			setTimeout(function(){
				$("#new_av").text("+ Add a new audio/video").css({"color":"black"});
				$("#add_block_av").animate({"height":"0px"}, 150);
				setTimeout(function(){
					object.text("Add");
				}, 200);
			}, 1500);
			update();
		});
	});
	$("#new_book").click(function(){
		if($(this).text() == "+ Add a new book"){
			$(this).text("Close").css({"color":"#ff4751"});
			$("#add_block_book").animate({"height":"268px"}, 150);
			$("#new_av").text("+ Add a new audio/video").css({"color":"black"});
			$("#add_block_av").animate({"height":"0px"}, 150);
		}
		else if($(this).text() == "Close"){
			$(this).text("+ Add a new book").css({"color":"black"});
			$("#add_block_book").animate({"height":"0px"}, 150);
		}
	});
	$("#new_av").click(function(){
		if($(this).text() == "+ Add a new audio/video"){
			$(this).text("Close").css({"color":"#ff4751"});
			$("#add_block_av").animate({"height":"141px"}, 150);
			$("#new_book").text("+ Add a new book").css({"color":"black"});
			$("#add_block_book").animate({"height":"0px"}, 150);
		}
		else if($(this).text() == "Close"){
			$(this).text("+ Add a new audio/video").css({"color":"black"});
			$("#add_block_av").animate({"height":"0px"}, 150);
		}
	});
	$(".queue").click(function(){
		var object = $(this).parent().find(".queue_box");
		if($(this).text() == "Queue"){
			$(this).text("Hide");
			$.post("/goToQueue", {id:$(this).attr("id")}, function(result){
				if(result != "Queue is empty"){	
					object.parent().animate({"height":"345px"}, 200);
					object.slideDown(0).html(result).animate({"height":"200px"}, 200);
				}
			});
		}
		else{
			$(this).text("Queue");
			object.parent().animate({"height":"145px"}, 200);
			object.animate({"height":"0px"}, 200).slideUp(0);
		}
	});
	$(document).on('click', ".otdat", function(){
    	$.post("/queueRequest", {id:$(this).attr("id")}, function(result){
      		$("#alert_message").text(result);
    		$("#alert_back").slideDown(0).animate({"opacity":"1"}, 200);
    	});
  	});
  	$("#booking_search_name").keypress(function(e){
  		var available = "False";
  		if(parseInt($("#booking_available_id").css("width")) == 10)
  			available = "True";
  		if(e.keyCode==13){
  			$.post("/bookingSearch", {text:$("#booking_search_name").val(), type:$("#booking_search_select").val(), available:available}, function(result){
  				if(result == ""){
  					$("#alert_message").text("Not found");
                	$("#alert_back").slideDown(0).animate({"opacity":"1"}, 200);
  				}
  				else{
  					$(".books_box").remove();
					$("#more_box20").append(result);
  				}
  			});
  		}
  	});
  	$("#booking_available").mouseenter(function(){
  		$("#booking_available_text").slideDown(0).animate({"opacity":"1", "margin-left":"28px"}, 100);
  	});
  	$("#booking_available").mouseout(function(){
  		$("#booking_available_text").animate({"opacity":"0", "margin-left":"8px"}, 100).slideUp(0);
  	});
  	$("#booking_available").click(function(){
  		if(parseInt($("#booking_available_id").css("width")) == 10)
  			$("#booking_available_id").animate({"margin-left":"788px", "margin-top":"-12px", "width":"0px", "height":"0px"}, 100);
  		else
  			$("#booking_available_id").animate({"margin-left":"783px", "margin-top":"-17px", "width":"10px", "height":"10px"}, 100);
  	});
});