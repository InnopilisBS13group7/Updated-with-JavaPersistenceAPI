$(document).ready(function(){
	$(".bookit").click(function(){
		$.post("/takeItem", {documentId:$(this).attr("id")}, function(result){
    		$("#alert_message").text(result);
    		$("#alert_back").slideDown(0).animate({"opacity":"1"}, 200);
		});
	});
	$(".deleteit").click(function(){
		$.post("/deleteItem", {documentId:$(this).attr("id")}, function(result){
    		$("#alert_message").text(result);
    		$("#alert_back").slideDown(0).animate({"opacity":"1"}, 200);
		});
	});
	$(".modifyit").click(function(){
		var object = $(this).parent();
		var id = $(this).attr("id");
		$.post("/modifyDocument", {documentId:id, title:object.find(".books_inputs_title").val(), author:object.find(".books_inputs_author").val(), publisher:object.find(".books_inputs_publisher").val(), year:object.find(".books_inputs_year").val(), edition:object.find(".books_inputs_edition").val(), note:object.find(".books_inputs_note").val()}, function(result){
			$("#alert_message").text(result);
    		$("#alert_back").slideDown(0).animate({"opacity":"1"}, 200);
		});
	});
	$("#add_save_book").click(function(){
		var object = $(this);
		$.post("/addDocument", {title:$("#add_book_title").val(), author:$("#add_book_author").val(), publisher:$("#add_book_publisher").val(), year:$("#add_book_year").val(), edition:$("#add_book_edition").val(), note:$("#add_book_note").val()}, function(result){
			object.text("The book is added");
			setTimeout(function(){
				$("#new_book").text("+ Add a new book").css({"color":"black"});
				$("#add_block_book").animate({"height":"0px"}, 150);
				setTimeout(function(){
					object.text("Add");
				}, 200);
			}, 1500);
		});
	});
	$("#add_save_av").click(function(){
		var object = $(this);
		$.post("/addAV", {title:$("#add_av_title").val(), author:$("#add_av_author").val()}, function(result){
			object.text("The book is added");
			setTimeout(function(){
				$("#new_av").text("+ Add a new audio/video").css({"color":"black"});
				$("#add_block_av").animate({"height":"0px"}, 150);
				setTimeout(function(){
					object.text("Add");
				}, 200);
			}, 1500);
		});
	});
	$("#new_book").click(function(){
		if($(this).text() == "+ Add a new book"){
			$(this).text("Close").css({"color":"#ff4751"});
			$("#add_block_book").animate({"height":"236px"}, 150);
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
			$("#add_block_av").animate({"height":"109px"}, 150);
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
  	$(document).on('keypress', ".booking_search_name", function(e){
	    if(e.keyCode==13){
	    	var search = new Array(16);
	    	var i = 0;
	    	$(".booking_search").each(function(){
	    		search[i] = $(this).find(".booking_search_name").val();
	    		search[i + 1] = $(this).find(".booking_search_select").val();
	    		i += 2;
	    	})
	    	//alert(search[15])
	    	$.post("/bookingSearch", {text0:search[0], type0:search[1], text1:search[2], type1:search[3], text2:search[4], type2:search[5], text3:search[6], type3:search[7], text4:search[8], type4:search[9], text5:search[10], type5:search[11], text6:search[12], type6:search[13], text7:search[14], type7:search[15]}, function(result){
      				alert(result)
    		});
	    }
	});
	$(document).on('click', ".search_booking_plus", function(){
		if($(this).css("transform") == "matrix(1, 0, 0, 1, 0, 0)"){
			$("#booking_search_box").append("<div class=booking_search><input type=text class=booking_search_name placeholder='Search' /><select class=booking_search_select><option>By title</option><option>By title</option><option>By author</option><option>By publisher</option><option>By year</option><option>By type</option><option>By edition</option><option>By note</option></select><div class=search_booking_plus style='transform:rotate(45deg)'>+</div></div>")
		}
		else if($(this).css("transform") == "matrix(0.707107, 0.707107, -0.707107, 0.707107, 0, 0)"){
			$(this).parent().remove();
		}
	});
});