<?php
	include 'DatabaseConfig.php' ;
 
	$con = mysqli_connect($HostName,$HostUser,$HostPass);
	
	if (!$con) {
		echo "Not connected to server";
	}
	
	if (!mysqli_select_db($con, $DatabaseName)) {
		echo "Not connected to database";
	}
	
	$orderID = $_POST['orderID'];
	$destination = $_POST['destination'];
	$customerID = $_POST['customerID'];
	
	$sql = "UPDATE orders SET destination = '$destination', customer_id = $customerID, order_date=NOW() WHERE id = $orderID;";
 
	if(!mysqli_query($con, $sql)){
		echo 'Not updated';
	}
	else{
		echo 'Updated';
	}
	
	mysqli_close($con);
?>