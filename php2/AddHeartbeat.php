<?php
$servername = "localhost";
$username = "id9276460_yestinyung";
$password = "12345678";
$dbname = "id9276460_hostipal";


$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
if (isset($_GET['id']) && isset($_GET['HB'])) {
    $id = $_GET['id'];
    $HB = $_GET['HB'];
    $isSave1 = "Y";
    if ((int)$HB > 120) {
        $isSave1 = "N";
    }
    $sql = "INSERT INTO heartbeat (heartbeat, patientID, isSave) VALUES ('$HB', '$id', '$isSave1');";
    if ($conn->multi_query($sql) === TRUE) {
        echo "New records created successfully";
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
    }
    $conn->close();
} else {
    echo "Exception";
}
?>
