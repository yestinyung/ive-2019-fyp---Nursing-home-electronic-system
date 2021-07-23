<?php
$servername = "localhost";
$username = "id9276460_yestinyung";
$password = "12345678";
$dbname = "id9276460_hostipal";

$array = array("bodytemperature", "heartbeat", "roomdetail", "urgentrecord");
$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
if (isset($_POST['id']) && isset($_POST['table'])) {
    $id = $_POST['id'];
    $table = $_POST['table'];
    $sql = "UPDATE $table SET isSave='Y' WHERE id='$id'";
    if (mysqli_query($conn, $sql)) {
        echo "Record updated successfully";
    } else {
        echo "Error updating record: " . mysqli_error($conn);
    }
} else {
    echo "Exception";
}
?>
