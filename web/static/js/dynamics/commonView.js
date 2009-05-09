var commonView = {
  showError: function(message) {
    var messageScreen = $('<div />').html(message).addClass("errorMessage").appendTo(document.body);
    setTimeout(function() {
      messageScreen.fadeOut(2000, function() { messageScreen.remove()});
    }, 8000);
  },
  showOk: function(message) {
    var messageScreen = $('<div />').html(message).addClass("okMessage").appendTo(document.body);
    messageScreen.fadeOut(2000, function() { messageScreen.remove()});
  },
  expandCollapse: function(parent, expandCb, collapseCb) {
	var button = $("<div />").addClass("dynamictable-expand").appendTo(parent);
	button.toggle(function() {
		button.toggleClass("dynamictable-expand").toggleClass("dynamictable-collapse");
		expandCb();
	},
	function() {
		button.toggleClass("dynamictable-expand").toggleClass("dynamictable-collapse");
		collapseCb();
	});
  },
  effortError: function(connectTo) {
	 var err = $("<div />").addClass("cellErrorMessage").appendTo(document.body);
	 $("<span />").css("color", "red").text("Invalid value!").appendTo(err);
	 $("<br />").appendTo(err);
	 $("<span />").text("e.g. 1.5h or 1h 30min").appendTo(err);
	 var cpos = connectTo.position();
	 err.css({position: "absolute", top: cpos.top + connectTo.height(), left: cpos.left});
	 return err;
  },
  requiredFieldError: function(connectTo) {
	  var err = $("<div />").addClass("cellErrorMessage").appendTo(document.body);
	 $("<span />").css("color", "red").text("Required field").appendTo(err);
	 var cpos = connectTo.position();
	 err.css({position: "absolute", top: cpos.top + connectTo.height(), left: cpos.left});
	 return err;
  }
  
};
