<?php
$servername = "localhost";
$dbname = "id5461621_see";
$uname = "id5461621_event";
$pass = "12345";
$conn = mysqli_connect($servername, $uname, $pass, $dbname);
if (!$conn) {
	die("Connection failed!");
}

$organizer_id = (int)$_POST['user_id'];
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

$sql = "select * from event_details where organizer_id = '$organizer_id' and name = '$event_name' and city = '$city_name' and venue = '$venue_name' and start_date = '$new_start_date' and start_time = '$start_time' and end_date = '$new_end_date' and end_time = '$end_time' and category = '$category' and description = '$description'";

$result = mysqli_query($conn, $sql);
if (mysqli_num_rows($result) > 0) {
	echo "error";
}

else{
	if ($success) {
		$qry = "insert into event_details (organizer_id, name, city, venue, start_date, start_time, end_date, end_time, category, description, image) values ($organizer_id, '$event_name', '$city_name', '$venue_name', '$new_start_date', '$start_time', '$new_end_date', '$end_time', '$category', '$description', '$image_url')";
		if (mysqli_query($conn, $qry)) {
			$last_id = mysqli_insert_id($conn);
			$qry2 = "insert into ticket (event_id, ticket_required, cost_per_ticket, total_tickets) values ($last_id, '$ticket_required', $cost_per_ticket, $total_tickets)";
			if (mysqli_query($conn, $qry2)) {
				echo "success";
			}
			else {
				echo "ticketError";
			}
		}
		else{
			echo "";
		}
	}
	
	else {
		echo "eventDetailsError";
	}
}
