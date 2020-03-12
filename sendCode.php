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
$random = (int)rand(10000,50000);

$sql = "select * from user where email = '$email'";

$result = mysqli_query($conn, $sql);
if (mysqli_num_rows($result) < 1) {
	echo "error";
}

else{
	$qry = "insert into change_password_verification (email, code) values ('$email' , $random)";
			
	$result = mysqli_query($conn, $qry);
	if(!$result){
		echo "dbError";
	}
	else{
		$to = $email; // Add your email address inbetween the '' replacing yourname@yourdomain.com - This is where the form will send a message to.
		$email_subject = "Password Change Verification Code";
		$email_body = "Please enter this code to verify your email\n\n".$random;
		$headers = "From: info@ujwalparajuli.com.np\n"; // This is the email address the generated message will be from. We recommend using something like noreply@yourdomain.com.
		$headers .= "Reply-To: uzwalparajuli07@gmail.com";   
		mail($to,$email_subject,$email_body,$headers);
		echo "success";
	}
}


?>