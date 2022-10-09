var btnLoadCountries;
var dropDownCountry;
var dropDownStates;
var btnAddState;
var btnUpdateState;
var btnDeleteState;
var labelStateName;
var fieldStateName;
var toastStates;

$(document).ready(function (){
    btnLoadCountries  = $("#buttonLoadCountriesForStates");
    dropDownCountry   = $("#dropDownCountry");
    dropDownStates    = $("#dropDownStates");
    btnDeleteState    = $("#btnDeleteState");
    btnUpdateState    = $("#btnUpdateState");
    btnAddState       = $("#btnAddState");
    labelStateName    = $("#labelStateName");
    fieldStateName    = $("#fieldStateName");
    toastStates       = $("#toastStates");

    btnLoadCountries.click(function (){
       loadCountries(dropDownCountry, btnLoadCountries, toastStates);
       changeStatesFormStateToNew();
    });

    dropDownCountry.on("change", function (){
        loadStatesForCurrentCountry(toastStates);
    })

    dropDownStates.on("change", function (){
        changeFormStateToSelectedState();
    })

    btnAddState.click(function (){
       if (dropDownCountry.val() == null) {
           showToastMessage("ERROR: Select country first!!!", toastStates);
       } else {
           if (btnAddState.val() === "Add") {
               addNewState();
           } else {
               changeStatesFormStateToNew();
           }
       }
    });

    btnUpdateState.click(function (){
        updateState();
    });

    btnDeleteState.click(function (){
       deleteState();
    });
});

function loadStatesForCurrentCountry(toast){
    currentCountryId = dropDownCountry.val().split("-")[0];

    url = contextPath + "states/list/" + currentCountryId;

    $.get(url, function (responseJSON) {
        dropDownStates.empty();

        $.each(responseJSON, function (index, state) {
            $("<option>").val(state.id).text(state.name).appendTo(dropDownStates);
        });
    }).done(function (){
        showToastMessage("All countries have been loaded.", toast);
    }).fail(function (){
        showToastMessage("ERROR: Could not connect to server or server encountered an error", toast)
    });
}

function changeFormStateToSelectedState(){
    btnAddState.prop("value", "New");
    btnUpdateState.prop("disabled", false);
    btnDeleteState.prop("disabled", false);
    labelStateName.text("Selected State/Province:");

    selectedStateName = $("#dropDownStates option:selected").text();
    fieldStateName.val(selectedStateName);
}

function changeStatesFormStateToNew(){
    btnAddState.prop("value", "Add");
    btnUpdateState.prop("disabled", true);
    btnDeleteState.prop("disabled", true);
    labelStateName.text("State/Province Name:");

    fieldStateName.val("").focus();
}

function selectNewlyAddedState(stateId, stateName){
    $("<option>").val(stateId).text(stateName).appendTo(dropDownStates);

    $("#dropDownStates option[value='"+ stateId +"']").prop("selected", true);
    fieldStateName.val("").focus();
}

function addNewState(){
    url = contextPath + "states/save";
    stateName = fieldStateName.val();
    countryId = dropDownCountry.val().split("-")[0];

    jsonData = {name: stateName, country: {id: countryId}};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr){
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function (stateId){
        selectNewlyAddedState(stateId, stateName);
        showToastMessage("The new state has been added", toastStates);
    }).fail(function (){
       showToastMessage("ERROR in request happened", toastStates);
    });
}

function updateState() {
    url = contextPath + "states/save";
    stateId = dropDownStates.val();
    stateName = fieldStateName.val();
    countryId = dropDownCountry.val().split("-")[0];

    jsonData = {id: stateId, name: stateName, country: {id: countryId}};
    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr){
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function (){
        $("#dropDownStates option:selected").text(stateName);
        showToastMessage("The current state has been updated", toastStates);
        changeStatesFormStateToNew();
    }).fail(function (){
        showToastMessage("ERROR in updating happened", toastStates);
    });
}

function deleteState(){
    stateId = dropDownStates.val();
    url = contextPath + "states/delete/" + stateId;

    $.ajax({
        type: 'DELETE',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function (){
        $("#dropDownStates option[value='" + stateId + "']").remove();
        changeStatesFormStateToNew();
        showToastMessage("The state has been deleted.", toastStates);
    }).fail(function (){
        showToastMessage("ERROR: failed to delete state", toastStates);
    });
}
