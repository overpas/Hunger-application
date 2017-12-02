<html>
	<head>
		<title>Hunger: заказы</title>
		<meta charset="utf-8">
		<link rel="stylesheet" href="deliveryservicestyle.css">
	</head>
	<body>
		<form align="center">
			<label class="showLabel" id="numberOfOrdersShown">Показать: </label><input type="number" name="numberOfOrdersShown" value="50" min="1" step="1" onkeypress="return event.charCode >= 48" id="numberOfOrdersShown">
			<input type="image" src="img/refresh.png" name="btnRefresh" id="btnRefresh">
		</form>
		<table align="center" id="table" border="1">
			<tr>
				<th width="10%">Номер заказа</th>
				<th width="37%">Адрес</th>
				<th width="20%">Имя</th>
				<th width="20%">Номер телефона</th>
				<th width="13%">Статус</th>
			</tr>
			<?php
			include '../DatabaseConfig.php';

			$numberOfOrdersShown = $_GET["numberOfOrdersShown"];
			$conn = mysqli_connect($HostName, $HostUser, $HostPass);

			if (!$conn) {
				die("Cannot connect " . mysqli_error());
			}

			mysqli_select_db($conn, $DatabaseName);

			$sql = "SELECT orders.id, orders.destination, customers.name, customers.phone, orders.status FROM orders INNER JOIN customers ON orders.customer_id = customers.id WHERE orders.destination IS NOT NULL AND orders.status = 'pending' ORDER BY orders.order_date ASC LIMIT $numberOfOrdersShown";

			$myData = mysqli_query($conn, $sql);

			while ($record = mysqli_fetch_array($myData)) {
				echo "<tr>";
				echo "<td>" . $record[0] . "</td>";
				echo "<td>" . $record[1] . "</td>";
				echo "<td>" . $record[2] . "</td>";
				echo "<td>" . $record[3] . "</td>";
				echo "<td>" . "Ожидание" . "</td>";
				echo "</tr>";
			}

			mysqli_close($conn);
			?>
		</table>

		<script type="text/javascript">
			var index, table = document.getElementById('table');
			for (var i = 1; i < table.rows.length; i++) {
				//console.log(table.rows[i].cells[0].innerHTML);
				table.rows[i].cells[4].onclick = function() {
					var c = confirm("Подтвердить доставку данного заказа?");

					if (c === true) {
						index = this.parentElement.rowIndex;
						//console.log(table.rows[index]);
						var orderID = table.rows[index].cells[0].innerHTML;
						confirmDelivery(orderID);
						table.deleteRow(index);
					}
				};
			}
		</script>
		<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
		<script type="text/javascript">
			function confirmDelivery(orderID) {
				$.post('../ConfirmDelivery.php', {orderID: orderID}, function(data, textStatus, xhr) {
					console.log(data);
					/*optional stuff to do after success */
					alert("Доставка подтверждена.");
				});
				return false;
			}
		</script>
	</body>
</html>