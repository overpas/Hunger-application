<?php
	include 'DatabaseConfig.php' ;
 
	$con = mysqli_connect($HostName,$HostUser,$HostPass);
	
	if (!$con) {
		echo -1;
	}
	
	if (!mysqli_select_db($con, $DatabaseName)) {
		echo -1;
	}
 
	$sql = "INSERT INTO orders(status) VALUES('pending')";
	
	if (!mysqli_query($con, $sql)) {
		echo -1;
	} else {
		$id = mysqli_insert_id($con);
		echo $id;
	}
	
	mysqli_close($con);
?>