# Catalog Tag Editor

University project for the IT Technologies for the Web course, Computer Science Engineering branch at Politecnico di Milano, A.Y. 2020/21.

The project involves creating a web app that lets the user sort, add and delete a catalog of tags in two ways:

- The first using Thymeleaf as a templating engine.
- The second using JavaScript and local storage to manage state.

Both of these use TomEE as the backend of the application.

**Team members:**

- Andrea Alesani ([@andrealesani](https://github.com/andrealesani))
- Javin Barone ([@Javinyx](https://github.com/Javinyx))
- Davide Veronesi ([@Verdax97](https://github.com/Verdax97))

**The design, database and event specifications are available [here](presentation.pdf) in italian.**

## Table of contents

- [Catalog Tag Editor](#catalog-tag-editor)
  - [Table of contents](#table-of-contents)
  - [Static HTML version (static-app)](#static-html-version-static-app)
  - [JavaScript Version (ria-app)](#javascript-version-ria-app)

## Static HTML version (static-app)

An application allows the user (for example the editor of an online catalog of images) to label images in order to allow searches by category. After logging in, the user accesses a HOME page where a hierarchical tree of categories appears. The categories do not depend on the user and are shared by all users. An example of a tree branch is the following:

```txt
9 Classical mythology and ancient history >> move
91 Deities of classical mythology >> move
911 Divinity of the sky >> move
9111 Jupiter >> move
91111 Attributes of Jupiter >> move
9112 Juno >> move
912 Divinity of the underworld >> move
9121 Pluto >> move
9122 Hecate >> move
```

The user can enter a new category in the tree. To do this, use a form on the HOME page where you specify the name of the new category and choose the parent category. Sending the new category involves updating the tree: the new category is hung from the parent category as the last sub-element. The new category is assigned a numeric code that reflects its position (for example, the new category Mercury, daughter of the category “911 Divinity of the sky” takes the code 9113). For simplicity, assume that for each category the maximum number of subcategories is 9, numbered from 1 to 9. After the creation of a category, the HOME page shows the updated tree. The user can move a category in position: to do this, click on the "move" link associated with the category to be moved. Following this action, the application shows, again on the HOME page, the tree with the subtree attesting to the category to be moved highlighted: all other categories have a "move here" link. For example, following clicking on the "move" link associated with the "9111 Giove" category, the application displays the tree as follows:

```txt
9 Classical mythology and ancient history >> move here
91 Deities of classical mythology >> move here
911 Divinity of Heaven >> move here
9111 Jupiter
91111 Attributes of Jupiter
9112 Juno >> move here
912 Divinity of the underworld >> move here
9121 Pluto >> move here
9122 Hecate >> move here
```

Selecting a "move here" link involves entering the category to be moved as the last child of the destination category. For example, selecting the "move here" link of the "912 Divinity of the underworld" category involves the following modification of the tree:

```txt
9 Classical mythology and ancient history >> move
91 Deities of classical mythology >> move
911 Divinity of the sky >> move
9111 Juno >> move
912 Divinity of the underworld >> move
9121 Pluto >> move
9122 Hecate >> move
9123 Jupiter >> move
91231 Attributes of Jupiter >> move
```

Changes made by a user and saved in the database become visible to other users.

## JavaScript Version (ria-app)

Create a web server client application that modifies the previous specifications as follows:

After the user login, the entire application is created with a single page.
Each user interaction is managed without completely reloading the page, but produces the asynchronous invocation of the server and any modification of the content to be updated following the event.
The function of moving a category is carried out by means of drag & drop.
Following the drop of the category to be moved, a dialog box appears with which the user can confirm or cancel the move. The confirmation produces the client-side update of the tree.

The user also makes multiple movements on the client side. Following the first move, a SAVE button appears, pressing which causes the server to send the list of the movements made (NOT the entire tree). The sending of the movements produces the updating of the tree in the database and the appearance of a message confirming the saving.
