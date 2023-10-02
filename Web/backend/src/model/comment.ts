import mongoose from "mongoose";

const Schema = mongoose.Schema;

let Comment = new Schema({
  id: {
    type: Number,
  },
  userId: {
    type: Number,
  },
  animalId: {
    type: Number,
  },
  text: {
    type: String,
  },
});

export default mongoose.model("Comment", Comment, "comments");
