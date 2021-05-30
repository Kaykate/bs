<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!doctype html>
<html class="no-js" lang="en">
    <head>
        <title>拍卖网站 | 订单收货地址修改</title>
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
                                <li>订单收货地址修改</li>
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
                        <div class="contact_message form order_edit">
                            <h3>订单收货地址修改</h3>
                            <form id="order_form" method="POST" action="#"
                                  enctype="multipart/form-data">
                                <input type="hidden" name="id" value="${order.id}">
                                <input type="hidden" name="productId" value="${order.productId}">
                                <input type="hidden" name="userId" value="${order.userId}">
                                <p>
                                    <label>订单号</label>
                                    <input name="orderNumber" placeholder="订单号" type="text" readonly value="${order.orderNumber}">
                                </p>
                                <p>
                                    <label>商品名称</label>
                                    <input type="text" name="productName" placeholder="商品名称" readonly value="${order.product.name}">
                                </p>
                                <p>
                                    <label>买家</label>
                                    <input name="userName" placeholder="买家" type="text" readonly value="${order.user.username}">
                                </p>
                                <p>
                                    <label>价格</label>
                                    <input name="price" placeholder="价格" type="text" readonly value="${order.price}">
                                </p>
                                <p>
                                    <label>地址</label>
                                    <input name="address" placeholder="地址" type="text" value="${order.address}">
                                </p>
                                <button type="button" id="order_submit">保存</button>
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
