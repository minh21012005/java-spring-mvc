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
                        const orgImage = "${product.image}";
                        if (orgImage) {
                            const urlImage = "/images/product/" + orgImage;
                            $("#avatarPreview").attr("src", urlImage);
                            $("#avatarPreview").css({ "display": "block" });
                        }

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
                                <h1 class="mt-4">Update</h1>
                                <ol class="breadcrumb mb-4">
                                    <li class="breadcrumb-item active"><a href="/admin">Dashboard</a></li>
                                    <li class="breadcrumb-item active">Products</li>
                                </ol>
                                <div class="mt-5">
                                    <div class="row">
                                        <div class="col-md-6 col-12 mx-auto">
                                            <h1>Update product</h1>
                                            <hr />
                                            <form:form method="post" action="/admin/product/update"
                                                modelAttribute="product" enctype="multipart/form-data">
                                                <div class="mb-3">
                                                    <label style="display: none;" for="exampleInputId"
                                                        class="form-label">Id</label>
                                                    <form:input style="display: none;" type="text" class="form-control"
                                                        id="exampleInputId" path="id" />
                                                </div>
                                                <div class="row">
                                                    <div class="mb-3 col-md-6 col-12">
                                                        <label for="name" class="form-label">Name</label>
                                                        <form:input type="text" class="form-control" id="name"
                                                            path="name" />
                                                    </div>
                                                    <div class="mb-3 col-md-6 col-12">
                                                        <label for="price" class="form-label">Price</label>
                                                        <form:input type="number" step="any" class="form-control"
                                                            id="price" path="price" />
                                                    </div>
                                                </div>
                                                <div class="mb-3">
                                                    <label for="detailDesc" class="form-label">Detail
                                                        description</label>
                                                    <form:input type="text" class="form-control" id="detailDesc"
                                                        path="detailDesc" />
                                                </div>
                                                <div class="row">
                                                    <div class="mb-3 col-md-6 col-12">
                                                        <label for="shortDesc" class="form-label">Short
                                                            description</label>
                                                        <form:input type="text" class="form-control" id="shortDesc"
                                                            path="shortDesc" />
                                                    </div>
                                                    <div class="mb-3 col-md-6 col-12">
                                                        <label for="quantity" class="form-label">Quantity</label>
                                                        <form:input type="number" class="form-control" id="quantity"
                                                            path="quantity" />
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-6 col-12">
                                                        <label for="factory" class="form-lable mb-2">Factory</label>
                                                        <form:select class="form-select"
                                                            aria-label="Default select example" id="factory"
                                                            path="factory">
                                                            <form:option value="APPLE">Apple(MacBook)</form:option>
                                                            <form:option value="ASUS">Asus</form:option>
                                                            <form:option value="LENOVO">Lenovo</form:option>
                                                            <form:option value="DELL">Dell</form:option>
                                                            <form:option value="HP">HP</form:option>
                                                            <form:option value="ACER">Acer</form:option>
                                                            <form:option value="MSI">MSI</form:option>
                                                            <form:option value="SURFACE">Surface</form:option>
                                                            <form:option value="GIGABYTE">Gigabyte</form:option>
                                                            <form:option value="HUAWEI">Huawei</form:option>
                                                        </form:select>
                                                    </div>
                                                    <div class="col-md-6 col-12">
                                                        <label for="target" class="form-lable mb-2">Target</label>
                                                        <form:select class="form-select"
                                                            aria-label="Default select example" id="target"
                                                            path="target">
                                                            <form:option value="GAMING">Gaming</form:option>
                                                            <form:option value="SINH VIEN - VAN PHONG">Sinh viên - Văn
                                                                phòng
                                                            </form:option>
                                                            <form:option value="THIET KE DO HOA">Thiết kế đồ họa
                                                            </form:option>
                                                            <form:option value="MONG NHE">Mỏng nhẹ</form:option>
                                                            <form:option value="DOANH NHAN">Doanh nhân</form:option>
                                                        </form:select>
                                                    </div>
                                                </div>
                                                <div class="col-md-6 col-12">
                                                    <label for="avatarFile" class="form-label">Avatar:</label>
                                                    <input class="form-control" type="file" id="avatarFile" name="file"
                                                        accept=".png, .jpg, .jpeg" />
                                                </div>
                                                <div class="col-12 mt-3">
                                                    <img style="max-height: 250px; display: none;" alt="avatar preview"
                                                        id="avatarPreview" />
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