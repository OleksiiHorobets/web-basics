const express = require("express");
const mongoose = require("mongoose");
const methodOverride = require("method-override");
const bookRoutes = require("./routes/bookRoutes");
const apiRoutes = require("./routes/bookApiRoutes");
const path = require("path");
const logger = require("./logger"); // Import the logger
require("dotenv").config();

const app = express();

app.use(express.static(path.join(__dirname, "public")));

mongoose
  .connect(process.env.MONGO_URI, {
    useNewUrlParser: true,
    useUnifiedTopology: true,
  })
  .then(() => {
    logger.info("Connected to MongoDB");
  })
  .catch((err) => {
    logger.error("Failed to connect to MongoDB", err);
  });

app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(methodOverride("_method"));

app.use((req, res, next) => {
  logger.info(`${req.method} ${req.url}`);
  next();
});

app.set("view engine", "ejs");
app.set("views", path.join(__dirname, "views"));

app.use(apiRoutes);
app.use(bookRoutes);

app.use((err, req, res, next) => {
  logger.error(err.stack);
  res.status(500).send("Something broke!");
});

app.listen(3000, () => {
  logger.info("Server is running on port 3000");
});
