/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function getProjectList(token) {
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
                $('#selectmenu').append('<option value = "' + data[i].id + '">' + data[i].name_doc + "</option>");
            }
            return data;
        },
        error: function (jqXHR, textStatus, errorThrown) {
            return '';
        }
    });
}

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
            