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

$sql = "select image from user where id = $user_id ";

$json_array = array();

$result = mysqli_query($conn, $sql);
if (mysqli_num_rows($result) < 1) {
	echo "error";
		
}
else{
	if ($row = mysqli_fetch_array($result)) {
		$json_array = $row;
		echo json_encode($json_array);
	}
}

?>