<?php
$servername = "localhost";
$dbname = "id5461621_see";
$uname = "id5461621_event";
$pass = "12345";
$conn = mysqli_connect($servername, $uname, $pass, $dbname);
if (!$conn) {
	die("Connection failed!");
}

$user_id = (int)$_POST['user_id'];
$data = array();

$sql = "select purchase.ticket_no, purchase.purchased_date, purchase.quantity, purchase.amount, event_details.id, event_details.organizer_id, event_details.name, event_details.city, event_details.venue, event_details.start_date, event_details.start_time, event_details.end_date, event_details.end_time, event_details.category, event_details.description, event_details.image, event_details.total_people, ticket.ticket_required, ticket.cost_per_ticket, ticket.total_tickets, user.full_name from event_details inner join ticket on event_details.id = ticket.event_id inner join user on event_details.organizer_id = user.id inner join purchase on event_details.id = purchase.event_id where purchase.user_id = $user_id ";

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
