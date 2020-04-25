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
$password = $_POST['password'];

$sql = "select * from user where email = '$email'";

$result = mysqli_query($conn, $sql);

$json_array = array();

if (mysqli_num_rows($result) < 1) {
	echo "error";
}

else{
	if ($row = mysqli_fetch_array($result)) {
		if ($email == $row["email"] && password_verify("$password", $row["password"])) {
			$json_array = $row;
			echo json_encode($json_array);	
		}

		elseif ($email != $row["email"] || password_verify("$password", $row["password"])) {

			echo "error";
		}

		elseif ($email == $row["email"] || (!password_verify("$password", $row["password"]))) {
			echo "error";
		}

		elseif ($email != $row["email"] || (!password_verify("$password", $row["password"]))) {
			echo "error";
		}

					
	}
	else{
		echo "error";
	}

}







	

