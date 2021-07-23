<?php
$servername = "localhost";
$username = "id9276460_yestinyung";
$password = "12345678";
$dbname = "id9276460_hostipal";


$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
if (isset($_POST['id']) && ($_POST['firstname']) && isset($_POST['lastname']) && isset($_POST['BedRoomNumber']) && isset($_POST['Address']) && isset($_POST['Tel']) && isset($_POST['Email']) && isset($_POST['Age']) && isset($_POST['departmentID']) && isset($_POST['precautions'])) {
    $id = $_POST['id'];
    $firstname = $_POST['firstname'];
    $lastname = $_POST['lastname'];
    $BedRoomNumber = $_POST['BedRoomNumber'];
    $Address = $_POST['Address'];
    $Tel = $_POST['Tel'];
    $Email = $_POST['Email'];
    $Age = $_POST['Age'];
    $departmentID = $_POST['departmentID'];
    $precautions = $_POST['precautions'];
    if (isset($_POST['username']) && isset($_POST['password'])) {
        $username1 = $_POST['username'];
        $password1 = $_POST['password'];
        $sql = "UPDATE patientdetail SET firstname='$firstname',lastName='$lastname',roomnum='$BedRoomNumber',address='$Address',tel='$Tel',email='$Email',age='$Age',departmentID='$departmentID',Precautions='$precautions',username='$username',password='$password' WHERE id='$id'";
    } else {
        $sql = "UPDATE patientdetail SET firstname='$firstname',lastName='$lastname',roomnum='$BedRoomNumber',address='$Address',tel='$Tel',email='$Email',age='$Age',departmentID='$departmentID',Precautions='$precautions' WHERE id='$id'";
    }
    if (mysqli_query($conn, $sql)) {
        echo "Record updated successfully";
    } else {
        echo "Error updating record: " . mysqli_error($conn);
    }
} else {
    echo "Exception";
}
?>