<?php
	include 'DatabaseConfig.php' ;
 
	$con = mysqli_connect($HostName,$HostUser,$HostPass);
	
	if (!$con) {
		echo "Not connected to server";
	}
	
	if (!mysqli_select_db($con, $DatabaseName)) {
		echo "Not connected to database";
	}
	
	$sql = "DELETE FROM orders WHERE destination IS NULL OR customer_id IS NULL OR order_date IS NULL";
 
	if(!mysqli_query($con, $sql)){
		echo 'Not deleted';
	}
	else{
		echo 'Deleted';
	}
	
	mysqli_close($con);
?>