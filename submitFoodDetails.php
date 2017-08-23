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
$mobile = $obj->guestMobile;
$amountOfFood = $obj->amountOfFood;
$typeOfFood = $obj->typeOfFood;
$locality = $obj->guestLocality;
$query = "insert into foodAvailableInformation (mobile,locality,amountOfFood,typeOfFood) values ('$mobile','$locality','$amountOfFood','$typeOfFood')";
if(mysqli_query($con,$query))
	echo ("Details submitted successfully");
else
	echo ("Failed in saving your data ,again try");
?>