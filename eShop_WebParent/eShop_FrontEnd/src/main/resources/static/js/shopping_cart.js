$(document).ready(function (){
    $(".linkMinus").on("click", function (evt){
       evt.preventDefault();
       decreaseQuantity($(this));
    });

    $(".linkPlus").on("click", function (evt){
        evt.preventDefault();
        increaseQuantity($(this));
    });

    $(".linkRemove").on("click", function (evt){
        evt.preventDefault();
        removeProduct($(this));
    });
});

function decreaseQuantity(link){
    productId = link.attr("pid");
    quantityInput = $("#quantity" + productId);
    newQuantity = parseInt(quantityInput.val()) - 1;

    if (newQuantity > 0 ) {
        quantityInput.val(newQuantity);
        updateQuantity(productId, newQuantity);
    } else {
        showWarningModal("Minimum quantity is 1");
    }
}

function increaseQuantity(link){
    productId = link.attr("pid");
    quantityInput = $("#quantity" + productId);
    newQuantity = parseInt(quantityInput.val()) + 1;

    if (newQuantity <= 5 ) {
        quantityInput.val(newQuantity);
        updateQuantity(productId, newQuantity);
    } else {
        showWarningModal("Maximum quantity is 5");
    }
}

function updateQuantity(productId, quantity) {
    url = contextPath + "cart/update/" + productId + "/" + quantity;

    $.ajax({
        type: "POST",
        url: url,
        beforeSend: function (xhr){
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function (updatedSubtotal){
        updateSubtotal(updatedSubtotal, productId);
        updateTotal();
    }).fail(function (){
        showErrorModal("Error while updating product quantity in shopping cart.");
    });
}

function updateSubtotal(updatedSubtotal, productId){
    formatedSubtotal = $.number(updatedSubtotal, 2);
    $("#subtotal" + productId).text(formatedSubtotal);
}

function updateTotal(){
    let total = 0.0;
    let productCount = 0;

    $(".subtotal").each(function (index, element){
        productCount++;
       total += parseFloat(element.innerHTML.replaceAll("&nbsp;", ""));
    });

    if (productCount < 1) {
        showEmptyShoppingCart();
    } else {
        let formattedTotal = $.number(total, 2);
        $("#total").text(formattedTotal);
    }
}

function showEmptyShoppingCart(){
    $("#sectionTotal").hide();
    $("#sectionEmptyCartMessage").removeClass("d-none");
}

function removeProduct(link){
    let url = link.attr("href");

    $.ajax({
        type: "DELETE",
        url: url,
        beforeSend: function (xhr){
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function (response){
        let rowNumber = link.attr("rowNumber");
        removeProductHtml(rowNumber);
        updateTotal();
        updateCountNumbers();
        showModalDialog("Shopping Cart", response);
    }).fail(function (){
        showErrorModal("Error while deleting product from shopping cart.");
    });
}

function removeProductHtml(rowNumber){
    $("#row" + rowNumber).remove();
    $("#blankLine" + rowNumber).remove();
}

function updateCountNumbers(){
    $(".divCount").each(function (index, element){
       element.innerHTML = "" + (index + 1);
    });
}