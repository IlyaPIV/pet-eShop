var dropDownCountry;
var dataStateList;
var fieldState;



$(document).ready(function (){
    dropDownCountry = $("#country");
    dataStateList = $("#listStates");
    fieldState = $("#state")

    dropDownCountry.on("change", function (){
        loadStatesForCountry();
        fieldState.val("").focus();
    });
});

function loadStatesForCountry(){
    selectedCountry = $("#country option:selected");
    countryId = selectedCountry.val();
    url = moduleURL + "settings/list_states_by_country/" + countryId;

    $.get(url, function (responseJSON){
        dataStateList.empty();

        $.each(responseJSON, function (index, state){
            $("<option>").val(state.name).text(state.name).appendTo(dataStateList);
        });
    }).fail(function (){
        alert("failed to connect to the server!")
    });

}

function checkPasswordMatch(confirmPassword){
    if (confirmPassword.value != $("#password").val()) {
        confirmPassword.setCustomValidity("Passwords do not match!");
    } else {
        confirmPassword.setCustomValidity("");
    }
}


function showErrorModal(message){
    showModalDialog("Error", message);
}

function showWarningModal(message){
    showModalDialog("Warning", message);
}

function showModalDialog(title, message) {
    $("#modalTitle").text(title);
    $("#modalBody").text(message);

    $("#modalDialog").modal();
}