<?php
$servername = "localhost";
$dbname = "id5461621_see";
$uname = "id5461621_event";
$pass = "12345";
$conn = mysqli_connect($servername, $uname, $pass, $dbname);
if (!$conn) {
	die("Connection failed!");
}

$user_id = (int)$_POST['user_id'];
$event_id = (int)$_POST['event_id'];

$sql = "delete from interested where user_id = $user_id and event_id = $event_id ";

if(mysqli_query($conn, $sql)) {
	$sql2 = "select * from interested where event_id = '$event_id' ";
	$result = mysqli_query($conn, $sql2);
	$total = mysqli_num_rows($result);
	$sql3 = "update event_details set total_people = '$total' where id = '$event_id' ";
	if (mysqli_query($conn, $sql3)) {
		$data['total_people'] = $total;
		echo json_encode($data);
	}
}
else{
	echo "error";
}


?>