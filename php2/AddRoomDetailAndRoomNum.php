<?php
$servername = "localhost";
$username = "id9276460_yestinyung";
$password = "12345678";
$dbname = "id9276460_hostipal";


$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
if (isset($_POST['cm1']) && isset($_POST['cm2']) && isset($_POST['humidity']) && isset($_POST['temperature']) && isset($_POST['roomnum']) && isset($_POST['isSave'])) {
    $cm1 = $_POST['cm1'];
    $cm2 = $_POST['cm2'];
    $humidity = $_POST['humidity'];
    $temperature = $_POST['temperature'];
    $roomnum = $_POST['roomnum'];
    $isSave = $_POST['isSave'];

    if ((int)$temperature > 50) {
        $isSave = "N";
    }

    $sql = "INSERT INTO roomdetail (cm1, cm2, humidity, temperature, isSave) VALUES ('$cm1', '$cm2', '$humidity', '$temperature','$isSave');";
    if (mysqli_query($conn, $sql) === TRUE) {
        $id = mysqli_insert_id($conn);
        $sql = "INSERT INTO roomnum (roomdetailID, roomnum) VALUES ('$id', '$roomnum');";
        if ($conn->multi_query($sql) === TRUE) {
            echo "New records created successfully";
        } else {
            echo "Error: " . $sql . "<br>" . $conn->error;
        }
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
    }
    $conn->close();
} else {
    echo "Exception";
}
?>