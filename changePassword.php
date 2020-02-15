<?php
$servername = "localhost";
$dbname = "id5461621_see";
$uname = "id5461621_event";
$pass = "12345";
$conn = mysqli_connect($servername, $uname, $pass, $dbname);
if (!$conn) {
	die("Connection failed!");
}

$email = $_POST['email'];
$password = $_POST['password'];

$sql = "select * from user where email = '$email'";

$result = mysqli_query($conn, $sql);
if (mysqli_num_rows($result) < 1) {
	echo "error";
}
else {
	$hashed_password = password_hash($password, PASSWORD_DEFAULT);
	$qry = "update user set password = '$hashed_password' where email = '$email'";

	$result = mysqli_query($conn, $qry);
	if(!$result){
		echo "dbError";
	}
	else{
		echo "success";
	}
}