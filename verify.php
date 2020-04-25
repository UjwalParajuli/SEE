<?php
$servername = "localhost";
$dbname = "id5461621_see";
$uname = "id5461621_event";
$pass = "12345";
$conn = mysqli_connect($servername, $uname, $pass, $dbname);
if (!$conn) {
	die("Connection failed!");
}

$email_address = strip_tags(htmlspecialchars($_POST['email']));
$random = (int)rand(1000,50000);

$sql = "select * from user where (email = '$email_address');";
$result = mysqli_query($conn, $sql);
if (mysqli_num_rows($result) > 0) {
	$row = mysqli_fetch_assoc($result);

	if ($email_address == $row['email']) {
		echo "error";
	}
		# code...
}
else {
	$qry = "insert into verification (email, code) values ('$email_address' , $random)";
			
			$result = mysqli_query($conn, $qry);
			if(!$result){
			echo "dbError";
			}
			else{
				$to = $email_address; // Add your email address inbetween the '' replacing yourname@yourdomain.com - This is where the form will send a message to.
				$email_subject = "Email Verification Code";
				$email_body = $random;
				$headers = "From: info@ujwalparajuli.com.np\n"; // This is the email address the generated message will be from. We recommend using something like noreply@yourdomain.com.
				$headers .= "Reply-To: $email_address";   
				mail($to,$email_subject,$email_body,$headers);
				echo "success";
			}
}
?>