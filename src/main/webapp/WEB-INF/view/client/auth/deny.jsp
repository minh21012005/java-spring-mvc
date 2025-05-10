<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <title>Access Denied</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                text-align: center;
                margin-top: 100px;
                background-color: #f2f2f2;
            }

            .container {
                display: inline-block;
                padding: 30px;
                background-color: #fff;
                border: 1px solid #ddd;
                border-radius: 8px;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            }

            h2 {
                color: #cc0000;
            }

            button {
                padding: 10px 20px;
                font-size: 16px;
                background-color: #0066cc;
                color: white;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                margin-top: 20px;
            }

            button:hover {
                background-color: #004d99;
            }
        </style>
    </head>

    <body>
        <div class="container">
            <h2>Access Denied</h2>
            <p>Bạn không có quyền truy cập vào trang này.</p>
            <form action="/">
                <button type="submit">Quay về Trang chủ</button>
            </form>
        </div>
    </body>

    </html>