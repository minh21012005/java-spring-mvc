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
                <script>
                    $(document).ready(() => {
                        const avatarFile = $("#avatarFile");
                        avatarFile.change(function (e) {
                            const imgURL = URL.createObjectURL(e.target.files[0]);
                            $("#avatarPreview").attr("src", imgURL);
                            $("#avatarPreview").css({ "display": "block" });
                        });
                    });
                </script>
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
                                <h1 class="mt-4">Manage users</h1>
                                <ol class="breadcrumb mb-4">
                                    <li class="breadcrumb-item active"><a href="/admin">Dashboard</a></li>
                                    <li class="breadcrumb-item active">Users</li>
                                </ol>
                                <div class="mt-5">
                                    <div class="row">
                                        <div class="col-md-6 col-12 mx-auto">
                                            <h1>Create a user</h1>
                                            <hr />
                                            <form:form method="post" action="/admin/user/create"
                                                modelAttribute="newUser" enctype="multipart/form-data">
                                                <div class="mb-3">
                                                    <c:set var="errorEmail">
                                                        <form:errors path="email" cssClass="invalid-feedback" />
                                                    </c:set>
                                                    <label for="exampleInputEmail1" class="form-label">Email</label>
                                                    <form:input type="email"
                                                        class="form-control ${not empty errorEmail?'is-invalid':''}"
                                                        id="exampleInputEmail1" path="email" />
                                                    ${errorEmail}
                                                </div>
                                                <div class="mb-3">
                                                    <c:set var="errorPassword">
                                                        <form:errors path="password" cssClass="invalid-feedback" />
                                                    </c:set>
                                                    <label for="exampleInputPassword1"
                                                        class="form-label">Password</label>
                                                    <form:input type="password"
                                                        class="form-control ${not empty errorPassword?'is-invalid':''}"
                                                        id="exampleInputPassword1" path="password" />
                                                    ${errorPassword}
                                                </div>
                                                <div class="mb-3">
                                                    <c:set var="errorFullName">
                                                        <form:errors path="fullName" cssClass="invalid-feedback" />
                                                    </c:set>
                                                    <label for="exampleInputName" class="form-label">Full name</label>
                                                    <form:input type="text"
                                                        class="form-control ${not empty errorFullName?'is-invalid':''}"
                                                        id="exampleInputName" path="fullName" />
                                                    ${errorFullName}
                                                </div>
                                                <div class="mb-3">
                                                    <label for="exampleInputPhoneNumber" class="form-label">Phone
                                                        number</label>
                                                    <form:input type="text" class="form-control"
                                                        id="exampleInputPhoneNumber" path="phone" />
                                                </div>
                                                <div class="mb-3">
                                                    <label for="exampleInputAddress" class="form-label">Address</label>
                                                    <form:input type="text" class="form-control"
                                                        id="exampleInputAddress" path="address" />
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-6 col-12">
                                                        <label for="select" class="form-lable mb-2">Role</label>
                                                        <form:select class="form-select"
                                                            aria-label="Default select example" id="select"
                                                            path="role.name">
                                                            <form:option value="ADMIN">ADMIN</form:option>
                                                            <form:option value="USER">USER</form:option>
                                                        </form:select>
                                                    </div>
                                                    <div class="col-md-6 col-12">
                                                        <label for="avatarFile" class="form-label">Avatar:</label>
                                                        <input class="form-control" type="file" id="avatarFile"
                                                            name="file" accept=".png, .jpg, .jpeg" />
                                                    </div>
                                                    <div class="col-12 mt-3">
                                                        <img style="max-height: 250px; display: none;"
                                                            alt="avatar preview" id="avatarPreview" />
                                                    </div>
                                                </div>
                                                <div class="mt-3"><button type="submit"
                                                        class="btn btn-primary">Create</button></div>
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
                <script src="js/scripts.js"></script>
            </body>

            </html>