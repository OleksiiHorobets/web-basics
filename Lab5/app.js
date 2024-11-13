const express = require("express");
const { ApolloServer } = require("apollo-server-express");
const mongoose = require("mongoose");
const fs = require("fs");
const path = require("path");
const resolvers = require("./resolvers");
require("dotenv").config();

const typeDefs = fs.readFileSync(
  path.join(__dirname, "schema.graphql"),
  "utf-8"
);

async function startServer() {
  const app = express();

  const server = new ApolloServer({ typeDefs, resolvers });
  await server.start();
  server.applyMiddleware({ app });

  console.log("Connecting to MongoDB" + process.env.MONGO_URI);
  mongoose
    .connect(process.env.MONGO_URI, {
      useNewUrlParser: true,
      useUnifiedTopology: true,
    })
    .then(() => {
      console.info("Connected to MongoDB");
    })
    .catch((err) => {
      console.error("Failed to connect to MongoDB", err);
    });

  app.listen({ port: 3000 }, () => {
    console.log(`Server ready at http://localhost:3000${server.graphqlPath}`);
  });
}

startServer();
