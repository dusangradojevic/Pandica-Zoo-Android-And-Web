import express from "express";
import { PromoCodeController } from "../controller/promo-code.controller";

const promoCodeRouter = express.Router();

promoCodeRouter.route("/insert").post((req, res) => {
  new PromoCodeController().insert(req, res);
});

promoCodeRouter.route("/update").post((req, res) => {
  new PromoCodeController().update(req, res);
});

export default promoCodeRouter;
