<?php
$servername = "localhost";
$username = "id9276460_yestinyung";
$password = "12345678";
$dbname = "id9276460_hostipal";


$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
if (isset($_POST['firstname']) && isset($_POST['lastname']) && isset($_POST['BedRoomNumber']) && isset($_POST['Address']) && isset($_POST['Tel']) && isset($_POST['Email']) && isset($_POST['Age']) && isset($_POST['departmentID']) && isset($_POST['precautions']) && isset($_POST['username']) && isset($_POST['password'])) {
    $firstname = $_POST['firstname'];
    $lastname = $_POST['lastname'];
    $username = $_POST['username'];
    $password = $_POST['password'];
    $BedRoomNumber = $_POST['BedRoomNumber'];
    $Address = $_POST['Address'];
    $Tel = $_POST['Tel'];
    $Email = $_POST['Email'];
    $Age = $_POST['Age'];
    $departmentID = $_POST['departmentID'];
    $precautions = $_POST['precautions'];
    $sql = "INSERT INTO patientdetail (firstname, lastName, username, password, roomnum, address, tel, email, age, departmentID, Precautions)
VALUES ('$firstname', '$lastname', '$username', '$password', '$BedRoomNumber', '$Address', '$Tel', '$Email', '$Age', '$departmentID', '$precautions');";
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