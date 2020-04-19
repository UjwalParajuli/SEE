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
$full_name = $_POST['full_name'];
$email = $_POST['email'];
$password = $_POST['password'];
$phone = $_POST['phone'];
$address = $_POST['address'];

$sql = "select * from verification where email = '$email' and code = $code";
$result = mysqli_query($conn, $sql);
if (mysqli_num_rows($result) < 1) {
	echo "error";
		
}
else {
	$hashed_password = password_hash($password, PASSWORD_DEFAULT);
	$qry = "insert into user (full_name, email, password, phone, address, image) values ('$full_name' , '$email', '$hashed_password', '$phone', '$address', 'https://ujwalparajuli.000webhostapp.com/android/uploads/user/dummy_user.jpg')";
			
	$result = mysqli_query($conn, $qry);
	if(!$result){
		echo "dbError";
	}
	else{
		echo "success";
	}
}
	
?>



