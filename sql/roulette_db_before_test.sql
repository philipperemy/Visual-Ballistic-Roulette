-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Oct 19, 2015 at 02:57 PM
-- Server version: 5.6.17
-- PHP Version: 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `roulette_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `ball_lap_times`
--

CREATE TABLE IF NOT EXISTS `ball_lap_times` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `SESSION_ID` int(10) NOT NULL,
  `TIME` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=14 ;

--
-- Dumping data for table `ball_lap_times`
--

INSERT INTO `ball_lap_times` (`ID`, `SESSION_ID`, `TIME`) VALUES
(1, 1, '2858'),
(2, 1, '3591'),
(3, 1, '4421'),
(4, 1, '5456'),
(5, 1, '6625'),
(6, 1, '7950'),
(7, 1, '9355'),
(8, 1, '10887'),
(9, 1, '12539'),
(10, 1, '14336'),
(11, 1, '16302'),
(12, 1, '18387'),
(13, 2, '99');

-- --------------------------------------------------------

--
-- Table structure for table `clockwise`
--

CREATE TABLE IF NOT EXISTS `clockwise` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `CLOCKWISE` int(11) NOT NULL,
  `SESSION_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `clockwise`
--

INSERT INTO `clockwise` (`ID`, `CLOCKWISE`, `SESSION_ID`) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 0, 11);

-- --------------------------------------------------------

--
-- Table structure for table `outcomes`
--

CREATE TABLE IF NOT EXISTS `outcomes` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `SESSION_ID` int(10) NOT NULL,
  `NUMBER` varchar(255) NOT NULL,
  `OBSTACLES` int(10) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `outcomes`
--

INSERT INTO `outcomes` (`ID`, `SESSION_ID`, `NUMBER`, `OBSTACLES`) VALUES
(1, 1, '15', 0);

-- --------------------------------------------------------

--
-- Table structure for table `session`
--

CREATE TABLE IF NOT EXISTS `session` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=12 ;

--
-- Dumping data for table `session`
--

INSERT INTO `session` (`ID`) VALUES
(1),
(2),
(3),
(4),
(5),
(6),
(7),
(8),
(9),
(10),
(11);

-- --------------------------------------------------------

--
-- Table structure for table `wheel_lap_times`
--

CREATE TABLE IF NOT EXISTS `wheel_lap_times` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `SESSION_ID` int(10) unsigned NOT NULL,
  `TIME` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=12 ;

--
-- Dumping data for table `wheel_lap_times`
--

INSERT INTO `wheel_lap_times` (`ID`, `SESSION_ID`, `TIME`) VALUES
(1, 1, '2600'),
(2, 1, '6168'),
(3, 1, '9810'),
(4, 1, '13427'),
(5, 1, '17154'),
(6, 1, '20810'),
(7, 2, '99'),
(8, 2, '100'),
(11, 2, '101');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
