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
$full_name = $_POST['full_name'];
$event_name = $_POST['event_name'];
$user_id = (int)$_POST['user_id'];
$event_id = (int)$_POST['event_id'];
$quantity = (int)$_POST['quantity'];
$total_cost = floatval($_POST['total_cost']);
date_default_timezone_set("Asia/Kathmandu");
$today_date = date("Y-m-d H:i:s");
$total_quantity = (int)$_POST['total_quantity'];
$random = (int)rand(10000,50000);

$remaining = $total_quantity - $quantity;

$sql = "insert into purchase (user_id, event_id, purchased_date, quantity, amount, ticket_no) values ($user_id, $event_id, '$today_date', $quantity, $total_cost, $random)";

if(mysqli_query($conn, $sql)){
	$sql2 = "update ticket set total_tickets = $remaining where event_id = $event_id ";
	if (mysqli_query($conn, $sql2)) {
		$to = $email; // Add your email address inbetween the '' replacing yourname@yourdomain.com - This is where the form will send a message to.
		$email_subject = "Search Event Everywhere | Ticket Purchase Successful";
		$email_body = "Dear"." ". $full_name . ",". "\n\n" . "Thank you for purchasing the ticket.\n\n". "Name: ". $full_name . "\n\n". "Ticket Number: ".$random ."\n\n" . "Event Name: ". $event_name . "\n\n". "Total Purchased Ticket: ". $quantity . "\n\n" . "Purchased Date: ". $today_date . "\n\n". "Total Amount: Rs.". $total_cost  ;
		$headers = "From: info@ujwalparajuli.com.np\n"; // This is the email address the generated message will be from. We recommend using something like noreply@yourdomain.com.
		$headers .= "Reply-To: uzwalparajuli07@gmail.com";   
		mail($to,$email_subject,$email_body,$headers);
		echo "success";
	}

	else{
		echo "error2";
	}

}
else{
	echo "error";
}




?>