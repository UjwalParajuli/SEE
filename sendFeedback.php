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
$user_id = (int)$_POST['user_id'];
$rating_type = $_POST['rating_type'];
$message = $_POST['message'];
$d=strtotime("today");
$today_date = date('Y-m-d', $d);
$email_address = "uzwalparajuli07@gmail.com";

$sql = "insert into feedback (user_id, rating, message, given_date) values ($user_id, '$rating_type', '$message', '$today_date') ";

if (mysqli_query($conn, $sql)) {
	$to = $email_address; // Add your email address inbetween the '' replacing yourname@yourdomain.com - This is where the form will send a message to.
	$email_subject = "Search Event Everywhere | Feedback";
	$email_body = "You have got a feedback for your application."."\n\n"."Rating: ". $rating_type . "\n\n" . "Message: ". $message;
	$headers = "From: ".$email."\n"; // This is the email address the generated message will be from. We recommend using something like noreply@yourdomain.com.
	$headers .= "Reply-To: ".$email;   
	mail($to,$email_subject,$email_body,$headers);
	echo "success";
}
else {
	echo "error";
}




?>