<?php

$servername = "localhost";
$username = "id9276460_yestinyung";
$password = "12345678";
$dbname = "id9276460_hostipal";

$conn = mysqli_connect($servername, $username, $password, $dbname)
or die(mysqli_connect_error());

$result = '';
if (isset($_POST['id'])) {
    $id = $_POST['id'];


    $sql = "SELECT * from medicalrecord WHERE patientID LIKE '$id' ";
    $rs = mysqli_query($conn, $sql) or die(mysqli_error($conn));


    if (mysqli_num_rows($rs) > 0) {
        // output data of each row
        $output = array();
        $output = $rs->fetch_all(MYSQLI_ASSOC);
        echo json_encode($output);
    } else {
        echo "false";
    }
} else {
    echo "Exception";
}

mysqli_free_result($rs);
mysqli_close($conn);

?>