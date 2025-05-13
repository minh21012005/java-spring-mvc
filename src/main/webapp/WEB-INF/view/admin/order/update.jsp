<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
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
                <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
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
                                <h1 class="mt-4">Update</h1>
                                <ol class="breadcrumb mb-4">
                                    <li class="breadcrumb-item active"><a href="/admin">Dashboard</a></li>
                                    <li class="breadcrumb-item active">Orders</li>
                                </ol>
                                <div class="mt-5">
                                    <div class="row">
                                        <div class="col-md-6 col-12 mx-auto">
                                            <h1>Update order</h1>
                                            <hr />
                                            <form:form method="post" action="/admin/order/update"
                                                modelAttribute="order">
                                                <form:input path="id" type="hidden"></form:input>
                                                <div class="d-flex w-100">
                                                    <span>Order id = ${order.id}</span>
                                                    <span class="mx-auto ms-5">Total Price = ${order.totalPrice}</span>
                                                </div>
                                                <div class="row">
                                                    <div class="mb-3 col-md-6 col-12">
                                                        <label for="name" class="form-label">Name</label>
                                                        <input type="text" class="form-control" id="name"
                                                            value="${sessionScope.fullName}" disabled="true" />
                                                    </div>
                                                    <div class="col-md-6 col-12">
                                                        <label for="status" class="form-lable mb-2">Status</label>
                                                        <form:select class="form-select"
                                                            aria-label="Default select example" id="status"
                                                            path="status">
                                                            <form:option value="PENDING">Pending</form:option>
                                                            <form:option value="SHIPPING">Shipping</form:option>
                                                            <form:option value="COMPLETE">Complete</form:option>
                                                            <form:option value="CANCEL">Cancel</form:option>
                                                        </form:select>
                                                    </div>
                                                </div>
                                                <button type="submit" class="btn btn-warning">Update</button>
                                            </form:form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </main>
                        <jsp:include page="../layout/footer.jsp" />
                    </div>
                </div>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
                    crossorigin="anonymous"></script>
                <script src="/js/scripts.js"></script>
            </body>

            </html>