module SpringBoard {
	exports jin.spring.board.service;
	exports jin.spring.board.dao;
	exports jin.spring.board.dto;
	exports jin.spring.test;
	exports jin.spring.board.controller;

	requires java.sql;
	requires lombok;
	requires org.mybatis;
	requires org.mybatis.spring;
	requires org.slf4j;
	requires spring.beans;
	requires spring.context;
	requires spring.core;
	requires spring.tx;
	requires spring.web;
	requires jakarta.servlet;
	requires spring.webmvc;
	requires spring.security.crypto;
}