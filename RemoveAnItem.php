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
	
	$sql = "DELETE FROM carts WHERE dish_id=$dishID AND order_id=$orderID LIMIT 1";
	
	if(!mysqli_query($con, $sql)){
		echo 'Not deleted';
	}
	else{
		echo 'deleted';
	}
	
	mysqli_close($con);
?>