$(document).ready(function () {

    updateViewFromServer();

    sessionStorage.setItem('storedChanges', '{"changes": []}');

    document.getElementById("submit-category").addEventListener("click", function () {
        if (sessionStorage.getItem('storedChanges').localeCompare('{"changes": []}') === 0 || confirm("Any unsaved changes to the tree will be lost")) {
            let categoryName = document.forms["create-category-form"]["categoryName"].value;
            let categoryParent = document.forms["create-category-form"]["categoryParent"].value;
            sessionStorage.setItem('storedChanges', '{"changes": []}');
            $.ajax({
                type: "POST",
                url: 'CreateCategory',
                data: {"categoryName": categoryName, "categoryParent": categoryParent},
                success: function (response) {
                    updateViewFromServer();
                },
                error:  function () {
                    alert("Couldn't reach the endpoint");
                }
            });
        }
    })

    document.getElementById("logout-button").addEventListener("click", function () {
        sessionStorage.clear();
        $.ajax({
            type: "POST",
            url: 'Logout',
            success: function (response) {
                console.log('Logged out');
                window.location.href = 'login.html';
            },
            failure: function (response)
            {
                window.location.href = 'login.html';
            }
        });
    })

    document.getElementById("send-tree-changes").addEventListener("click", function () {
        sendUpdatesToServer();
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
            if (!isParent(dragged.value, event.target.value)) {
                if (confirm("Are you sure?")) {
                    saveLocalChanges(dragged.value, event.target.value);
                    updateViewFromClient(dragged.value, event.target.value);
                }
            }
        }
    }, false);
});

function isParent(parentMoved, child)
{
    return child.toString().startsWith(parentMoved.toString());
}

function printCategories(categoriesArray, parentElement, count)
{
    let categoriesList = document.createElement("ul");
    parentElement.appendChild(categoriesList);
    categoriesList.className = "category";

    for (let i = 0; i < categoriesArray.length; i++) {
        let categoryListElement = document.createElement("li");
        if (categoriesArray[i].id === 0) {
            categoryListElement.setAttribute("id", "root-category");
            categoryListElement.setAttribute("draggable", "false");
        } else {
            categoryListElement.setAttribute("draggable", "true");
        }
        categoryListElement.setAttribute("class", "dropzone");
        categoryListElement.value = categoriesArray[i].id;
        categoriesList.appendChild(categoryListElement);
        categoryListElement.appendChild(document.createTextNode(categoriesArray[i].id + " " + categoriesArray[i].name));
        printCategories(categoriesArray[i].subCategories, categoriesList, categoriesArray[i].id);
    }
}

function updateCategoriesIds(categoriesArray, count)
{
    for (let i = 0; i < categoriesArray.length; i++) {
        if (categoriesArray[i].id !== 0)
            categoriesArray[i].id = count * 10 + i + 1;
        updateCategoriesIds(categoriesArray[i].subCategories, categoriesArray[i].id);
    }
}

function fillCategoriesDropdown(categoriesArray)
{
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

function updateViewFromServer()
{
    document.getElementById("category-list-div").innerHTML = "";
    document.getElementById("categoryParent").innerHTML = "";
    $.ajax({
        type: "GET",
        url: 'GetCategories',
        success: function (response) {
            let categoriesArray = JSON.parse(response);
            fillCategoriesDropdown(categoriesArray);
            printCategories(categoriesArray, document.getElementById("category-list-div"), 0);
            sessionStorage.setItem('categories', response);
        },
        statusCode: {
            404: function () {
                alert("Couldn't reach the endpoint");
            },
            401: function () {
                window.location.href = 'login.html';
            }
        }
    });
}

function updateViewFromClient(categoryId, parentId) {
    let categoriesArray = JSON.parse(sessionStorage.getItem('categories'));
    let category = removeCategory(categoriesArray, categoryId);
    addChildByParentId(categoriesArray, category, parentId);


    sessionStorage.setItem('categories', JSON.stringify(categoriesArray));
    document.getElementById("category-list-div").innerHTML = "";

    updateCategoriesIds(categoriesArray, 0);
    printCategories(categoriesArray, document.getElementById('category-list-div'), 0);
    sessionStorage.setItem('categories', JSON.stringify(categoriesArray));
}

function removeCategory(categoriesArray, categoryId) {
    let category = null;
    for (let i = 0; i < categoriesArray.length; i++) {
        if (categoriesArray[i].id === categoryId) {
            category = categoriesArray[i];
            categoriesArray.splice(i, 1);
            return category;
        } else {
            category = removeCategory(categoriesArray[i].subCategories, categoryId);
            if (category !== null) {
                return category;
            }
        }
    }
    return null;
}

function findCategory(categoriesArray, categoryId) {
    let category = null;
    for (let i = 0; i < categoriesArray.length; i++) {
        if (categoriesArray[i].id === categoryId) {
            category = categoriesArray[i];
            return category;
        } else {
            category = findCategory(categoriesArray[i].subCategories, categoryId);
            if (category !== null) {
                return category;
            }
        }
    }
    return null;
}

function addChildByParentId(categoriesArray, child, parentId) {
    for (let i = 0; i < categoriesArray.length; i++) {
        if (categoriesArray[i].id === parentId) {
            child.id = categoriesArray[i].id * 10 + categoriesArray[i].subCategories.length + 1;
            categoriesArray[i].subCategories.push(child);
            return true;
        } else {
            if (addChildByParentId(categoriesArray[i].subCategories, child, parentId)) {
                return true;
            }
        }
    }
    return false;
}

function sendUpdatesToServer() {
    $.ajax({
        type: "POST",
        url: 'MoveCategory',
        data: {"changes": JSON.stringify(JSON.parse(sessionStorage.getItem('storedChanges'))['changes'])},
        success: function (response) {
            alert("Categories updated correctly!")
            updateViewFromServer();
        },
        failure: function (response)
        {
            alert("Categories could not update!")
            //updateViewFromServer();
        }
    });

    sessionStorage.setItem('storedChanges', '{"changes": []}');
    document.getElementById("send-tree-changes").style.display = "none";
}

function saveLocalChanges(id, parentId) {
    let localChanges = JSON.parse(sessionStorage.getItem('storedChanges'));
    let categories = JSON.parse(sessionStorage.getItem('categories'));

    let newChild = findCategory(categories, id);
    let newParent = findCategory(categories, parentId);

    localChanges['changes'].push({
        "categoryId": id,
        "databaseId": newChild.databaseId,
        "parentId": parentId,
        "parentDatabaseId": newParent.databaseId
    });
    sessionStorage.setItem('storedChanges', JSON.stringify(localChanges));
    document.getElementById("send-tree-changes").style.display = "inline-block";
}

