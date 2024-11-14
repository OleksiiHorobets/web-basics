const Book = require("../models/Book");
const logger = require("../logger");

exports.apiGetAllBooks = async (req, res) => {
  try {
    const books = await Book.find();
    logger.info("API: Fetched all books");
    res.json(books);
  } catch (err) {
    logger.error("API: Error fetching all books", err);
    res.status(500).json({ error: "Error fetching all books" });
  }
};

exports.apiGetBookById = async (req, res) => {
  try {
    const book = await Book.findById(req.params.id);
    if (!book) {
      logger.warn(`API: Book not found with id: ${req.params.id}`);
      return res.status(404).json({ error: "Book not found" });
    }
    logger.info(`API: Fetched book with id: ${req.params.id}`);
    res.json(book);
  } catch (err) {
    logger.error(`API: Error fetching book with id: ${req.params.id}`, err);
    res.status(500).json({ error: "Error fetching book" });
  }
};

exports.apiCreateBook = async (req, res) => {
  try {
    const book = new Book(req.body);
    await book.save();
    logger.info(`API: Created new book with id: ${book._id}`);
    res.status(201).json(book);
  } catch (err) {
    logger.error("API: Error creating new book", err);
    res.status(500).json({ error: "Error creating new book" });
  }
};

exports.apiUpdateBook = async (req, res) => {
  try {
    const book = await Book.findByIdAndUpdate(req.params.id, req.body, {
      new: true,
    });
    if (!book) {
      logger.warn(`API: Book not found with id: ${req.params.id}`);
      return res.status(404).json({ error: "Book not found" });
    }
    logger.info(`API: Updated book with id: ${req.params.id}`);
    res.json(book);
  } catch (err) {
    logger.error(`API: Error updating book with id: ${req.params.id}`, err);
    res.status(500).json({ error: "Error updating book" });
  }
};

exports.apiDeleteBook = async (req, res) => {
  try {
    logger.info(`API: Attempting to delete book with id: ${req.params.id}`);
    const book = await Book.findByIdAndDelete(req.params.id);
    if (!book) {
      logger.warn(`API: Book not found with id: ${req.params.id}`);
      return res.status(404).json({ error: "Book not found" });
    }
    logger.info(`API: Deleted book with id: ${req.params.id}`);
    res.status(204).send();
  } catch (err) {
    logger.error(`API: Error deleting book with id: ${req.params.id}`, err);
    res.status(500).json({ error: "Error deleting book" });
  }
};
