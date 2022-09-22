function clearFilter(linkURL){
    // window.location = moduleURL;
    window.location = linkURL;
}

function showDeleteConfirmModal(link, entityName){
    let entityId = link.attr("entityID");

    $("#yesButton").attr("href", link.attr("href"));
    $("#confirmText").text("Are you sure you want to delete this " +
                            entityName + "(ID=" + entityId + ")?");
    $("#confirmModal").modal();
}