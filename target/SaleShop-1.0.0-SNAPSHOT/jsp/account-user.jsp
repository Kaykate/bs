<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!doctype html>
<html class="no-js" lang="en">
    <head>
        <title>拍卖网站 | 我的账号</title>
        <link rel="stylesheet" href="/assets/css/bootstrap3.3.7.css">
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
                                <li>账号中心</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--breadcrumbs area end-->


        <section class="main_content_area" style="min-height: 500px;">
            <div class="container">
                <div class="account_dashboard">
                    <div class="row">
                        <div class="col-sm-12 col-md-3 col-lg-3">
                            <!-- Nav tabs -->
                            <div class="dashboard_tab_button">
                                <ul role="tablist" class="nav flex-column dashboard-list">
                                    <li><a href="#details" data-toggle="tab" id="tab-details" class="nav-link">基本信息</a>
                                    </li>

                                    <li><a href="#password" data-toggle="tab" id="tab-password" class="nav-link">修改密码</a></li>
                                    <li><a href="#biddings" data-toggle="tab" id="tab-biddings" class="nav-link ">我的竞拍</a></li>
                                    <li><a href="#products" data-toggle="tab" id="tab-products" class="nav-link ">我的商品</a></li>
                                    <li><a href="#orders" data-toggle="tab" id="tab-orders" class="nav-link ">我的订单</a></li>
                                    <li><a href="#pay" data-toggle="tab" id="tab-pay" class="nav-link ">充值</a></li>
                                    <li><a href="/logout" class="nav-link">退出</a></li>
                                </ul>
                            </div>
                        </div>
                        <div class="col-sm-12 col-md-9 col-lg-9">
                            <!-- Tab panes -->
                            <div class="tab-content dashboard_content">
                                <div class="tab-pane fade" id="details">
                                    <h3>账号基本信息 </h3>
                                    <div class="login">
                                        <div class="login_form_container">
                                            <div class="account_login_form">
                                                <form action="#" id="user_account_form">
                                                    <label>用户名</label>
                                                    <input type="text" name="username" value="${user.username}">
                                                    <label>真实名</label>
                                                    <input type="text" name="displayName" value="${user.displayName}">
                                                    <label>手机号</label>
                                                    <input type="text" name="phone" value="${user.phone}">
                                                    <label>地址</label>
                                                    <input type="text" name="address" value="${user.address}">
                                                    <label>余额</label>
                                                    <%--                                                    <input type="text" readonly value="${user.balance}">--%>

                                                    <c:out value="${user.balance}"></c:out>
                                                    <div class="save_button primary_btn default_button">
                                                        <button type="button" id="save_account_submit">保存</button>
                                                        <%--                                                        <button type="button" id="update_account_submit">刷新</button>--%>
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                    <br>
                                </div>
                                <div class="tab-pane fade" id="pay">
                                    <h3>充值</h3>
                                    <div class="login">
                                        <div class="login_form_container">
                                            <div class="account_login_form">
                                                <form action="#" id="pay_form">
                                                    <label>用户名</label>
                                                    <input type="text" name="username" readonly value="${user.username}">
                                                    <label>真实名</label>
                                                    <input type="text" name="displayName"  readonly value="${user.displayName}">
                                                    <label>充值金额</label>
                                                    <input type=number id = "balance" name="balance" required="">
                                                    <label>您目前的金额：</label>
