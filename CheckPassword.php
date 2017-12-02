<?php 
	include 'DatabaseConfig.php';
	$conn = new mysqli($HostName, $HostUser, $HostPass, $DatabaseName);

	if ($conn->connect_error) {
	   die('Connection failed: ' . $conn->connect_error);
	}

	$sql = 'SELECT password FROM passwords WHERE id = 1';
	$result = $conn->query($sql);
	$typedPassword = $_POST['password'];

	if ($result->num_rows >0) {
		$rows = mysqli_fetch_array($result);
		$realPassword = $rows[0];
	    
		if ($typedPassword == $realPassword) {
			echo "Confirmed";
		} else {
			echo "Denied";
		}
	} else { 
	    echo 'No Results Found.'; 
	} 

	$conn->close();
?>