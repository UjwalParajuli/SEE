<?php
$servername = "localhost";
$dbname = "id5461621_see";
$uname = "id5461621_event";
$pass = "12345";
$conn = mysqli_connect($servername, $uname, $pass, $dbname);
if (!$conn) {
	die("Connection failed!");
}

$event_id = (int)$_POST['event_id'];

$data = array();
$sql = "select user.full_name, user.email, user.phone, user.address, user.image, interested.interested_date from user inner join interested on user.id = interested.user_id where interested.event_id = $event_id ";

$result = mysqli_query($conn, $sql);
if (mysqli_num_rows($result) < 0) {
	echo "error";
}

else {
	while ($row = mysqli_fetch_assoc($result)) {
		$data[] = $row;
	}
	

}
echo json_encode($data);
?>