const mongoose = require("mongoose");

const bookSchema = new mongoose.Schema({
  title: {
    type: String,
    required: true,
  },
  reader: {
    type: String,
    required: true,
  },
  date_of_issue: {
    type: Date,
    required: true,
  },
  return_date: {
    type: Date,
    required: true,
  },
});

module.exports = mongoose.model("Book", bookSchema);
