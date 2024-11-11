const Book = require("../models/Book");
const logger = require("../logger");

exports.getAllBooks = async (req, res) => {
  try {
    const books = await Book.find();
    logger.info("Fetched all books");
    res.render("books/index", { books });
  } catch (err) {
    logger.error("Error fetching all books", err);
    res.status(500).send(err);
  }
};

exports.getBookById = async (req, res) => {
  try {
    const book = await Book.findById(req.params.id);
    if (!book) {
      logger.warn(`Book not found with id: ${req.params.id}`);
      return res.status(404).send("Book not found");
    }
    logger.info(`Fetched book with id: ${req.params.id}`);
    res.render("books/detail", { book });
  } catch (err) {
    logger.error(`Error fetching book with id: ${req.params.id}`, err);
    res.status(500).send(err);
  }
};

exports.createBook = async (req, res) => {
  try {
    const book = new Book(req.body);
    await book.save();
    logger.info(`Created new book with id: ${book._id}`);
    res.redirect("/books");
  } catch (err) {
    logger.error("Error creating new book", err);
    res.status(500).send(err);
  }
};

exports.updateBook = async (req, res) => {
  try {
    await Book.findByIdAndUpdate(req.params.id, req.body);
    logger.info(`Updated book with id: ${req.params.id}`);
    res.redirect("/books");
  } catch (err) {
    logger.error(`Error updating book with id: ${req.params.id}`, err);
    res.status(500).send(err);
  }
};

exports.deleteBook = async (req, res) => {
  try {
    logger.info(`Attempting to delete book with id: ${req.params.id}`);
    const book = await Book.findByIdAndDelete(req.params.id);
    if (!book) {
      logger.warn(`Book not found with id: ${req.params.id}`);
      return res.status(404).send("Book not found");
    }
    logger.info(`Deleted book with id: ${req.params.id}`);
    res.redirect("/books");
  } catch (err) {
    logger.error(`Error deleting book with id: ${req.params.id}`, err);
    res.status(500).send(err);
  }
};

exports.renderEditForm = async (req, res) => {
  try {
    const book = await Book.findById(req.params.id);
    if (!book) {
      logger.warn(`Book not found with id: ${req.params.id}`);
      return res.status(404).send("Book not found");
    }
    logger.info(`Rendering edit form for book with id: ${req.params.id}`);
    res.render("books/edit", { book });
  } catch (err) {
    logger.error(
      `Error rendering edit form for book with id: ${req.params.id}`,
      err
    );
    res.status(500).send(err);
  }
};
