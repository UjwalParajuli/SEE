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
$d=strtotime("today");
$today_date = date('Y-m-d', $d);

$sql = "insert into interested (user_id, event_id, interested_date) values ('$user_id', '$event_id', '$today_date') ";

if(mysqli_query($conn, $sql)){
	echo "success";
}
else{
	echo "error";
}




?>