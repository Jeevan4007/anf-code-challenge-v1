(function($, $document) {
    "use strict";
$(document).on("click", "#userForm form button", function () {

            //Fetching Age from input field
    		let currentAge = $("input[name=currentAge]").val();
            let firstName = $("input[name=firstName]").val();
            let lastName = $("input[name=lastName]").val();
            let country = $('select[name=selectCountry]').val();

            //Ajax call for servlet for validation
    		$.ajax({
    			type: 'GET',
    			url: '/bin/saveUserDetails',
    			data: {
    				"age": currentAge,
                    "firstName": firstName,
                    "lastName": lastName,
                    "country": country
    			},
    			contentType: 'text/plain',
    			success: function(resp) {
    				if (resp == "false") {
    				    //Showing warning for non eligible age
                        alert("You are not eligible");
    					event.preventDefault();
                        return false;

    				} else {
    				    //Redirecting to Thank you Page
    					window.location.href = "/content/anf-code-challenge/us/en/thank-you-for-submission.html";
    				}
    			}
    		});
    });
}(jQuery, jQuery(document)));