import mongoose from "mongoose";

const Schema = mongoose.Schema;

let Like = new Schema({
  id: {
    type: Number,
  },
  userId: {
    type: Number,
  },
  eventId: {
    type: Number,
  },
});

export default mongoose.model("Like", Like, "likes");
