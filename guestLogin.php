<?php
$servername='localhost';
$username='id1124944_anoop';
$db='id1124944_pic_management';
$password="00000000";
$con = mysqli_connect($servername,$username,$password,$db);
if(!$con)
	die("Connection error".mysqli_connect_error);
$json = file_get_contents("php://input");
$obj = json_decode($json);
$phone = $obj->guestPhone;
$password = $obj->guestPassword;
$query = "select name from guest where mobile='$phone' and pass='$password'";
$result = mysqli_query($con,$query);
if($result){
	if(mysqli_num_rows($result)>0){
	 $row=mysqli_fetch_array($result);
		echo("@".$row['name']);
	}else{
		echo("invalid details".$phone.",".$password);
	}
}else{
	echo("Database connection error");
}
?>