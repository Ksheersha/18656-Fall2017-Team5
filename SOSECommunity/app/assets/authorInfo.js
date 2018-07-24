function start() {
	var name = document.getElementById('name').value;
	document.getElementById('name').value = '';
	$('.fullName').text(name);
	name = name.replace(' ', '_');

	$(document).ready(function() {
	    $.ajax({
	        url: "http://localhost:9000/getAuthorInfo/" + name
	    }).then(function(data) {
	       $('.email').text(data.email);
	       $('.affiliation').text(data.affiliation);
	       $('.homepage').text(data.homepage);
	       $('.picture').text(data.picture);
	       $('.interests').text(data.interests);
	    }).catch(function(err) {
			console.log(err);
		});
	});
}