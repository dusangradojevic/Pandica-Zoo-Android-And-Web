import mongoose from "mongoose";

const Schema = mongoose.Schema;

let Ticket = new Schema({
  id: {
    type: Number,
  },
  userId: {
    type: Number,
  },
  promoPackageId: {
    type: Number,
  },
  quantity: {
    type: Number,
  },
  price: {
    type: Number,
  },
  promoCodeId: {
    type: Number,
  },
  status: {
    type: String,
  },
});

export default mongoose.model("Ticket", Ticket, "tickets");
