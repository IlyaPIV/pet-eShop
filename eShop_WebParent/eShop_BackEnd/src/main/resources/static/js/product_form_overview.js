dropdownBrands = $("#brand");
dropdownCategories = $("#category");

$(document).ready(function (){

    $("#shortDescription").richText();
    $("#fullDescription").richText();

    dropdownBrands.change(function (){
        dropdownCategories.empty();
        getCategories();
    });

    getCategoriesForNewForm();

});

function getCategoriesForNewForm(){

    catIdField = $("#categoryID");
    editMode = false;

    if (catIdField.length) {
        editMode = true;
    }

    if (!editMode) getCategories();
}

function getCategories() {
    brandId = dropdownBrands.val();
    url = brandModuleURL + "/" + brandId + "/categories";

    $.get(url, function (responseJson){
        $.each(responseJson, function (index, category){
            $("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);
        });
    });
}

function checkUnique(form){

    prodId     = $("#id").val();
    prodName     = $("#name").val();
    csrfValue   = $("input[name='_csrf']").val();

    params = {id: prodId, name: prodName, _csrf: csrfValue};

    $.post(checkUniqieUrl, params, function (response){
        if (response === "OK") {
            form.submit();
        } else if (response === "Duplicate") {
            showWarningModal("There is another brand having same name - " + prodName);
        } else {
            showErrorModal("Unknown response from server");
        }
    }).fail(function (){
        showErrorModal("Could not connect to the server.")
    });

    return false;
}


