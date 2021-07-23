<?php

$conn = new mysqli("localhost", "id9276460_yestinyung", "12345678", "id9276460_hostipal");
$sql = "SELECT * FROM patientdetail";
$rs = mysqli_query($conn, $sql) or die(mysqli_error($conn));
$output = array();
$output = $rs->fetch_all(MYSQLI_ASSOC);
echo json_encode($output);
?>