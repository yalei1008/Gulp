<!-- deposit.html -->
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Restaurant Details</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
	<style type="text/css">
		.navbar-default {
		    background-color: #8181F7;
		    border-color: #A9BCF5;
		}
		.navbar-default .navbar-brand {
   			color: #FFF;
		}
		/* link */
		.navbar-default .navbar-nav > li > a {
		    color: #FFF;
		}
		.navbar-default .navbar-nav > li > a:hover,
		.navbar-default .navbar-nav > li > a:focus {
		    color: #FFF;
		}
		.navbar-default .navbar-nav > .active > a, 
		.navbar-default .navbar-nav > .active > a:hover, 
		.navbar-default .navbar-nav > .active > a:focus {
		    color: #FFF;
		    background-color: #A9BCF5;
		}
		.navbar-default .navbar-nav > .open > a, 
		.navbar-default .navbar-nav > .open > a:hover, 
		.navbar-default .navbar-nav > .open > a:focus {
		    color: #555;
		    background-color: #D5D5D5;
		}
	</style>
	
</head>
<body>

<nav class="navbar navbar-default">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>                        
      </button>
      <a class="navbar-brand" href="restlist">Gulp!</a>
    </div>
    <div class="collapse navbar-collapse" id="myNavbar">
      <ul class="nav navbar-nav">
		<li><a href="restlist">Restaurant List</a></li>
        <li><a href="user">Account Info</a></li>
      </ul>
      <ul class="nav navbar-nav navbar-right">
        <!-- li><a href="login"><span class="glyphicon glyphicon-user"></span> Sign Up</a></li>
        <li class="active"><a href="login"><span class="glyphicon glyphicon-log-in"></span> Login</a></li -->
        ${navRight}
      </ul>
    </div>
  </div>
</nav>
  
<div class="container">
	<p>
		${results}
	</p>  
	
	${loginerror}
	
			<h3>Write a review</h3>
		<form role="form" method="post" action="restpage">
			
			<div class="form-group">
				<a href="#" data-toggle="tooltip" title="Select rating">
				<select class="form-control" name="rating">
			        <option>5</option>
			        <option>4</option>
			        <option>3</option>
			        <option>2</option>
			        <option>1</option>
			    </select></a>
		    </div>
			<div class="form-group">
				<textarea type="text" class="form-control" name="review" placeholder="Please enter reviews"></textarea>
			</div>
			<button type="submit" class="btn btn-default" id="submit">Add</button>
		</form>



</div>

</body>
</html>

