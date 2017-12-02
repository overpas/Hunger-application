<?php
	include 'DatabaseConfig.php' ;
 
	$con = mysqli_connect($HostName, $HostUser, $HostPass);
	
	if (!$con) {
		echo "Not connected to server";
	}
	
	if (!mysqli_select_db($con, $DatabaseName)) {
		echo "Not connected to database";
	}
	
	$orderID = $_POST['orderID'];
	
	$sql = "UPDATE orders SET status = 'received' WHERE id = $orderID;";
 
	if(!mysqli_query($con, $sql)){
		echo 'Not updated';
	} else{
		echo 'Updated';
	}
	
	mysqli_close($con);
?>