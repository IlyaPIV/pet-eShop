function loadCountries(dropDown, btnLoad, toast){
    url = contextPath + "countries/list";
    $.get(url, function (responseJSON) {
        dropDown.empty();

        $.each(responseJSON, function (index, country) {
            optionValue = country.id + "-" + country.code;
            $("<option>").val(optionValue).text(country.name).appendTo(dropDown);
        });
    }).done(function (){
        btnLoad.val("Refresh Country List");
        showToastMessage("All countries have been loaded.", toast);
    }).fail(function (){
        showToastMessage("ERROR: Could not connect to server or server encountered an error", toast)
    });
}


function showToastMessage(message, toast){
    $(".toastMessage").text(message);
    toast.toast('show');
}