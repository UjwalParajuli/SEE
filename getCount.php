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

$sql = "select * from interested where user_id = $user_id ";
$sql2 = "select * from purchase where user_id = $user_id ";
$sql3 = "select * from event_details where organizer_id = $user_id ";

if(mysqli_query($conn, $sql)){
	$result = mysqli_query($conn, $sql);
	$total = mysqli_num_rows($result);
	if(mysqli_query($conn, $sql2)){
		$result2 = mysqli_query($conn, $sql2);
		$total2 = mysqli_num_rows($result2);
		if(mysqli_query($conn, $sql2)){
			$result3 = mysqli_query($conn, $sql3);
			$total3 = mysqli_num_rows($result3);
			$data['total_interested'] = $total;
			$data['total_purchased'] = $total2;
			$data['total_organized'] = $total3;
			echo json_encode($data);
		}
	}
}
else{
	echo "error";
}


?>