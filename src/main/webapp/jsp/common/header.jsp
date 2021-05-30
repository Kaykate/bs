<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!--header area start-->
<header class="header_area header_two">
    <!--header top start-->
    <div class="header_top">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-lg-3 col-md-9">
                    <div class="welcome_text">
                        <h2>物品拍卖网站</h2>
                    </div>
                </div>
                <div class="col-lg-9 col-md-9">
                    <div class="top_right text-right">
                        <ul>
                            <c:choose>
                                <c:when test="${sessionScope.user == null}">
                                    <li class="top_links">
                                        <a href="/login">登录</a>&nbsp;
                                        <a href="/register">注册</a>
                                    </li>
                                </c:when>
                                <c:when test="${sessionScope.user != null && sessionScope.user.type == 0}">
                                    <li><a href="/account/product/publish"> 发布商品</a></li>
                                    <li class="top_links"><a href="/account">${sessionScope.user.displayName}<i
                                            class="fa fa-angle-down"
                                            aria-hidden="true"></i></a>
                                        <ul class="dropdown_links">
                                            <li><a href="/account?tab=details">基本信息 </a></li>
                                            <li><a href="/account?tab=biddings">我的竞拍</a></li>
                                            <li><a href="/account?tab=products">我的商品</a></li>
                                            <li><a href="/account?tab=orders">我的订单</a></li>
                                            <li><a href="/account?tab=pay">充值</a></li>
                                            <li><a href="/logout">退出 </a></li>
                                        </ul>
                                    </li>
                                </c:when>
                                <c:when test="${sessionScope.user != null && sessionScope.user.type == 1}">
                                    <li class="top_links"><a href="/account">${sessionScope.user.displayName}<i
                                            class="fa fa-angle-down"
                                            aria-hidden="true"></i></a>
                                        <ul class="dropdown_links">
                                            <li><a href="/account?tab=details">后台管理 </a></li>
                                            <li><a href="/logout">退出 </a></li>
                                        </ul>
                                    </li>
                                </c:when>
                            </c:choose>
                        </ul>

                    </div>
                </div>
            </div>
        </div>
    </div>
    <!--header bottom satrt-->
    <div class="header_bottom">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-lg-3">
                    <div class="categories_menu">
                        <div class="categories_title">
                            <h2 class="category_toggle">
                                <c:choose>
                                    <c:when test="${category != null}">${category.name}</c:when>
                                    <c:otherwise>所有分类</c:otherwise>
                                </c:choose>

                            </h2>
                        </div>
                        <div class="categories_menu_toggle" style="display: none;">
                            <ul id="categories_ul"></ul>
                        </div>
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="search-container">
                        <form action="/">
                            <div class="search_box">
                                <input placeholder="搜索商品..." type="text" name="keywords"
                                       value="${param.keywords}">
                                <button type="submit"><i class="icon-search"></i></button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!--header bottom end-->

</header>
<!--header area end-->
