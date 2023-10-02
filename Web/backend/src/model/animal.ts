import mongoose from "mongoose";

const Schema = mongoose.Schema;

let Animal = new Schema({
  id: {
    type: Number,
  },
  name: {
    type: String,
  },
  description: {
    type: String,
  },
  photo: {
    type: String,
  },
});

export default mongoose.model("Animal", Animal, "animals");
