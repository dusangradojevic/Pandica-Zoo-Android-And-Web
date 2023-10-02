import express from "express";
import { PromoPackageController } from "../controller/promo-package.controller";

const promoPackageRouter = express.Router();

promoPackageRouter.route("/getAll").get((req, res) => {
  new PromoPackageController().getAll(req, res);
});

promoPackageRouter.route("/insert").post((req, res) => {
  new PromoPackageController().insert(req, res);
});

promoPackageRouter.route("/update").post((req, res) => {
  new PromoPackageController().update(req, res);
});

promoPackageRouter.route("/remove").post((req, res) => {
  new PromoPackageController().remove(req, res);
});

promoPackageRouter.route("/getById").post((req, res) => {
  new PromoPackageController().getById(req, res);
});

promoPackageRouter.route("/getByIds").post((req, res) => {
  new PromoPackageController().getByIds(req, res);
});

export default promoPackageRouter;
