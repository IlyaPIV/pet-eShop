var dropDownCountry;
var dataStateList;
var fieldState;


$(document).ready(function (){
    dropDownCountry = $("#country");
    dataStateList = $("#listStates");
    fieldState = $("#state");

    dropDownCountry.on("change", function (){
        loadStatesForCountry();
        fieldState.val("").focus();
    });

    loadStatesForCountry();
});

function loadStatesForCountry() {
    selectedCountry = $("#country option:selected");
    countryId = selectedCountry.val();
    url = contextPath + "states/list/" + countryId;

    $.get(url, function (responseJSON) {
        dataStateList.empty();

        $.each(responseJSON, function (index, state) {
            $("<option>").val(state.name).text(state.name).appendTo(dataStateList);
        });
    }).fail(function () {
        alert("failed to connect to the server!")
    });
}