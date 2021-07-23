<?php

$servername = "localhost";
$username = "id9276460_yestinyung";
$password = "12345678";
$dbname = "id9276460_hostipal";
try {
    $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    die("Cannot connect to db");
}
$result = '';
if (isset($_POST['username']) && isset($_POST['password'])) {
    $username = $_POST['username'];
    $password = $_POST['password'];
    $stmt = $conn->prepare('SELECT * FROM patientdetail WHERE username=:username AND password=:password');
    $stmt->bindParam(':username', $username, PDO::PARAM_STR);
    $stmt->bindParam(':password', $password, PDO::PARAM_STR);
    $stmt->execute();
    if ($stmt->rowCount()) {
        // output data of each row
        echo "true,";
        $row = $stmt->fetch();
        echo $row["id"] . "," . $row["firstname"] . "," . $row["lastname"] . "," . $row["username"] . "," . $row["password"] . "," . $row["address"] . "," . $row["tel"] .
            "," . $row["email"] . "," . $row["age"] . "," . $row["precautions"] . "," . $row["departmentID"] . "," . $row["roomnum"];
    } else {
        $stmt = $conn->prepare('SELECT * FROM doctordetail WHERE username=:username AND password=:password');
        $stmt->bindParam(':username', $username, PDO::PARAM_STR);
        $stmt->bindParam(':password', $password, PDO::PARAM_STR);
        $stmt->execute();
        if ($stmt->rowCount()) {
            // output data of each row
            echo "true,doctor,";
            $row = $stmt->fetch();
            echo $row["id"] . "," . $row["firstname"] . "," . $row["lastname"] . "," . $row["username"] . ","
                . $row["password"] . "," . $row["departmentID"];
        } else {
            $stmt = $conn->prepare('SELECT * FROM admin WHERE username=:username AND password=:password');
            $stmt->bindParam(':username', $username, PDO::PARAM_STR);
            $stmt->bindParam(':password', $password, PDO::PARAM_STR);
            $stmt->execute();
            if ($stmt->rowCount()) {
                // output data of each row
                echo "true,";
                $row = $stmt->fetch();
                echo $row["id"] . "," . $row["username"] . "," . $row["password"];
            } else {
                echo "false";
            }
        }
    }
} else {
    echo "Exception";
}

?>