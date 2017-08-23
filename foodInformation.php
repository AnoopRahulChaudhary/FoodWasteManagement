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
$locality = $obj->foodLocality;
$query = "select mobile from foodAvailableInformation where locality='$locality'";
$result = mysqli_query($con,$query);
if($result){
        $out = array(); 
        $out=$result->fetch_all(MYSQLI_ASSOC);
        $jsonArray=json_encode($out);
	if(mysqli_num_rows($result)>0){
	 $row=mysqli_fetch_array($result);
		echo("0"."@"."{'status':0,'mobile':$jsonArray}");
	}else{
		echo("1"."@"."No food available");
	}
}else{
	echo("2"."@"."Database connection error");
}
?>