<%--                                                    <c:out value="${user.balance}"></c:out>--%>
                                                    <p id = "currentprice">${user.balance}</p>
                                                    <p>请在输入金额后点击充值，付款成功后点击付款成功</p>
                                                    <br>
                                                    <img src="/assets/photo/IMG_20210521_183705.jpg" id = "image" width = "200px" height="200px"  style="display:none;" />
                                                    <div class="save_button primary_btn default_button" >
                                                        <button type="button" id="paybutton">充值</button>
                                                    </div>
                                                    <div class="save_button primary_btn default_button">
                                                        <button type="button" id="pay_submit" style="display:none;">充值成功</button>
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                    <br>
                                </div>

                                <div class="tab-pane fade" id="password">
                                    <h3>修改密码 </h3>
                                    <div class="login">
                                        <div class="login_form_container">
                                            <div class="account_login_form">
                                                <form action="#" id="user_password_form">
                                                    <label>原密码</label>
                                                    <input type="password" name="oldPassword">
                                                    <label>新密码</label>
                                                    <input type="password" name="newPassword">
                                                    <label>确认密码</label>
                                                    <input type="password" name="confirmPassword">
                                                    <div class="save_button primary_btn default_button">
                                                        <button type="button" id="save_password_submit">保存</button>
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                    <br>
                                </div>
                                <div class="tab-pane fade" id="biddings">
                                    <h3>竞拍记录</h3>
                                    <div class="table-responsive">
                                        <table class="table" id="biddings-table">
                                            <thead>
                                            <tr>
                                                <th>序号</th>
                                                <th>商品名称</th>
                                                <th>当前竞价</th>
                                                <th>我的竞价</th>
                                                <th>状态</th>
                                                <th>操作</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach items="${biddingList}" var="c" varStatus="status">
                                                <tr>
                                                    <!--varStatus="status"是为了竞拍列表排序，status.index从0开始，所以要加一-->
                                                    <td>${status.index+1}</td>
                                                    <td>${c.product.name}</td>
                                                    <td>${c.product.currentPrice}</td>
                                                    <td>${c.price}</td>
                                                    <td>
                                                        <!--这里的status是product表以及bidding表里自带的status-->
                                                        <!--product中的1是正在拍卖，2成交。bidding中1成功，0失败-->
                                                        <c:choose>
                                                            <c:when test="${c.product.status == 1}">
                                                                <span class="text-primary">竞拍中</span>
                                                            </c:when>
                                                            <c:when test="${c.product.status == 2 && c.status == 1}">
                                                                <span class="text-success">竞拍成功</span>
                                                            </c:when>
                                                            <c:when test="${c.product.status == 2 && c.status == 0}">
                                                                <span class="text-danger">竞拍失败</span>
                                                            </c:when>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <a href="/product/${c.product.id}" class="view" target="_blank">查看</a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                        <br><br><br><br>
                                    </div>
                                </div>
                                <div class="tab-pane fade" id="products">
                                    <h3>我的商品</h3>
                                    <div class="table-responsive">
                                        <table class="table" id="products-table">
                                            <thead>
                                            <tr>
                                                <th>序号</th>
                                                <th>商品名称</th>
                                                <th>结束时间</th>
                                                <th>起拍价</th>
                                                <th>最高价</th>
                                                <th>竞拍状态</th>
                                                <th>操作</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach items="${productList}" var="c" varStatus="status">
                                                <tr>
                                                    <td>${status.index+1}</td>
                                                    <td><a href="/product/${c.id}" target="_blank">${c.name}</a></td>
                                                    <td><fmt:formatDate value="${c.endTime}"
                                                                        pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                                    <td>${c.startPrice}</td>
                                                    <td>${c.currentPrice}</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${c.status == 0}">
                                                                <span class="text-danger">流拍</span>
                                                            </c:when>
                                                            <c:when test="${c.status == 1}">
                                                                <span class="text-primary">正在拍卖</span>
                                                            </c:when>
                                                            <c:when test="${c.status == 3}">
                                                                <span class="text-primary">下架</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="text-success">成交</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <a href="/product/${c.id}" class="view" target="_blank">查看</a>
                                                        <c:if test="${c.status != 2&&c.status != 0}">
                                                            <a href="/account/product/edit/${c.id}" class="view text-info">编辑</a>
                                                        </c:if>
                                                        <a href="javascript:void(0)" title="删除"
                                                           class="delete_product text-danger"
                                                           data-id="${c.id}" class="view">删除</a>
                                                        <c:choose>
                                                            <c:when test="${c.status == 3}">
                                                                <a href="javascript:void(0)" title="上架"
                                                                   class="grounding_product text-warning"
                                                                   data-id="${c.id}" class="view">上架</a>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <a href="javascript:void(0)" title="下架"
                                                                   class="off_product text-warning"
                                                                   data-id="${c.id}" class="view">下架</a>
                                                            </c:otherwise>
                                                        </c:choose>


                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>

                                        <br><br><br><br>
                                    </div>
                                </div>
                                <div class="tab-pane fade" id="orders">
                                    <h3>我的订单</h3>
                                    <div class="table-responsive">
                                        <table class="table" id="orders-table">
                                            <thead>
                                            <tr>
                                                <th>序号</th>
                                                <th>商品名称</th>
                                                <th>价格</th>
                                                <th>创建时间</th>
                                                <th>收货地址</th>
                                                <th>操作</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach items="${orderList}" var="c" varStatus="status">
                                                <tr>
                                                    <td>${status.index+1}</td>
                                                    <td><a href="/product/${c.productId}"
                                                           target="_blank">${c.product.name}</a></td>
                                                    <td>${c.price}</td>
                                                    <td><fmt:formatDate value="${c.createTime}"
                                                                        pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                                    <td>${c.address}</td>
                                                    <td>
                                                        <a href="/product/${c.product.id}" class="view" target="_blank">查看</a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>

                                        <br><br><br><br>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>


        <%@ include file="common/footer.jsp" %>
