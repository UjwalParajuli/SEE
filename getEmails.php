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
$sql = "select email from user where email != '$email' ";

$result = mysqli_query($conn, $sql);
if (mysqli_num_rows($result) <= 0) {
	echo "error";
}

else {
	while ($row = mysqli_fetch_assoc($result)) {
		$data[] = $row;
	}
	

}
echo json_encode($data);
?>