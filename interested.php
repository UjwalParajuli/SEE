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
date_default_timezone_set("Asia/Kathmandu");
$today_date = date("Y-m-d H:i:s");
$data = array();

$sql = "insert into interested (user_id, event_id, interested_date) values ('$user_id', '$event_id', '$today_date') ";

if(mysqli_query($conn, $sql)){
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