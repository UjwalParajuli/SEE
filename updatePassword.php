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
$password = $_POST['password'];
$hashed_password = password_hash($password, PASSWORD_DEFAULT);

$sql = "update user set password = '$hashed_password' where id = $user_id ";

if (mysqli_query($conn, $sql)) {
	echo "success";
}
else {
	echo "error";
}

?>