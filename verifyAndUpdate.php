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
$user_id = (int)$_POST['user_id'];
$image = $_POST["image"];
$full_name = $_POST['full_name'];
$email = $_POST['email'];
$phone = $_POST['phone'];
$address = $_POST['address'];

$upload_path = "uploads/user/";
$image = str_replace(' ', '+', $image);
$data = base64_decode($image);
$file = $upload_path.uniqid(). '.jpg';
$success = file_put_contents($file, $data);

$image_url = 'https://ujwalparajuli.000webhostapp.com/android/'.$file;

$sql = "select * from verification where email = '$email' and code = $code";
$result = mysqli_query($conn, $sql);
if (mysqli_num_rows($result) < 1) {
	echo "error";
		
}
else{
	if ($success) {
		$qry = "update user set full_name = '$full_name', email = '$email', phone = '$phone', address = '$address', image = '$image_url'   where id = $user_id ";
		if (mysqli_query($conn, $qry)) {
			echo "success";
		}
		else {
				echo "error1";
		}
	}
	else{
			echo "error2";
	}  

}

?>