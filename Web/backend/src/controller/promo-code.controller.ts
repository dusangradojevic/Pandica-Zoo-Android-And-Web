import * as express from "express";
import PromoCode from "../model/promo-code";

export class PromoCodeController {
  insert = async (req: express.Request, res: express.Response) => {
    const code = req.body.code;
    const discount = req.body.discount;
    const quantity = req.body.quantity;

    const promoCodes = await PromoCode.find().sort({ id: -1 }).limit(1);
    let newPromoCodeId = 0;
    if (promoCodes.length > 0) {
      newPromoCodeId = promoCodes[0].id + 1;
    }

    const newPromoCode = new PromoCode({
      id: newPromoCodeId,
      code,
      discount,
      quantity,
    });

    newPromoCode
      .save()
      .then(() => {
        res.status(200).json({ message: "Ok" });
      })
      .catch(() => {
        res.json({ message: "Error" });
      });
  };

  update = async (req: express.Request, res: express.Response) => {
    const ticketId = parseInt(req.body.ticketId);
    await PromoCode.updateOne(
      { id: ticketId },
      { $set: { status: "accepted" } }
    );

    let errors: Array<String> = [];
    PromoCode.findOne({ id: ticketId }, (err: any, ticket: any) => {
      if (err || ticket == null || !ticket) {
        errors.push("Doslo je do greske prilikom azuriranja podataka.");
        res.json({ errors: errors });
      } else {
        res.status(200).json({ ticket: ticket });
      }
    });
  };
}
