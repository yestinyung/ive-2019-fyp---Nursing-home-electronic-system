<?php

$servername = "localhost";
$username = "id9276460_yestinyung";
$password = "12345678";
$dbname = "id9276460_hostipal";

$conn = mysqli_connect($servername, $username, $password, $dbname)
or die(mysqli_connect_error());

$rs ='';
for($i = 0; $i < 4; $i++) {
    if($i == 3){
        $sql = "SELECT id, patientID, isSave FROM urgentrecord WHERE isSave = 'N' ";
    } elseif ($i == 2){
        $sql = "SELECT id, cm1, cm2, humidity, temperature, isSave, roomnum from roomdetail r1 INNER JOIN roomnum r2 ON r1.id = r2.roomdetailID WHERE isSave = 'N' ";
    } elseif ($i == 1){
        $sql = "SELECT h1.id, heartbeat, patientID, isSave, firstname, lastname from heartbeat h1 INNER JOIN patientdetail p1 on h1.patientID = p1.id WHERE isSave = 'N' ";
    } else {
        $sql = "SELECT b1.id, bodyTemperature, patientID, isSave, firstname, lastname from bodytemperature b1 INNER JOIN patientdetail p1 on b1.patientID = p1.id WHERE isSave = 'N' ";
    }
    $rs = mysqli_query($conn, $sql) or die(mysqli_error($conn));
    if (mysqli_num_rows($rs) > 0) {
        // output data of each row
        $output = array();
        $output += $rs->fetch_all(MYSQLI_ASSOC);
        echo json_encode($output);
    } else {
        echo "[false]";
    }
}
    mysqli_close($conn);

?>