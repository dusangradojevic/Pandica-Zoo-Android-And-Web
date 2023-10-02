import * as express from "express";
import PromoPackage from "../model/promo-package";

export class PromoPackageController {
  getAll = (req: express.Request, res: express.Response) => {
    PromoPackage.find({}, (err: any, packages: any) => {
      if (err) {
        res.json({
          message: "Error",
          errorMessage: "Promo packages do not exist.",
        });
      } else {
        res.json({ message: "Ok", packages: packages });
      }
    });
  };

  insert = async (req: express.Request, res: express.Response) => {
    const newName = req.body.name;
    const newPrice = req.body.price;
    const newType = req.body.type;

    const packages = await PromoPackage.find().sort({ id: -1 }).limit(1);
    let newPackageId = 0;
    if (packages.length > 0) {
      newPackageId = packages[0].id + 1;
    }

    let errors: Array<String> = [];

    PromoPackage.findOne({ name: newName }, (err: any, promoPackage: any) => {
      if (promoPackage) {
        errors.push("Ime mora biti jedinstveno.");
        res.json({ errors: errors });
        return;
      }

      const newPackage = new PromoPackage({
        id: newPackageId,
        name: newName,
        price: newPrice,
        type: newType,
      });

      newPackage
        .save()
        .then(() => {
          res.status(200).json(promoPackage);
        })
        .catch(() => {
          res.json({ message: "Error" });
        });
    });
  };

  update = async (req: express.Request, res: express.Response) => {
    const packageId = parseInt(req.body.packageId);
    const newName = req.body.name;
    const newPrice = req.body.price;
    const newType = req.body.type;

    let errors: Array<String> = [];

    await PromoPackage.updateOne(
      { id: packageId },
      {
        $set: {
          name: newName,
          price: newPrice,
          type: newType,
        },
      }
    );

    PromoPackage.findOne({ id: packageId }, (err: any, promoPackage: any) => {
      if (err || promoPackage == null || !promoPackage) {
        errors.push("Doslo je do greske prilikom azuriranja podataka.");
        res.json({ errors: errors });
      } else {
        res.status(200).json({ promoPackage: promoPackage });
      }
    });
  };

  remove = async (req: express.Request, res: express.Response) => {
    const packageId = req.body.packageId;
    const promoPackage = await PromoPackage.findOne({ id: packageId });
    if (!promoPackage || promoPackage == null) {
      res.json({ message: "Error", errorMessage: "Neocekivana greska." });
      return;
    }

    promoPackage.deleteOne({ id: packageId }, (err: any, resp: any) => {
      if (err) {
        res.json({ message: "Error", errorMessage: "Neocekivana greska." });
      } else {
        res.status(200).json({ message: "Ok" });
      }
    });
  };

  getById = (req: express.Request, res: express.Response) => {
    const packageId = req.body.id;
    PromoPackage.findOne({ id: packageId }, (err: any, promoPackage: any) => {
      if (err) {
        res.json({ message: "Error", errorMessage: "Package does not exist." });
      } else {
        res.json({ message: "Ok", promoPackage: promoPackage });
      }
    });
  };

  getByIds = async (req: express.Request, res: express.Response) => {
    const packageIds = req.body.packageIds;
    let packagesReturn = [];
    PromoPackage.find(
      { id: { $in: packageIds } },
      (err: any, packages: any) => {
        if (err) {
          res.json({ message: "Error", packages: [], errorMessage: err });
          return;
        }
        for (let i = 0; i < packages.length; ++i) {
          for (let j = 0; j < packageIds.length; ++j) {
            if (packages[i].id == packageIds[j]) {
              packagesReturn.push(packages[i]);
            }
          }
        }
        res.json({ message: "Ok", packages: packagesReturn });
      }
    );
  };
}
