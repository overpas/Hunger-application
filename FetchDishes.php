<?php
	include 'DatabaseConfig.php';

	// Create connection
	$conn = new mysqli($HostName, $HostUser, $HostPass, $DatabaseName);

	$categoryID = $_GET['categoryID'];

	if ($conn->connect_error) {
		die("Connection failed: " . $conn->connect_error);
	} 

	$sql = "SELECT id, name, url, price, category_id FROM dishes WHERE category_id=$categoryID ORDER BY id";

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