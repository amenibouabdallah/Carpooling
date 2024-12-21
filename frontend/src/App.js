import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./pages/Auth/Login/login";
import Signup from "./pages/Auth/Signup/signup";
import Home from "./pages/home/home";
import GetDrive from "./pages/GetDrive/GetDrive";
import Profile from "./pages/Profile/Profile";
import History from "./pages/History/History";
import CreateRide from "./pages/CreateRide/CreateRide";
const NotFound = () => <h1>404 - Page Not Found</h1>;

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/get-drive" element={<GetDrive />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/history" element={<History/>}
        />
        <Route path="/create-ride" element={<CreateRide />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
    </Router>
  );
};

export default App;
