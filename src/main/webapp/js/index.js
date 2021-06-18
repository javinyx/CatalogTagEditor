$(document).ready(function () {

    updateViewCategories();

    document.getElementById("submit-category").addEventListener("click", function () {
        alert(document.getElementById("create-category-form"));
        let categoryName = document.forms["create-category-form"]["categoryName"].value;
        let categoryParent = document.forms["create-category-form"]["categoryParent"].value;
        $.ajax({
            type: "POST",
            url: 'CreateCategory',
            data: {"categoryName": categoryName, "categoryParent": categoryParent},
            success: function (response) {
                updateViewCategories();
            }
        });
    })


    // set welcome message
    document.getElementById("welcome-message").textContent = `Welcome, ${sessionStorage.username}`;

    let dragged;

    document.addEventListener("dragstart", function (event) {
        // store a ref. on the dragged elem
        dragged = event.target;
        // make it a lil bit transparent
        event.target.style.opacity = ".7";
    }, false);

    document.addEventListener("dragend", function (event) {
        // reset the transparency
        event.target.style.opacity = "";

    }, false);

    /* events fired on the drop targets */
    document.addEventListener("dragover", function (event) {
        // prevent default to allow drop
        event.preventDefault();
    }, false);

    document.addEventListener("dragenter", function (event) {
        // highlight potential drop target when the draggable element enters it
        if (event.target.className == "dropzone") {
            event.target.style.background = "purple";
        }

    }, false);

    document.addEventListener("dragleave", function (event) {
        // reset background of potential drop target when the draggable element leaves it
        if (event.target.className == "dropzone") {
            event.target.style.background = "";
        }

    }, false);

    document.addEventListener("drop", function (event) {
        // prevent default action (open as link for some elements)
        event.preventDefault();
        // move dragged elem to the selected drop target
        if (event.target.className == "dropzone") {
            event.target.style.background = "";

            $.ajax({
                type: "GET",
                url: 'MoveCategory',
                data: {"toMove": dragged.value, "newParent": event.target.value},
                success: function (response) {
                    updateViewCategories();
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
        if (categoriesArray[i].id === 0) {
            categoryListElement.setAttribute("draggable", "false");
        } else {
            categoryListElement.setAttribute("draggable", "true");
        }
        categoryListElement.setAttribute("class", "dropzone");
        categoryListElement.value = categoriesArray[i].id;
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
        categoryDropdownOption.appendChild(document.createTextNode(categoriesArray[i].id + " " + categoriesArray[i].name));
        categoryDropdownOption.value = categoriesArray[i].databaseId;
        categoryDropdownSelect.appendChild(categoryDropdownOption);
        fillCategoriesDropdown(categoriesArray[i].subCategories);
    }
}

function updateViewCategories() {
    document.getElementById("category-list-div").innerHTML = "";
    document.getElementById("categoryParent").innerHTML = "";
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
}