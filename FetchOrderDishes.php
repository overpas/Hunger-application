<?php
	include 'DatabaseConfig.php';

	// Create connection
	$conn = new mysqli($HostName, $HostUser, $HostPass, $DatabaseName);

	$orderID = $_GET['orderID'];

	if ($conn->connect_error) {
		die("Connection failed: " . $conn->connect_error);
	} 

	$sql = "SELECT * FROM dishes INNER JOIN carts ON dishes.id = carts.dish_id WHERE carts.order_id='$orderID'";

	$result = $conn->query($sql);

	if ($result->num_rows >0) {
 
		while($row[] = $result->fetch_assoc()) {
			$tem = $row;
			$json = json_encode($tem);
		}
 
	} else {
		echo "No Results Found.";
	}
	
	echo $json;
	
	$conn->close();
?>