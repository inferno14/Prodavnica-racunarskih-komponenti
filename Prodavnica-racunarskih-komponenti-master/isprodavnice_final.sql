-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: 22. Avg 2019. u 20:28
-- Verzija servera: 10.1.40-MariaDB
-- PHP Version: 7.1.29

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `isprodavnice`
--

-- --------------------------------------------------------

--
-- Struktura tabele `admin`
--

CREATE TABLE `admin` (
  `id` int(8) NOT NULL,
  `username` varchar(32) NOT NULL,
  `password` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struktura tabele `prijem`
--

CREATE TABLE `prijem` (
  `tip` varchar(32) NOT NULL,
  `proizvodjac` varchar(32) NOT NULL,
  `model` varchar(64) NOT NULL,
  `kolicina` int(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struktura tabele `prodaja`
--

CREATE TABLE `prodaja` (
  `model` varchar(64) NOT NULL,
  `cena` float NOT NULL,
  `datum_prodaje` date NOT NULL,
  `kolicina` int(12) NOT NULL,
  `tip` varchar(32) NOT NULL,
  `proizvodjac` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struktura tabele `roba`
--

CREATE TABLE `roba` (
  `tip` varchar(64) NOT NULL,
  `proizvodjac` varchar(64) NOT NULL,
  `model` varchar(64) NOT NULL,
  `cena` float NOT NULL,
  `kolicina` int(8) NOT NULL,
  `Dostupnost` varchar(8) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Prikaz podataka tabele `roba`
--

INSERT INTO `roba` (`tip`, `proizvodjac`, `model`, `cena`, `kolicina`, `Dostupnost`) VALUES
('Monitor', 'Dell', '21.5\" P2217H IPS', 15990, 10, 'active'),
('Procesor', 'Intel', 'CORE I7 Series', 44990, 10, 'active'),
('Procesor', 'Intel', 'CORE I9 Series', 119990, 10, 'active'),
('RAM Memorija', 'Crucial', 'CT16G4DFD824A', 8990, 10, 'active'),
('RAM Memorija', 'Crucial', 'CT32G4DFX824A', 21990, 10, 'active'),
('RAM Memorija', 'Crucial', 'CT8G4DFS824A', 4990, 10, 'active'),
('Procesor', 'Intel', 'Cyclone 10', 38990, 10, 'active'),
('Procesor', 'AMD', 'FX6300', 9490, 10, 'active'),
('RAM Memorija', 'GEIL', 'GAEXY48GB3000C16ASC', 6190, 10, 'active'),
('RAM Memorija', 'GEIL', 'GAFR416GB3200C16ADC', 12490, 10, 'active'),
('RAM Memorija', 'GEIL', 'GN34GB1600C11S', 2390, 10, 'active'),
('Graficka kartica', 'MSI', 'GTX 1060', 29890, 10, 'active'),
('Graficka kartica', 'Gygabyte', 'GV-N1030D5-2GL', 9990, 10, 'active'),
('Graficka kartica', 'Gygabyte', 'GV-N1660GAMING', 32500, 10, 'active'),
('Graficka kartica', 'Gygabyte', 'GV-N166TOC-6', 37990, 10, 'active'),
('RAM Memorija', 'Kingston', 'HX432C18FB/16', 10990, 10, 'active'),
('RAM Memorija', 'Kingston', 'HX432C18FB/32', 21990, 10, 'active'),
('RAM Memorija', 'Kingston', 'KVR24N17D8/16', 8590, 10, 'active'),
('Monitor', 'Samsung', 'LU28H750UQUXEN', 54990, 10, 'active'),
('SSD', 'Samsung', 'MZ-76E250B/EU', 6890, 10, 'active'),
('SSD', 'Samsung', 'MZ-V7P512BW', 18990, 10, 'active'),
('SSD', 'Samsung', 'MZ-V7S1T0BW', 27990, 10, 'active'),
('Graficka kartica', 'Asus', 'nVidia GeForce GTX 1050 2GB', 16990, 10, 'active'),
('Graficka kartica', 'Asus', 'nVidia GeForce GTX 1050 Ti 4GB', 23990, 10, 'active'),
('Graficka kartica', 'Asus', 'R5230-SL-1GD3-L', 4790, 10, 'active'),
('Graficka kartica', 'MSI', 'RTX 2060 SUPER ', 62490, 10, 'active'),
('Graficka kartica', 'MSI', 'RTX 2060 VENTUS 6G OC', 49990, 10, 'active'),
('Procesor', 'AMD', 'Ryzen 5', 13990, 10, 'active'),
('Procesor', 'AMD', 'Ryzen 7', 25990, 10, 'active'),
('SSD', 'Biostar', 'S100-120GB', 2990, 10, 'active'),
('SSD', 'Biostar', 'S100-240GB', 3690, 10, 'active'),
('SSD', 'Biostar', 'S100-480GB', 6190, 10, 'active'),
('Monitor', 'Dell', 'S2419HGF LED', 24990, 10, 'active'),
('SSD', 'Kingston', 'SA400S37/480G', 6590, 10, 'active'),
('SSD', 'Kingston', 'SUV500/240G', 4890, 10, 'active'),
('SSD', 'Kingston', 'SUV500/960G', 16590, 10, 'active'),
('Monitor', 'Dell', 'U2412M LED', 24990, 10, 'active'),
('Monitor', 'Asus', 'VA326H FHD', 33390, 10, 'active');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `prijem`
--
ALTER TABLE `prijem`
  ADD KEY `model` (`model`);

--
-- Indexes for table `prodaja`
--
ALTER TABLE `prodaja`
  ADD KEY `model` (`model`);

--
-- Indexes for table `roba`
--
ALTER TABLE `roba`
  ADD PRIMARY KEY (`model`);

--
-- Ograničenja za izvezene tabele
--

--
-- Ograničenja za tabele `prijem`
--
ALTER TABLE `prijem`
  ADD CONSTRAINT `prijem_ibfk_1` FOREIGN KEY (`model`) REFERENCES `roba` (`model`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ograničenja za tabele `prodaja`
--
ALTER TABLE `prodaja`
  ADD CONSTRAINT `prodaja_ibfk_1` FOREIGN KEY (`model`) REFERENCES `roba` (`model`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
