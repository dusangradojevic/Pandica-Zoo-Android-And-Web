import express from "express";
import { AnimalController } from "../controller/animal.controller";

const animalRouter = express.Router();

animalRouter.route("/getAll").get((req, res) => {
  new AnimalController().getAll(req, res);
});

animalRouter.route("/insert").post((req, res) => {
  new AnimalController().insert(req, res);
});

animalRouter.route("/update").post((req, res) => {
  new AnimalController().update(req, res);
});

animalRouter.route("/remove").post((req, res) => {
  new AnimalController().remove(req, res);
});

animalRouter.route("/getPhoto").post((req, res) => {
  new AnimalController().getPhoto(req, res);
});

export default animalRouter;
