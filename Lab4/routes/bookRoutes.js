const express = require("express");
const router = express.Router();
const bookController = require("../controllers/bookController");

// Route to render form for creating a new book
router.get("/books/new", (req, res) => {
  res.render("books/new");
});

// Route to render form for editing an existing book
router.get("/books/:id/edit", bookController.renderEditForm);

router.get("/books", bookController.getAllBooks);
router.get("/books/:id", bookController.getBookById);
router.post("/books", bookController.createBook);
router.put("/books/:id", bookController.updateBook);
router.delete("/books/:id", bookController.deleteBook);

module.exports = router;
