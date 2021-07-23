<?php
$servername = "localhost";
$username = "id9276460_yestinyung";
$password = "12345678";
$dbname = "id9276460_hostipal";


$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
if (isset($_GET['id']) && isset($_GET['Temp'])) {
    $id = $_GET['id'];
    $Temp = $_GET['Temp'];
    $isSave2 = "Y";
    if ((int)$Temp > 38) {
        $isSave2 = "N";
    }
    $sql = "INSERT INTO bodytemperature (bodytemperature, patientID, isSave) VALUES ('$Temp', '$id', '$isSave2');";
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
