<!DOCTYPE html>
<html>
<head>
<title>Login Form</title>
<script src="jquery/jquery.js"></script>
<script>
$(document).ready(function(){
$('#loginForm').on('submit', function(event) {
event.preventDefault();
var username = $('#username').val();
var password = $('#password').val();
$.ajax({
url: 'productService/login/add',
type: 'POST',
contentType: 'application/json',
data: JSON.stringify({
username: username,
password: password
}),
success: function(response) {
alert('Login successful: ' + response);
$("#nextPageContent").text(response);

},
error: function(xhr, status, error) {
$("#error").html('Login failed: ' + xhr.responseText);
}
});
});
$('#nextPage').click(function() {
console.log("button clicked");
$.ajax({
url : 'productService/login/next',
type: 'GET',
success : function(response){
$("#nextPageContent").text(response);
},
error : function(xhr,status,error){
console.log(xhr.status + " : " + xhr.statusText);
}

});
});
});
</script>
</head>
<body>
<h2>Login Form</h2>
<form id="loginForm">
<div>
<label for="username">Username:</label>
<input type="text" id="username" name="username" required>
</div>
<div>
<label for="password">Password:</label>
<input type="password" id="password" name="password" required>
</div>
<div>
<button type="submit">Login</button>
</div>
<div id='error'></div>
</form>
<button id="nextPage">next page</button>
<div id='nextPageContent'></div>
</body>
</html>
