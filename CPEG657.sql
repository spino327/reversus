-- phpMyAdmin SQL Dump
-- version 3.3.9.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Apr 24, 2012 at 11:53 PM
-- Server version: 5.5.9
-- PHP Version: 5.3.5

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `CPEG657`
--

-- --------------------------------------------------------

--
-- Table structure for table `CATEGORY`
--

CREATE TABLE `CATEGORY` (
  `ID` int(10) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `CATEGORY`
--


-- --------------------------------------------------------

--
-- Table structure for table `LOCATIONDATA`
--

CREATE TABLE `LOCATIONDATA` (
  `ID` int(10) NOT NULL,
  `USERID` int(10) NOT NULL,
  `PLACEID` int(10) NOT NULL,
  `LONGITUDE` float NOT NULL,
  `LATITUDE` float NOT NULL,
  `TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  KEY `USERID` (`USERID`,`PLACEID`),
  KEY `PLACEID` (`PLACEID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `LOCATIONDATA`
--


-- --------------------------------------------------------

--
-- Table structure for table `PLACE`
--

CREATE TABLE `PLACE` (
  `ID` int(10) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `LONGITUDE` float NOT NULL,
  `LATITUDE` float NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `NAME` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `PLACE`
--


-- --------------------------------------------------------

--
-- Table structure for table `PLACECATEGORY`
--

CREATE TABLE `PLACECATEGORY` (
  `PLACEID` int(10) NOT NULL,
  `CATEGORYID` int(10) NOT NULL,
  KEY `PLACEID` (`PLACEID`,`CATEGORYID`),
  KEY `CATEGORYID` (`CATEGORYID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `PLACECATEGORY`
--


-- --------------------------------------------------------

--
-- Table structure for table `USER`
--

CREATE TABLE `USER` (
  `ID` int(10) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `USER`
--


-- --------------------------------------------------------

--
-- Table structure for table `USERNEED`
--

CREATE TABLE `USERNEED` (
  `USERID` int(10) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  KEY `USERID` (`USERID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `USERNEED`
--


--
-- Constraints for dumped tables
--

--
-- Constraints for table `LOCATIONDATA`
--
ALTER TABLE `LOCATIONDATA`
  ADD CONSTRAINT `LOCATIONDATA_ibfk_2` FOREIGN KEY (`PLACEID`) REFERENCES `PLACE` (`ID`) ON DELETE CASCADE,
  ADD CONSTRAINT `LOCATIONDATA_ibfk_1` FOREIGN KEY (`USERID`) REFERENCES `USER` (`ID`) ON DELETE CASCADE;

--
-- Constraints for table `PLACECATEGORY`
--
ALTER TABLE `PLACECATEGORY`
  ADD CONSTRAINT `PLACECATEGORY_ibfk_2` FOREIGN KEY (`CATEGORYID`) REFERENCES `CATEGORY` (`ID`) ON DELETE CASCADE,
  ADD CONSTRAINT `PLACECATEGORY_ibfk_1` FOREIGN KEY (`PLACEID`) REFERENCES `PLACE` (`ID`) ON DELETE CASCADE;

--
-- Constraints for table `USERNEED`
--
ALTER TABLE `USERNEED`
  ADD CONSTRAINT `USERNEED_ibfk_1` FOREIGN KEY (`USERID`) REFERENCES `USER` (`ID`) ON DELETE CASCADE;
