<?php
	include 'DatabaseConfig.php' ;
 
	$con = mysqli_connect($HostName,$HostUser,$HostPass);
	
	if (!$con) {
		echo -1;
	}
	
	if (!mysqli_select_db($con, $DatabaseName)) {
		echo -1;
	}
	
	$customerName = $_POST['customerName'];
	$customerPhone = $_POST['customerPhone'];
 
	$sql = "INSERT INTO customers(name, phone) VALUES('$customerName', '$customerPhone')";
	
	if (!mysqli_query($con, $sql)) {
		echo -1;
	} else {
		$id = mysqli_insert_id($con);
		echo $id;
	}
	
	mysqli_close($con);
?>