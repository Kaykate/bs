<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!doctype html>
<html class="no-js" lang="en">
    <head>
        <title>拍卖网站 | 用户信息修改</title>
        <link rel="stylesheet" href="/assets/css/bootstrap4.1.1.css">
        <%@ include file="common/head.jsp" %>
    </head>
    <body>
        <%@ include file="common/header.jsp" %>
        <div class="breadcrumbs_area">
            <div class="container">
                <div class="row">
                    <div class="col-12">
                        <div class="breadcrumb_content">
                            <ul>
                                <li><a href="/">首页</a></li>
                                <li>用户信息修改</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--breadcrumbs area end-->

        <div class="contact_area">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12 col-md-12">
                        <div class="contact_message form user_edit">
                            <h3>用户信息修改</h3>
                            <form id="user_form" method="POST" action="#"
                                  enctype="multipart/form-data">
                                <input type="hidden" name="id" value="${user.id}">
                                <p>
                                    <label>用户名</label>
                                    <input name="username" readonly placeholder="用户名" type="text" value="${user.username}">
                                </p>
                                <p>
                                    <label>密码</label>
                                    <input type="password" readonly name="password" placeholder="密码" value="${user.password}">
                                </p>
                                <p>
                                    <label>真实名</label>
                                    <input name="displayName" readonly placeholder="真实名" type="text" value="${user.displayName}">
                                </p>
                                <p>
                                    <label>手机号</label>
                                    <input name="phone"  placeholder="手机号" type="text" value="${user.phone}">
                                </p>
                                <p>
                                    <label>地址</label>
                                    <input name="address"  placeholder="地址" type="text" value="${user.address}">
                                </p>
                                <p>
                                    <label>余额</label>
                                    <input name="balance" placeholder="余额" type="text" value="${user.balance}">
                                </p>
                                <button type="button" id="user_submit">保存</button>
                                <p class="form-messege"></p>
                            </form>

                        </div>
                    </div>
                </div>
            </div>
        </div>

        <%@ include file="common/footer.jsp" %>
        <script>

        </script>
