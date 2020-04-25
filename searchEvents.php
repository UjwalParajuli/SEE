<?php
$servername = "localhost";
$dbname = "id5461621_see";
$uname = "id5461621_event";
$pass = "12345";
$conn = mysqli_connect($servername, $uname, $pass, $dbname);
if (!$conn) {
	die("Connection failed!");
}

$city_name = $_POST['city_name'];
$start_date = strtotime($_POST['start_date']);
$new_start_date = date('Y-m-d', $start_date);
$end_date = strtotime($_POST['end_date']);
$new_end_date = date('Y-m-d', $end_date);
$category = $_POST['category'];

$data = array();

if ($category == 'All') {
	$sql = "select event_details.id, user.image as user_image, event_details.created_on, event_details.organizer_id, event_details.name, event_details.city, event_details.venue, event_details.start_date, event_details.start_time, event_details.end_date, event_details.end_time, event_details.category, event_details.description, event_details.image, event_details.total_people, ticket.ticket_required, ticket.cost_per_ticket, ticket.total_tickets, user.full_name from event_details inner join ticket on event_details.id = ticket.event_id inner join user on event_details.organizer_id = user.id where (event_details.start_date >= '$new_start_date' and event_details.end_date <= '$new_end_date') and (event_details.city LIKE CONCAT('%', '$city_name', '%') or event_details.venue LIKE CONCAT('%', '$city_name', '%'))";

	$result = mysqli_query($conn, $sql);
	if (mysqli_num_rows($result) < 0) {
		echo "error";
	}

	else {
		while ($row = mysqli_fetch_assoc($result)) {
			$data[] = $row;
		}
		

	}
}

else{
	$sql2 = "select event_details.id, user.image as user_image, event_details.organizer_id, event_details.name, event_details.city, event_details.venue, event_details.start_date, event_details.start_time, event_details.end_date, event_details.end_time, event_details.category, event_details.description, event_details.image, event_details.total_people, ticket.ticket_required, ticket.cost_per_ticket, ticket.total_tickets, user.full_name from event_details inner join ticket on event_details.id = ticket.event_id inner join user on event_details.organizer_id = user.id where (event_details.start_date >= '$new_start_date' and event_details.end_date <= '$new_end_date' and event_details.category = '$category') and (event_details.city LIKE CONCAT('%', '$city_name', '%') or event_details.venue LIKE CONCAT('%', '$city_name', '%'))";

	$result = mysqli_query($conn, $sql2);
	if (mysqli_num_rows($result) < 0) {
		echo "error";
	}

	else {
		while ($row = mysqli_fetch_assoc($result)) {
			$data[] = $row;
		}
		

	}


}

echo json_encode($data);
?>