<?php
$servername = "localhost";
$username = "id9276460_yestinyung";
$password = "12345678";
$dbname = "id9276460_hostipal";


$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
if (isset($_POST['firstname']) && isset($_POST['lastname']) && isset($_POST['departmentID']) && isset($_POST['username']) && isset($_POST['password'])) {
    $firstname = $_POST['firstname'];
    $lastname = $_POST['lastname'];
    $username = $_POST['username'];
    $password = $_POST['password'];
    $departmentID = $_POST['departmentID'];
    $sql = "INSERT INTO doctordetail (firstname, lastname, username, password, departmentID) VALUES ('$firstname', '$lastname', '$username', '$password', '$departmentID');";
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