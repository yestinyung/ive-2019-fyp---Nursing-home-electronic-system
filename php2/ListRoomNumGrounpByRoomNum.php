<?php

$servername = "localhost";
$username = "id9276460_yestinyung";
$password = "12345678";
$dbname = "id9276460_hostipal";

$conn = mysqli_connect($servername, $username, $password, $dbname)
or die(mysqli_connect_error());

$rs = '';
$sql = "SELECT roomnum FROM roomnum GROUP BY roomnum";
$rs = mysqli_query($conn, $sql) or die(mysqli_error($conn));
if (mysqli_num_rows($rs) > 0) {
    // output data of each row
    $output = array();
    $output += $rs->fetch_all(MYSQLI_ASSOC);
    echo json_encode($output);
} else {
    echo "[false]";
}

mysqli_close($conn);

?>