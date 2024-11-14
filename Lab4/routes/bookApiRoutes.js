const express = require("express");
const router = express.Router();
const bookController = require("../controllers/bookRestController");

router.get("/api/books", bookController.apiGetAllBooks);
router.get("/api/books/:id", bookController.apiGetBookById);
router.post("/api/books", bookController.apiCreateBook);
router.put("/api/books/:id", bookController.apiUpdateBook);
router.delete("/api/books/:id", bookController.apiDeleteBook);

module.exports = router;
