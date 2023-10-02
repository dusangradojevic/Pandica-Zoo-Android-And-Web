import express from "express";
import cors from "cors";
import bodyParser from "body-parser";
import expressValidator from "express-validator";
import mongoose from "mongoose";
import multer from "multer";
import userRouter from "./route/user.routes";
import animalRouter from "./route/animal.routes";
import eventRouter from "./route/event.router";
import promoPackageRouter from "./route/promo-package.routes";
import ticketRouter from "./route/ticket.routes";
import commentRouter from "./route/comment.routes";
import promoCodeRouter from "./route/promo-code.routes";

const app = express();

app.use(cors());
app.use(bodyParser.json());
app.use(expressValidator());

mongoose.connect("mongodb://localhost:27017/zoo_pandica");

const connection = mongoose.connection;
const port = 4000;

connection.once("open", () => {
  console.log("Connection successful");
});

const storage = multer.memoryStorage();
const upload = multer({ storage: storage });
app.use(upload.single("photo"));

const router = express.Router();
router.use("/user", userRouter);
router.use("/animal", animalRouter);
router.use("/event", eventRouter);
router.use("/promoPackage", promoPackageRouter);
router.use("/promoCode", promoCodeRouter);
router.use("/ticket", ticketRouter);
router.use("/comment", commentRouter);

app.use("/", router);

app.listen(port, () => console.log(`Express server running on port ` + port));
