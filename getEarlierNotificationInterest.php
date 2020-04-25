<?php
$servername = "localhost";
$dbname = "id5461621_see";
$uname = "id5461621_event";
$pass = "12345";
$conn = mysqli_connect($servername, $uname, $pass, $dbname);
if (!$conn) {
	die("Connection failed!");
}

$organizer_id = $_POST['organizer_id'];
$d=strtotime("today");
$today_date = date('Y-m-d', $d);

$data = array();
$sql = "select user.full_name, user.email, user.phone, user.address, user.image, interested.interested_date, event_details.name, event_details.image as event_image from user inner join interested on interested.user_id = user.id inner join event_details on interested.event_id = event_details.id where event_details.organizer_id = $organizer_id and interested.interested_date < '$today_date 00:00:00' ";

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
