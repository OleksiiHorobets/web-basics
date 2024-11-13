const { Book } = require("./models/Book");

const resolvers = {
  Query: {
    getBooks: async () => await Book.find(),
    getBook: async (_, { id }) => await Book.findById(id),
  },
  Mutation: {
    addBook: async (_, { title, reader, date_of_issue, return_date }) => {
      const book = new Book({ title, reader, date_of_issue, return_date });
      return await book.save();
    },
    updateBook: async (
      _,
      { id, title, reader, date_of_issue, return_date }
    ) => {
      const book = await Book.findById(id);
      if (!book) throw new Error("Book not found");

      if (title !== undefined) book.title = title;
      if (reader !== undefined) book.reader = reader;
      if (date_of_issue !== undefined) book.date_of_issue = date_of_issue;
      if (return_date !== undefined) book.return_date = return_date;

      return await book.save();
    },
    deleteBook: async (_, { id }) => {
      const book = await Book.findByIdAndDelete(id);
      return book;
    },
  },
};

module.exports = resolvers;
