<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="utf-8" />
            <meta http-equiv="X-UA-Compatible" content="IE=edge" />
            <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
            <meta name="description" content="Hỏi dân IT - Dự án laptopshop" />
            <meta name="author" content="Hỏi dân IT" />
            <title>Dashboard - Hỏi Dân IT</title>
            <link href="/css/styles.css" rel="stylesheet" />
            <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
        </head>

        <body class="sb-nav-fixed">
            <jsp:include page="../layout/header.jsp" />
            <div id="layoutSidenav">
                <div id="layoutSidenav_nav">
                    <jsp:include page="../layout/sidebar.jsp" />
                </div>
                <div id="layoutSidenav_content">
                    <main>
                        <div class="container-fluid px-4">
                            <h1 class="mt-4">Order detail</h1>
                            <ol class="breadcrumb mb-4">
                                <li class="breadcrumb-item active"><a href="/admin">Dashboard</a></li>
                                <li class="breadcrumb-item active">Order Details</li>
                            </ol>
                            <div class="mt-5">
                                <div class="row">
                                    <table class="table table-bordered table-hover">
                                        <thead>
                                            <tr>
                                                <th scope="col">Image</th>
                                                <th scope="col">Name</th>
                                                <th scope="col">Price</th>
                                                <th scope="col">Quantity</th>
                                                <th scope="col">Total Price</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="orderDetail" items="${orderDetails}">
                                                <tr>
                                                    <td><img style="max-width: 75px;"
                                                            src="/images/product/${orderDetail.product.image}" /></td>
                                                    <td><a
                                                            href="/product/${orderDetail.product.id}">${orderDetail.product.name}</a>
                                                    </td>
                                                    <td>${orderDetail.price}</td>
                                                    <td>${orderDetail.quantity}</td>
                                                    <td>${orderDetail.quantity * orderDetail.price}</td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                                <a href="/admin/order" class="btn btn-primary mt-3">Back</a>
                            </div>
                        </div>
                    </main>
                    <jsp:include page="../layout/footer.jsp" />
                </div>
            </div>
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
                crossorigin="anonymous"></script>
            <script src="js/scripts.js"></script>
        </body>

        </html>