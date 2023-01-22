var fieldProductCost;
var fieldSubtotal;
var fieldShippingCost;
var fieldTax;
var fieldTotal;

$(document).ready(function () {
    fieldProductCost = $("#productsCost");
    fieldSubtotal = $("#subtotal");
    fieldShippingCost = $("#shippingCost");
    fieldTax = $("#tax");
    fieldTotal = $("#total");

    formatOrderAmounts();
    formatProductAmounts();

    let productList = $("#productList");

    productList.on("change", ".quantity-input", function (e){
       updateSubtotalWhenQuantityChanged($(this));
       updateOrderAmounts();
    });
    productList.on("change", ".price-input", function (e){
        updateSubtotalWhenPriceChanged($(this));
        updateOrderAmounts();
    });
    productList.on("change", ".cost-input", function (e){
        updateOrderAmounts();
    });
    productList.on("change", ".ship-input", function (e){
        updateOrderAmounts();
    });
});

function updateProductsCost() {
    let totalCost = 0.0;
    $(".cost-input").each(function (e) {
        costInputField = $(this);
        rowNumber = costInputField.attr("rowNumber");
        quantityValue = $("#quantity" + rowNumber).val();

        productCost = getNumberValueRomovedThousandSeparator(costInputField);
        totalCost += parseInt(quantityValue) * productCost;
    });

    setAndFormatNubmerForField("productsCost", totalCost);

    return totalCost;
}

function updateSubtotal() {
    let orderSubtotal = 0.0;
    $(".subtotal-output").each(function (e) {
        orderSubtotal += getNumberValueRomovedThousandSeparator($(this));
    });

    setAndFormatNubmerForField("subtotal", orderSubtotal);

    return orderSubtotal;
}

function updateShippingCost() {
    let shippingCost = 0.0;
    $(".ship-input").each(function (e) {
        shippingCost += getNumberValueRomovedThousandSeparator($(this));
    });

    setAndFormatNubmerForField("shippingCost", shippingCost);

    return shippingCost;
}

function updateOrderAmounts() {
    let totalCost = updateProductsCost();
    let orderSubtotal = updateSubtotal();
    let shippingCost = updateShippingCost();

    let tax = getNumberValueRomovedThousandSeparator(fieldTax);

    let orderTotal = orderSubtotal + shippingCost + tax;
    setAndFormatNubmerForField("total", orderTotal);
}

function formatOrderAmounts(){
    formatNumberForField(fieldProductCost);
    formatNumberForField(fieldSubtotal);
    formatNumberForField(fieldShippingCost);
    formatNumberForField(fieldTax);
    formatNumberForField(fieldTotal);
}

function formatProductAmounts(){
    $(".cost-input").each(function (e) {
       formatNumberForField($(this));
    });
    $(".price-input").each(function (e) {
        formatNumberForField($(this));
    });
    $(".subtotal-output").each(function (e) {
        formatNumberForField($(this));
    });
    $(".ship-input").each(function (e) {
        formatNumberForField($(this));
    });
}

function updateSubtotalWhenQuantityChanged(input){
    quantityValue = input.val();
    rowNumber = input.attr("rowNumber");
    priceValue = getNumberValueRomovedThousandSeparator($("#price" + rowNumber));
    newSubtotal = parseFloat(quantityValue) * priceValue;

    setAndFormatNubmerForField("subtotal" + rowNumber, newSubtotal);
}

function updateSubtotalWhenPriceChanged(input){
    priceValue = getNumberValueRomovedThousandSeparator(input);
    rowNumber = input.attr("rowNumber");
    quantityField = $("#quantity" + rowNumber);
    quantityValue = quantityField.val();

    newSubtotal = parseFloat(quantityValue) * priceValue;

    setAndFormatNubmerForField("subtotal" + rowNumber, newSubtotal);

    formatNumberForField(input);
}

function setAndFormatNubmerForField(fieldId, fieldValue) {
    $("#" + fieldId).val($.number(fieldValue, 2));
}

function getNumberValueRomovedThousandSeparator(fieldRef){
    fieldValue = fieldRef.val()
//        .replace(",","")
        .replace(" ","");
    return parseFloat(fieldValue);
}

function formatNumberForField(fieldRef) {
    fieldRef.val($.number(fieldRef.val(), 2));
}

function processFormBeforeSubmit(){
    setCountryName();

    removeThousandSeparatorForField(fieldProductCost);
    removeThousandSeparatorForField(fieldSubtotal);
    removeThousandSeparatorForField(fieldShippingCost);
    removeThousandSeparatorForField(fieldTax);
    removeThousandSeparatorForField(fieldTotal);

    $(".cost-input").each(function (e) {
        removeThousandSeparatorForField($(this));
    });
    $(".price-input").each(function (e) {
        removeThousandSeparatorForField($(this));
    });
    $(".subtotal-output").each(function (e) {
        removeThousandSeparatorForField($(this));
    });
    $(".ship-input").each(function (e) {
        removeThousandSeparatorForField($(this));
    });
}

function removeThousandSeparatorForField(fieldRef) {
    fieldRef.val(fieldRef.val().replace(" ",""));
}

function setCountryName(){
    selectedCountry = $("#country option:selected");
    countryName = selectedCountry.text();
    $("#countryName").val(countryName);
}