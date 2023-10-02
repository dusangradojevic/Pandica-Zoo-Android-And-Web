import mongoose from "mongoose";

const Schema = mongoose.Schema;

let PromoCode = new Schema({
  id: {
    type: Number,
  },
  code: {
    type: String,
  },
  discount: {
    type: Number,
  },
  quantity: {
    type: Number,
  },
});

export default mongoose.model("PromoCode", PromoCode, "promoCodes");
