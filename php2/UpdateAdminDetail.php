<?php
$servername = "localhost";
$username = "id9276460_yestinyung";
$password = "12345678";
$dbname = "id9276460_hostipal";

$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
if (isset($_POST['id']) && isset($_POST['username']) && isset($_POST['password'])) {
    $id = $_POST['id'];
    $username = $_POST['username'];
    $password = $_POST['password'];
    $sql = "UPDATE admin SET username='$username',password='$password' WHERE id='$id'";
    if (mysqli_query($conn, $sql)) {
        echo "Record updated successfully";
    } else {
        echo "Error updating record: " . mysqli_error($conn);
    }
} else {
    echo "Exception";
}
?>
