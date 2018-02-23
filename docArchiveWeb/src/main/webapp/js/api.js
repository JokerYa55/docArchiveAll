/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

// Получить список проектов
function getProjectList(token, elem) {
    console.log("getProductList");
    $.ajax({
        headers: {
            'Authorization': 'Bearer ' + token
        },
        url: "http://192.168.0.20:8080/docArchiveAPI/api/project",
        success: function (data, textStatus, jqXHR) {
            console.log(jqXHR);
            console.log(data);
            for (var i = 0; i < data.length; i++) {
                console.log(data[i].name_doc);
                $('#' + elem).append('<option value = "' + data[i].id + '">' + data[i].name_doc + "</option>");
            }
            return data;
        },
        error: function (jqXHR, textStatus, errorThrown) {
            return '';
        }
    });
}

// Получить список филиалов
function getBranchList(token, elem) {
    console.log("getBranchList");
    $.ajax({
        headers: {
            'Authorization': 'Bearer ' + token
        },
        url: "http://192.168.0.20:8080/docArchiveAPI/api/branch",
        success: function (data, textStatus, jqXHR) {
            console.log(jqXHR);
            console.log(data);
            for (var i = 0; i < data.length; i++) {
                console.log(data[i].name_doc);
                $('#' + elem).append('<option value = "' + data[i].id + '">' + data[i].name_branch + "</option>");
            }
            return data;
        },
        error: function (jqXHR, textStatus, errorThrown) {
            return '';
        }
    });
}

// Получить список типов документов
function getDocTypeList(token, elem) {
    console.log("getDocTypeList");
    $.ajax({
        headers: {
            'Authorization': 'Bearer ' + token
        },
        url: "http://192.168.0.20:8080/docArchiveAPI/api/doctype",
        success: function (data, textStatus, jqXHR) {
            console.log(jqXHR);
            console.log(data);
            for (var i = 0; i < data.length; i++) {
                console.log(data[i].name_doc);
                $('#' + elem).append('<option value = "' + data[i].id + '">' + data[i].name_type + "</option>");
            }
            return data;
        },
        error: function (jqXHR, textStatus, errorThrown) {
            return '';
        }
    });
}

// Получить список продуктов
function getProductList(token, elem) {
    console.log("getProductList");
    $.ajax({
        headers: {
            'Authorization': 'Bearer ' + token
        },
        url: "http://192.168.0.20:8080/docArchiveAPI/api/product",
        success: function (data, textStatus, jqXHR) {
            console.log(jqXHR);
            console.log(data);
            for (var i = 0; i < data.length; i++) {
                console.log(data[i].name_doc);
                $('#' + elem).append('<option value = "' + data[i].id + '">' + data[i].name_product + "</option>");
            }
            return data;
        },
        error: function (jqXHR, textStatus, errorThrown) {
            return '';
        }
    });
}

// Получить список документов проекта
function getProjectDocList(token, project_id, elem_id) {
    console.log("getProjectDocList");
    var jqhxr = $.ajax({
        headers: {
            'Authorization': 'Bearer ' + token
        },
        url: "http://192.168.0.20:8080/docArchiveAPI/api/projectdoc/" + project_id
    }).done(function (data) {
        //alert("second success > " + data);
    })
            .fail(function () {
                alert("error");
            })
            .always(function (data) {
                //alert("finished > " + data);
                var table = $('<table>');
                table.attr('id', 'idDataTable');
                $("#"+elem_id).append(table);
                for (var i = 0; i < data.length; i++) {
                    console.log(data[i]);
                    var tr = $('<tr>');
                    var td1 = $('<td>');
                    td1.width(10);
                    td1.html('<p>' + data[i].id + '</p>');
                    tr.append(td1);
                    
                    var td2 = $('<td>');
                    td2.width(50);
                    td2.html('<p>' + data[i].file_name + '</p>');
                    tr.append(td2);
                    
                    var td3 = $('<td>');
                    td3.width(50);
                    td3.html('<p>' + data[i].doc_type.name_type + '</p>');
                    tr.append(td3);
                    
                    var td4 = $('<td>');
                    td4.width(50);
                    td4.html('<p>' + data[i].project.name_doc + '</p>');
                    tr.append(td4);
                    
                    var td5 = $('<td>');
                    td5.width(50);
                    td5.html('<p>' + data[i].project.product.name_product + '</p>');
                    tr.append(td5);
                    
                    table.append(tr);
                }
                console.log(table);
            });

}