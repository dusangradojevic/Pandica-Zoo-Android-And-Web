import mongoose from "mongoose";

const Schema = mongoose.Schema;

let PromoPackage = new Schema({
  id: {
    type: Number,
  },
  name: {
    type: String,
  },
  price: {
    type: Number,
  },
  type: {
    type: String,
  },
});

export default mongoose.model("PromoPackage", PromoPackage, "promoPackages");
