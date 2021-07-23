<?php


$conn = new mysqli("localhost", "id9276460_yestinyung", "12345678", "id9276460_hostipal");
$result = $conn->query("SELECT * FROM doctordetail");
$output = array();
$output = $result->fetch_all(MYSQLI_ASSOC);
echo json_encode($output);
?>