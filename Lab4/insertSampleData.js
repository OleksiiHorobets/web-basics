const mongoose = require("mongoose");
const Book = require("./models/Book"); // Adjust path based on your file structure

// Replace with your MongoDB connection string
const mongoURI =
  "mongodb+srv://oleksii:ulauhiBqL6xEo4p2@cluster0.ir7bx.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
// Sample data for books
const sampleBooks = [
  {
    title: "The Great Gatsby",
    reader: "John Doe",
    date_of_issue: new Date("2024-01-01"),
    return_date: new Date("2024-01-15"),
  },
  {
    title: "1984",
    reader: "Jane Smith",
    date_of_issue: new Date("2024-02-01"),
    return_date: new Date("2024-02-14"),
  },
  {
    title: "To Kill a Mockingbird",
    reader: "Alice Brown",
    date_of_issue: new Date("2024-03-01"),
    return_date: new Date("2024-03-15"),
  },
  {
    title: "Pride and Prejudice",
    reader: "Bob White",
    date_of_issue: new Date("2024-04-01"),
    return_date: new Date("2024-04-14"),
  },
  {
    title: "Moby Dick",
    reader: "Charlie Black",
    date_of_issue: new Date("2024-05-01"),
    return_date: new Date("2024-05-15"),
  },
];

mongoose
  .connect(mongoURI, { useNewUrlParser: true, useUnifiedTopology: true })
  .then(() => {
    console.log("Connected to MongoDB");
    return Book.insertMany(sampleBooks);
  })
  .then(() => {
    console.log("Sample data inserted successfully!");
    mongoose.connection.close();
  })
  .catch((error) => {
    console.error("Error inserting sample data:", error);
    mongoose.connection.close();
  });
