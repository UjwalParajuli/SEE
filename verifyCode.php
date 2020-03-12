<?php
$servername = "localhost";
$dbname = "id5461621_see";
$uname = "id5461621_event";
$pass = "12345";
$conn = mysqli_connect($servername, $uname, $pass, $dbname);
if (!$conn) {
	die("Connection failed!");
}

$code = (int)$_POST['code'];
$email = $_POST['email'];

$sql = "select * from change_password_verification where email = '$email' and code = $code";
$result = mysqli_query($conn, $sql);
if (mysqli_num_rows($result) < 1) {
	echo "error";
		
}

else{
	$result = mysqli_query($conn, $qry);
	if(!$result){
		echo "dbError";
	}
	else{
		echo "success";
	}
}

?>