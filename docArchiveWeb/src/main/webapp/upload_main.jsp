<%-- 
    Document   : upload
    Created on : 29.01.2018, 13:39:35
    Author     : vasiliy.andricov
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">        
        <title>JSP Page</title>
    </head>
    <body>
        <form action="/docArchive/upload" method="post" enctype="multipart/form-data">
            <table>
                <tr>
                    <td>Описание</td>
                    <td><input name="description" type="text"></td>
                </tr>
                <tr>
                    <td>Филиал</td>
                    <td>
                        <select name="branch_id">
                            <c:forEach items="${branches}" var="item">
                                <option value="${item.id}">${item.name_branch}</option>
                            </c:forEach>

                        </select>
                    </td>
                </tr>
                <tr>
                    <td>Продукт</td>
                    <td>
                        <select name="product_id">
                            <c:forEach items="${products}" var="item">
                                <option value="${item.id}">${item.name_product}</option>
                            </c:forEach>

                        </select>
                    </td>
                </tr>
                <tr>
                    <td>Проект</td>
                    <td>
                        <select name="project_id">
                            <c:forEach items="${projects}" var="item">
                                <option value="${item.id}">${item.num_doc}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>Тип документа</td>
                    <td>
                        <select name="doc_type_id">
                            <c:forEach items="${doctype}" var="item">
                                <option value="${item.id}">${item.name_type}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr colspan="2">
                    <td><input name="data" type="file"><td>
                </tr>
                <tr colspan="2">
                    <td>
                        <input type="submit"><br>
                    </td>
                </tr>
            </table>
        </form>        
    </body>
</html>
