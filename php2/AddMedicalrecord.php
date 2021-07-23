<?php
$servername = "localhost";
$username = "id9276460_yestinyung";
$password = "12345678";
$dbname = "id9276460_hostipal";

$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
if (isset($_POST['id']) && isset($_POST['medicalrecord']) && isset($_POST['$recordDate']) && isset($_POST['doctorID'])) {
    $id = $_POST['id'];
    $medicalrecord = $_POST['medicalrecord'];
    $recordDate = $_POST['$recordDate'];
    $doctorID = $_POST['doctorID'];
    $sql = "INSERT INTO medicalrecord (medicalRecord, date, patientID, doctorID) VALUES ('$medicalrecord', '$recordDate', '$id', '$doctorID');";
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