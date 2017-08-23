<?php
$servername='localhost';
$username='id1124944_anoop';
$db='id1124944_pic_management';
$password="00000000";
$con=mysqli_connect($servername,$username,$password,$db);
if(!$con){
	die("Connection Error ".mysqli_connect_errno);
}
$json = file_get_contents('php://input');
$obj = json_decode($json);
$name = $obj->guestName;
$phone = $obj->guestPhone;
$pass = $obj->guestPassword;
$query = "insert into guest (name,mobile,pass) values ('$name','$phone','$pass')";
if(mysqli_query($con,$query))
	echo ("Successfully Resistered");
else
	echo ("Failed in saving your data ,may be due to duplicate mobile number");
?>