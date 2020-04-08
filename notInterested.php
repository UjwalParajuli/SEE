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
	echo "success";
}
else{
	echo "error";
}


?>