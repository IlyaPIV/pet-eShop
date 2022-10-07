var buttonLoad;
var dropDownCountries;
var btnAdd;
var btnUpdate;
var btnDelete;
var labelCountryName;
var fieldCountryName;
var fieldCountryCode;

$(document).ready(function (){
    buttonLoad          = $("#buttonLoadCountries");
    dropDownCountries   = $("#dropDownCountries");
    btnDelete           = $("#btnDeleteCountry");
    btnUpdate           = $("#btnUpdateCountry");
    btnAdd              = $("#btnAddCountry");
    labelCountryName    = $("#labelCountryName");
    fieldCountryName    = $("#fieldCountryName");
    fieldCountryCode    = $("#fieldCountryCode");

    buttonLoad.click(function (){
       loadCountries();
    });

    dropDownCountries.on("change", function (){
       changeFormStateToSelectedCountry();
    });

    btnAdd.click(function (){
        if (btnAdd.val() === "Add") {
            addCountry();
        } else {
            changeFormStateToNew();
        }
    });

    btnUpdate.click(function (){
        updateCountry();
    });

    btnDelete.click(function (){
       deleteCountry();
    });
});

function loadCountries(){
    url = contextPath + "countries/list";
    $.get(url, function (responseJSON) {
        dropDownCountries.empty();

        $.each(responseJSON, function (index, country) {
            optionValue = country.id + "-" + country.code;
            $("<option>").val(optionValue).text(country.name).appendTo(dropDownCountries);
        });
    }).done(function (){
        buttonLoad.val("Refresh Country List");
        showToastMessage("All countries have been loaded.");
    }).fail(function (){
        showToastMessage("ERROR: Could not connect to server or server encountered an error")
    });
}

function showToastMessage(message){
    $("#toastMessage").text(message);
    $(".toast").toast('show');
}

function changeFormStateToSelectedCountry(){
    btnAdd.prop("value", "New");
    btnUpdate.prop("disabled", false);
    btnDelete.prop("disabled", false);

    labelCountryName.text("Selected Country:")
    selectedCountryName = $("#dropDownCountries option:selected").text();
    fieldCountryName.val(selectedCountryName);

    countryCode = dropDownCountries.val().split("-")[1];
    fieldCountryCode.val(countryCode);
}

function changeFormStateToNew(){
    btnAdd.val("Add");
    labelCountryName.text("Country Name:");
    btnUpdate.prop("disabled", true);
    btnDelete.prop("disabled", true);

    fieldCountryCode.val("");
    fieldCountryName.val("").focus();
}

function addCountry(){
    url = contextPath + "countries/save";
    countryName = fieldCountryName.val();
    countryCode = fieldCountryCode.val();

    jsonData = {name: countryName, code: countryCode};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function (countryId){
        selectNewlyAddedCountry(countryId, countryName, countryCode);
        showToastMessage("The new country has been added");
    }).fail(function (){
        showToastMessage("ERROR happened");
    });
}

function selectNewlyAddedCountry(countryId, countryName, countryCode){
    optionValue = countryId + "-" + countryCode;
    $("<option>").val(optionValue).text(countryName).appendTo(dropDownCountries);

    $("#dropDownCountries option[value='" + optionValue + "']").prop("selected", true);
    fieldCountryCode.val("");
    fieldCountryName.val("").focus();
}

function updateCountry(){
    url = contextPath + "countries/save";
    countryName = fieldCountryName.val();
    countryCode = fieldCountryCode.val();
    countryId = dropDownCountries.val().split("-")[0]

    jsonData = {id: countryId, name: countryName, code: countryCode};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function (countryId){
        selected = $("#dropDownCountries option:selected");
        selected.val(countryId + "-" + countryCode);
        selected.text(countryName);
        showToastMessage("The country has been updated");
        changeFormStateToNew();
    }).fail(function (){
        showToastMessage("ERROR happened");
    });
}

function deleteCountry(){
    optionValue = dropDownCountries.val();
    countryId = optionValue.split("-")[0];

    url = contextPath + "/countries/delete/" + countryId;

    $.get(url, function (responseJSON) {
        $("#dropDownCountries option[value='" + optionValue + "']").remove();
        changeFormStateToNew();
    }).done(function (){
        showToastMessage("The country has been deleted.");
    }).fail(function (){
        showToastMessage("ERROR: Could not connect to server or server encountered an error")
    });
}