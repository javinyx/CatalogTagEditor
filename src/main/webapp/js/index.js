$(document).ready(function () {
    $.ajax({
        type: "GET",
        url: 'GetCategories',
        success: function (response) {
            response.forEach(function (item, value) {
                let categoryListElement = document.createElement("li");
                categoryListElement.id = item.id;
                categoryListElement.dataset.itemid = item.id;
                categoryListElement.className = "category";

                categoryListElement.appendChild(document.createTextNode(item.name));

                let subCategoryListContainer = document.createElement("ul");
                if (item.level > 0) { // subCategoryList.length
                    item.parent.forEach(function (item1, value1) {
                        let subCategoryListElement = document.createElement("li");
                        subCategoryListElement.id = item1.id;
                        subCategoryListElement.dataset.name = item1.name;
                        subCategoryListElement.dataset.itemid = item1.id;
                        subCategoryListElement.setAttribute("draggable", 'true');
                        subCategoryListElement.className = "subCategory";
                    })
                }

                categoryListElement.appendChild(subCategoryListContainer);
                // this syntax is equivalent to `document.getElementById()`
                $("#categoryList").append(categoryListElement);
            })
        },
        error: function (response) {
            // this function is run if the server responds with an error code
            document.getElementById("error-message").textContent = response.responseText;
            window.location.href = 'index.html';
        }
    });

    // set welcome message
    document.getElementById("welcome-message").textContent = `Welcome, ${sessionStorage.username}`;

    let dragged;
    const errorMsg = document.getElementById("error-message");
    document.addEventListener("drag", function (event) {
        // events on drag
    }, false);
    document.addEventListener("dragstart", function (event) {
        dragged = event.target;
        errorMsg.textContent = `You're moving ${dragged.className} ${dragged.dataset.name}`;
        event.target.style.opacity = .5;
    }, false);

    document.addEventListener("dragend", function (event) {
        // reset the transparency
        errorMsg.textContent = "";
        event.target.style.opacity = "";
    }, false);

    /* events fired on the drop targets */
    document.addEventListener("dragover", function (event) {
        // prevent default (to allow drop)
        event.preventDefault();
    }, false);

    document.addEventListener("dragenter", function (event) {
        // highlight potential drop target when the draggable element enters it
        if (event.target.className === "category" && dragged.className === "subCategory" || event.target.className === "subCategory") {
            event.target.style.background = "aqua";
        }
    }, false);

    document.addEventListener("dragleave", function (event) {
        // reset background of potential drop target when the draggable element leaves it
        if (event.target.className === "folder" && dragged.className === "subfolder" || event.target.id === "wastebin" || event.target.className === "subfolder") {
            event.target.style.background = "";
        }
    }, false);

    document.addEventListener("drop", function (event) {
        // prevent default action (open as link for some elements)
        event.preventDefault();
        // move dragged elem to the selected drop target
        if (event.target.className === "folder" && dragged.className === "subfolder") {
            let dataForm = {
                entity_id: parseInt(dragged.dataset.itemid),
                to: parseInt(event.target.dataset.itemid)
            }
            event.target.style.background = "";
            $.ajax({
                type: "POST",
                url: 'MoveCategory',
                data: JSON.stringify(dataForm),
                success: function (response) {
                    dragged.parentNode.removeChild(dragged);
                    event.target.getElementsByTagName("ul")[0].appendChild(dragged);
                    errorMsg.textContent = response.responseText;
                },
                error: function (response) {
                    errorMsg.textContent = response.responseText;
                }
            });
        }
    }, false);
});

function handleLogout() {
    sessionStorage.clear();
    window.location.href = '/Logout';
}