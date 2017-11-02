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

	$sql = "DELETE FROM orders WHERE id=$orderID";
	
	if(!mysqli_query($con, $sql)){
		echo 'Not deleted';
	}
	else{
		echo 'deleted';
	}
	
	mysqli_close($con);
?>