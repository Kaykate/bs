<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!doctype html>
<html class="no-js" lang="en">
    <head>
        <title>拍卖网站 | 注册</title>
        <link rel="stylesheet" href="/assets/css/bootstrap4.1.1.css">
        <%@ include file="common/head.jsp" %>
    </head>
    <body>
        <%@ include file="common/header.jsp" %>
        <!--breadcrumbs area start-->
        <div class="breadcrumbs_area">
            <div class="container">
                <div class="row">
                    <div class="col-12">
                        <div class="breadcrumb_content">
                            <ul>
                                <li><a href="/">首页</a></li>
                                <li>注册</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--breadcrumbs area end-->


        <!-- customer login start -->
        <div class="customer_login">
            <div class="container">
                <div class="row">
                    <!--register area start-->
                    <div class="col-lg-6 col-md-6">
                        <div class="account_form register">
                            <h2>注册</h2>
                            <form action="#" id="register_form">
                                <p>
                                    <label>用户名 <span>*</span></label>
                                    <input type="text" name="username">
                                </p>
                                <p>
                                    <label>密码 <span>*</span></label>
                                    <input type="password" name="password">
                                </p>
                                <div class="login_submit">
                                    <button type="button" id="register_submit">注册</button>
                                </div>
                            </form>
                        </div>
                    </div>
                    <!--register area end-->
                </div>
                <br><br><br>
            </div>
        </div>
        <!-- customer login end -->

        <%@ include file="common/footer.jsp" %>
