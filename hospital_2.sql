-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- 主機： 127.0.0.1
-- 產生時間： 2019 年 04 月 08 日 18:38
-- 伺服器版本： 10.1.38-MariaDB
-- PHP 版本： 7.3.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫： `hospital`
--

-- --------------------------------------------------------

--
-- 資料表結構 `admin`
--

CREATE TABLE `admin` (
  `id` int(11) NOT NULL,
  `username` varchar(16) NOT NULL,
  `password` varchar(16) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- 傾印資料表的資料 `admin`
--

INSERT INTO `admin` (`id`, `username`, `password`) VALUES
(1, 'admin', 'admin');

-- --------------------------------------------------------

--
-- 資料表結構 `bodytemperature`
--

CREATE TABLE `bodytemperature` (
  `id` int(11) NOT NULL,
  `bodyTemperature` varchar(255) NOT NULL,
  `patientID` int(11) NOT NULL,
  `isSave` char(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- 資料表結構 `department`
--

CREATE TABLE `department` (
  `id` int(11) NOT NULL,
  `departmentName` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- 傾印資料表的資料 `department`
--

INSERT INTO `department` (`id`, `departmentName`) VALUES
(1, 'General'),
(2, 'Pharmacy'),
(3, 'Accident and Emergency Department'),
(4, 'Mortuary'),
(5, 'Prosthetic and Orthotic Department'),
(6, 'Intensive Care Unit'),
(7, 'Death Documentation Office'),
(8, 'Physiotherapy Department'),
(9, 'Speech Therapy Department');

-- --------------------------------------------------------

--
-- 資料表結構 `doctordetail`
--

CREATE TABLE `doctordetail` (
  `id` int(11) NOT NULL,
  `firstname` varchar(255) NOT NULL,
  `lastname` varchar(255) NOT NULL,
  `username` varchar(16) NOT NULL,
  `password` varchar(16) NOT NULL,
  `departmentID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- 傾印資料表的資料 `doctordetail`
--

INSERT INTO `doctordetail` (`id`, `firstname`, `lastname`, `username`, `password`, `departmentID`) VALUES
(1, 'peter', 'chan', 'doctor', 'doctor', 2),
(2, 'peter', 'peter', 'peter', 'peter', 1);

-- --------------------------------------------------------

--
-- 資料表結構 `heartbeat`
--

CREATE TABLE `heartbeat` (
  `id` int(11) NOT NULL,
  `heartbeat` int(11) NOT NULL,
  `patientID` int(11) NOT NULL,
  `isSave` char(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- 資料表結構 `medicalrecord`
--

CREATE TABLE `medicalrecord` (
  `id` int(11) NOT NULL,
  `medicalRecord` text NOT NULL,
  `date` varchar(255) NOT NULL,
  `patientID` int(11) NOT NULL,
  `doctorID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- 傾印資料表的資料 `medicalrecord`
--

INSERT INTO `medicalrecord` (`id`, `medicalRecord`, `date`, `patientID`, `doctorID`) VALUES
(12, '123', '06:April:2019 07:26:29 AM', 0, 2),
(13, 'asdasdfasd', '06:April:2019 07:30:18 AM', 0, 2);

-- --------------------------------------------------------

--
-- 資料表結構 `patientdetail`
--

CREATE TABLE `patientdetail` (
  `id` int(11) NOT NULL,
  `firstname` varchar(250) NOT NULL,
  `lastname` varchar(250) NOT NULL,
  `username` varchar(16) NOT NULL,
  `password` varchar(16) NOT NULL,
  `address` varchar(250) NOT NULL,
  `tel` varchar(250) NOT NULL,
  `email` varchar(250) NOT NULL,
  `age` varchar(250) NOT NULL,
  `Precautions` text NOT NULL,
  `departmentID` int(11) NOT NULL,
  `roomnum` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- 傾印資料表的資料 `patientdetail`
--

INSERT INTO `patientdetail` (`id`, `firstname`, `lastname`, `username`, `password`, `address`, `tel`, `email`, `age`, `Precautions`, `departmentID`, `roomnum`) VALUES
(0, 'peter', 'chan', 'asdf', 'asdf', 'HongKong', '12345678', 'abcd@example.com', '18', '/*-+/*-+/*-+/*-+/*-+', 1, 101);

-- --------------------------------------------------------

--
-- 資料表結構 `roomdetail`
--

CREATE TABLE `roomdetail` (
  `id` int(11) NOT NULL,
  `cm1` int(11) NOT NULL,
  `cm2` int(11) NOT NULL,
  `humidity` int(11) NOT NULL,
  `temperature` int(11) NOT NULL,
  `isSave` char(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- 傾印資料表的資料 `roomdetail`
--

INSERT INTO `roomdetail` (`id`, `cm1`, `cm2`, `humidity`, `temperature`, `isSave`) VALUES
(1, 0, 0, 0, 0, ''),
(2, 0, 0, 0, 0, ''),
(3, 0, 0, 0, 0, ''),
(4, 0, 0, 0, 0, ''),
(5, 0, 0, 0, 0, ''),
(6, 1, 1, 1, 1, 'Y'),
(7, 1, 1, 1, 1, 'Y'),
(8, 1, 1, 1, 1, 'Y');

-- --------------------------------------------------------

--
-- 資料表結構 `roomnum`
--

CREATE TABLE `roomnum` (
  `roomdetailID` int(11) NOT NULL,
  `roomnum` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- 傾印資料表的資料 `roomnum`
--

INSERT INTO `roomnum` (`roomdetailID`, `roomnum`) VALUES
(1, 101),
(2, 102),
(3, 103),
(4, 104),
(5, 105);

-- --------------------------------------------------------

--
-- 資料表結構 `urgentrecord`
--

CREATE TABLE `urgentrecord` (
  `id` int(11) NOT NULL,
  `patientID` int(11) NOT NULL,
  `isSave` char(1) NOT NULL,
  `location` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- 已傾印資料表的索引
--

--
-- 資料表索引 `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `bodytemperature`
--
ALTER TABLE `bodytemperature`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_bodytemperaturePatient` (`patientID`);

--
-- 資料表索引 `department`
--
ALTER TABLE `department`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `doctordetail`
--
ALTER TABLE `doctordetail`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_DoctorDetailDepartment` (`departmentID`);

--
-- 資料表索引 `heartbeat`
--
ALTER TABLE `heartbeat`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_HearttBeatPatientdetail` (`patientID`);

--
-- 資料表索引 `medicalrecord`
--
ALTER TABLE `medicalrecord`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_MedicalRecordPatient` (`patientID`),
  ADD KEY `FK_MedicalRecordDoctor` (`doctorID`);

--
-- 資料表索引 `patientdetail`
--
ALTER TABLE `patientdetail`
  ADD PRIMARY KEY (`id`) USING BTREE,
  ADD KEY `FK_patientDetailDepartment` (`departmentID`),
  ADD KEY `roomnum` (`roomnum`),
  ADD KEY `roomnum_2` (`roomnum`);

--
-- 資料表索引 `roomdetail`
--
ALTER TABLE `roomdetail`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id` (`id`);

--
-- 資料表索引 `roomnum`
--
ALTER TABLE `roomnum`
  ADD PRIMARY KEY (`roomdetailID`,`roomnum`),
  ADD KEY `roomnum` (`roomnum`),
  ADD KEY `roomdetailID` (`roomdetailID`);

--
-- 資料表索引 `urgentrecord`
--
ALTER TABLE `urgentrecord`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_UrgentrecordPatient` (`patientID`);

--
-- 在傾印的資料表使用自動增長(AUTO_INCREMENT)
--

--
-- 使用資料表自動增長(AUTO_INCREMENT) `admin`
--
ALTER TABLE `admin`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- 使用資料表自動增長(AUTO_INCREMENT) `bodytemperature`
--
ALTER TABLE `bodytemperature`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表自動增長(AUTO_INCREMENT) `department`
--
ALTER TABLE `department`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- 使用資料表自動增長(AUTO_INCREMENT) `doctordetail`
--
ALTER TABLE `doctordetail`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- 使用資料表自動增長(AUTO_INCREMENT) `heartbeat`
--
ALTER TABLE `heartbeat`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表自動增長(AUTO_INCREMENT) `medicalrecord`
--
ALTER TABLE `medicalrecord`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- 使用資料表自動增長(AUTO_INCREMENT) `roomdetail`
--
ALTER TABLE `roomdetail`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- 使用資料表自動增長(AUTO_INCREMENT) `urgentrecord`
--
ALTER TABLE `urgentrecord`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 已傾印資料表的限制(constraint)
--

--
-- 資料表的限制(constraint) `bodytemperature`
--
ALTER TABLE `bodytemperature`
  ADD CONSTRAINT `FK_bodytemperaturePatient` FOREIGN KEY (`patientID`) REFERENCES `patientdetail` (`id`);

--
-- 資料表的限制(constraint) `doctordetail`
--
ALTER TABLE `doctordetail`
  ADD CONSTRAINT `FK_DoctorDetailDepartment` FOREIGN KEY (`departmentID`) REFERENCES `department` (`id`);

--
-- 資料表的限制(constraint) `heartbeat`
--
ALTER TABLE `heartbeat`
  ADD CONSTRAINT `FK_HearttBeatPatientdetail` FOREIGN KEY (`patientID`) REFERENCES `patientdetail` (`id`);

--
-- 資料表的限制(constraint) `medicalrecord`
--
ALTER TABLE `medicalrecord`
  ADD CONSTRAINT `FK_MedicalRecordDoctor` FOREIGN KEY (`doctorID`) REFERENCES `doctordetail` (`id`),
  ADD CONSTRAINT `FK_MedicalRecordPatient` FOREIGN KEY (`patientID`) REFERENCES `patientdetail` (`id`);

--
-- 資料表的限制(constraint) `patientdetail`
--
ALTER TABLE `patientdetail`
  ADD CONSTRAINT `FK_patientDetailDepartment` FOREIGN KEY (`departmentID`) REFERENCES `department` (`id`),
  ADD CONSTRAINT `FK_patientdetailroomnum` FOREIGN KEY (`roomnum`) REFERENCES `roomnum` (`roomnum`);

--
-- 資料表的限制(constraint) `urgentrecord`
--
ALTER TABLE `urgentrecord`
  ADD CONSTRAINT `FK_UrgentrecordPatient` FOREIGN KEY (`patientID`) REFERENCES `patientdetail` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
