<?php
	include 'DatabaseConfig.php' ;
 
	$con = mysqli_connect($HostName,$HostUser,$HostPass);
	
	if (!$con) {
		echo "Not connected to server";
	}
	
	if (!mysqli_select_db($con, $DatabaseName)) {
		echo "Not connected to database";
	}
	
	$dishID = $_POST['dishID'];
	$orderID = $_POST['orderID'];
	
	$sql = "INSERT INTO carts(dish_id, order_id) VALUES('$dishID', '$orderID')";
 
	if(!mysqli_query($con, $sql)){
		echo 'Not inserted';
	}
	else{
		echo 'Inserted';
	}
	
	mysqli_close($con);
?>