<?php

$servername = "localhost";
$username = "id9276460_yestinyung";
$password = "12345678";
$dbname = "id9276460_hostipal";

$conn = mysqli_connect($servername, $username, $password, $dbname)
or die(mysqli_connect_error());

$rs = '';
if (isset($_POST["roomnum"])) {
    $roomnum = $_POST["roomnum"];

    $sql = "SELECT id, humidity, temperature, isSave, roomnum from roomdetail r1 INNER JOIN roomnum r2 ON r1.id = r2.roomdetailID WHERE r2.roomnum = $roomnum ";

    $rs = mysqli_query($conn, $sql) or die(mysqli_error($conn));
    if (mysqli_num_rows($rs) > 0) {
        // output data of each row
        $output = array();
        $output += $rs->fetch_all(MYSQLI_ASSOC);
        echo json_encode($output);
    } else {
        echo "[false]";
    }
} else {
    echo "Exception";
}

mysqli_close($conn);

?>
