$(document).ready(function () {
    $.ajax({
        type: "GET",
        url: 'GetCategories',
        success: function (response) {
            let categoriesArray = JSON.parse(response);
            console.log(response);
            fillCategoriesDropdown(categoriesArray);
            printCategories(categoriesArray, document.getElementById("category-list-div"));
        },
        statusCode: {
            404: function () {
                alert("Couldn't reach the endpoint")
            }
        }
    });

    document.getElementById("submit-category").addEventListener("click", function () {
        alert(document.getElementById("create-category-form"));
        let categoryName = document.forms["create-category-form"]["categoryName"].value;
        let categoryParent = document.forms["create-category-form"]["categoryParent"].value;
        $.ajax({
            type: "POST",
            url: 'CreateCategory',
            data: {"categoryName": categoryName, "categoryParent": categoryParent},
            success: function (response) {
                alert("Create success!");
                $.ajax({
                    type: "GET",
                    url: 'GetCategories',
                    success: function (response) {
                        console.log("Printing categories");
                        let categoriesArray = JSON.parse(response);
                        printCategories(categoriesArray, document.getElementById("category-list-div"));
                        alert("Get success!");
                    },
                    error: function (){
                        alert("Something went wrong getting categories");
                    },
                    statusCode: {
                        404: function () {
                            alert("Couldn't reach the endpoint");
                        }
                    }
                });
            },
            error: function (){
                alert("Create error");
            },
            /*statusCode: {
                400: function () {
                    alert("Bad parameters, didn't create category");
                },
                404: function () {
                    alert("Couldn't reach the endpoint");
                }
            }*/
        });
    })


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

function printCategories(categoriesArray, parentElement) {
    let categoriesList = document.createElement("ul");
    parentElement.appendChild(categoriesList);
    categoriesList.className = "category";

    for (let i = 0; i < categoriesArray.length; i++) {
        let categoryListElement = document.createElement("li");
        categoriesList.appendChild(categoryListElement);
        categoryListElement.appendChild(document.createTextNode(categoriesArray[i].id + " " + categoriesArray[i].name));
        printCategories(categoriesArray[i].subCategories, categoriesList);
    }
}

function fillCategoriesDropdown(categoriesArray) {
    let categoryDropdownSelect = document.getElementById("categoryParent");
    if (categoryDropdownSelect === null) {
        alert("categoryDropdownSelect is null");
    }
    for (let i = 0; i < categoriesArray.length; i++) {
        let categoryDropdownOption = document.createElement("option");
        categoryDropdownOption.appendChild(document.createTextNode(categoriesArray[i].name));
        categoryDropdownOption.value = categoriesArray[i].databaseId;
        categoryDropdownSelect.appendChild(categoryDropdownOption);
        fillCategoriesDropdown(categoriesArray[i].subCategories);
    }
}