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
$image = $_POST["image"];
$event_name = $_POST['event_name'];
$city_name = $_POST['city_name'];
$venue_name = $_POST['venue_name'];
$start_date = strtotime($_POST['start_date']);
$new_start_date = date('Y-m-d', $start_date);
$end_date = strtotime($_POST['end_date']);
$new_end_date = date('Y-m-d', $end_date);
$start_time = $_POST['start_time'];
$end_time = $_POST['end_time'];
$category = $_POST['category'];
$description = $_POST['description'];
$ticket_required = $_POST['ticket_required'];
$cost_per_ticket = floatval($_POST['cost_per_ticket']);
$total_tickets = (int)$_POST['total_tickets'];

$upload_path = "uploads/";
$image = str_replace(' ', '+', $image);
$data = base64_decode($image);
$file = $upload_path.uniqid(). '.jpg';
$success = file_put_contents($file, $data);

$image_url = 'https://ujwalparajuli.000webhostapp.com/android/'.$file;   


if ($success) {
	$qry = "update event_details set name = '$event_name', city = '$city_name', venue = '$venue_name', start_date = '$new_start_date', start_time = '$start_time', end_date = '$new_end_date', end_time = '$end_time', category = '$category', description = '$description', image = '$image_url' where id = $event_id ";
	if (mysqli_query($conn, $qry)) {
		$qry2 = "update ticket set ticket_required = '$ticket_required', cost_per_ticket = $cost_per_ticket, total_tickets = $total_tickets where event_id = $event_id ";
		if (mysqli_query($conn, $qry2)) {
			echo "success";
		}
		else {
			echo "error";
		}
		
	}
	else {
			echo "error1";
	}
}
else{
		echo "error2";
}
	
