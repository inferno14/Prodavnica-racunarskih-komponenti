-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 30, 2019 at 06:15 AM
-- Server version: 10.4.6-MariaDB
-- PHP Version: 7.3.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `isprodavnice_finall`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `id` int(8) NOT NULL,
  `username` varchar(32) NOT NULL,
  `password` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id`, `username`, `password`) VALUES
(3, 'pera', 'pera123'),
(4, 'laki', '4321laki');

-- --------------------------------------------------------

--
-- Table structure for table `prodaja`
--

CREATE TABLE `prodaja` (
  `id` int(10) NOT NULL,
  `model` varchar(30) NOT NULL,
  `cena` float NOT NULL,
  `datum_prodaje` date NOT NULL,
  `kolicina` int(15) NOT NULL,
  `tip` varchar(30) NOT NULL,
  `proizvodjac` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `prodaja`
--

INSERT INTO `prodaja` (`id`, `model`, `cena`, `datum_prodaje`, `kolicina`, `tip`, `proizvodjac`) VALUES
(6, 'MZ-76E250B/EU', 116770, '2019-08-30', 2, 'SSD', 'SSD'),
(7, 'i3', 24000, '2019-08-08', 5, 'Procesor', 'Intel'),
(8, 'gtx 1060', 55555, '2019-08-08', 2, 'Graficka kartica', 'Asus'),
(9, 'i3', 24000, '2019-08-08', 5, 'Procesor', 'Intel'),
(10, 'gtx 1060', 55555, '2019-08-08', 2, 'Graficka kartica', 'Asus'),
(11, 'i3', 24990, '2019-04-16', 8, 'Procesor', 'Intel'),
(12, 'i3', 24990, '2019-04-16', 8, 'Procesor', 'Intel'),
(13, 'MZ-V7P512BW', 18990, '2019-08-30', 1, 'SSD', 'Samsung');

-- --------------------------------------------------------

--
-- Table structure for table `roba`
--

CREATE TABLE `roba` (
  `fotografija` varchar(40) NOT NULL,
  `tip` varchar(64) NOT NULL,
  `proizvodjac` varchar(64) NOT NULL,
  `model` varchar(64) NOT NULL,
  `cena` float NOT NULL,
  `kolicina` int(8) NOT NULL,
  `Dostupnost` varchar(8) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `roba`
--

INSERT INTO `roba` (`fotografija`, `tip`, `proizvodjac`, `model`, `cena`, `kolicina`, `Dostupnost`) VALUES
('/aplikacija/DellMon1.jpg', 'Monitor', 'Dell', '21.5\" P2217H IPS', 15990, 10, 'active'),
('/aplikacija/Intel_Core7.jpg', 'Procesor', 'Intel', 'CORE I7 Series', 44990, 10, 'active'),
('/aplikacija/Intel_CoreI9.jpg', 'Procesor', 'Intel', 'CORE I9 Series', 119990, 10, 'active'),
('/aplikacija/CrucialRam1.jpg', 'RAM Memorija', 'Crucial', 'CT16G4DFD824A', 8990, 10, 'deleted'),
('/aplikacija/CrucialRam2.jpg', 'RAM Memorija', 'Crucial', 'CT32G4DFX824A', 21990, 21, 'active'),
('/aplikacija/CrucialRam2.jpg', 'RAM Memorija', 'Crucial', 'CT8G4DFS824A', 4990, 10, 'active'),
('/aplikacija/Intel_Cyclone.jpg', 'Procesor', 'Intel', 'Cyclone 10', 38990, 10, 'active'),
('/aplikacija/AMD_FX6300.jpg', 'Procesor', 'AMD', 'FX6300', 9490, 10, 'active'),
('/aplikacija/GeilRam3.jpg', 'RAM Memorija', 'GEIL', 'GAEXY48GB3000C16ASC', 6190, 10, 'deleted'),
('/aplikacija/Ram1.jpg', 'RAM Memorija', 'GEIL', 'GAFR416GB3200C16ADC', 12490, 10, 'active'),
('/aplikacija/GeilRam2.jpg', 'RAM Memorija', 'GEIL', 'GN34GB1600C11S', 2390, 10, 'active'),
('/aplikacija/MSIGraf3.jpg', 'Graficka kartica', 'MSI', 'GTX 1060', 29890, 10, 'active'),
('/aplikacija/GygabyteGraf3.jpg', 'Graficka kartica', 'Gygabyte', 'GV-N1030D5-2GL', 9990, 10, 'active'),
('/aplikacija/GygabyteGraf2.jpg', 'Graficka kartica', 'Gygabyte', 'GV-N1660GAMING', 32500, 8, 'active'),
('/aplikacija/GygabyteGraf1.jpg', 'Graficka kartica', 'Gygabyte', 'GV-N166TOC-6', 37990, 7, 'active'),
('/aplikacija/KinstonRam3.jpg', 'RAM Memorija', 'Kingston', 'HX432C18FB/16', 10990, 10, 'active'),
('/aplikacija/KinstonRam2.jpg', 'RAM Memorija', 'Kingston', 'HX432C18FB/32', 21990, 10, 'active'),
('/aplikacija/KinstonRam1.jpg', 'RAM Memorija', 'Kingston', 'KVR24N17D8/16', 8590, 10, 'active'),
('/aplikacija/SamsMonit1.jpg', 'Monitor', 'Samsung', 'LU28H750UQUXEN', 54990, 10, 'active'),
('/aplikacija/SamsSSd.jpg', 'SSD', 'Samsung', 'MZ-76E250B/EU', 6890, 8, 'active'),
('/aplikacija/SamsSSD2.jpg', 'SSD', 'Samsung', 'MZ-V7P512BW', 18990, 6, 'active'),
('/aplikacija/SamsSSd.jpg', 'SSD', 'Samsung', 'MZ-V7S1T0BW', 27990, 10, 'deleted'),
('/aplikacija/Assusgraf3.jpg', 'Graficka kartica', 'Asus', 'nVidia GeForce GTX 1050 2GB', 16990, 10, 'active'),
('/aplikacija/AsusGraf2.jpg', 'Graficka kartica', 'Asus', 'nVidia GeForce GTX 1050 Ti 4GB', 23990, 10, 'active'),
('/aplikacija/AsusGraf1.jpg', 'Graficka kartica', 'Asus', 'R5230-SL-1GD3-L', 4790, 9, 'active'),
('/aplikacija/MSIGraf1.jpg', 'Graficka kartica', 'MSI', 'RTX 2060 SUPER ', 62490, 10, 'active'),
('/aplikacija/MSIGraf2.jpg', 'Graficka kartica', 'MSI', 'RTX 2060 VENTUS 6G OC', 49990, 10, 'active'),
('/aplikacija/AMD_Ryzen5.jpg', 'Procesor', 'AMD', 'Ryzen 5', 13990, 10, 'active'),
('/aplikacija/AMD_Ryzen.jpg', 'Procesor', 'AMD', 'Ryzen 7', 25990, 10, 'active'),
('/aplikacija/BiostarSSd3.jpg', 'SSD', 'Biostar', 'S100-120GB', 2990, 12, 'active'),
('/aplikacija/BiostarSSd2.jpg', 'SSD', 'Biostar', 'S100-240GB', 3690, 10, 'active'),
('/aplikacija/BiostarSSd1.jpg', 'SSD', 'Biostar', 'S100-480GB', 6190, 10, 'active'),
('/aplikacija/DellMon3.jpg', 'Monitor', 'Dell', 'S2419HGF LED', 24990, 10, 'active'),
('/aplikacija/KingstonSSd3.jpg', 'SSD', 'Kingston', 'SA400S37/480G', 6590, 10, 'active'),
('/aplikacija/KinstonSSd2.jpg', 'SSD', 'Kingston', 'SUV500/240G', 4890, 10, 'active'),
('/aplikacija/KingstonSSD.jpg', 'SSD', 'Kingston', 'SUV500/960G', 16590, 10, 'active'),
('', 'f', 'g', 't', 55, 55, 'deleted'),
('/aplikacija/DellMon2.jpg', 'Monitor', 'Dell', 'U2412M LED', 24990, 10, 'active'),
('/aplikacija/AsusMon1.jpg', 'Monitor', 'Asus', 'VA326H FHD', 33390, 10, 'active');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `prodaja`
--
ALTER TABLE `prodaja`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `roba`
--
ALTER TABLE `roba`
  ADD PRIMARY KEY (`model`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `id` int(8) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `prodaja`
--
ALTER TABLE `prodaja`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
