<?php
$servername = "localhost";
$username = "id9276460_yestinyung";
$password = "A616296";
$dbname = "id9276460_hostipal";


$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
if (isset($_POST['id']) && isset($_POST['$location']) && isset($_POST['isSave'])) {
    $id = $_POST['id'];
    $isSave = $_POST['isSave'];
    $location = $_POST['$location'];
    $sql = "INSERT INTO urgentrecord (patientID, isSave, location) VALUES ('$id', '$isSave', '$location');";
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

